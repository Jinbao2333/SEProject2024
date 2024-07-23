<template>

  <div style="padding: 10px">

  </div>
  <div style="margin-top: 10px">
    <el-card shadow="hover">
      <div id="myChart" :style="{width: '1000px', height: '600px'}"></div>
    </el-card>
  </div>

</template>

<script>
import request from "@/utils/request";
import {onMounted, toRaw} from "vue";

export default {

  data() {
    return {
      name: "Home",
      param:[{
        type:0,
        user_id:0
      }],
      param1:[{

      }],
      id:1,
      tableData:[{
        count:1,
        Date:1
      }],
      tableData1:[{
        count:0,
        Date:0
      }],
      tableData2:[{
        name:'',
        value:1
      }]
    }
  },
  async mounted() {
    this.user1 = JSON.parse(window.sessionStorage.getItem('user'));
    let that = this;
    this.id = parseInt(this.user1.user_id);
    this.param = {user_id: this.id,type: 0};
    //console.log(this.param);
    const result =await request.post("/api/Field",this.param1)
    this.tableData2 = result
    console.log(this.tableData2)
    request.post("/api/home",this.param).then((res) => {
      this.tableData = res
      //console.log(this.tableData)
      this.param={user_id: 0,type: 1};
      request.post("/api/home",this.param).then((res) => {
        this.tableData1 = res
        //console.log(this.tableData1)
        this.drawLine();
        this.drawLine1();
      });
    });


  },
  // created() {
  //   let userStr = sessionStorage.getItem("user") || "{}"
  //   this.user = JSON.parse(userStr)
  //   // 请求服务端，确认当前登录用户的 合法信息
  //   request.get("/user/" + this.user.id).then(res => {
  //     if (res.code === 0) {
  //       this.user = res.data
  //     }
  //   })
  //
  //   this.load()
  // },
  methods: {
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$root.echarts.init(document.getElementById('myChart'))
      let option = {
        title: {
          text: '今日送达',
          left: 'left',
          bottom:'bottom'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          trigger: 'item'
        },
        toolbox: {
	  top: 570,
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            name: '送达数量',
            type: 'pie',
            radius: [0, 250],
            center: ['50%', '50%'],
            roseType: 'radius',
	    
            itemStyle: {
              borderRadius: 8
            },
            data: [
            ]
          },

        ]
      }

      this.tableData2.filter(item=>{
        if (item.name !=="全部"){
          option.series[0].data.push(item)

        }
      })
      myChart.setOption(option);
      // request.get("/user/count").then(res => {
      //   if (res.code === '0') {
      //     res.data.forEach(item => {
      //       option.series[0].data.push({name: item.address, value: item.count})
      //     })
      //     // 绘制图表
      //     myChart.setOption(option);
      //   }
      // })

    }
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
