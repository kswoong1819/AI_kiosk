<template>
<div class="container">
    <img :src="require(`@/assets/img/forbidden.jpg`)">
    <div class="h1 hsize text-center" @click="goMain">홈으로 돌아가기</div>
</div>
</template>
<script>
export default {
        name:'Forbidden',
        methods:{
            goMain(){
                this.$router.push({name:"main"})

            }   
        }
}
</script>
<style scoped>
    .container{
        width:100%;
    }
    img{
        width:100%;
    }
    .hsize{
        width: 200px;
        height: 40px;
        margin: 20px auto;
        
    }
    .hsize:hover{
    background-color: gold;
    }
</style>
