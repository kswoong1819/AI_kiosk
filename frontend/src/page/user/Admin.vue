
<template>
  <div class="container">
    <p class="h1 text-center mb-4">관리자 로그인</p>
    <label for="defaultFormLoginEmailEx" class="grey-text"
      ><h3>관리자 아이디 :</h3></label
    >
    <input
      @keyup.enter="onSubmit"
      type="text"
      v-model="loginForm.aid"
      id="defaultFormLoginEmailEx"
      class="form-control"
    />
    <br />
    <label for="defaultFormLoginPasswordEx" class="grey-text"
      ><h3>관리자 비밀번호 :</h3></label
    >
    <input
      @keyup.enter="onSubmit"
      type="password"
      v-model="loginForm.password"
      id="defaultFormLoginPasswordEx"
      class="form-control"
    />

    <div class="text-center mt-4">
      <button
        class="btn btn-dark btn-lg btn-block"
        @click="onSubmit"
        type="button"
      >
        Login
      </button>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import constants from "@/lib/constants";

const baseURL = constants.baseUrl;

export default {
  name: "Admin",
  data() {
    return {
      loginForm: {
        aid: "",
        password: "",
      },
    };
  },
  methods: {
    onSubmit() {
      if (this.aid == "") {
        alert("아이디를 입력해주세요.");
      } else if (this.password == "") {
        alert("비밀번호를 입력해주세요.");
      } else {
        axios
          .post(`${baseURL}/admin/login`, this.loginForm)
          .then((res) => {
            if (res.data != "") {
              let jwtToken = res.data;
              this.$cookies.set("Admin-Auth-Token", jwtToken);
              // console.log(jwtToken)
              this.$router.push({ name: "dashboard" });
            } else {
              alert("존재하지 않는 계정이거나 비밀번호가 틀렸습니다.");
            }
          })
          .catch((err) => {
            console.log(err)
          });
      }
    },
  },
};
</script>
<style scoped>
.container {
  background-color: white;
  width: 1000px;
  margin: 100px auto;
  border: solid 1px black;
  padding: 60px 200px;
}
</style>