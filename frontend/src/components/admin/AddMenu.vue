<template>
  <div class="container2">
      <div class="row">
    <div class="col">
    </div>
    <div class="col-6">
      <b-button id="show-btn" block variant="success" @click="showModal"><h1>메뉴 추가</h1></b-button>
    </div>
    <div class="col">
    </div>
  </div>
    

    <b-modal ref="my-modal" hide-footer title="새로운 메뉴 추가">
      <div>
        <b-form @submit="onSubmit" v-if="show">
          <b-form-group
            id="input-group-1"
            label="지점 번호"
            label-for="input-1"
          >
            <b-form-input
              id="input-1"
              v-model="form.sid"
              type="number"
              required
              placeholder="지점 번호를 입력하세요."
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-2"
            label="메뉴 이름:"
            label-for="input-2"
          >
            <b-form-input
              id="input-2"
              v-model="form.name"
              required
              placeholder="Enter name"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-3"
            label="메뉴 설명:"
            label-for="input-3"
          >
            <b-form-input
              id="input-3"
              v-model="form.description"
              required
              placeholder="Enter Description"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-4"
            label="메뉴 가격:"
            label-for="input-4"
          >
            <b-form-input
              id="input-4"
              v-model="form.price"
              required
              placeholder="Enter Price"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-5"
            label="메뉴 사진:"
            label-for="input-5"
          >
            <b-form-input
              id="input-5"
              v-model="form.image"
              required
              placeholder="Enter image"
            ></b-form-input>
          </b-form-group>

          <b-form-group id="input-group-6" label="Kcal:" label-for="input-6">
            <b-form-input
              id="input-6"
              v-model="form.kcal"
              required
              placeholder="Enter Kcal"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-7"
            label="Menu Category1:"
            label-for="input-7"
          >
            <b-form-input
              id="input-7"
              v-model="form.category1"
              required
              placeholder="Enter name"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            id="input-group-8"
            label="Menu Category2:"
            label-for="input-8"
          >
            <b-form-input
              id="input-8"
              v-model="form.category2"
              required
              placeholder="Enter name"
            ></b-form-input>
          </b-form-group>
          <b-button type="submit" class="mt-2" variant="outline-warning" block
            >메뉴 추가</b-button
          >
          <b-button
            class="mt-3"
            variant="outline-danger"
            block
            @click="hideModal"
            >취소</b-button
          >
        </b-form>
      </div>

      <div class="d-block text-center"></div>
    </b-modal>
  </div>
</template>
<script>
import axios from "axios";
import constants from "@/lib/constants";

const baseURL = constants.baseUrl;

export default {
  name: "AddMenu",
  data() {
    return {
      form: {
        sid: "",
        menuid: "",
        name: "",
        description: "",
        price: "",
        image: "",
        kcal: "",
        category1: "",
        category2: "",
      },
      show: true,
    };
  },
  methods: {
    showModal() {
      this.$refs["my-modal"].show();
    },
    hideModal() {
      this.$refs["my-modal"].hide();
    },
    onSubmit() {
      axios
        .post(`${baseURL}/admin/addmenu`, this.form)
        .then((res) => {
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getMenuInfo() {
      axios
        .get(`${baseURL}/admin/menuinfo`, {
          params: { menuid: this.form.menuid },
        })
        .then((res) => {
          // console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style scoped>
.container2{
  width: 100%;
  height: 100px;
  margin: 20px auto;
  padding: 0;
  
}
#show-btn{
  height:100px;
}
</style>
