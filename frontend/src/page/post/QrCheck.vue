<template>
  <div class="container">

    <section class="cameras">
      <h2 style="text-align:center;">QR CODE CHECK IN</h2>
      <ul>
        <li v-if="cameras.length === 0" class="empty">No cameras found</li>
        <li v-for="camera in cameras" v-bind:key="camera.id">
          <span
            v-if="camera.id == activeCameraId"
            :title="formatName(camera.name)"
            class="active"
            >{{ formatName(camera.name) }}</span
          >
          <span
            v-if="camera.id != activeCameraId"
            :title="formatName(camera.name)"
          >
            <a @click.stop="selectCamera(camera)">{{
              formatName(camera.name)
            }}</a>
          </span>
        </li>
      </ul>
    </section>
    <section class="scans">
      <h2 >Scans</h2>
      <ul v-if="scans.length === 0">
        <li class="empty">아직 QR 코드 인증이 되지 않았습니다.</li>
      </ul>
      <transition-group name="scans" tag="ul">
        <!-- <li v-for="scan in scans" :key="scan.date" :title="scan.content">
          {{ scan.content }}
        </li> -->
      </transition-group>
    </section>
    <div class="preview-container" style="text-align:center;" >
      <video id="preview"></video>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import constants from "../../lib/constants";

const baseURL = constants.baseUrl;

export default {
  name: "QrCheck",
  data() {
    return {
      scanner: null,
      activeCameraId: null,
      cameras: [],
      scans: [],
    };
  },
  mounted() {
    var self = this;
    self.scanner = new Instascan.Scanner({
      video: document.getElementById("preview"),
      scanPeriod: 5,
    });
    self.scanner.addListener("scan", function (content, image) {
      self.scans.unshift({ date: +Date.now(), content: content });
      self.UnRegistered();
      self.scanner.stop()
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
              title: 'QR 체크인 완료'
            })

      self.$router.push('main')
    });
    Instascan.Camera.getCameras()
      .then(function (cameras) {
        self.cameras = cameras;
        if (cameras.length > 0) {
          self.activeCameraId = cameras[0].id;
          self.scanner.start(cameras[0]);
        } else {
          console.error("No cameras found.");
        }
      })
      .catch(function (e) {
        console.error(e);
      });
  },
  methods: {
    UnRegistered() {
      axios
        .post(`${baseURL}/qr`)
        .then((res) => {
        })
        .catch((err) => {
          console.log(err);
        });
    },
    formatName(name) {
      return name || "(unknown)";
    },
    selectCamera(camera) {
      this.activeCameraId = camera.id;
      this.scanner.start(camera);
    },
  },
};
</script>
<style scoped>
.container{
    margin: 0 auto;
}
</style>
