<template>
  <div class="user mt-5" id="join">
    <div class="container">
      <h1 style="text-align: center">회원 가입</h1>

      <!-- 이름 -->
      <b-form-group
        id="input-group-1"
        label="Name"
        label-size="lg"
        label-for="input-1"
        valid-feedback="Good!"
        invalid-feedback="2글자 이상"
        :state="nameState"
      >
        <b-input-group size="lg">
          <b-input-group-prepend is-text>
            <b-icon icon="person-fill"></b-icon>
          </b-input-group-prepend>
          <b-form-input
            id="input-1"
            v-model="name"
            :state="nameState"
            required
            aria-describedby="input-live-help input-live-feedback"
            placeholder="Enter name"
          ></b-form-input>
        </b-input-group>
        <!-- <b-form-invalid-feedback id="input-live-feedback">
          2글자 이상
        </b-form-invalid-feedback> -->
      </b-form-group>

      <!-- 이메일 -->
      <b-form-group
        id="input-group-2"
        label="Email"
        label-size="lg"
        label-for="input-2"
        description="We'll never share your email with anyone else."
        valid-feedback="Good!"
        invalid-feedback="이메일 형식에 맞춰 쓰세요."
        :state="emailState"
      >
        <b-input-group size="lg">
          <b-input-group-prepend is-text>
            <b-icon icon="envelope"></b-icon>
          </b-input-group-prepend>
          <b-form-input
            id="input-2"
            v-model="email"
            type="email"
            :state="emailState"
            required
            placeholder="me@example.com"
          ></b-form-input>
        </b-input-group>
        <b-form-invalid-feedback id="input-live-feedback">
          Your email doesn't match an email form.
        </b-form-invalid-feedback>
      </b-form-group>

      <!-- 성별 -->
      <b-form-group
        id="input-group-3"
        label="Gender"
        label-size="lg"
        label-for="input-3"
      >
        <b-form-radio v-model="gender" name="gender" value="남"
          >남</b-form-radio
        >
        <b-form-radio v-model="gender" name="gender" value="여"
          >여</b-form-radio
        >
      </b-form-group>

      <!-- 나이 -->
      <b-form-group
        id="input-group-4"
        label="Age"
        label-size="lg"
        label-for="input-4"
      >
        <b-form-input
          id="input-4"
          v-model="age"
          type="number"
          required
          placeholder="10"
          min="10"
          max="100"
          step="10"
        ></b-form-input>
      </b-form-group>

      <!-- 전화번호 -->
      <b-form-group
        id="input-group-5"
        label="Tel"
        label-size="lg"
        label-for="input-5"
      >
        <b-input-group size="lg">
          <b-input-group-prepend is-text>
            <b-icon icon="telephone-fill"></b-icon>
          </b-input-group-prepend>
          <b-form-input
            id="input-5"
            v-model="tel"
            required
            placeholder="Enter tel"
          ></b-form-input>
        </b-input-group>
      </b-form-group>

      <!-- 파일 -->

      <b-form-group
        id="input-group-6"
        label="Picture"
        label-size="lg"
        label-for="input-6"
        style="text-align :center"
      >
        <video
          id="video"
          class="ml-4"
          width="480"
          height="360"
          autoplay
        ></video>
        <canvas id="canvas" width="480" height="360" style="display: none;">
        </canvas>
        <b-btn class="btn btn-default btn-lg" @click="snap">영상촬영</b-btn>
      </b-form-group>

      <b-row>
        <b-col lg="4" class="pb-2"> </b-col>
        <b-col lg="4" class="pb-2" >
          <b-button 
            type="submit"
            variant="success"
            block
           
            :disabled="!(emailState && nameState)"
            @click="sendSignupFrom()"
            >가입하기</b-button
          > 
          </b-col
        >
        <b-col lg="4" class="pb-2">
          <b-button type="submit" variant="danger" @click="home()"
            >뒤로가기</b-button
          ></b-col
        >
      </b-row>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";
import { mapGetters } from "vuex";
import Swal from "sweetalert2";

const baseURL = constants.baseUrl;

export default {
  components: {},
  created() {},
  computed: {
    emailState() {
      const regExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;

      return regExp.test(this.email);
    },
    nameState() {
      return this.name.length >= 2 ? true : false;
    },
    invalidName() {
      if (this.name.length > 0) {
        return "Enter at least 4 characters.";
      }
      return "Please enter something.";
    },

    ...mapGetters(["getKakaoId"]),
  },
  methods: {
    sendSignupFrom() {
      var kakaoToken = "";
      var user = {
        uid: this.getKakaoId,
        email: this.email,
        name: this.name,
        gender: this.gender,
        age: this.age,
        tel: this.tel,
        learningfile: this.learningfile,
      };
      axios
        .post(`${constants.baseUrl}/account/signup`, user)
        .then((Response) => {
          kakaoToken = Response.data;
          console.log(Response.data);
          this.$cookies.set("Auth-Token", kakaoToken);
          axios
            .get(baseURL + "/account/justlearn")
            .then(() => {
              this.$router.push("/");
            })
            .catch((Error) => {
              console.log(Error);
            });
        })
        .catch((Error) => {
          console.log(Error);
        });
    },

    start() {
      var video = document.getElementById("video");
      if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        navigator.mediaDevices
          .getUserMedia({ video: true })
          .then(function(stream) {
            video.srcObject = stream;
            video.play();
          });
      }
    },
    home() {
      this.$router.push("/");
    },
    tmp(cnt) {
      var canvas = document.getElementById("canvas");
      var context = canvas.getContext("2d");
      context.drawImage(video, 0, 0, 480, 360);
      var dataURL = canvas.toDataURL();

      this.imagebase64.push(dataURL);
      setTimeout(() => {
        if (cnt == this.howmany - 1) {
    
          console.log(this.imagebase64);
          axios
            .post(baseURL + "/imageset", this.imagebase64)
            .then((response) => {
              this.imagebase64 = [];
            })
            .catch((err) => {
              console.log(err);
            });
        }
      }, 500);
    },

    snap() {
            const Toast = Swal.mixin({
              toast: true,
              width: 500,
           
              position: 'top',
              showConfirmButton: false,
              timer: 3000,
              timerProgressBar: true,
              didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
              }
            })

            Toast.fire({
              icon: 'success',
              title: '촬영 중입니다'
            })
      this.imagebase64.push(this.getKakaoId.toString());

      for (let i = 0; i < this.howmany; i++) {
        this.tmp(i);
      }
       setTimeout(() => {

      }, 2000);
    },
  },
  mounted() {
    this.start();
  },
  watch: {},
  data: () => {
    return {
      uid: "",
      email: "",
      name: "",
      gender: "",
      age: "",
      tel: "",
      learningfile: "",
      // isTerm: false,

      imagebase64: [],
      howmany: 20,
    };
  },
};
</script>

<style scoped>
h1 {
  margin-bottom: 30px;
}
.container {
  background-color: white;
  /* padding: 0 30px 0 30px; */
}

@media (min-width: 768px) {
  .container {
    padding: 70px 30px 50px 30px;
    width: 700px;
  }
}

@media (min-width: 992px) {
  .container {
    padding: 70px 50px 50px 50px;
    width: 700px;
  }
}

#input-group-1 {
  padding: 10px 20px 10px 20px;
  border: 1px solid gray;
}

#input-group-2 {
  padding: 10px 20px 10px 20px;
  border: 1px solid gray;
  margin-bottom: 0;
}

#input-group-3 {
  padding: 10px 20px 10px 20px;
  border: 1px solid gray;
  margin-bottom: 0;
}

#input-group-4 {
  padding: 10px 20px 10px 20px;
  border: 1px solid gray;
  margin-bottom: 0;
}
#input-group-5 {
  padding: 10px 20px 10px 20px;
  border: 1px solid gray;
  margin-bottom: 0;
}
#input-group-6 {
  padding: 10px 20px 10px 20px;
  border: 1px solid gray;
  margin-bottom: 50px;
}

/* @media (min-width: 1200px) {
  .container {
    width: 700px;
  }
} */
</style>
