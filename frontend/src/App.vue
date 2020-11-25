<template>
  <div id="app">
   
    <router-view />
  </div>
</template>

 <!--<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>-->
<script>
import constants from "./lib/constants";

export default {
  name: "App",
  components: {
    
    // SignupForm,
  },
  created() {
    let url = this.$route.name;

    this.checkUrl(url);
  },
  watch: {
    $route(to) {
      this.checkUrl(to.name);
    },
  },
  methods: {
    checkUrl(url) {
      let array = [
        constants.URL_TYPE.USER.LOGIN,
        constants.URL_TYPE.POST.KIOSKMAIN,
        constants.URL_TYPE.ADMIN.DASHBOARD,
        constants.URL_TYPE.ADMIN.VISITHISTORY,
        constants.URL_TYPE.POST.LIST,
        constants.URL_TYPE.POST.MAIN,
        constants.URL_TYPE.POST.KIOSKSTART,
      ];

    
   
     
    },
  },
  data: function () {
    return {
     
      constants,
    };
  },
};
</script>

<style>
@font-face {
  font-family: "Light";
  src: url('./assets/Light.ttf');
}

*{
  font-family: "Light";
}
</style>
