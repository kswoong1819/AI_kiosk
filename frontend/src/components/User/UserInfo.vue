<template>
  <div class="container">
    <h1 class="border-bottom border-secondary" style="text-align: center">
      <strong>MY PAGE</strong>
    </h1>
    <br />
    <div>
      <table class="table table-bordered table-striped table-hover">
        <thead class="thead-dark">
          <tr>
            <th scope="col">#</th>
            <th scope="col">정보</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th scope="row">이름</th>
            <td>{{ form.name }}</td>
          </tr>
          <tr>
            <th scope="row">이메일</th>
            <td>{{ form.email }}</td>
          </tr>
          <tr>
            <th scope="row">성별</th>
            <td>{{ form.gender }}</td>
          </tr>
          <tr>
            <th scope="row">나이</th>
            <td>{{ form.age }}</td>
          </tr>
          <tr>
            <th scope="row">전화번호</th>
            <td>{{ form.tel }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-button
      fill
      block
      variant="info"
      router-link
      :to="{ name: 'userupdate' }"
    >
      개인정보 수정</b-button
    >
  </div>
</template>
<script>
import axios from "axios";
import constants from "../../lib/constants";

export default {
  name: "UserInfo",
  data() {
    return {
      form: {
        uid: "",
        email: "",
        name: "",
        gender: "",
        age: "",
        tel: "",
        learningfile: "",
      },
    };
  },
  created() {
    if (this.$cookies.get("Auth-Token") == null) {
      this.$router.push("/");
    } else {
      this.getUserInfo();
    }
  },
  methods: {
    getUserInfo() {
      const axiosConfig = {
        headers: {
          jwtToken: `${this.$cookies.get("Auth-Token")}`,
        },
      };
      axios
        .post(`${constants.baseUrl}/authuser`, "", axiosConfig)
        .then((res) => {
          this.form = res.data;
        })
        .catch((err) => console.log(err));
    },
  },
};
</script>
<style scoped>
</style>