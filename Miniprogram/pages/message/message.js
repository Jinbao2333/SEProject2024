// pages/message/message.js

Page({
  data: {
    hidden: true,
    nocancel: true,
    location_path: '/assets/images/area_A.png',
    shelf_num: '',
    delivery_date: '',
    package_description: '',
    ids: [],
    messages: [
      {
        id: '1',
        title: '货物A',
        area: 'A',
        date: '2024-07-09',
        image: 'https://unsplash.it/400/300',
        summary: '这是A的summary',
        shelf_num: '1-1',
        description: '这是A的描述信息。',
      },
      {
        id: '2',
        title: '货物B',
        area: 'B',
        date: '2024-07-08',
        image: 'https://unsplash.it/400/300',
        summary: '这是B的summary',
        shelf_num: '2-1',
        description: '这是B的描述信息。',
      },
      {
        id: '3',
        title: '货物C',
        area: 'D',
        date: '2024-07-07',
        image: 'https://unsplash.it/400/300',
        summary: '这是C的summary',
        shelf_num: '3-2',
        description: '这是C的描述信息。',
      },
    ],
  },
  onLoad: function (options) {
    const messages = this.data.messages;
    this.setData({ messages });
  },

  mySort: function (e) {
    var property = 'date';
    var self = this;
    var arr = self.data.messages;
    var sortRule = false; // 正序倒序
    self.setData({
      arr: arr.sort(self.compare(property, sortRule))
    });
  },
  compare: function (property, bol) {
    return function (a, b) {
      var value1 = Date.parse(new Date(a[property]));  //转换成十六进制获取日期
      var value2 = Date.parse(new Date(b[property]));
      if (bol) {
        return value1 - value2;
      } else {
        return value2 - value1;
      }
    };
  },

  onReady: function () {
    var foo = this;
    wx.showLoading({
      title: 'Loading...',
    });
    setTimeout(function () {
      foo.onRefresh();
      wx.hideLoading();
    }, 500);
  },
  onRefresh: function () {
    this.mySort();
    this.setData({
      messages: this.data.messages
    });
    console.log("下拉刷新啦");
    setTimeout(function () {
      wx.stopPullDownRefresh();
    }, 500);
  },
  onPullDownRefresh: function () {
    this.onRefresh();
  },

  cancel: function () {
    this.setData({
      hidden: true
    });
  },

  confirm: function () {
    this.setData({
      hidden: true
    });
  },

  click: function (e) {
    console.log(e);
    var item = e.currentTarget.dataset['obj'];
    this.setData({
      location_path: '/assets/images/area_' + item['area'] + '.png',
      shelf_num: item['shelf_num'],
      delivery_date: item['date'],
      package_description: item['description'],
      hidden: false
    });
  },
});
