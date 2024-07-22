<template>
<div>
   <div ref="vantaRef" style="width:100%;height:100vh"></div>
</div>
  <div class="homepage-hero-module">
    <div class="video-container">
      <div :style="fixStyle" class="filter">
        <div class="mainlogin" style="width: 600px; margin: 100px auto;background-color: light ;border-radius: 20px;box-shadow: 0 10px 20px  rgba(0, 0, 0, .05);">
          <div style="width: 400px; margin: 100px auto">
            <div style="font-size: 40px; text-align: center; padding: 30px 0; color: white;font-family:ziti">易达物流管理系统</div>
            <el-form ref="form" :model="form" size="normal" :rules="rules">
              <el-form-item prop="username">
                <el-input prefix-icon="el-icon-user-solid" v-model="form.username" placeholder="请输入账号"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password placeholder="请输入密码"></el-input>
              </el-form-item>
              <el-form-item>
                <div style="display: flex">
                  <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 50%" placeholder="请输入验证码"></el-input>
                  <ValidCode @input="createValidCode" style="margin-left: 99px;background-color: white" />
                </div>
              </el-form-item>
              <!--            <el-form-item>-->
              <!--              <el-radio v-model="form.role" :label="1" style="color: white">管理员</el-radio>-->
              <!--              <el-radio v-model="form.role" :label="2" style="color: white">普通用户</el-radio>-->
              <!--            </el-form-item>-->
              <el-form-item>
                <el-button style="width: 100%;background-color:aquamarine;border:0;color:black;font-size: 1.2rem " type="primary" @click="login()">登 录</el-button>
              </el-form-item>
              <el-form-item><el-button style="color:aquamarine" type="text" @click="$router.push('/register')">前往注册 >> </el-button></el-form-item>
            </el-form>
          </div>

        </div>

      </div>
      <div class="login-container"
           v-bind:style="{backgroundImage:'url(' + bg + ')',
        backgroundRepeat:'no-repeat',
        backgroundSize:'100% 100%'}">
      </div>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";
import {activeRouter} from "@/utils/permission";
import * as THREE from 'three'
import Net from 'vanta/src/vanta.net'
import md5 from "js-md5"
export default {
  name: "Login",
  components: {
    ValidCode,
  },
  data() {
    return {
      data: {

      },
      form: {

      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      },
      validCode: '',
      bg: {
        backgroundImage: "url(" + require("@/assets/bcg.jpg") + ")",
        backgroundRepeat: "no-repeat",
        backgroundSize: "100% 100%"
      }
    }
  },
  mounted() {
    this.vantaEffect = Net({
      el: this.$refs.vantaRef,
      THREE: THREE
    })
    VANTA.NET({
  el: this.$refs.vantaRef,
  mouseControls: true,
  touchControls: true,
  gyroControls: false,
  minHeight: 200.00,
  minWidth: 200.00,
  scale: 1.00,
  scaleMobile: 1.00,
  color: 0xC2A500,
  backgroundColor: 0x000000,
  spacing: 12.00
})
  },
  beforeDestroy() {
    if (this.vantaEffect) {
      this.vantaEffect.destroy()
    }
  },


  methods: {
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },

    login () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }

          request.post("/api/login", this.form).then(res => {
            if (res.code === 0) {
              this.$message({
                type: "success",
                message: "登录成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息
              request.post("/api/permission?user_id=" + res.data.user_id).then(res1 => {
                sessionStorage.setItem("userPermission", JSON.stringify(res1))  // 缓存用户信息
                let userStrr = sessionStorage.getItem("userPermission") || "{}"
                this.permissionList = JSON.parse(userStrr)
                activeRouter()
                this.$router.push("/")  //登录成功之后进行页面的跳转，跳转到主页
              })

              // 登录成功的时候更新当前路由
              //activeRouter()


            } else  {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style>
.homepage-hero-module,
.video-container {
   z-index: 999;
  position: fixed;
  top: 10%;
  left: 30%;
  color: aquamarine;
  font-size: 100px;
  font-weight: bolder;
}

.video-container .poster img{
  z-index: 0;
  position: absolute;
}

.video-container .filter {
  z-index: 1;
  position: absolute;
  /*background: rgba(0, 0, 0, 0.4);*/
  width: 100%;

}

.fillWidth {
  width: 100%;
}
</style>
