<template>
  <div>
    <el-select v-model="value" placeholder='value' style="width: 8%; padding: 5px; border-radius: 50px;margin-left: 7%">
      <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      >
      </el-option>
    </el-select>
    <el-input
        v-model="state"
        placeholder="请输入内容"
        style="width: 50%; padding: 10px; border-radius: 50px;"
    ></el-input>
    <el-button v-on:click="loadData()" style = " border : 0;background-color :#B95AEC" type="primary" icon="el-icon-search">搜索</el-button>
    <div>
      <el-button v-show="show" @click="clearFilter()">清除所有过滤器</el-button>
      <el-table
          ref="filterTable"
          :data="this.tableData"
          style="width: 100%"
          v-show="show"
          v-loading="loading"
          border>
        <el-table-column
            prop="paper_id"
            label="id"
            sortable
        >
        </el-table-column>
        <el-table-column
            prop="title"
            label="标题"
        >
    
        </el-table-column>
        <el-table-column
            prop="role_name"
            label="姓名"
            :filters="Showauthor"
            :filter-method="filterauthor"
            filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column
            prop="field"
            label="楼层"
            :filters="ShowField"
            :filter-method="filterField"
            filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column
            label="操作"
        >
          <template v-slot="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
            <el-button type="text" size="small" @click="changPath(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="Delete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="sizeChange" @current-change="currentChange"
                     :current-page="page" :page-size="size" :page-sizes="pageSizes" v-show="show1"
                     layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-left:37%">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import VueSimpleSpinner from 'vue-simple-spinner'
import request from "@/utils/request";
import {values} from "core-js/stable/dom-collections";
export default {
  components: {
    VueSimpleSpinner
  },
  name: "Search",
  data() {
    return {
      page: 1, //第几页
      size: 10, //一页多少条
      total: 0, //总条目数
      pageSizes: [1, 3, 5, 10, 20, 50, 100, 200, 300, 400, 500, 1000], //可选择的一页多少条
      show: false,
      show1: false,
      loading: true,
      Showitem: [],
      ShowField:[],
      Showauthor:[],
      Showtype:[],
      options: [{
        value: 'id',
        label: 'id'
      }, {
        value: '2',
        label: '标题'
      }, {
        value: '3',
        label: '姓名'
      }], 
      value: 'id',
      tableData: [
        {
          count:1,
          paper_id: 123,
          title: '',
          conference: '',
          summary: '',
          role_name: '',
          type: '',
          field: '',
          fields:[],
          author:[]
        }
      ],
      restaurants: [],
      state: '',
      timeout: null,
      param: {
        Page:0,
        Time:10,
        value1: '',
        paper_id1: '',
      }
    }
  },
  mounted() {

  },
  methods: {
    loadData() {
      this.show = false
      this.show1 = false
      this.loading = true
      console.log("aaaaaa")
      this.getTabelData()
    },
    handleClick(row) {
      this.$router.push({path: '/detail', query: {paper_id: row.paper_id}})
    },
    changPath(row) {
      this.$router.push({path: '/changePaper', query: {origin_paper_id: row.paper_id, upload_id: row.paper_id}});
    },
    Delete(row) {
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
    getTabelData() {
      this.param.paper_id1 = this.state
      this.param.value1 = this.value
      this.param.Time=this.size
      this.param.Page=this.page-1
      request({
        url:"/api/search",
        method: 'post',
        params:this.param
      }).then((res) => {
            console.log(this.param)
            console.log(res)
            if(res!=null)
            this.$message({
              duration: 700,
              type: "success",
              message: "搜索成功"
            })
        else{
              this.$message({
                duration: 700,
                type: "error",
                message: "搜索无结果"
              })
            }
            this.tableData = res
            console.log(this.tableData)
            this.Showitem = []
            this.ShowField = []
            this.Showauthor = []
            this.Showtype = []
            this.tableData.filter(item => {
              let text = item.conference
              let value = item.conference
              this.Showitem.push({text, value})
            })
            this.tableData.filter(item => {
              let text = item.type
              let value = item.type
              this.Showtype.push({text, value})
            })
            this.tableData.filter(item=>{
              item.fields.filter(res=>{
                let text = res;
                let value = res;
                this.ShowField.push({text,value})
              })
            })
            this.tableData.filter(item=>{
              item.author.filter(res=>{
                let text = res;
                let value = res;
                this.Showauthor.push({text,value})
              })
            })
            console.log(this.ShowField)
            const map = new Map()
            this.Showitem = this.Showitem.filter(key => !map.has(key.value) && map.set(key.value, 1))
            this.ShowField = this.ShowField.filter(key => !map.has(key.value) && map.set(key.value, 1))
            this.Showauthor = this.Showauthor.filter(key => !map.has(key.value) && map.set(key.value, 1))
            this.Showtype = this.Showtype.filter(key => !map.has(key.value) && map.set(key.value, 1))
            console.log(this.Showitem)
            this.loading = false
            this.show = true
            this.show1 = true
            this.total = this.tableData[0].count
          }
      )
    },

    //page改变时的回调函数，参数为当前页码
    currentChange(val) {
      console.log("翻页，当前为第几页", val);
      this.page = val;
      this.getTabelData();
      local.reload()
    },
    //size改变时回调的函数，参数为当前的size
    sizeChange(val) {
      console.log("改变每页多少条，当前一页多少条数据", val);
      this.size = val;
      this.page = 1;
      this.getTabelData();
      local.reload()
    },
    created() {
      this.getTabelData();
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },

    createStateFilter(queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    clearFilter() {
      this.$refs.filterTable.clearFilter();
    },
    filterConferece(value, row) {
      return row.conference === value;
    },
    filtertype(value, row) {
      return row.type === value;
    },
    filterField(value, row) {
      let a = false;
      row.fields.filter(item=>{
        if(item==value)
          a=true
        return
      })
      return a
    },
    filterauthor(value, row) {
      let a = false;
      row.author.filter(item=>{
        if(item==value)
          a=true
        return
      })
      return a
    }
  },
}
</script>

<style scoped>

</style>
