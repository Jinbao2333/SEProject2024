<template>
    <div style="width: 50%;">
      <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
        <el-avatar class="header-img" :size="40" :src="circleUrl"></el-avatar>
        <div class="reply-info">
          <div
              tabindex="0"
              contenteditable="true"
              id="replyInput"
              spellcheck="false"
              placeholder="输入评论..."
              class="reply-input"
              @focus="showReplyBtn"
              @input="onDivInput($event)"
          ></div>
        </div>
        <div class="reply-btn-box" v-show="btnShow">
          <el-button
              class="reply-btn"
              size="medium"
              @click="sendComment"
              type="primary"
          >
            发表回复
          </el-button>
        </div>
      </div>
      <div
          v-for="(item, i,user_id) in comments"
          :key="i"
          class="author-title reply-father"
      >
        <el-avatar class="header-img" :size="40" :src="circleUrl"></el-avatar>
        <div class="author-info">
          <span class="author-name">{{ item.name }}</span>
          <span class="author-time">{{ item.time }}</span>
        </div>
        <div class="icon-btn">
          <el-button
              style="size:13px;background-color : #E7E7E7"
              @click="showReplyInput(i, item.name, item.comment_id)" round
          >
            回复
          </el-button>
          <el-button
              style="size:13px"
              @click="deleteReply(i,item.name, item.comment_id)" round
              v-if="role===1 || item.name === this.userName"
              type="danger"
          >
            删除
          </el-button>
          <el-popover placement="left" :width="400" trigger="click" v-if="role===1 || item.name === this.userName"
          >
            <template #reference>
              <el-button type="warning" style="size: 13px;background-color :#B95AEC "
                         round>
                修改</el-button>
            </template>
            <div>
              <el-input v-model="this.comments[i].text" placeholder="请输入内容" style="width: 300px"></el-input>
              <el-button style=" margin-left: 10px" type="primary" @click="changeReply(this.comments[i])" round>
                保存
              </el-button>
            </div>

          </el-popover>
        </div>
        <div class="talk-box">
          <p>
            <span class="reply" >{{ item.text }}</span>
          </p>
        </div>
        <div class="reply-box">
          <div v-for="(reply, j) in item.reply" :key="j" class="author-title">
            <el-avatar
                class="header-img"
                :size="40"
                :src="circleUrl"
            ></el-avatar>
            <div class="author-info">
              <span class="author-name">{{ reply.name }}</span>
              <span class="author-time">{{ reply.time }}</span>
            </div>
            <div class="icon-btn">
              <el-button
                  style="size:13px;background-color : #E7E7E7"
                  @click="showReplyInput(i, reply.name, reply.comment_id)" round

              >
                回复
              </el-button>

              <el-popconfirm title="确定删除吗？" @confirm="deleteReply(i, reply.name, reply.comment_id)"
               v-if="this.role===1 ||reply.name === this.userName">
                <template #reference>
                  <el-button
                      style="size:13px"
                      round type="danger">
                    删除
                  </el-button>
                </template>
              </el-popconfirm>

              <el-popover placement="left" :width="400" trigger="click" v-if="this.role===1 || reply.name === this.userName"
              >
                <template #reference>
                  <el-button type="warning" style="size: 13px;background-color :#B95AEC "
                             round>
                    修改</el-button>
                </template>
                <div >
                  <el-input v-model="this.comments[i].reply[j].text" placeholder="请输入内容" style="width: 300px"></el-input>
                  <el-button style=" margin-left: 10px" type="primary" @click="changeReply(this.comments[i].reply[j])" round>
                    保存
                  </el-button>
                </div>
              </el-popover>

            </div>
            <div class="talk-box"  style="font-size: 14px">
              <p>
                <span>回复 {{ reply.to }}:</span>
                <span class="reply">{{ reply.text }}</span>
              </p>
            </div>
            <div class="reply-box"></div>
          </div>
        </div>
        <div v-show="_inputShow(i)" class="my-reply my-comment-reply">
          <el-avatar class="header-img" :size="40" :src="circleUrl"></el-avatar>
          <div class="reply-info">
            <div
                tabindex="0"
                contenteditable="true"
                spellcheck="false"
                placeholder="输入评论..."
                @input="onDivInput($event)"
                class="reply-input reply-comment-input"
            ></div>
          </div>
          <div class="reply-btn-box">
            <el-button
                class="reply-btn"
                size="medium"
                @click="sendCommentReply(i,this.super_id)"
            >
              发表回复
            </el-button>
          </div>
        </div>
      </div>
    </div>


</template>



<script>
import request from "@/utils/request";

const clickoutside = {
  // 初始化指令
  bind(el, binding) {
    function documentHandler(e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false;
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e);
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler;
    document.addEventListener('click', documentHandler);
  },
  update() {},
};
export default {
  name:'Comment',
  props:{
    paperid: Number
  },
  inject:['reload'],
  data(){
    return{

      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      user_id:'',
      userName:'',
      role:'',
      loading:true,
      paper_id:'',
      comment_id:'',
      btnShow: false,
      index:'0',
      replyComment:'',
      myHeader:'https://ae01.alicdn.com/kf/Hd60a3f7c06fd47ae85624badd32ce54dv.jpg',
      to:'',
      super_id :0,
      comments:[
      ],
      form:{

      }
    }
  },
  directives: {clickoutside},
  created() {

    let userStrr = sessionStorage.getItem("user")
    this.user_id = JSON.parse(userStrr).user_id
    this.role = JSON.parse(userStrr).role
    this.userName = JSON.parse(userStrr).userName
    this.paper_id = this.paperid
    console.log("before load comment")
    request.post("/api/getcomment?paper_id="+this.paper_id).then(res => {

      if (res.code === 0) {
        for (let i = 0;i<res.data.length;i++){
          if (res.data[i].user_id!==-1){
            this.comments.push(res.data[i])
          }
        }
        console.log("after load comment")
        console.log(res.data)

        //this.comments = res.data
        this.$message.success("成功加载评论")

      }else{
        this.$message.error("失败")

      }
    })
  },

  methods: {
    changeReply(row){
      console.log(row)
      request.post("/api/comment/modify",row).then(res => {
        if (res.code === 0) {
          console.log(res.data)
          this.comments = res.data
          this.$message.success("成功修改评论")
        }else{
          this.$message.error("修改评论失败")
        }
        this.reload()
      })
    },
    inputFocus(){
      var replyInput = document.getElementById('replyInput');
      replyInput.style.padding= "8px 8px"
      replyInput.style.border ="2px solid blue"
      replyInput.focus()
    },
    showReplyBtn(){
      this.btnShow = true
    },
    hideReplyBtn(){
      this.btnShow = false
      replyInput.style.padding= "10px"
      replyInput.style.border ="none"
    },
    showReplyInput(i,name,id){
      this.comments[this.index].inputShow = false
      this.index =i
      this.comments[i].inputShow = true
      this.to = name
      console.log(i)
      console.log(this.comments[i].comment_id)
      this.super_id = this.comments[i].comment_id
    },
    deleteReply(i,name,id){
      console.log(id)
      request.post("/api/comment/delete?comment_id="+id).then(res=>{
        if (res.code === 0){
          this.$message({
            type: "success",
            message: "删除成功"
          })
          this.reload()
        }else{
          this.$message({
            type: "error",
            message: "删除失败"
          })
          this.reload()
        }
      })

      // this.comments[this.index].inputShow = false
      // this.index =i
      // this.comments[i].inputShow = true
      // this.to = name
      // console.log(i)
      // console.log(this.comments[i].comment_id)
      // this.super_id = this.comments[i].comment_id
    },
    _inputShow(i){
      return this.comments[i].inputShow
    },
    sendComment(){
      if(!this.replyComment){
        this.$message({
          showClose: true,
          type:'warning',
          message:'评论不能为空'
        })
      }else{
        let a ={}
        let input =  document.getElementById('replyInput')
        let time= this.getCurTime()

        a.user_id = this.user_id
        a.userName= this.userName
        a.text =this.replyComment
        a.paper_id = this.paper_id
        //a.headImg = this.myHeader
        a.time = time
        a.super_id = 0
        console.log(a)
        this.replyComment = ''
        input.innerHTML = '';
        request.post("/api/comment",a).then(res=>{
          if (res.code === 0){
            this.$message({
              type: "success",
              message: "评论成功"
            })
            this.reload()

          }else{
            this.$message({
              type: "error",
              message: "评论失败"
            })
          }
        })
      }
    },
    sendCommentReply(i,toid){
      console.log("toId: "+toid);
      if(!this.replyComment){
        this.$message({
          showClose: true,
          type:'warning',
          message:'评论不能为空'
        })
      }else{
        let a ={}

        let time= this.getCurTime()
        a.user_id = this.user_id
        a.from= this.userName
        a.to = this.to
        a.fromHeadImg = this.myHeader
        a.text =this.replyComment
        a.time = time
        a.super_id = toid
        console.log(a)
        console.log(this.to)
        a.paper_id = this.paper_id
        request.post("/api/comment",a).then(res=>{
          if (res.code === 0){
            this.$message({
              type: "success",
              message: "评论成功"
            })
          }else{
            this.$message({
              type: "error",
              message: "评论失败"
            })
          }
        })
        this.replyComment = ''
        document.getElementsByClassName("reply-comment-input")[i+48].innerHTML = ""
        this.reload()
      }
    },
    onDivInput: function(e) {
      this.replyComment = e.target.innerHTML;
    },
    getCurTime(){
      //获取js 时间戳
      var time=new Date()
      //去掉 js 时间戳后三位，与php 时间戳保持一致
      return time.toLocaleString( )
    }
  },
}

</script>
<style lang="scss" scoped>
.my-reply {
  padding: 10px;
  background-color: #fafbfc;
  .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .reply-info {
    display: inline-block;
    margin-left: 5px;
    width: 70%;
    //根据屏幕宽度自适应
    @media screen and(max-width:1200px) {
      width: 80%;
    }
    .reply-input {
      min-height: 20px;
      line-height: 22px;
      padding: 10px 10px;
      color: #ccc;
      background-color: #fff;
      border-radius: 5px;
      // &:empty::before{
      //   content attr(placeholder);
      // }
      &:empty:before {
        content: attr(placeholder);
      }
      &:focus:before {
        content: none;
      }
      &:focus {
        padding: 8px 8px;
        border: 2px solid blue;
        box-shadow: none;
        outline: none;
      }
    }
  }
  .reply-btn-box {
    height: 25px;
    // margin: 10px 0;
    display: inline-block;
    .reply-btn {
      position: relative;
      float: right;
      margin-left: 15px;
      // margin-right: 15px;
    }
  }
}
.my-comment-reply {
  margin-left: 50px;
  .reply-input {
    width: flex;
  }
}
.author-title:not(:last-child) {
  border-bottom: 2px solid rgba(255,255,255, .3)
}
.reply-father {
  padding: 10px;
  .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .author-info {
    display: inline-block;
    margin-left: 5px;
    width: 60%;
    height: 40px;
    line-height: 20px;
    span {
      display: block;
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .author-name {
      color: black;
      font-size: 15px;
      font-weight: bold;
    }
    .author-time {
      font-size: 12px;
    }
  }
  .icon-btn {
    width: 30%;
    padding: 0 !important ;
    float: right;
    @media screen and (max-width: 1200px) {
      width: 20%;
      padding: 7px;
    }
    span {
      cursor: pointer;
    }
    .iconfont {
      margin: 0 5px;
    }
  }
  .talk-box {
    margin: 0 50px;
    p {
      margin: 0;
    }
    .reply {
      font-size: 14px;
      margin-left: 5px;
      color: black;
    }
  }
  .reply-box {
    margin: 10px 0 0 50px;

  }
}
</style>




