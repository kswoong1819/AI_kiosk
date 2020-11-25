<template>
  <div class="container">
    <div>
        <h1 style="text-align:center;">매장에서 가장 잘 팔리는 메뉴 5가지</h1>
        <popular-menu></popular-menu>    
    </div> 
      <br />
      <br />
      <br />
      <h1 style="text-align:center;">오늘의 결제 내역</h1>
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
import PopularMenu from "@/components/admin/PopularMenu.vue"
const baseURL = constants.baseUrl;

export default {
  name: "PurchaseHistory",
  components:{
      PopularMenu,
  },
  data() {
    return {
      fields: [
        {
          key: "1",
          label: "회원 번호",
        },
        {
          key: "2",
          label: "메뉴 이름",
        },
        {
          key: "3",
          label: "메뉴 가격",
        },
        {
          key: "4",
          label: "주문 시각",
          sortable: true    
        },
      ],
      perPage: 10,
      currentPage: 1,
      items: [],
    };
  },
  created() {
    this.isAdmin();
    this.getPayment();
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
    getPayment() {
      axios
        .get(`${baseURL}/admin/getpayment`)
        .then((res) => {
          this.items = res.data;
        })
        .catch((err) => {
          console.log(err)
        })
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