<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-12">
        <div
          class="card text-center justify-content-center shaodw-lg card-1 border-0 bg-white px-sm-2"
        >
          <div class="card-body show ">
            <div class="row mb-3">
              <div class="col">
                <h1 class="mt-2" style="font-size: 40px">
                  <b>체크인을 해주세요</b>
                </h1>
                <p style="float:right;">
                  <b-button
                    style="width:100px; height: 30px; background-color: pink; border:0"
                    v-b-modal.modalPopover
                    >회원가입 QR코드</b-button
                  >

                  <b-modal id="modalPopover" ok-only size="sm">
                    <div
                      class="w-100 d-flex justify-content-center align-items-center"
                    >
                      <a
                        rel="nofollow"
                        href="https://ko.qr-code-generator.com/"
                        border="0"
                        style="cursor:default"
                        ><img
                          src="https://chart.googleapis.com/chart?cht=qr&chl=http%3A%2F%2Fk3b107.p.ssafy.io%2F&chs=180x180&choe=UTF-8&chld=L|2"
                          alt=""
                      /></a>
                    </div>
                  </b-modal>
                </p>
              </div>
            </div>
            <div
              class="radio-group row justify-content-between px-3 text-center a"
            >
              <div class="col-auto mr-5 mx-1 card-block py-0 text-center radio">
                <div class="flex-row">
                  <div class="col">
                    <div class="pic" @click="Member()">
                      <img
                        src="~@/assets/img/member.png"
                        width="200"
                        height="200"
                      />
                    </div>
                    <p class="texts mt-5">회원</p>
                  </div>
                </div>
              </div>
              <div class="mt-3 col-auto mx-1 card-block py-0 text-center radio">
                <div class="flex-row">
                  <div class="col">
                    <div class="pic" @click="nonMember">
                      <img
                        src="~@/assets/img/rejected.png"
                        width="200"
                        height="200"
                      />
                    </div>
                    <p class="texts mt-5">비회원</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const baseURL = constants.baseUrl;
export default {
  name: "KioskMain",

  methods: {
    Member() {
      this.$router.push("facecheck");
    },
    nonMember(){
      this.$router.push('qrcheck')
    }
  },
};
</script>

<style scoped>

.texts {
  font-size: 30px;
}

.btn-primary {
  background-color: #42a5f5 !important;
  border-color: #42a5f5 !important;
}

.cursor-pointer {
  cursor: pointer;
  color: #42a5f5;
}

.pic {
  margin-top: 13%;
  margin-bottom: 20px;
}

.card-block {
  border: 1px solid lightgrey;
  border-radius: 5px !important;
  background-color: #fafafa;
  margin-bottom: 30px;
}

.card-body.show {
  display: block;
}

.card {
  margin-top: 10%;
  height: 70vh;
  padding-bottom: 20px;
  box-shadow: 2px 2px 6px 0px lightgray;
}

.radio {
  width: 40%;
  display: inline-block;
  border-radius: 0;
  box-sizing: border-box;
  cursor: pointer;
  color: #000;
  font-weight: 500;
  -webkit-filter: grayscale(100%);
  -moz-filter: grayscale(100%);
  -o-filter: grayscale(100%);
  -ms-filter: grayscale(100%);
  filter: grayscale(100%);
}

.radio:hover {
  box-shadow: 2px 2px 2px 2px rgba(0, 0, 0, 0.5);
}

.radio {
  box-shadow: 0px 8px 16px 0px #eeeeee;
  -webkit-filter: grayscale(0%);
  -moz-filter: grayscale(0%);
  -o-filter: grayscale(0%);
  -ms-filter: grayscale(0%);
  filter: grayscale(0%);
}

.selected {
  background-color: #e0f2f1;
}

.a {
  justify-content: center !important;
  height: 50vh;
}

.btn {
  border-radius: 0px;
}

.btn,
.btn:focus,
.btn:active {
  outline: none !important;
  box-shadow: none !important;
}

</style>
