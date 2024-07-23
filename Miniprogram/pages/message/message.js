// pages/message/message.js

import mqtt from'../../utils/mqtt.js';
const aliyunOpt = require('../../utils/aliyun/aliyun_connect.js');

let that = null;

Page({
  data: {
      
    hidden:true,
    nocancel:true,
    location_path: '/assets/images/area_A.png',
    shelf_num: '',
    delivery_date: '',
    package_description: '',
    ids: [],
    messages: [],
    client:null,//记录重连的次数
    reconnectCounts:0,//MQTT连接的配置
    options:{
    protocolVersion: 4, //MQTT连接协议版本
    clean: false,
    reconnectPeriod: 1000, //1000毫秒，两次重新连接之间的间隔
    connectTimeout: 30 * 1000, //1000毫秒，两次重新连接之间的间隔
    resubscribe: true, //如果连接断开并重新连接，则会再次自动订阅已订阅的主题（默认true）
    clientId: '',
    password: '',
    username: '',
    },

    aliyunInfo: {
    productKey: 'ieubb9AjIOu', //阿里云连接的三元组 
    deviceName: 'raspberry', //阿里云连接的三元组
    deviceSecret: '455605f0959b135ed6431a341dc4a900', //阿里云连接的三元组
    regionId: 'cn-shanghai', //阿里云连接的三元组
    pubTopic: '/ieubb9AjIOu/raspberry/user/update', //发布消息的主题
    subTopic: '/ieubb9AjIOu/raspberry/user/get', //订阅消息的主题
    },
  },
  onLoad: function (options) {
    const messages = this.data.messages;
    // for (var i = 0; i < 2; i++) {
    //   messages.push({
    //     title: "免费送票！超有内涵的门票。",
    //     area: "C",
    //     date: "2024-3-"+(5-i),
    //     image: "https://unsplash.it/400/300",
    //     summary: "最糟糕的，也许就是最幸运的。",
    //   });
    // }
    this.setData({ messages });

    that = this;
    let clientOpt = aliyunOpt.getAliyunIotMqttClient({
      productKey: that.data.aliyunInfo.productKey,
      deviceName: that.data.aliyunInfo.deviceName,
      deviceSecret: that.data.aliyunInfo.deviceSecret,
      regionId: that.data.aliyunInfo.regionId,
      port: that.data.aliyunInfo.port,
    });

    //console.log("get data:" + JSON.stringify(clientOpt));
    let host = 'wxs://' + clientOpt.host;
    console.log(host)
    
    this.setData({
      'options.clientId': clientOpt.clientId,
      'options.password': clientOpt.password,
      'options.username': clientOpt.username,
    })
    //console.log("this.data.options host:" + host);
    //console.log("this.data.options data:" + JSON.stringify(this.data.options));

    //访问服务器
    this.data.client = mqtt.connect(host, this.data.options);

    this.data.client.on('connect', function (connack) {
    //   wx.showToast({
    //     title: '连接成功'
    //   })
      console.log("连接成功");
    })

    //接收消息监听
    this.data.client.on("message", function (topic, payload) {
      console.log(" 收到 topic:" + topic + " , payload :" + payload); //[id，地区号，货架号，层号，区域号，姓名，手机号，到货时间，详细信息]

      var msg_list=payload.toString().split('/');
      var msg={
        id: msg_list[0],
        title: msg_list[1]+' '+msg_list[2]+'-'+msg_list[3]+'/'+msg_list[4],
        area: msg_list[4],
        date: msg_list[7],
        //image: "https://unsplash.it/400/300",
        summary: '   '+msg_list[5]+' '+msg_list[8],
        shelf_num: msg_list[2]+'-'+msg_list[3],
        description: msg_list[8],
      };
      if (that.data.ids.indexOf(msg_list[0])>=0){
        that.data.messages.forEach((element, index, array) => {
            if(element['id']==msg_list[0]){
                array['index']=msg
            }
        });
      }else{
        that.data.ids.push(msg_list[0]);
        that.data.messages.push(msg);
        that.setData({
            ids: that.data.ids,
        })
      };

      // that.setData({
        //转换成JSON格式的数据进行读取
        // temperature:JSON.parse(payload).currentTemperature,
        // humidity:JSON.parse(payload).read,
      // })
/*       wx.showModal({
        content: " 收到topic:[" + topic + "], payload :[" + payload + "]",
        showCancel: false,
      }); */
    })

    //服务器连接异常的回调
    that.data.client.on("error", function (error) {
      console.log(" 服务器 error 的回调" + error)

    })
    //服务器重连连接异常的回调
    that.data.client.on("reconnect", function () {
      console.log(" 服务器 reconnect的回调")

    })
    //服务器连接异常的回调
    that.data.client.on("offline", function (errr) {
      console.log(" 服务器offline的回调")
    })

    this.data.client.subscribe(this.data.aliyunInfo.subTopic,function(err){
        //console.log(err)
        if(!err){
          console.log("订阅成功");
        };
        // wx.showModal({
        //   content: "订阅成功",
        //   showCancel: false,
        // })
    })
  },

    mySort: function (e) {
        //property 根据什么排序
        var property = 'date';
        var self = this;
        var arr = self.data.messages;
        var sortRule = false; // 正序倒序
        self.setData({
            arr: arr.sort(self.compare(property, sortRule))
        })
        //console.log(arr)
    },
    compare: function (property, bol) {
        return function (a, b) {
        var value1 = Date.parse(new Date(a[property]));  //转换成十六进制获取日期
        var value2 = Date.parse(new Date(b[property]));
        if(bol){
        return value1 - value2;
        }else {
        return value2 - value1;
        }
    }
    },

  onReady: function () {
    var foo=this;
    wx.showLoading({
        title: 'Loading...',
    })
    setTimeout(function () {
        foo.onRefresh();
        wx.hideLoading();
    },500)
    // const query = wx.createSelectorQuery()
    // //求出bottom的位置
    // query.select("#bottom").boundingClientRect();
    // query.exec(res=>{
    //   wx.pageScrollTo({
    //     scrollTop:res[0].top
    //   })
    // })
  },
  onRefresh:function(){
    //导航条加载动画
    //wx.showNavigationBarLoading()
    //loading 提示框
    // wx.showLoading({
    //   title: 'Loading...',
    // })
    this.mySort();
    this.setData({
        messages:this.data.messages
    })
    console.log("下拉刷新啦");
    setTimeout(function () {
      //wx.hideLoading();
      //wx.hideNavigationBarLoading();
      //停止下拉刷新
      wx.stopPullDownRefresh();
    }, 500)
  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh:function(){
    this.onRefresh();
  },

    cancel: function(){
        this.setData({
            hidden: true
        });
    },
 
    /**
     *  点击确认
     */
    confirm: function(){
        this.setData({
            hidden: true
        })
    },

    click: function(e){
        console.log(e)
        var item=e.currentTarget.dataset['obj']
        this.setData({
            location_path: '/assets/images/area_'+item['area']+'.png',
            shelf_num: item['shelf_num'],
            delivery_date: item['date'],
            package_description: item['description'],
            hidden: false
        })
    },
});
