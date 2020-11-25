<template>
  <canvas id="popular-menu"></canvas>
</template>
<script>
import axios from "axios";
import constants from "@/lib/constants";
import Chart from "chart.js";

const baseURL = constants.baseUrl;
export default {
  name: "PopularMenu",
  data() {
    return {
      type: "pie",
      data: {
        labels: [],
        datasets: [
          {
            label: "이번달 인기 메뉴",
            data: [],
            backgroundColor: [
              "#ff5f34",
              "#41774e",
              "#003663",
              "#49acc5",
              "#c0504e",
            ],
            borderColor: ["black", "black", "black", "black", "black"],
            borderWidth: 1,
          },
        ],
      },
      options: {
        animation: {
          animateScale: true,
          animateRotate: true,
        },
        tooltips: {
          callbacks: {
            label: function (tooltipItem, data) {
              var dataset = data.datasets[tooltipItem.datasetIndex];
              var total = dataset.data.reduce(function (
                previousValue,
                currentValue,
                currentIndex,
                array
              ) {
                return previousValue + currentValue;
              });
              var currentValue = dataset.data[tooltipItem.index];
              var percentage = Math.floor((currentValue / total) * 100 + 0.5);
              return percentage + "%";
            },
          },
        },
        responsive: true,
        lineTension: 2,
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
                padding: 25,
              },
            },
          ],
        },
      },
    };
  },
  created() {
    this.getPopularMenu();
  },
  mounted() {
    setTimeout(() => {
      this.createChart("popular-menu");
    }, 500);
  },
  methods: {
    createChart(chartId) {
      const ctx = document.getElementById(chartId);

      const myChart = new Chart(ctx, {
        type: this.type,
        data: this.data,
        options: this.options,
      });
    },
    getPopularMenu() {
      axios
        .get(`${baseURL}/admin/popularmenu`)
        .then((res) => {
          const label = [];
          const tempData = [];
          for (let i = 0; i < res.data.length; i++) {
            label.push(res.data[i][0]);
            tempData.push(res.data[i][1]);
          }
          this.data.datasets[0].data = tempData;
          this.data.labels = label;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style scoped>
</style>