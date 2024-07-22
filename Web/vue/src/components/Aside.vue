<template>
  <div>
    <el-menu
        style="width: 145px; min-height: calc(100vh - 50px);height: 100%; " 
        :default-active="$route.path"
        background-color="#E0E0E0"
        text-color=black
        active-text-color=#962E38
        router
    >
      <div  v-for="m in permissionList" :key="m.permission_id">
      <el-menu-item :index="m.path"  v-if="m.name !== 'ChangeProfile'&& m.name!=='PaperList'&& m.name!=='Detail'&& m.name!=='ChangePaper'" style="font-size: 18px; font-weight: bold;">
         <i :class="['icon-bold', 'icon-large','icon-color', m.icon]"></i> {{ m.comment }}
      </el-menu-item>
    </div>
    </el-menu>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  data() {
    return {
      user:{
      },
      permissionList:{

      },
      data: {
      },
    }
  },
  created() {
    let userStr = sessionStorage.getItem("userPermission") || "{}"
    this.permissionList = JSON.parse(userStr)
    console.log(this.permissionList)
    //请求服务端，确认当前登录用户的 合法信息
    //alert("/api/permission?user_id=" + this.user.user_id)
    // request.post("/api/permission?user_id=" + this.user.user_id).then(res => {
    //     console.log(res)
    //     console.log("permission Get")
    //     this.permissionList = res.data
    //     sessionStorage.setItem("userPermission", JSON.stringify(res))  // 缓存用户信息
    //     console.log(res.data)
    //     let userStrr = sessionStorage.getItem("userPermission") || "{}"
    //     this.permissionList = JSON.parse(userStrr)
    // })
  }
}
</script>

<style scoped>
.icon-bold {
  font-weight: bold;
}

.icon-large {
  font-size: 20px; /* 根据需要调整大小 */
}
.icon-color {
  color: black; /* 根据需要调整大小 */
}

</style>
