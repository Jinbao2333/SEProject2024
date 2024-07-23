4<template>
  <el-card style="width: 50%;margin: 10px" >
    <div s>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px"  >
        <el-form-item label="标题"  prop="title" style="width: 50%">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="货架" prop="conference" style="width: 50%">

        <div class="block">
            <el-cascader
                v-model="tableData"
                :options="options"
                :props="props"
                clearable></el-cascader>
        </div>
        </el-form-item>

      
        <div style="display: inline-block">
          <el-form-item label="姓名"  prop="conference" style="width: 100%;">
            <el-tag
                :key="author"
                v-for="author in form.authors"
                closable
                :disable-transitions="false"
                @close="handleClose(author)" style="">
              {{author}}
            </el-tag>
          </el-form-item>
        </div>

        <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+</el-button>
  
        <el-form-item label="发布时间" prop="time" >
          <el-col :span="11">
            <el-date-picker type="date" placeholder="日期" v-model="form.date" style="width: 60%;"  format="YYYY/MM/DD" value-format="YYYY-MM-DD" ></el-date-picker>
          </el-col>

        

    
       
        </el-form-item>

        <el-form-item label="具体内容">
          <div id="div1" style="position:relative; z-index: 0">
          </div>
        </el-form-item>

        <el-form-item label="附加文件">
          <el-upload
              class="upload-demo"
              action="/api/file/upload?file"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :on-success="handleSuccess"
              :on-error="handleError"
              :before-upload="beforeUpload"
              :before-remove="beforeRemove"
              multiple
              :limit="3"
              :on-exceed="handleExceed"
              :file-list="files">
            <el-button size="small" >点击上传</el-button>
          </el-upload>

        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即发布</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-card>


</template>

<script>
import request from "@/utils/request";

let editor;
import E from 'wangeditor'
export default {
  name: "Upload",
  components:{
  },
  data() {
    return {
      rules: {
        title: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ],
	

      },
      props: { multiple: true,
        checkStrictly: true,
        value:'field_id',
      label: 'field_name'
      },
      inputVisible: false,
      inputValue: '',
      tableData:[],
      files:[],
      form: {
        user_id:'',
        title: '',
        date: '',
        conference:'',
        type: '',
        content:"",
        summary:'',
        authors: [],
        references:[],
        link:'',
        field: [],
        fileList: []

      },

      options: [],
      refOptions:[]
    }
  },
  created() {
    request.post("/api/allfield").then(res => {
      this.options = res.data
    })
    request.post("/api/findRef").then(res => {
      console.log(res.data)
      this.refOptions = res.data[0]
    })
    //this.getTreeData(this.tableData)
  },
  mounted() {
    this.init()
    let userId = sessionStorage.getItem("user")
    this.form.user_id = JSON.parse(userId).user_id
    console.log(this.form.user_id)
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handleError(err,file,fileList){
      console.log(err)
    },
    handleSuccess(res,file,fileList){
      console.log(res)
      this.form.fileList.push(res.data[0])
      console.log(this.form.fileList)
    },
    beforeUpload(file) {
      let fileSize = file.size
      const FIVE_M= 20*1024*1024;
      
      if(fileSize>FIVE_M){
        this.$message.error("最大上传20M")
        return  false
      }
      

      return true
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    onSubmit() {
      this.form.content = editor.txt.html()  // 获取 编辑器里面的值，然后赋予到实体当中
      console.log(this.tableData)
      const letters = new Set()
      for(let i = 0;i<this.tableData.length;i++){
        for (let j = 0;j<this.tableData[i].length;j++){
          letters.add(this.tableData[i][j])
        }
      }
      console.log(letters)

      this.form.field =Array.from(letters);
      console.log(this.form)
     
        request.post("/api/paper/upload",this.form).then(res=>{
          console.log(res)
          if (res.code === 0){
            this.$message({
              type: "success",
              message: res.msg,
            })
          }else {
            this.$message({
              type: "error",
              message: res.msg,
            })
          }
        })
      
    },
    init(){
      editor = new E('#div1')
      // 或者 const editor = new E( document.getElementById('div1') )
      editor.create()
      editor.txt.html()

    },
    handleClose(author) {
      this.form.authors.splice(this.form.authors.indexOf(author), 1);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.form.authors.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    }
  }
}
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
  
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
