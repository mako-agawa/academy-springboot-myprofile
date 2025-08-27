// fetch("/api/chart-data")
//   .then(res => res.json())
//   .then(data => {

//     new Chart(document.getElementById("myChart"), {
//       type: "bar",
//       data: {
//         labels: ["先々月", "先月", "今月"],
//         datasets: [
//           {
//             label: "バックエンド",
//             data: [data.backend.twoMonthsAgo / 60, data.backend.lastMonth / 60, data.backend.currentMonth / 60],
//             backgroundColor: "rgb(243, 181, 194)"
//           },
//           {
//             label: "フロントエンド",
//             data: [data.frontend.twoMonthsAgo / 60, data.frontend.lastMonth / 60, data.frontend.currentMonth / 60],
//             backgroundColor: "#F7D1AA"
//           },
//           {
//             label: "インフラ",
//             data: [data.infra.twoMonthsAgo / 60, data.infra.lastMonth / 60, data.infra.currentMonth / 60],
//             backgroundColor: "#FAE6B5"
//           }
//         ]
//       },
//       options: {
//         scales: {
//           y: {
//             beginAtZero: true,
//             suggestedMax: Math.max(
//               ...[
//                 data.backend.twoMonthsAgo,
//                 data.backend.lastMonth,
//                 data.backend.currentMonth,
//                 data.frontend.twoMonthsAgo,
//                 data.frontend.lastMonth,
//                 data.frontend.currentMonth,
//                 data.infra.twoMonthsAgo,
//                 data.infra.lastMonth,
//                 data.infra.currentMonth
//               ].map(v => v / 60)  // ← 時間をhに変換済み
//             ) * 1.1 // ← 最大値に10%の余白
//           }
//         }
//       }
//     });
//   });


// =======================================

document.addEventListener("DOMContentLoaded", () => {
  const canvas = document.getElementById("myChart");
  if (!canvas) return;

  // ★ API ではなく Thymeleaf から受け取る
  const data = window.CHART_DATA || {};

  const toHours = (m) => (typeof m === "number" ? m / 60 : 0);

  const labels = ["先々月", "先月", "今月"];

  // ラベル⇔データの対応をまとめて定義（増減に強い）
  const series = [
    { label: "バックエンド", key: "backend", color: "rgb(243, 181, 194)" },
    { label: "フロントエンド", key: "frontend", color: "#F7D1AA" },
    { label: "インフラ", key: "infra", color: "#FAE6B5" },
  ];

  const datasets = series.map(({ label, key, color }) => {
    const o = data?.[key] || {};
    return {
      label,
      data: [toHours(o.twoMonthsAgo), toHours(o.lastMonth), toHours(o.currentMonth)],
      backgroundColor: color
    };
  });

  // console.log("===========");
  // console.log(datasets);
  // console.log(datasets[0]);
  // console.log(datasets[0].data[0]);
  // console.log("===========");

  const suggestedMax =
    Math.max(0, ...datasets.flatMap(ds => ds.data)) * 1.1; // 10%余白

  new Chart(canvas, {
    type: "bar",
    data: { labels, datasets },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: "top" },

        tooltip: {
          callbacks: {
            label: (ctx) => `${ctx.dataset.label}: ${ctx.parsed.y.toFixed(1)} h`,
          },
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          suggestedMax,
          ticks: {
            callback: (v) => `${Number(v).toFixed(0)} h`,
          },
        },
      },
    },
  });
});