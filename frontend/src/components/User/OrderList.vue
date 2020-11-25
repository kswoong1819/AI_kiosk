<template>
  <div class="container">
    <div class="mt-5 ml-5  col-9" style="float:left;">
      <div
        class="mb-5"
        v-for="(menu, i) in Mymenu"
        :key="i"
        data-holder-rendered="true"
      >
        <div>
          <button>
            {{ i.substring(5, 7) }}월 {{ i.substring(8, 11) }}일
            {{ i.substring(11, 13) }}시 {{ i.substring(14, 16) }}분
          </button>
          <div
            v-for="(menuDetail, j) in Mymenu[i]"
            :key="j"
            id="collapse-1"
            class="mt-2"
          >
            <b-card class="row">
              {{ j + 1 }}
              <div class="col-3">
                <b-img class="" :src="menuDetail[3]" height="100px"></b-img>
              </div>
              <div class="col-5">{{ menuDetail[2] }}</div>
              <div class="col-2">{{ menuDetail[4] }}</div>
            </b-card>
          </div>
        </div>
      </div>
     </div>

    <div class="col-2" style="float:right;">
      많이 구매한 상점 내역<br />

      총액 :
    </div>
  </div>

</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const baseURL = constants.baseUrl;

export default {
  name: "OrderList",
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
      Mymenu: [],
      MymenuDate: [],
    };
  },
  created() {
    this.getUserInfo();
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
          this.getUserOrderList();
        })
        .catch((err) => console.log(err));
    },
    getUserOrderList() {
      axios
        .get(baseURL + "/mypage/searchorder", {
          params: { sid: 1, uid: this.form.uid },
        })
        .then((res) => {
          this.Mymenu = res.data.object;

          for (var i = 0; i < this.Mymenu.length; i++) {
            const day = this.Mymenu[i][0];
            this.Mymenu[i][0] = day;
          }
          var tmp = {};
          for (let j = 0; j < this.Mymenu.length; j++) {
            const aa = this.Mymenu[j][0];
            if (aa in tmp) {
              tmp[aa].push(this.Mymenu[j]);
            } else {
              tmp[aa] = [];
              tmp[aa].push(this.Mymenu[j]);
            }
          }
          this.Mymenu = tmp;
        })
        .catch((err) => console.log(err.response));
    },
  },
};
</script>

<style scoped>
.row > div {
  display: flex;
  align-items: center;
  justify-content: center;
}
.row {
  font-size: 20px;
}
</style>
