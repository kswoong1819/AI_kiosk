<template>
  <div class="container">
    <add-menu></add-menu>
    <b-table
      id="my-table"
      :items="items"
      :per-page="perPage"
      :current-page="currentPage"
      :fields="fields"
      head-variant="dark"
      text-center
      small
      hover
      outlined
      responsive="sm"
    >
      <template #cell(actions)="row" style="width: 200px">
        <b-btn
          class="btn-margin"
          pill
          id="show-btn"
          block
          variant="info"
          @click="showModal(row.item.menuid)"
          >메뉴 수정</b-btn
        >
        <b-btn
          class="btn-margin"
          pill
          variant="outline-danger"
          @click="deleteMenu(row.item.menuid)"
          >메뉴 삭제</b-btn
        >
        <!-- <b-btn id="show-btn" @click="test">메뉴 수정</b-btn> -->
      </template>

      <template #cell(name)="data"> {{ data.item.name }} </template>

      <template #cell(image)="data">
        <b-img :src="data.item.image" rounded />
      </template>
    </b-table>
    <div class="mt-3">
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
    <update-menu ref="updateMenu" :menuinfo="menuinfo"></update-menu>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";
import AddMenu from "@/components/admin/AddMenu.vue";
import UpdateMenu from "@/components/admin/UpdateMenu.vue";
const baseURL = constants.baseUrl;

export default {
  name: "MenuList",
  components: {
    AddMenu,
    UpdateMenu,
  },
  data() {
    return {
      fields: [
        { key: "actions", label: "Actions" },
        {
          key: "sid",
          label: "지점 번호",
        },
        {
          key: "menuid",
          label: "메뉴 번호",
          sortable: true,
        },
        {
          key: "name",
          label: "메뉴 이름",
        },
        {
          key: "description",
          label: "메뉴 설명",
        },
        {
          key: "price",
          label: "메뉴 가격",
          sortable: true,
        },
        {
          key: "image",
          label: "메뉴 사진",
        },
        {
          key: "kcal",
          // label:'kcal'
          sortable: true,
        },
        {
          key: "category1",
          label: "category1",
        },
        {
          key: "category2",
          label: "category2",
        },
      ],
      perPage: 10,
      currentPage: 1,
      menuAll: {},
      items: [],
      menuinfo: {},
    };
  },
  created() {
    this.isAdmin();
    this.getMenuList(); //로딩시 메뉴 가져오기
  },
  computed: {
    rows() {
      return this.items.length;
    },
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
    showModal(mid) {
      this.getMenuInfo(mid);
      this.$refs.updateMenu.$refs["my-modal"].show();
    },
    getMenuList() {
      axios
        .get(`${baseURL}/branch/menu`, { params: { sid: 1 } })
        .then((res) => {
          this.items = res.data.object;
        })
        .catch((err) => console.log(err.response));
    },
    getMenuInfo(mid) {
      axios
        .get(`${baseURL}/admin/menuinfo`, { params: { menuid: mid } })
        .then((res) => {
          this.menuinfo = res.data;
        })
        .catch((err) => console.log(err.response));
    },

    deleteMenu(mid) {
      axios
        .delete(`${baseURL}/admin/deletemenu`, { params: { menuid: mid } })
        .then((res) => {
          // console.log(res);
          alert("삭제되었습니다.");
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style scoped>
.container {
  width: 1400px;
  margin: 100px auto;
  padding: 20px;
}
.btn-margin {
  margin: 15px 0;
}
img {
  width: 100px;
}
</style>
