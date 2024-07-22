<template>
  <div>
    <el-container>
      <el-header>
        <h1 style="text-align: center">{{form.title}}</h1>
        <div style="text-align: center">{{form.authors}}</div>
      </el-header>
      <el-main style="margin-top: 10px">


        
        <div style="font-size: 15px;">日期：{{form.date}}</div>
        <el-divider></el-divider>
        <div style="font-size: 15px;">货架：{{form.field}}</div>

        <el-divider></el-divider>
        <div style="font-size: 15px;">内容：
        <div v-html="this.form.content"></div>
        </div>

        <el-divider v-if="this.form.references.length!==0"></el-divider>
        <div style="font-size: 10px;" v-if="this.form.references.length!==0">引用文献：
          <el-button v-for="a in form.references" type="text" @click="jumpRef(a.paper_id)">

              {{a.title}}</el-button>
        </div>

        <el-divider content-position="center">
          <div style="margin:0px auto">
            <el-button v-if="role === 1 || user_id ===this.form.user_id" @click="jumpPaperChange(form)">修改信息</el-button>
            <el-button v-if="this.form.files.length!==0" @click="downFiles(form.files)">下载附件</el-button>
          </div>
        </el-divider>

        <comment style="width: 100%" :paperid =this.paper_id>
        </comment>

      </el-main>
    </el-container>

  </div>

</template>

<script>
import comment from "@/components/Comment";
import request from "@/utils/request";
import axios from "axios";
export default {
  name: "Detail",
  inject:['reload'],
  components:{
    comment
  },
  data(){
    return{
      field_name:'',
      paper_id:'',
      user_id:'',
      role:'',
      lastPath:'',
      form:{
        field:String,
        files:[],
        references:[]
      },
      Req:{
        serverfile:'',
        savefile:''
      }
    }
  },

  methods:{
    jumpRef(paperid){
      window.location.href='detail?paper_id='+paperid
      //this.$router.push({path: '/detail',query:{paper_id: paperid }})
    },
    routeClick(e){
      window.location.href = e;
    },
    jumpPaperChange(row){
      this.$router.push({path:'/changePaper', query:{origin_paper_id: row.paper_id,upload_id : row.upload_id}})
    },
    downFiles(files){
      for (let i = 0;i<files.length;i++){
        this.Req.serverfile = files[i]
        this.Req.savefile = '/Users/liujiaming/Downloads//'+files[i]
        //console.log(this.Req)

        axios({
          url:"/api/file/download",
          method: 'post',
          responseType:'blob',
          data: this.Req
        }).then((res) => {
          console.log(res.config.responseType)
            console.log(res)
            let url = window.URL.createObjectURL(new Blob([res.data])) // 将获取的文件转化为blob格式
            let a = document.createElement('a'); // 此处向下是打开一个储存位置
            a.style.display = 'none';
            a.href = url;
            // 下面两行是自己项目需要的处理，总之就是得到下载的文件名（加后缀）即可
            //var fileNameArray = path.split('#')[1].split('/')
            //var fileName = fileNameArray[fileNameArray.length-1]

            a.setAttribute('download',files[i]);
            document.body.appendChild(a);
            a.click();//点击下载
            document.body.removeChild(a);// 下载完成移除元素
            window.URL.revokeObjectURL(url);// 释放掉blob对象
            this.$message.success("下载成功")
            //console.log(res.msg)
            }
        )
        // request.post("/api/file/download",this.Req).then(res => {
        //   if (res.code === 0){
        //     console.log(res)
        //     let url = window.URL.createObjectURL(new Blob([res.data])) // 将获取的文件转化为blob格式
        //     let a = document.createElement('a'); // 此处向下是打开一个储存位置
        //     a.style.display = 'none';
        //     a.href = url;
        //     // 下面两行是自己项目需要的处理，总之就是得到下载的文件名（加后缀）即可
        //     //var fileNameArray = path.split('#')[1].split('/')
        //     //var fileName = fileNameArray[fileNameArray.length-1]
        //
        //     a.setAttribute('download',files[i]);
        //     document.body.appendChild(a);
        //     a.click();//点击下载
        //     document.body.removeChild(a);// 下载完成移除元素
        //     window.URL.revokeObjectURL(url);// 释放掉blob对象
        //     this.$message.success("下载成功")
        //     //console.log(res.msg)
        //   }else{
        //     this.$message.error("下载失败")
        //     //console.log(res.msg)
        //   }
        // })
      }

    }
  },
  created() {
    let userStrr = sessionStorage.getItem("user")
    this.role = JSON.parse(userStrr).role
    this.user_id = JSON.parse(userStrr).user_id
    this.paper_id = this.$route.query.paper_id
    console.log("jiazai详情")
    request.post("/api/paper?paper_id="+this.paper_id).then(res => {
      if (res.code === 0){
        this.form = res.data
        console.log(this.form)
        var temp
        for (var i = 0;i<res.data.fields.length;i++){
          //console.log(res.data.fields[i].field_name)
          if (i===0){
            temp = res.data.fields[i].field_name
          }else{
            temp= temp + ','+ res.data.fields[i].field_name
          }
        }
        this.form.field=temp
        for (var i = 0;i<res.data.authors.length;i++){
          //console.log(res.data.fields[i].field_name)
          if (i===0){
            temp = res.data.authors[i]
          }else{
            temp= temp + ','+ res.data.authors[i]
          }
        }
        this.form.authors=temp
      }else{
        this.$message.error("加载失败")
        //console.log(res.msg)
      }
    })
  },

}
</script>

<style scoped>

</style>
