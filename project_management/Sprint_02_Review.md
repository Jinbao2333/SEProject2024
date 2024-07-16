# Sprint Review 2

### 回顾

按照 Sprint Planning 1 的内容，我们在本周四（2024年7月11日）之前需要完成以下任务：

1. **BearPi1**
   - 完成信息源设备的功能开发
   - 确保设备能够正常工作和处理数据

2. **NFC 感应模块 (Arduino)**
   - 能成功读取 NFC 标签作为货物代号
   - 正确处理数据并转发到下一设备

3. **通信**（可选）
   - 实现 BearPi 设备之间的通信

#### 分工

- **负责人**：@贺云航
- **BearPi1**：@姜嘉祺 @王雪飞
- **Arduino**：@林童奕凡 @杨鸣谦

### 工作汇报

**首先**，我们使用了 Arduino Nano + NFC 模块进行设计，可以将读取到的 NFC 标签信息进行转写，如下图所示。

![Arduino NFC](/project_management/images/2_1_Arduino.png)

**然后**，我们进行测试：在NFC Tag中，写入货物代号：2023，Arduino 端读取到该编号后，通过 Serial 串口转发给 BearPi。此时，与 BearPi 相连的电脑通过 Serial 串口即可获取到 Arduino 端传入过来的 NFC 信息，电脑端 Python 代码与数据获取结果如图所示。

![Arduino Data](/project_management/images/2_2_ArdData.png)

**接着**，当电脑获取到 NFC Tag 中的信息后，需要将其转入到 BearPi 设备中，为了能够模拟更加真实的环境，我们采用了本地局域网内的 Socket 连接进行数据的传输。获取数据后，Python 端通过 ip、端口号创建 BearPi 端对应的 Socket 对象以连接 BearPi，之后还需要在 BearPi 端代码中定义 server_sock 对象，以便与电脑端建立连接，此后通过 socket 接收来自电脑端的数据并打印到屏幕上。

**最后**即可借助 Python 端创建的 client_socket 与 BearPi端的 server_sock 实现 socket 通信，并借助 client_socket.sendall 函数将 Arduino 端的数据推送至 BearPi 设备中。

### 反馈与规划

我们将在下一部分(Sprint Retrospective)总结之前的不足，并在最后一部分总结我们的下一步计划。
