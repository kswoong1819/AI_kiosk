<template>
  <div>
    <Kakao class="kakao"/>
    <div v-if="videoshow" class="videomodal">
      <div>
        <iframe
          style="width: 100%"
          height="350"
          src="https://www.youtube.com/embed/N2aEAEYtewE?autoplay=1&amp;mute=1"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>
      <div class="mt-3 text-center">
        <b-button @click="videoshow=false" class="p-3" variant="outline-light" style="font-size: 20px;">그만 보기</b-button>
      </div>
    </div>
    <img src="@/assets/img/scrollimg.png" class="scrollimg">
    <img src="@/assets/img/main/logo.png" alt="logo" class="logoimg">
    <div class="sections-menu">
      <span
        class="menu-point"
        :class="{ active: activeSection == index }"
        @click="scrollToSection(index)"
        v-for="(offset, index) in offsets"
        :key="index"
      >
      </span>
    </div>
    <section class="fullpage">
      <div class="card text-white">
        <div class="card-img-overlay">
          <p class="card-text maintextleft">키오스크의<br />완성은<br />얼굴</p>
        </div>
        <img class="firstmain" src="@/assets/img/main/main1.png" v-if="!isShow" alt="">
        <p v-if="isShowtext" class="card-text imgtext">회원입니다.</p>
        <transition name="slide" >
          <img class="firstmain" src="@/assets/img/main/main2.png" v-if="isShow" alt="">
        </transition>
        <transition name="service">
          <div class="slidein" v-if="isShowperson">
            <img class="firstmain" src="@/assets/img/main/main3.png" v-if="isShowperson" alt="">
          </div>
        </transition>
      </div>
    </section>

    <section class="fullpage">
      <div class="card text-white">
        <video autoplay="autoplay" loop="loop" class="mainvideo" src="@/assets/mainvideo.mp4" type="video/mp4" muted ></video>
        <div class="card-img-overlay">
          <p class="card-text maintextright">당신의<br />10초를<br />아끼세요.</p>
        </div>
      </div>
    </section>

    <section class="fullpage">
      <div class="card text-white">
        <video autoplay="autoplay" loop="loop" class="mainvideo2" src="@/assets/img/main/mainrecommend.mp4" type="video/mp4" muted ></video>
        <div class="card-img-overlay">
          <p class="card-text maintextleft">메뉴<br />고민<br />10초면 끗!</p>
        </div>
      </div>
    </section>

    <section class="fullpage">
      <div class="row text-center text-white" style="padding: 100px; padding-top: 30vh">
        <div v-for="p in endpage" :key="p.content" class="col-4">
          <b-card style="background-color: #002b49; font-size: 30px;">
            <b-card-text>{{p.title}}</b-card-text>
            <b-card-text>{{p.content}}</b-card-text>
          </b-card>
        </div>
      </div>
      <hr>
      <Carousel />
    </section>
  </div>
</template>

<script>
// import axios from "axios";
import Kakao from  "../user/Kakao"
import Carousel from  "./Carousel"

export default {
  // name: "main",
  components: {
    Kakao,
    Carousel
  },
  data() {
    return {
      inMove: false,
      activeSection: 0,
      offsets: [0,1,2,3],
      touchStartY: 0,
      isShow: false,
      isShowtext: false,
      isShowperson: false,
      videoshow: true,
      endpage: [
        {
          title : "등록 매장수",
          content : 5,
        },
        {
          title : "등록 메뉴",
          content : 257,
        },
        {
          title : "일일 방문자수",
          content : 12408,
        }
      ]
    };
  },
  created() {
    this.te()
    this.calculateSectionOffsets();
    
    window.addEventListener('DOMMouseScroll', this.handleMouseWheelDOM);  // Mozilla Firefox
    window.addEventListener('mousewheel', this.handleMouseWheel, { passive: false }); // Other browsers
    
    window.addEventListener('touchstart', this.touchStart, { passive: false }); // mobile devices
    window.addEventListener('touchmove', this.touchMove, { passive: false }); // mobile devices
  },
  destroyed() {
    window.removeEventListener('mousewheel', this.handleMouseWheel, { passive: false });  // Other browsers
    window.removeEventListener('DOMMouseScroll', this.handleMouseWheelDOM); // Mozilla Firefox
    
    window.removeEventListener('touchstart', this.touchStart); // mobile devices
    window.removeEventListener('touchmove', this.touchMove); // mobile devices
  },
  methods: {
    te () {
      setTimeout(() => {
        this.isShow = !this.isShow
        this.isShowtext = !this.isShowtext
        this.isShowperson = !this.isShowperson
      }, 2000);
      setTimeout(() => {
        this.isShowtext = !this.isShowtext
      }, 1000);
    },

    calculateSectionOffsets() {
      let sections = document.getElementsByTagName('section');
      let length = sections.length;
      
      for(let i = 0; i < length; i++) {
        let sectionOffset = sections[i].offsetTop;
        this.offsets.push(sectionOffset);
      }
    },
    handleMouseWheel: function(e) {
      
      if (e.wheelDelta < 30 && !this.inMove) {
        this.moveUp();
      } else if (e.wheelDelta > 30 && !this.inMove) {
        this.moveDown();
      }
        
      e.preventDefault();
      return false;
    },
    handleMouseWheelDOM: function(e) {
      
      if (e.detail > 0 && !this.inMove) {
        this.moveUp();
      } else if (e.detail < 0 && !this.inMove) {
        this.moveDown();
      }
      
      return false;
    },
    moveDown() {
      this.inMove = true;
      this.activeSection--;
        
      if(this.activeSection < 0) this.activeSection = this.offsets.length - 1;
        
      if (this.activeSection == 0) {
        this.isShow = false
        this.isShowtext = false
        this.isShowperson = false
        this.te()
      }
      this.scrollToSection(this.activeSection, true);
    },
    moveUp() {
      this.inMove = true;
      this.activeSection++;
        
      if(this.activeSection > this.offsets.length - 1) this.activeSection = 0;
      if (this.activeSection == 0) {
        this.isShow = false
        this.isShowtext = false
        this.isShowperson = false
        this.te()
      }
      this.scrollToSection(this.activeSection, true);
    },
    scrollToSection(id, force = false) {
      if(this.inMove && !force) return false;
      
      this.activeSection = id;
      this.inMove = true;
      document.getElementsByTagName('section')[id].scrollIntoView({behavior: 'smooth'});
      
      setTimeout(() => {
        this.inMove = false;
      }, 400);
      
    },
    touchStart(e) {
      e.preventDefault();
      
      this.touchStartY = e.touches[0].clientY;
    },
    touchMove(e) {
      if(this.inMove) return false;
      e.preventDefault();
      
      const currentY = e.touches[0].clientY;
      
      if(this.touchStartY < currentY) {
        this.moveDown();
      } else {
        this.moveUp();
      }
      
      this.touchStartY = 0;
      return false;
    }
  },
};
</script>

<style scoped>
.firstmain {
  position: absolute;
  right: 20vw;
  top: 25vh;
  height: 50vh;
}
.imgtext {
  position: absolute;
  right: 8vw;
  top: 40vh;
  font-size: 4vw;
}
.slide-enter,
.slide-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: 1s;
}
.slidein {
  position: absolute;
  right: 1vw;
  transition: all 2s ease-in-out;
}
.service-enter, .service-leave-active {
  right: -100%;
}

.fullpage {
  height: 100vh;
  width: 100%;
  background-color: #002b49;
}
.sections-menu {
  position: fixed;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
}
.sections-menu .menu-point {
  width: 10px;
  height: 10px;
  background-color: #fff;
  display: block;
  margin: 1rem 0;
  opacity: 0.6;
  transition: 0.4s ease all;
}
.sections-menu .menu-point.active {
  opacity: 1;
  transform: scale(1.5);
}
.maintextleft {
  position: absolute;
  top: 20vh;
  left: 100px;
  font-size: 7vw;
}
.maintextright {
  position: absolute;
  top: 20vh;
  right: 100px;
  font-size: 7vw;
}
.kakao {
  position: fixed;
  top: 7vh;
  right: 7vw;
  z-index: 1;
}
.mainvideo {
  position: absolute;
  left: 5vw;
  top: 20vh;
  width: 50vw;
}
.mainvideo2 {
  position: absolute;
  right: 20vw;
  top: 25vh;
  height: 50vh;
}
.scrollimg {
  position: fixed;
  bottom: 10px;
  left: 45vw;
  z-index: 1;
  width: 100px;
}
.logoimg {
  position: fixed;
  top: 50px;
  left: 30px;
  z-index: 1;
  width: 90px;
}
.videomodal {
  position: fixed;
  top: 25vh;
  left: 25vw;
  z-index: 1;
  width: 50vw;
}
</style>