export default {
    TITLE: '키.완.얼',
    baseUrl: 'https://k3b107.p.ssafy.io:8080',
    // baseUrl: 'http://localhost:8080',
    LS_KEY: {
        USER_TOKEN: 'userToken',
    },
    ERROR: {
        FRONT_ERROR: 'FE00'
    },
    URL_TYPE: {
        USER: {
            LOGIN: 'login',
            JOIN: 'join',
            MYPAGE: 'mypage',
            USER_UPDATE: 'userupdate',
            USER_INFO: 'userinfo',
            ORDER_LIST: 'orderlist',
            ADMIN: 'admin',
        },
        ADMIN: {
            DASHBOARD: 'dashboard',
            MENULIST: 'menulist',
            VISITHISTORY: 'visithistory',
            PURCHASEHISTORY:'purchasehistory'
            // UPDATEMENU: 'updatemenu',
            // ADDMENU: 'addmenu',
        },

        POST: {
            MAIN: "main",
            LIST: "list",
            KIOSKMAIN: "kisokmain",
            KIOSKSTART: "kioskstart",
            FACECHECK: "facecheck",
            QRCHECK: "qrcheck",
        },
        ERROR:{
            FORBIDDEN: 'forbidden',
        }
    }
}

