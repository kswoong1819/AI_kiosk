<template>
  <div class="container">
        <div class="row">
      <div class="col"></div>
      <div class="h1 col-6 text-center" style="font-weight:bold; border-bottom:solid 2px black;">
        관리자 페이지
      </div>
      <div class="col">
        <div class="h1 lsize text-center" @click="logout">
      Logout
    </div>
      </div>
    </div>
    
    <div class="row">
      <div class="col-lg" @click="goVisitHistory">
        <div class="card text-center c-hover" style="width: 100%; height: 100%">
          <div class="card-body">
            <h1 class="card-title">오늘의 방문자 수</h1>
            <p class="card-text" style="font-size: 3em">
              {{ todayCount }}
            </p>
            <!-- <b-btn
              href="#"
              class="card-link"
              router-link
              :to="{ name: 'visithistory' }"
              >방문기록 확인</b-btn
            > -->
          </div>
        </div>
      </div>
      <div class="col-lg" @click="goMenulist">
        <div class="card text-center c-hover" style="width: 100%; height: 100%">
          <div class="card-body">
            <h1>메뉴 관리</h1>
          </div>
        </div>
      </div>
      <div class="col-lg" @click="goPurchaseHistory">
        <div class="card text-center c-hover" style="width: 100%; height: 100%">
          <div class="card-body">
            <h1 class="card-title">결제 내역</h1>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-lg">
        <div class="card" style="width: 100%; height: 100%">
          <div class="card-body">
            <h1 class="card-title text-center">월별 매출액 & 방문자 수</h1>

            <chart></chart>
            <!-- <a href="#" class="card-link">Card link</a>
            <a href="#" class="card-link">Another link</a> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import constants from "@/lib/constants";
import Chart from "@/components/admin/Chart.vue";

const baseURL = constants.baseUrl;

export default {
  name: "DashBoard",
  components: {
    Chart,
  },
  data() {
    return {
      todayCount: 0,
    };
  },
  created() {
    this.isAdmin();
    this.getTodayVisitor();
  },
  mounted() {},
  methods: {
    isAdmin() {
      const axiosConfig = {
        headers: {
          jwtToken: `${this.$cookies.get("Admin-Auth-Token")}`,
        },
      };
      axios
        .post(`${baseURL}/admin/isAdmin`, "", axiosConfig)
        .then((res) => {
          // console.log(res);
          if (res.data == false) {
            // this.$router.push({ name: "dashboard" });
            this.$router.push({ name: "forbidden" });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getTodayVisitor() {
      axios
        .get(`${baseURL}/admin/getvisitorcount`)
        .then((res) => {
          this.todayCount = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    logout() {
      this.$cookies.remove("Admin-Auth-Token");
      this.$router.push({name:"admin"})
  },
  goVisitHistory(){
    this.$router.push('visithistory')
  },
  goMenulist(){
    this.$router.push('menulist')
  },
  goPurchaseHistory(){
    this.$router.push('purchasehistory')
  }

  }
}
</script>
<style scoped>
.container {
  background-color: white;
  width: 1000px;
  margin: 60px auto;
  padding: 50px;
}
.row {
  margin-bottom: 50px;
}
.lsize{
  background-color: #f4511e;
  border: none;
  color: white;
  padding: 16px 32px;
  text-align: center;
  font-size: 16px;
  margin: 4px 2px;
  opacity: 0.6;
  transition: 0.3s;
}
.lsize:hover {
  opacity: 1;
}
.c-hover:hover{
  background-color:gray;
}
</style>
