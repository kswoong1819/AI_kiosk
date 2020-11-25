<template>
  <canvas id="planet-chart"></canvas>
</template>
<script>
import axios from "axios";
import constants from "@/lib/constants";
import Chart from "chart.js";

const baseURL = constants.baseUrl;

export default {
  name: "Chart",
  data() {
    return {
      type: "line",
      data: {
        labels: [
          "1월",
          "2월",
          "3월",
          "4월",
          "5월",
          "6월",
          "7월",
          "8월",
          "9월",
          "10월",
          "11월",
          "12월",
        ],
        datasets: [
          {

            label: "월별 매출액(천원)",
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            backgroundColor: ["rgba(71, 183,132,.5)"],
            borderColor: ["#47b784"],
            borderWidth: 3,
          },
          {
            label: "월별 방문자 수",
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            backgroundColor: ["rgba(54,73,93,.5)"],
            borderColor: ["#36495d"],
            borderWidth: 3,
          },
        ],
      },
      options: {
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
    this.getMonthIncome();
    this.getmonthvisitors();
  },
  mounted() {
    setTimeout(() => {
      this.createChart('planet-chart');
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
    getMonthIncome() {
      axios
        .get(`${baseURL}/admin/getmonthincome`)
        .then((res) => {
          for (let i = 0; i < res.data.length; i++) {
            this.data.datasets[0].data[res.data[i][0] - 1] =
              res.data[i][1] * 0.001;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getmonthvisitors() {
      axios
        .get(`${baseURL}/admin/getmonthvisitors`)
        .then((res) => {
          for (let i = 0; i < res.data.length; i++) {
            this.data.datasets[1].data[res.data[i][0] - 1] = res.data[i][1];
          }
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