<template>
  <div style="padding: 10px">
    <div style="margin: 10px 0">
      <el-button style="border : 0;background-color :#B95AEC" type="primary" @click="add">新增</el-button>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 10%" clearable></el-input>
      <el-button type="primary" style="border : 0 ; background-color :#B95AEC; margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        border
        stripe
        style="width: 100%">
      <el-table-column
          prop="role_id"
          label="roleId"
          sortable
      >
      </el-table-column>
      <el-table-column
          prop="name"
          label="用户类型">
      </el-table-column>
      <el-table-column
          prop="comment"
          label="备注">
      </el-table-column>
      <el-table-column label="权限菜单">
        <template #default="scope">
          <el-select clearable v-model="scope.row.permissions" multiple placeholder="请选择" style="width: 100%">
            <el-option v-for="item in permissions" :key="item.permission_id" :label="item.comment" :value="item.permission_id"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="mini" style ="background-color : #2AC39C ; border : 0" type="primary" @click="handleChange(scope.row); ">保存</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.user_id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
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

    <el-dialog title="提示" v-model="dialogVisible" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="用户类型名称">
          <el-input v-model="form.name" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.comment" style="width: 80%"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>


import request from "@/utils/request";
import {activeRouter} from "@/utils/permission";

export default {
  name: 'Role',
  components: {},
  data() {
    return {
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
      permissions: [],
      changePermissions:{
        role_id:'',
        permission_id:[]
      }
    }
  },
  created() {
    this.load()
    //this.setCurrentPageData()
  },
  methods: {
    save() {
      request.post("/api/admin/addrole?name="+this.form.name+"&comment="+this.form.comment, this.form).then(res => {
          if (res.code === 0) {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })


    },
    handleChange(row) {
      this.changePermissions.role_id = row.role_id
      this.changePermissions.permission_id = row.permissions
      request.post("/api/admin/modifypermission",this.changePermissions).then(res => {
        if (res.code === 0) {
          this.$message.success("更新成功")
          if (res.data) {
            this.$router.push("/login")
          }
        }
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    load() {
      this.loading = true
      request.post("/api/admin/allroleinfo").then(res => {
        if (res.code === 0){
          this.loading = false
          this.tableData = res.data
          for(let i=0;i<this.tableData.length;i++){
            let arr = new Array()
            for (let j = 0;j<this.tableData[i].permissions.length;j++){
              arr.push(this.tableData[i].permissions[j].permission_id)
            }
            this.tableData[i].permissions = arr;
          }
          this.total = res.data.length
        }else if (res.code === 1){
          this.$message({
            type: "error",
            message: res.msg,
          })
          sessionStorage.removeItem("user")
          sessionStorage.removeItem("userPermission")
          this.$router.push("/login")
        }else{
          this.$message({
            type: "error",
            message: res.msg,
          })
        }

      })

      request.post("/api/admin/allpermission").then(res => {
        this.permissions = res.data
        //console.log(res.data)
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
