<template>
  <div class="container">
    <h1 style="text-align: center">회원 정보 수정</h1>
    <b-form @submit="onSubmit" @reset="onReset" v-if="show">
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
            v-model="form.name"
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
            v-model="form.email"
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
        <!-- <b-form-input
          id="input-3"
          v-model="form.gender"
          required
          placeholder="Enter gender"
        ></b-form-input> -->
        <b-form-radio v-model="form.gender" name="gender" value="남"
          >남</b-form-radio
        >
        <b-form-radio v-model="form.gender" name="gender" value="여"
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
          v-model="form.age"
          type="number"
          required
          placeholder="Enter age"
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
            v-model="form.tel"
            required
            placeholder="Enter tel"
          ></b-form-input>
        </b-input-group>
      </b-form-group>

      <!-- 파일 -->
      <b-form-group
        id="input-group-6"
        label="Images"
        label-size="lg"
        label-for="input-6"
      >
        <b-input-group size="lg">
          <b-input-group-prepend is-text>
            <b-icon icon="images"></b-icon>
          </b-input-group-prepend>
          <b-form-input
            id="input-6"
            v-model="form.learningfile"
            required
            placeholder="Enter images"
          ></b-form-input>
        </b-input-group>
      </b-form-group>

      <b-row>
        <b-col lg="4" class="pb-2"></b-col>
        <b-col lg="4" class="pb-2"
          ><b-button
            type="submit"
            variant="success"
            block
            :disabled="!(emailState && nameState)"
            >수정하기</b-button
          ></b-col
        >
        <b-col lg="4" class="pb-2"></b-col>
      </b-row>

      <!-- <b-button type="reset" variant="danger">Reset</b-button> -->
    </b-form>
  </div>
</template>
<script>
import axios from "axios";
import constants from "../../lib/constants";

export default {
  name: "UserUpdate",
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
      show: true,
    };
  },
  created() {
    if (this.$cookies.get("Auth-Token") == null) {
      this.$router.push("/");
    } else {
      this.getUserInfo();
    }
  },
  computed: {
    emailState() {
      const regExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;

      return regExp.test(this.form.email);
    },
    nameState() {
      return this.form.name.length > 2 ? true : false;
    },
    invalidName() {
      if (this.name.length > 0) {
        return "Enter at least 4 characters.";
      }
      return "Please enter something.";
    },
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
    onSubmit(evt) {
      evt.preventDefault();

      const axiosConfig = {
        headers: {
          jwtToken: `${this.$cookies.get("Auth-Token")}`,
        },
      };
      axios
        .put(`${constants.baseUrl}/updateuser`, this.form, axiosConfig)
        .then((res) => {
          alert("정보가 수정되었습니다.");
          this.$router.push("/");
        })
        .catch((err) => {
          console.log(err);
        });

      alert(JSON.stringify(this.form));
    },
    onReset(evt) {
      evt.preventDefault();

      this.form.email = this.form.email;
      this.form.name = this.form.name;
      this.form.gender = this.form.gender;
      this.form.age = this.form.age;
      this.form.tel = this.form.tel;

      this.show = false;
      this.$nextTick(() => {
        this.show = true;
      });
    },
  },
};
</script>

<style scoped>
h1 {
  margin-bottom: 50px;
}
.container {
  background-color: white;
  margin-top: 50px;
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
</style>