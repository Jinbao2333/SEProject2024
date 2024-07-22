<template>
  <el-card style="width: 50%;margin: 10px" >
    <div s>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-form-item label="论文标题"  prop="title" style="width: 50%">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="研究领域" style="width: 50%">

          <div class="block">
            <el-cascader
                v-model="tableData"
                :options="options"
                :props="props"
                placeholder="请重新选择"
                clearable></el-cascader>
          </div>
        </el-form-item>

        <el-form-item label="发布会议" prop="conference" style="width: 50%">
          <el-input v-model="form.conference"></el-input>
        </el-form-item>
        <div style="display: inline-block">
          <el-form-item label="论文作者"  style="width: 100%;">
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
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+新增作者</el-button>
        <el-form-item label="论文摘要" prop="summary">
          <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              v-model="form.summary">
          </el-input>
        </el-form-item>
        <el-form-item label="发布时间" >
          <el-col :span="11">
            <el-date-picker type="date" placeholder="重新选择发表日期" v-model="form.date" style="width: 100%;"  format="YYYY/MM/DD"
                            value-format="YYYY-MM-DD" ></el-date-picker>
          </el-col>

        </el-form-item>

        <el-form-item label="论文类型" >
          <el-select v-model="form.type" placeholder="请选择论文类型" >
            <el-option label="理论证明型" value="理论证明型" ></el-option>
            <el-option label="综述型" value="综述型"></el-option>
            <el-option label="实验型" value="实验型"></el-option>
            <el-option label="工具型" value="工具型"></el-option>
            <el-option label="数据集型" value="数据集型"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="论文链接" style="width: 50%">
          <el-input v-model="form.link"></el-input>
        </el-form-item>
        <el-form-item label="引用文献" >
          <el-select
              v-model="form.references"
              multiple
              collapse-tags

              placeholder="请选择">
            <el-option
                v-for="item in refOptions"
                :key="item.paper_id"
                :label="item.title"
                :value="item.paper_id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="笔记内容">
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
          <el-button type="primary" @click="onSubmit">确认修改</el-button>
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
  name: "ChangePaper",
  components:{
  },
  data() {
    return {
      rules: {
        title: [
          {required: true, message: '请输入论文标题', trigger: 'blur'}
        ],
        field: [
          {required: true, message: '请输入邮箱', trigger: 'blur'},
        ],
        conference: [
          {required: true, message: '请输入发布会议', trigger: 'blur'},
        ],
        summary: [
          {required: true, message: '请输入论文摘要', trigger: 'blur'},
        ],

      },
      props: { multiple: true,
        checkStrictly: true,
        value:'field_id',
        label: 'field_name'
      },
      origin_paper_paper_id:'',
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
    this.origin_paper_id = this.$route.query.origin_paper_id
    this.upload_id = this.$route.query.upload_id

    request.post("/api/paper?paper_id="+this.origin_paper_id).then(res => {
      if (res.code === 0){
        this.form.title = res.data.title
        this.form.origin_paper_id = this.origin_paper_id
        this.form.origin_upload_id = this.upload_id
        this.form.authors = res.data.authors
        this.form.conference = res.data.conference
        //this.form.references = res.data.references
        this.form.link = res.data.link
        //this.form.fileList = res.data.fileList
        this.form.type = res.data.type
        this.form.summary = res.data.summary
        this.form.user_id = res.data.user_id
        this.form.content = res.data.content
        //console.log(this.form)
      }else{
        this.$message.error("加载失败")
        console.log(res.msg)
      }
    })
    request.post("/api/allfield").then(res => {
      this.options = res.data
    })
    request.post("/api/findRef").then(res => {
      //console.log(res.data)
      this.refOptions = res.data[0]
    })
    //this.getTreeData(this.tableData)
  },
  mounted() {
    this.init(this.form.content)
    let userId = sessionStorage.getItem("user")
    this.form.user_id = JSON.parse(userId).user_id
    //console.log(this.form.user_id)
  },
  methods: {
    handleRemove(file, fileList) {
      //console.log(file, fileList);
    },
    handleError(err,file,fileList){
      //console.log(err)
    },
    handleSuccess(res,file,fileList){
      //console.log(res)
      this.form.fileList.push(res.data[0])
      //console.log(this.form.fileList)
    },
    beforeUpload(file) {
      let fileSize = file.size
      const FIVE_M= 5*1024*1024;
      //大于5M，不允许上传
      if(fileSize>FIVE_M){
        this.$message.error("最大上传5M")
        return  false
      }
      //判断文件类型，必须是xlsx格式

      return true
    },
    handlePreview(file) {
      //console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    onSubmit() {
      // this.form.content = JSON.parse(JSON.stringify(this.BasicEditor.getHtml()))
      //console.log(this.form)
      this.form.content = editor.txt.html()  // 获取 编辑器里面的值，然后赋予到实体当中

      for(let i = 0;i<this.tableData.length;i++){
        this.form.field.push(this.tableData[i][this.tableData[i].length-1])
      }
      if (this.form.title === null || this.form.date === null || this.form.conference === null || this.form.field.length === 0 ||
          this.form.type === null ||this.form.authors.length === 0 ||this.form.content === null){
        this.$message.error("请完善论文信息")
      }else{
        request.post("/api/paper/modify",this.form).then(res=>{
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
      }
      //console.log('submit!');
      //console.log(this.form)
    },
    init(e){
      editor = new E('#div1')
      // 或者 const editor = new E( document.getElementById('div1') )
      editor.create()
      editor.txt.html()

    },
    handleChange(value) {
      //console.log(value);
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
