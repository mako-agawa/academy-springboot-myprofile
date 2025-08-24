fetch("/api/chart-data")
  .then(res => res.json())
  .then(data => {
    new Chart(document.getElementById("myChart"), {
      type: "bar",
      data: {
        labels: ["先々月", "先月", "今月"],
        datasets: [
          {
            label: "バックエンド",
            data: [data.backend.twoMonthsAgo / 60, data.backend.lastMonth / 60, data.backend.currentMonth / 60],
            backgroundColor: "rgb(243, 181, 194)"
          },
          {
            label: "フロントエンド",
            data: [data.frontend.twoMonthsAgo / 60, data.frontend.lastMonth / 60, data.frontend.currentMonth / 60],
            backgroundColor: "#F7D1AA"
          },
          {
            label: "インフラ",
            data: [data.infra.twoMonthsAgo / 60, data.infra.lastMonth / 60, data.infra.currentMonth / 60],
            backgroundColor: "#FAE6B5"
          }
        ]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            suggestedMax: Math.max(
              ...[
                data.backend.twoMonthsAgo,
                data.backend.lastMonth,
                data.backend.currentMonth,
                data.frontend.twoMonthsAgo,
                data.frontend.lastMonth,
                data.frontend.currentMonth,
                data.infra.twoMonthsAgo,
                data.infra.lastMonth,
                data.infra.currentMonth
              ].map(v => v / 60)  // ← 時間をhに変換済み
            ) * 1.1 // ← 最大値に10%の余白
          }
        }
      }
    });
  });