<template>
  <div v-on:click.left="clickEvent()" style="height:100vh; overflow: hidden;" class = "test">
    <div>
      <b-sidebar id="sidebar-right" backdrop title="장바구니" right shadow width=400px>
        <div class="px-2 py-2">
          <div class="d-flex mb-4" v-for="(menu, k) in basket" :key="k">
            <th>
              <b-img
                :src="menu.image"
                class="rounded image mb-2"
                width="140%"
              ></b-img>
            </th>
            <div class="px-3 py-3 container col-8">
              <div style="font-size: 25px">
                {{
                  menu.name
                }}
              </div>
                <div style="font-size: 25px;" class = "text-danger">
                {{
                  numberWithCommas(menu.price)
                }}원
                </div>
                <div style ="text-align:right;" class="col-12">
                  <b-icon
                  icon="x-circle"
                  scale="2"
                  variant="danger"
                  @click="DeleteBasket(menu)"
                  class="click"
                ></b-icon>
                <p>             </p>

                </div>
            </div>
 
            <br /><br />
          </div>

        </div>
        <div class="px-2">
          <th>
          <td>
          <p style="font-size: 40px; width : 150px">총 금액 : </p>
          </td>
          <td>
          <p style="font-size: 40px;" class = "text-danger"> {{ numberWithCommas(basketPrice) }}원</p> 
          </td>
          </th>
          <div>
            <th>
            <td v-if ="basket!=''">
            <b-button variant ="success" style="width: 190px; height:60px; font-size:20px;" v-b-modal.modal-1>결제하기</b-button>
            </td>
            <td>
            <b-button  variant ="danger" style="width: 190px; height:60px; font-size:20px;" v-b-modal.modal-2>취소하기</b-button>
            </td>
            </th>
            
            <b-modal id="modal-1" centered size = "lg" style="font-size: 50px">
              <div>
                    <p class = "my-4" style="font-size:50px; text-align:center;">선택해 주세요</p>
                <th>
                  <td style="text-align:center;">
                    <img src="@/assets/img/takeout.png" style="width : 90%;" @click="purchase()" class = "hover">
                    <p class = "my-4" style="font-size:35px;">포장 주문</p>
                  </td>
                  <td style="text-align:center;">
                    <img src="@/assets/img/coffee.png" style="width : 90%;" @click="purchase()" class = "hover">
                    <p class = "my-4" style="font-size:35px;">매장 식사</p>
                  </td>
                </th> 
              </div>
              <template #modal="{cancel}">
                <b-button style="width:70px; height:50px; font-size:15px;" variant="danger" @click="cancel()">
                  Cancel
                </b-button>
                </template>
            </b-modal>

            <b-modal id="modal-2" centered button-size="lg" @ok="okcancel()" style="font-size:50px;">
              <p class = "my-4" style="font-size:50px; text-align:center;">정말 취소 하시겠습니까?</p>
                <template #modal="{ ok,cancel }">
                <b-button style="width:70px; height:50px; font-size:15px;" variant="success" @click="ok()">
                  OK
                </b-button>
                <b-button style="width:70px; height:50px; font-size:15px;" variant="danger" @click="cancel()">
                  Cancel
                </b-button>
                </template>
            </b-modal>
          </div>
        </div>

      </b-sidebar>
    </div>

    <div class="mt-1 p-0">
      <div id="body" >
        <div id="content">
          <b-card no-body>
            <b-tabs
              align="center"
              active-nav-item-class="font-weight-bold text-uppercase text-danger"
              active-tab-class="font-weight-bold"
              style="font-size: 40px"
              >
              <div v-if="loginCheck == 1">
              <b-tab 
              title-link-class="text-dark"
              title="추천">
                <div class="overflow">
                    <br />
                    <!-- 최근 먹은 메뉴-->

                    <div  v-if="basketRecent.length > 0" class="row container-fluid">
                    <div class = "col-10" >
                      <p style = "font-size:45px; color:#65ca00">최근 먹은 메뉴</p>
                    </div>
                      <div
                        v-for="(slide, index) in Recent"
                        :key="index"
                        class="col-4"
                      >
                        <div v-if="index<3" class ="divclass">
                          <div
                            class="m-3 hover"
                            @click="GetMenuId(slide)"
                            v-b-toggle.sidebar-right
                          >
                            <div style="text-align: center;">
                              <img
                                style="width: 70%"
                                :src="slide.image"
                                class="rounded image mb-2"
                              />
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-dark test">
                              {{ slide.name }}
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-danger">
                              {{ numberWithCommas(slide.price) }}원
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <br/>
                    <!-- 인기 음료 메뉴-->
                    <div  v-if="basketPopular.length > 0" class="row container-fluid">
                      <div class = "col-11">
                      <p style = "margin-left:5px; font-size:45px; color:#65ca00">인기 음료 메뉴</p>
                    </div> 
                      <div
                        v-for="(slide, jndex) in basketPopular"
                        :key="jndex"
                        class="col-4"
                      >
                        <div v-if="jndex<3" class ="divclass">
                          <div
                            class="m-3 hover"
                            @click="GetMenuId(slide)"
                            v-b-toggle.sidebar-right
                          >
                            <div style="text-align: center;">
                              <img
                                style="width: 70%"
                                :src="slide.image"
                                class="rounded image mb-2"
                              />
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-dark test">
                              {{ slide.name }}
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-danger">
                              {{ numberWithCommas(slide.price) }}원
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <br/>
                    <!-- 인기 푸드 메뉴-->
                    <div  v-if="basketFoodPopular.length > 0" class="row container-fluid">
                      <div class = "col-11">
                      <p style = "margin-left:5px; font-size:45px; color:#65ca00">인기 푸드 메뉴</p>
                    </div> 
                      <div
                        v-for="(slide, kndex) in basketFoodPopular"
                        :key="kndex"
                        class="col-4"
                      >
                        <div v-if="kndex<3" class ="divclass">
                          <div
                            class="m-3 hover"
                            @click="GetMenuId(slide)"
                            v-b-toggle.sidebar-right
                          >
                            <div style="text-align: center;">
                              <img
                                style="width: 70%"
                                :src="slide.image"
                                class="rounded image mb-2"
                              />
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-dark test">
                              {{ slide.name }}
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-danger">
                              {{ numberWithCommas(slide.price) }}원
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>


                </div>
              </b-tab>
              </div>


              <b-tab 
                title-link-class="text-dark"      
                @click="seperateCate(1, 1); rightTmp(); clickEvent();" 
                title="음료">
                <div>
                  <div>
                    <b-tabs
                      content-class="mt-5"
                      align="center"
                      style="font-size: 30px"
                      active-nav-item-class="font-weight-bold text-uppercase text-danger"
                    >
                      <b-tab
                        title-link-class="text-success"
                        title="콜드 브루"
                        @click="seperateCate(1, 1); rightTmp(); clickEvent();"
                      ></b-tab>
                      <b-tab 
                        title-link-class="text-success"
                        title="리저브" 
                        @click="seperateCate(1, 2); rightTmp(); clickEvent();"></b-tab>
                      <b-tab
                        title-link-class="text-success"
                        title="에스프레소"
                        @click="seperateCate(1, 3); rightTmp(); clickEvent();"
                      ></b-tab>
                      <b-tab 
                        title-link-class="text-success"
                        title="블론드" 
                        @click="seperateCate(1, 5); rightTmp(); clickEvent();"></b-tab>
                      <b-tab
                        title-link-class="text-success"
                        title="프라푸치노"
                        @click="seperateCate(1, 6); rightTmp(); clickEvent();"
                      ></b-tab>
                      <b-tab
                        title-link-class="text-success"
                        title="블렌디드"
                        @click="seperateCate(1, 7); rightTmp(); clickEvent();"
                      ></b-tab>
                      <b-tab 
                        title-link-class="text-success"
                        title="피지오" 
                        @click="seperateCate(1, 8); rightTmp(); clickEvent();"></b-tab>
                      <b-tab 
                        title-link-class="text-success"
                        title="티바나" 
                        @click="seperateCate(1, 9); rightTmp(); clickEvent();"></b-tab>
                      <b-tab
                        title-link-class="text-success"
                        title="브루드 커피"
                        @click="seperateCate(1, 10); rightTmp(); clickEvent();"
                      ></b-tab>
                      <b-tab 
                        title-link-class="text-success"
                        title="기타" 
                        @click="seperateCate(1, 11); rightTmp(); clickEvent();"></b-tab>
                      <b-tab
                        title-link-class="text-success"
                        title="병음료"
                        @click="seperateCate(1, 12); rightTmp(); clickEvent();"
                      ></b-tab>


                      <div class="row overflow container-fluid">
                        <div
                          v-for="(menu, i) in menusCateTmp"
                          :key="i"
                          class="col-4"
                        >
                          <div
                            class="m-3 hover"
                            v-b-toggle.sidebar-right
                            @click="GetMenuId(menu)"
                          >
                            <div style = "text-align: center;">
                              <img
                                style="width: 70%"
                                :src="menu.image"
                                class="rounded image mb-2"
                              />
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "test">
                              {{ menu.name }}
                            </div>
                            <div style="text-align: center; font-size: 30px" class = "text-danger">
                              {{ numberWithCommas(menu.price) }}원
                            </div>
                          </div>
                          <div v-if = "menusCate.length > 9 && menusCate.length > aa+9">
                            <img src="@/assets/img/right.png" style="height: 60px;" class = "cursor right" @click="right()">
                          </div>
                          <div v-if = "aa >= 9">
                            <img src="@/assets/img/left.png" style="height: 60px;" class = "cursor left" @click="left()">
                          </div>
                        </div>
                      </div>


                    </b-tabs>
                  </div>
                </div>
              </b-tab>
              <b-tab 
                title-link-class="text-dark"
                @click="seperateCate(2, 1); rightTmp(); clickEvent();" 
                title="푸드">
                <div>
                  <b-tabs
                    content-class="mt-3"
                    align="center"
                    style="font-size: 30px"
                    active-nav-item-class="font-weight-bold text-uppercase text-danger"
                  >
                    <b-tab
                      title-link-class="text-success"
                      title="브레드" 
                      @click="seperateCate(2, 1); rightTmp(); clickEvent();"></b-tab>
                    <b-tab 
                      title-link-class="text-success"
                      title="케이크" 
                      @click="seperateCate(2, 2); rightTmp(); clickEvent();"></b-tab>
                    <b-tab
                      title-link-class="text-success"
                      title="샌드위치&샐러드"
                      @click="seperateCate(2, 3); rightTmp(); clickEvent();"
                    ></b-tab>
                    <b-tab
                      title-link-class="text-success"
                      title="따뜻한 푸드"
                      @click="seperateCate(2, 4); rightTmp(); clickEvent();"
                    ></b-tab>
                    <b-tab
                      title-link-class="text-success"
                      title="과일&요거트"
                      @click="seperateCate(2, 5); rightTmp(); clickEvent();"
                    ></b-tab>
                    <b-tab
                      title-link-class="text-success"
                      title="스낵&미니디저트"
                      @click="seperateCate(2, 6); rightTmp(); clickEvent();"
                    ></b-tab>
                    <b-tab
                      title-link-class="text-success"
                      title="아이스크림"
                      @click="seperateCate(2, 7); rightTmp(); clickEvent();"
                    ></b-tab>
                    <div class="row overflow container-fluid">
                      <div
                        v-for="menu in menusCateTmp"
                        :key="menu.id"
                        class="col-4"
                      >
                        <div
                          class="m-2 hover"
                          v-b-toggle.sidebar-right
                          @click="GetMenuId(menu)"
                        >
                          <div style="text-align: center;">
                            <img
                              style="width: 70%"
                              :src="menu.image"
                                class="rounded image mb-2"
                            />
                          </div>
                          <div style="text-align: center; font-size: 30px" class = "test">
                            {{ menu.name }}
                          </div>
                          <div style="text-align: center; font-size: 30px" class = "text-danger">
                            {{ numberWithCommas(menu.price) }}원
                          </div>
                        </div>
                        <div v-if = "menusCate.length > 9 && menusCate.length > aa+9">
                            <img src="@/assets/img/right.png" style="height: 60px;" class = "cursor right" @click="right()">
                          </div>
                          <div v-if = "aa >= 9">
                            <img src="@/assets/img/left.png" style="height: 60px;" class = "cursor left" @click="left()">
                          </div>
                      </div>
                    </div>
                  </b-tabs>
                </div>
              </b-tab>
            </b-tabs>
          </b-card>
      <b-button class="tmp" variant ="success" style="width: 100%; height:60px; font-size:20px;" v-b-toggle.sidebar-right>결제하기</b-button>
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
  name: "List",
  components: {},
  watch: {},
  computed: {
    basketCheck() {
      if (this.$route.params.basket == "") {
        return false;
      } else {
        return true;
      }
    },
  },
  data() {
    return {
      test: true,
      menusCate: {},
      menusCateTmp: {},
      menuAll: {},
      y: 1,
      aa: 0,
      basket: [],
      basketRecent: {},
      Recent: {},
      basketPopular: {},
      basketFoodPopular: {},
      modalShow: false,
      basketPrice: 0,
      uid: "",
      loginCheck: 0,
      time:90,
    };
  },
  created() {
    this.authUser();
    this.GetMenuInfo();
    this.GetMenuListPopular();
    this.GetMenuListPopular2();
    this.checkVisitor();
  },
  methods: {
    purchase() {
      // console.log("logger - purchase out of method " + this.uid);
      var IMP = window.IMP;
      var msg;
      var b = this.basket;

      b[0].uid = this.uid;
      // console.log(b);
      // var tempUid = this.uid;
      let x = this;
      IMP.init("imp40062977");
      IMP.request_pay(
        {
          pg: "html5_inicis",
          pay_method: "card",
          merchant_uid: "merchant_" + new Date().getTime(),
          name: "스타벅스 학하동점",
          amount: this.basketPrice,
          buyer_email: "iamport@siot.do",
          buyer_name: "스타벅스",
          buyer_tel: "010-1234-5678",
          buyer_addr: "서울특별시 강남구 삼성동",
          buyer_postcode: "123-456",
        },
        function (rsp) {
          if (rsp.success) {
            var msg = "결제가 완료되었습니다.";
            // alert(msg);
            msg += "\n고유ID : " + rsp.imp_uid;
            msg += "\n상점 거래ID : " + rsp.merchant_uid;
            msg += "\n결제 금액 : " + rsp.paid_amount + "원";

            // console.log(b);
            // alert("logger - purchase inner " + b);
            axios
              .post(baseURL + "/create/order", b)
              .then((response) => {
                this.$cookies.remove("Auth-Token");
                x.$router.push({name: "kioskstart"});
              })
              .catch((err) => {
                console.log(err);
              });
          } else {
            var msg = "결제에 실패하였습니다.";
            msg += "에러내용 : " + rsp.error_msg;
          }
          // alert(msg);
        }
      );
    },
    authUser() {
      const axiosConfig = {
        headers: {
          jwtToken: `${this.$cookies.get("Auth-Token")}`,
        },
      };
      if (axiosConfig.headers.jwtToken != "null") {
        this.loginCheck = 1;
        axios
          .post(`${constants.baseUrl}/authuser`, "", axiosConfig)
          .then((res) => {
            this.uid = res.data.uid;
            this.GetMenuListRecent();
          })
          .catch((err) => console.log(err));
      } else {
        this.loginCheck = 0;
      }
    },
    GetMenuInfo() {
      axios
        .get(baseURL + "/branch/menu", { params: { sid: 1 } })
        .then((res) => {
          this.menuAll = res.data.object;
          this.seperateCate(1, 1);
          this.rightTmp();
        })
        .catch((err) => console.log(err.response));
    },
    GetMenuId(res) {
      this.basketPrice += res.price;
      this.basket.push(res);
    },
    GetMenuListRecent() {
      axios
        .get(`${baseURL}/order/mymenu`, { params: { uid: this.uid, sid: 1 } })
        .then((res) => {
          this.basketRecent = res.data;
          const Tmp = res.data;
          this.recent();
        })
        .catch((err) => console.log(err.response));
    },
    GetMenuListPopular() {
      axios
        .get(baseURL + "/order/hotcurrentdrink")
        .then((res) => {
          this.basketPopular = res.data.object;
        })
        .catch((err) => console.log(err.response));
    },
    GetMenuListPopular2() {
      axios
        .get(baseURL + "/order/hotcurrentfood")
        .then((res) => {
          this.basketFoodPopular = res.data.object;
        })
        .catch((err) => console.log(err.response));
    },
    seperateCate(a, b) {
      var tmpCate = this.menuAll.filter((cate) => cate.category1 == a);
      this.menusCate = tmpCate.filter((cate) => cate.category2 == b);
      this.aa = 0;
    },
    DeleteBasket(Menu) {
      var tmpBasket = this.basket;
      const a = Menu.menuid;
      function findMenuid(bb) {
        return bb.menuid === a;
      }
      var i = tmpBasket.findIndex(findMenuid);
      this.basketPrice -= Menu.price;
      tmpBasket = tmpBasket.splice(i, 1);
    },
    okcancel() {
      this.$cookies.remove("Auth-Token");
      this.$router.push({name: "kioskstart"});
    },
    numberWithCommas(x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    rightTmp() {
      this.menusCateTmp = this.menusCate.slice(this.aa, this.aa + 9);
    },
    right() {
      this.aa += 9;
      this.rightTmp();
    },
    left() {
      this.aa -= 9;
      this.rightTmp();
    },
    recent() {
      this.Recent = this.basketRecent.slice(0, 3);
    },
    checkVisitor(){
      tmp();
      var x = this;
      function tmp(){
        setTimeout(function(){
          if(x.time > 0){
            x.time -= 2;
            tmp();
          }else{
            x.$router.push({name: "kioskstart"});
          }
        }, 1000);
      }
    },
    clickEvent(){
      this.time = 90;
    }
  },
};
</script>

<style scoped>
* {
  font-size: 13px;
  line-height: 35px;
}
.hover:hover {
  background-color: #eee;
  cursor: pointer;
}
.click:hover {
  cursor: pointer;
}
.cursor {
  cursor: pointer;
}
.overflow {
  height: 80vh;
  overflow-x: hidden;
  overflow-y: auto;
}

.wrap {
  text-align: center;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.tmp {
  position: fixed;
  bottom: 0;
}
.right {
  position: fixed;
  z-index: 160;
  bottom: 630px;
  right: 8px;
}
.left {
  position: fixed;
  z-index: 160;
  bottom: 630px;
  left: 5px;
}
.test {
  word-break: keep-all;
}

.tmp {
  position: fixed;
  bottom: 0;
}
.right {
  position: fixed;
  z-index: 160;
  bottom: 630px;
  right: 8px;
}
.left {
  position: fixed;
  z-index: 160;
  bottom: 630px;
  left: 5px;
}
p.test {
  word-break: break-all;
}
.left {
  position: fixed;
  z-index: 160;
  bottom: 630px;
  left: 5px;
}
.divclass {
  max-width: 80%;
  height: auto;
}
</style>
