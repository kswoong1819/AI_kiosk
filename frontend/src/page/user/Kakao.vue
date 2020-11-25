<template>
  <div>
    <div v-if="isToken">
      <div class="btn" @click="logout">Logout</div>
    </div>
    <div v-if="!isToken">
      <div class="btn" @click="kakaojoin">Login</div>
    </div>
    <div v-if="isToken">
      <router-link to="/user/Mypage">
        <div class="btn">Purchase details</div>
      </router-link>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const baseURL = constants.baseUrl;
export default {
  name: "Kakao",
  data() {
    return {
      isToken: false,
    };
  },
  computed: {},
  watch: {},
  created() {
    this.islogin();
  },
  methods: {
    islogin() {
      this.isToken = this.$cookies.isKey("Auth-Token");
    },
    kakaoInfoUpdate: function(id) {
      this.$store.commit("kakaoIdUpdate", id);
    },
    kakaojoin() {
      let x = this;
      var kakaoToken = "";
      Kakao.Auth.login({
        success: function(authObj) {
          Kakao.API.request({
            url: "/v2/user/me",

            success: function(res) {
              x.kakao.uid = res.id;
              x.kakao.name = res.properties.nickname;

              axios
                .post(`${baseURL}/account/kakaologin`, x.kakao)
                .then((response) => {
                  if (response.data != "") {
                    kakaoToken = response.data;
                    x.$cookies.set("Auth-Token", kakaoToken);
                    x.$router.go("/");
                  } else {
                    x.kakaoInfoUpdate(res.id);
                    x.$router.push({ name: "join" });
                  }
                })
                .catch((err) => console.log(err.response));
            },
          });
        },

        fail: function(error) {
          alert(JSON.stringify(error));
        },
      });
    },

    logout: function() {
      this.$cookies.remove("Auth-Token");
      setTimeout(() => {
        this.$router.push("/").catch((err) => {
          console.log(err);
        });
        this.$router.go();
      }, 1000);
    },
  },

  data: function() {
    return {
      kakao: {
        uid: "",
        name: "",
      },
      constants,
      keyword: "",
    };
  },
};
</script>
<style scoped>
.btn {
  font-size: 30px;
  color: white;
}
</style>
