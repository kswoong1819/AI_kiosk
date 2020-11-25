import Vue from 'vue'
import Router from 'vue-router'

import constants from '../lib/constants'

// 유저
import Login from '../page/user/Login.vue'
import Join from '../page/user/Join.vue'
import MyPage from '../page/user/MyPage.vue'
import UserUpdate from '../page/user/UserUpdate.vue'
import UserInfo from '../components/User/UserInfo.vue'
import FaceCheck from '../page/post/FaceCheck.vue'
import OrderList from '../components/User/OrderList.vue'


// 어드민
import Admin from '../page/user/Admin.vue'
import DashBoard from '@/page/admin/DashBoard.vue'
import VisitHistory from '@/page/admin/VisitHistory.vue'
import MenuList from '@/page/admin/MenuList.vue'
import PurchaseHistory from '@/page/admin/PurchaseHistory.vue'
// import UpdateMenu from '@/components/admin/UpdateMenu.vue'
// import AddMenu from '@/components/admin/AddMenu.vue'

//error
import Forbidden from '@/page/error/Forbidden.vue'

// 포스트
import List from '../page/post/List.vue'
import Main from '../page/post/Main.vue'
import KioskMain from '../page/post/KioskMain.vue'
import KioskStart from '../page/post/KioskStart.vue'
import QrCheck from '../page/post/QrCheck.vue'
Vue.use(Router) 
 
export default new Router({
  mode: 'history',
  routes: [   
    // 로그인/가입
    { 
      path: '/user/login',
      name: constants.URL_TYPE.USER.LOGIN,
      component: Login
    },

    // 회원가입 
    {
      path: '/user/join',
      // name: constants.URL_TYPE.USER.JOIN,
      name: 'join',
      component: Join
    },
    // { 
    //   path: '/user/signup',
    //   name: constants.URL_TYPE.USER.SIGNUP,
    //   component: Singup
    // },

    // 포스트
    { 
      path: '/list',
      name: constants.URL_TYPE.POST.LIST,
      component: List,
    },

    { 
      path: '/',
      name: constants.URL_TYPE.POST.MAIN,
      component: Main,
    },
    { 
      path: '/kiosk/main',
      name: constants.URL_TYPE.POST.KIOSKMAIN,
      component: KioskMain,
    },
    { 
      path: '/kiosk/kioskstart',
      name: constants.URL_TYPE.POST.KIOSKSTART,
      component: KioskStart,
    },
    { 
      path: '/kiosk/qrcheck',
      name: constants.URL_TYPE.POST.QRCHECK,
      component: QrCheck,
    },
    { 
      path: '/kiosk/facecheck',
      name: constants.URL_TYPE.POST.FACECHECK,
      component: FaceCheck,
    },
    { // 마이페이지
      path: '/user/mypage',
      name: constants.URL_TYPE.USER.MYPAGE,
      component: MyPage
    },
    { // 유저업데이트
      path: '/user/update',
      name: constants.URL_TYPE.USER.USER_UPDATE,
      component: UserUpdate
    },
    { // 유저정보
      path: '/user/userinfo',
      name: constants.URL_TYPE.USER.USER_INFO,
      component: UserInfo
    },
    { // 구매내역
      path: '/user/orderlist',
      name: constants.URL_TYPE.USER.ORDER_LIST,
      component: OrderList
    },
    { // 어드민
      path: '/admin',
      name: constants.URL_TYPE.USER.ADMIN,
      component: Admin
    },
    { // 어드민 대시보드
      path: '/admin/dashboard',
      name: constants.URL_TYPE.ADMIN.DASHBOARD,
      component: DashBoard
    },
    { // 어드민 메뉴리스트
      path: '/admin/menulist',
      name: constants.URL_TYPE.ADMIN.MENULIST,
      component: MenuList
    },
    { // 어드민 방문기록
      path: '/admin/visithistory',
      name: constants.URL_TYPE.ADMIN.VISITHISTORY,
      component: VisitHistory
    },
    { // 어드민 방문기록
      path: '/admin/purchasehistory',
      name: constants.URL_TYPE.ADMIN.PURCHASEHISTORY,
      component: PurchaseHistory
    },
    { 
      path: '/admin/forbidden',
      name: constants.URL_TYPE.ERROR.FORBIDDEN,
      component: Forbidden
    },

  ]
})
