// document.addEventListener("DOMContentLoaded", () => {
//   const canvas = document.getElementById("myChart");
//   if (!canvas) return;

//   // ★ API ではなく Thymeleaf から受け取る
//   const data = window.CHART_DATA || {};

//   const toHours = (m) => (typeof m === "number" ? m / 60 : 0);

//   const labels = ["先々月", "先月", "今月"];

//   // ラベル⇔データの対応をまとめて定義（増減に強い）
//   const series = [
//     { label: "バックエンド", key: "backend", color: "rgb(243, 181, 194)" },
//     { label: "フロントエンド", key: "frontend", color: "#F7D1AA" },
//     { label: "インフラ", key: "infra", color: "#FAE6B5" },
//   ];

//   const datasets = series.map(({ label, key, color }) => {
//     const o = data?.[key] || {};
//     return {
//       label,
//       data: [toHours(o.twoMonthsAgo), toHours(o.lastMonth), toHours(o.currentMonth)],
//       backgroundColor: color
//     };
//   });

//   console.log("===========");
//   console.log(datasets);
//   console.log(datasets[0]);
//   console.log(datasets[0].data[0]);
//   console.log("===========");

//   const suggestedMax =
//     Math.max(0, ...datasets.flatMap(ds => ds.data)) * 1.1; // 10%余白

//   new Chart(canvas, {
//     type: "bar",
//     data: { labels, datasets },
//     options: {
//       responsive: true,
//       maintainAspectRatio: false,
//       plugins: {
//         legend: { position: "top" },

//         tooltip: {
//           callbacks: {
//             label: (ctx) => `${ctx.dataset.label}: ${ctx.parsed.y.toFixed(1)} h`,
//           },
//         },
//       },
//       scales: {
//         y: {
//           beginAtZero: true,
//           suggestedMax,
//           ticks: {
//             callback: (v) => `${Number(v).toFixed(0)} h`,
//           },
//         },
//       },
//     },
//   });
// });

// ========パターンB======

// /script/learning-chart.js
document.addEventListener("DOMContentLoaded", () => {
  const canvas = document.getElementById("myChart");
  if (!canvas) return;

  // Thymeleaf から: { "YYYY-MM": { "バックエンド": 分, "フロントエンド": 分, "インフラ": 分 }, ... }
  const dataByMonth = window.CHART_DATA || {};

  const toHours = (m) => (typeof m === "number" ? m / 60 : 0);

  // 日本時間で Year-Month を "YYYY-MM" 文字列に
  const ym = (date) => {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, "0");
    return `${y}-${m}`;
  };

  // 今月/先月/先々月のキーを作る
  const now = new Date();
  const current = new Date(now.getFullYear(), now.getMonth(), 1);
  const last = new Date(now.getFullYear(), now.getMonth() - 1, 1);
  const twoAgo = new Date(now.getFullYear(), now.getMonth() - 2, 1);

  const monthKeys = [ym(twoAgo), ym(last), ym(current)]; // ["2025-06","2025-07","2025-08"]
  const labels = ["先々月", "先月", "今月"];

  // シリーズ設定（データ内の“日本語カテゴリ名”を参照）
  const series = [
    { label: "バックエンド", color: "rgb(243, 181, 194)" },
    { label: "フロントエンド", color: "#F7D1AA" },
    { label: "インフラ", color: "#FAE6B5" },
  ];

  const datasets = series.map(({ label, color }) => {
    const arr = monthKeys.map((k) => toHours(dataByMonth[k]?.[label] ?? 0));
    return { label, data: arr, backgroundColor: color };
  });

  const suggestedMax = Math.max(100, ...datasets.flatMap((d) => d.data));

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
          ticks: { callback: (v) => `${Number(v).toFixed(0)} h` },
        },
      },
    },
  });
});