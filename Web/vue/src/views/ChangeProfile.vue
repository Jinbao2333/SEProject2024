<template>

  <div>
    <el-card style="width: 45%; margin: 10px">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item style="text-align: center" label-width="0">
            <el-upload
                class="avatar-uploader"
                action="http://localhost:9090/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
            >
              <img v-if="form.avatar" :src="form.avatar" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="用户ID">
            <el-input v-model="form.user_id" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="form.userName"></el-input>
          </el-form-item>


          <!--        <el-form-item label="密码">-->
          <!--          <el-input v-model="form.password" show-password></el-input>-->
          <!--        </el-form-item>-->
          <div style="display: flex">
            <el-form-item label="邮箱验证" style="width: 50%">
              <el-input prefix-icon="el-icon-caret-right" v-model="form.identify" placeholder="请输入验证码" ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button v-if="disabled" type="primary" size="mini" style="width: 200px; height: 40px;margin-left: 12px" @click="verification" plain>
                发送邮箱验证码
              </el-button>
              <el-button v-if="!disabled" type="info" disabled size="mini" style="width: 200px;height: 40px;margin-left: 12px">
                <div>
                  {{ timer }}秒后重试
                </div>
              </el-button>
            </el-form-item>
          </div>
        <el-form-item label="新密码" prop="newPass">
          <el-input v-model="form.newPass" show-password></el-input>
        </el-form-item>
        <el-form-item label="再次确认" prop="confirmPass">
          <el-input v-model="form.confirmPass" show-password></el-input>
        </el-form-item>
      </el-form>

      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ChangeProfile",
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(userStr)
  },
  data() {
    return {
      disabled: true,
      timer: 60,
     form: {

       userName: "",
       user_id: "",
       identify: '',
       password: '',
       confirmPass: ''
     },
      userInfo:{

      },
      rules: {
        userName: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
          ,{
            min: 3,
            max: 20,
            message: '长度在 3 到 20 个字符',
            trigger: 'blur'
          }
        ],
        newPass: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
        ],
        confirmPass: [
          {required: true, message: '请输入确认新密码', trigger: 'blur'},
        ],
      },
    }
  },
  methods: {
    handleAvatarSuccess(res) {
      this.form.avatar = res.data
      this.$message.success("上传成功")
      // this.update()
    },
    verification() {

      this.disabled = false
      this.send()// 调获取验证码接口
      const authTimer = setInterval(() => {
        this.timer--
        if (this.timer <= 0) {
          this.disabled = true
          this.timer = 11
          clearInterval(authTimer)
        }
      }, 1000)
    },
    send() {
      request.post("/api/register/sendemail", this.form).then(res => {
        //console.log("发送验证码")
      })

    },
    update() {
            //console.log(this.form)
            request.post("api/modifypwd", this.form).then(res => {
              //console.log(res)
              if (res.code === 0) {
                this.$message({
                  type: "success",
                  message: "更新成功",
                })
                sessionStorage.removeItem("user")
                sessionStorage.removeItem("userPermission")
                request.post("/api/logout",this.form).then(res=>{
                  //console.log(res)
                })
                this.$router.push('/login')
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
