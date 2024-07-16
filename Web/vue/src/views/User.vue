<template>
  <div style="padding: 10px">


    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 10%" clearable></el-input>
      <el-button type="primary" style="border : 0;background-color :#B95AEC ;margin-left: 5px" @click="test">查询</el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        border
        stripe
        style="width: 100%">
      <el-table-column
          prop="user_id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column
          prop="userName"
          label="用户名">
      </el-table-column>

      <el-table-column label="用户角色" width="300">
        <template #default="scope">
          <el-select v-model="scope.row.role" placeholder="请选择" style="width: 80%">
            <el-option v-for="item in roles" :key="item.role_id" :label="item.comment" :value="item.role_id"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="400">
        <template #default="scope">
          <el-button size="mini" type="primary" style ="background-color : #2AC39C ; border : 0" @click="handleChange(scope.row)">保存</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.user_id)">
            <template #reference>
              <el-button size="mini"  type="danger">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button size="mini"  style ="background-color : #E7E7E7 ; border : 0" @click="handleUploadList(scope.row)" v-if="thisRole === 1">该用户的上传</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>



  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'User',
  components: {},
  data() {
    return {
      thisRole:'',
      loading: true,
      form: {},
      dialogVisible: false,
      bookVis: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      totalPage:1,
      tableData: [],
      roles: []
    }
  },
  created() {
    let userStrr = sessionStorage.getItem("user")
    this.thisRole = JSON.parse(userStrr).role
    this.load()
    //this.setCurrentPageData()
  },
  methods: {
    handleChange(row) {
      request.post("/api/admin/user/changerole?user_id="+row.user_id+"&role_id="+row.role).then(res => {
        if (res.code === 0) {
          this.$message.success("更新成功")
          if (res.data) {
            this.$router.push("/user")
          }
        }
      })
    },
    handleUploadList(row) {
      this.$router.push({path: '/paperList',query:{user_id:row.user_id,showtype: '1' }})
    },
    test(){
      request.post("/api/getsession").then(res => {
      })
    },
    load() {
      this.loading = true
      request.post("/api/admin/alluser").then(res => {
        if (res.code === 0){
          this.loading = false
          this.tableData = res.data
          this.total = res.data.length
        }else{
          this.$message({
            type: "error",
            message: res.msg,
          })
        }

      })

      request.post("/api/admin/allrole").then(res => {
        this.roles = res.data
      })
    },

    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(id) {
      request.post("/api/admin/user/delete?user_id=" + id).then(res => {
        if (res.code === 0) {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
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
<style>
.el-table__header th, .el-table__header tr {

background-color: #696969;

color: white;
}

</style>
