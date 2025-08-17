const ctx = document.getElementById('myChart');

new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ['先々月', '先月', '今月'],
    datasets: [
      {
        label: 'バックエンド',
        data: [12, 4, 3],
        borderWidth: 1,
        backgroundColor: "rgb(243, 181, 194)",
      },
      {
        label: 'フロントエンド',
        data: [2, 3, 18],
        borderWidth: 1,
        backgroundColor: "#F7D1AA",
      },
      {
        label: 'インフラ',
        data: [8, 10, 1],
        borderWidth: 1,
        backgroundColor: "#FAE6B5"
      }
    ]
  },
  options: {
    scales: {
      yAxes: [{
        ticks: {
          suggestedMax: 100,
          suggestedMin: 0,
          stepSize: 10,

        }
      }]
    },
  }
});

