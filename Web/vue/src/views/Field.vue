<template>
  <div style="padding: 10px">
<!--      <el-button type="primary" @click="addRoot" >新增根领域</el-button>-->
    <el-table
        v-loading="loading"
        :data="this.tableData"
        border
        stripe
        style="width: 100%;margin-top: 10px"
        row-key="field_id"
        default-expand-all>

      <el-table-column
          prop="field_name"
          label="货架">
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" style ="background-color : white ; color:black ;border : 1" @click="handleEdit(scope.row)" >编辑</el-button>

          <el-button size="mini" style ="background-color : #2AC39C ; color:white ; border : 0" @click="add(scope.row)" >新增</el-button>

          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.field_id)">
            <template #reference>
              <el-button size="mini"  type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>



    <div style="margin: 10px 0">
      <el-dialog title="新增" v-model="dialogVisible2" width="30%">
        <el-form label-width="120px">
          <el-form-item label="名称">
            <el-input v-model="form.field_name" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible2 = false">取 消</el-button>
            <el-button type="primary" @click="saveAdd">确 定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>

    <div style="margin: 10px 0">
      <el-dialog title="设置新名称" v-model="dialogVisible3" width="30%">
        <el-form label-width="120px">
          <el-form-item label="名称">
            <el-input v-model="form.field_name" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible3 = false">取 消</el-button>
            <el-button type="primary" @click="updateName">确 定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'Field',
  components: {

  },
  data() {
    return {
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      dialogVisible2: false,
      dialogVisible3: false,

      tableData:[],

    }
  },

  created() {

    this.load()
  },
  methods: {

    add(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.pid=this.form.field_id;
      this.form.field_id = null

      this.dialogVisible2 = true
    },
    addRoot() {

      this.form.pid=null;
      this.form.field_id = null
      this.dialogVisible2 = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible3 = true

    },
    updateName() {
      // 新增
      //console.log("传过去的： ")
      //console.log(this.form)
      request.post("/api/admin/modifyfieldname", this.form).then(res => {
        if (res.code === 0) {
          this.$message({
            type: "success",
            message: "更新成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() // 刷新表格的数据
        this.dialogVisible3 = false  // 关闭弹窗
      })
    },
    saveAdd() {
      // 新增
      //console.log("传过去的： ")
      //console.log(this.form)

      request.post("/api/admin/addfield", this.form).then(res => {
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
        this.dialogVisible2 = false  // 关闭弹窗
      })
    },
    load() {
      this.loading = true
      request.post("/api/allfield").then(res => {
        this.loading = false
        this.tableData = res.data
        //console.log(this.tableData)
      })
    },

    handleDelete(id) {
      //console.log(id)
      request.post("/api/admin/deletefield?field_id=" + id).then(res => {
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
  }
}
</script>
<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
