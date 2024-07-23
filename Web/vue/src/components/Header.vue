<template>
  <div style="height: 70px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="display: flex">
      <div>
        <!-- <el-icon :size="28" style="color:#962E38;  width: 2%; height: auto; align-items: center; margin-left: 10px; margin-top:5px; left:10px ;font-weight: bold;">₍ᐢ..ᐢ₎♡</el-icon> -->
        <el-icon :size="28" style="color:#962E38;  width: 175px; height: auto; align-items: center; margin-left: 0px; margin-top:19px; left:10px ;font-weight: bold;">=͟͟͞͞( •̀д•́)))</el-icon>
      </div>
      <div style="width: 1000px; padding-left: 0px; font-weight: bold; color: #962E38;font-size: 30px; margin-top: 10px; font-weight: bold; font-family:ziti ">易达智慧物流管理系统</div>
      <div style="width: 300px; padding-left: 0px; color: #962E38;font-size: 30px; margin-top: 10px; font-size: 18px;font-family:ziti ">你的易达！不，这是你的易达~~</div>

    </div>
    <div style="flex: 1"></div>

    <div>
      <el-badge class="item">
        <el-button size="small">通知</el-button>
      </el-badge>
      <el-badge  class="item">
        <el-button size="small">消息</el-button>
      </el-badge>

    </div>
    <div style="width: 100px ">
      <el-dropdown style="margin-top: 10px;">
        <span class="el-dropdown-link">
          <el-avatar :size="30" :src="circleUrl" style="position: relative; top: 10px"></el-avatar>
           {{ this.theName }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/changeProfile')">修改信息</el-dropdown-item>
            <el-dropdown-item @click="loginOut">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {Sunrise} from "@element-plus/icons";
import request from "@/utils/request";

export default {
  name: "Header",
  props: ['user'],
  data() {
    return {
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      theName:''
    }
  },
  created() {
    let users = sessionStorage.getItem("user") || "{}"
    this.theName =  JSON.parse(users).userName

  },
  components: {
    Sunrise,

  },
  methods:{
    loginOut() {
      sessionStorage.removeItem("user")
      sessionStorage.removeItem("userPermission")
      request.post("/api/paper/modify",this.form).then(res=>{
        console.log(res)
      })
      this.$router.push('/login')
    }
  }
}
</script>

<style>
.item {
  margin-top: 10px;
  margin-right: 10px;
  display: flex;
  justify-content: flex-start; 
}

</style>
