<template>
  <div style="padding: 10px">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/category' }">浏览</el-breadcrumb-item>
      <el-breadcrumb-item>{{field_name}}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 1100px;margin-top: 10px;">
      <el-table-column
          prop="title"
          label="标题"
      >
      </el-table-column>
      <el-table-column
          prop="authors"
          label="姓名">
      </el-table-column>

      <el-table-column
          prop="date"
          label="日期">
      </el-table-column>
      <el-table-column label="操作" >
        <template #default="scope">
          <el-button size="mini" type="primary" @click="seeDetail(scope.row)">详情</el-button>
          <el-button size="mini" @click="deletePaper(scope.row)" >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[1,3,5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>


import request from "@/utils/request";
import comment from "@/components/Comment";
export default {
  name: 'Role',
  components: {
    comment
  },
  inject:['reload'],
  data() {
    return {

      loading: true,
      field_id:'',
      paper_id:'',
      user_id:'',
      field_name:'',
      form: {},
      dialogVisible: false,
      bookVis: false,
      search: '',
      currentPage: 1,
      showtype:'',
      pageSize: 10,
      total: 0,
      totalPage:1,
      tableData: [],
    }
  },
  created() {
    this.showtype = this.$route.query.showtype
    if (this.showtype === '0'){
      this.field_id = this.$route.query.field_id
      this.field_name = this.$route.query.field_name
    }else if (this.showtype === '1'){

      this.user_id = this.$route.query.user_id
    }

    this.load()

    //this.setCurrentPageData()
  },
  methods: {
    load() {
      this.loading = true
      console.log("okNow")
      let page_no = this.currentPage-1
      if (this.showtype === '0'){
        request.post("/api/findpaperbyfield?field_id="+this.field_id+"&page_no="+page_no +"&page_size="+this.pageSize).then(res => {
          if (res.code === 0){
            this.tableData = res.data[1]
            this.total = res.data[0]
          }
        })
      }else if(this.showtype === '1'){
        request.post("/api/findpaperbyuser?user_id="+this.user_id+"&page_no="+page_no+"&page_size="+this.pageSize).then(res => {
          if (res.code === 0) {
            this.tableData = res.data[1]
            this.total = res.data[0]
          }
        })
      }

      this.loading = false

    },

    seeDetail(row) {
      this.$router.push({path: '/detail',query:{paper_id:row.paper_id }})

    },
    deletePaper(row) {
      let user = JSON.parse(window.sessionStorage.getItem('user'));
      let user_id = parseInt(user.user_id);
      console.log(user_id)
      request.post("/api/paper/delete?paper_id=" + row.paper_id + "&user_id=" + user_id).then(res => {
        if (res.code === 0) {
          this.$message({
            duration: 700,
            type: "success",
            message: "删除成功"
          })
          location.reload();
        } else {
          this.$message({
            duration: 1000,
            type: "error",
            message: "该日志无法删除"
          })
        }
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
