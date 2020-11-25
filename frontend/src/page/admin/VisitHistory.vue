<template>
  <div class="container">
    <b-table
      id="my-table"
      :items="items"
      :per-page="perPage"
      :current-page="currentPage"
      :fields="fields"
      head-variant="dark"
      hover
      text-center
      small
    ></b-table>
    <b-pagination
      v-model="currentPage"
      :total-rows="rows"
      :per-page="perPage"
      aria-controls="my-table"
      align="center"
      pills
      size="lg"
    ></b-pagination>
  </div>
</template>
<script>
import axios from "axios";
import constants from "@/lib/constants";
const baseURL = constants.baseUrl;

export default {
  name: "VisitHistory",
  data() {
    return {
      fields: [
        {
          key: "vid",
          label: "방문 번호",
        },
        {
          key: "uid",
          label: "회원 번호",
        },
        {
          key: "tel",
          label: "전화번호",
        },
        {
          key: "currenttime",
          label: "방문 시각",
        },
      ],
      perPage:10,
      currentPage: 1,
      items: [],
    };
  },
  created() {
    this.isAdmin();
    this.getVisitors();
  },
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
          if (res.data == false) {
            this.$router.push({ name: "forbidden" });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getVisitors() {
      axios
        .get(`${baseURL}/admin/getvisitors`)
        .then((res) => {
          this.items = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  computed: {
    rows() {
      return this.items.length;
    },
  },
};
</script>
<style scoped>
.container {
  width: 1000px;
  margin: 100px auto;
}
</style>