#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"

#include "lwip/sockets.h"
#include "wifi_connect.h"

#define _PROT_ 8888
#define _PROT2_ 8888
#define TCP_BACKLOG 10

#define MSGQUEUE_OBJECTS 16

static void TCP2in1Task(void) // 从电脑收
{
	// 连接Wifi
	WifiConnect("dacongming", "79#zB791");

	// 待添加调试信息...
	printf("WiFi connected\n");

	// 在sock_fd 进行监听， 在 new_fd 接收新的链接
	int sock_fd, new_fd;

	char *buf = "GET!!";

	// 服务端地址信息
	struct sockaddr_in server_sock;

	// 客户端地址信息
	struct sockaddr_in client_sock;


	// 创建 socket
	if((sock_fd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
	{
		perror("socket is erroe\r\n");
		exet(1);
	}

	// 定义server_sock对象， 以便与电脑端建立连接
	bzero(&server_sock, sizeof(server_sock));
	server_sock.sin_family = AF_INIT;
	server_sock.sin_addr.s_addr = htonl(INADDR_ANY);
	server_sock.sin_port = htons(_PORT_);

	// 调用bind函数绑定socket和地址
	if (bind(sock_fd, (struct sockaddr *)&server_sock, sizeof(struct sockaddr)) == -1)
	{
		perror("bind is error\r\n");
		exit(1);
	}

	// 调用listen函数监听(指定port监听)
	if (listen(sock_fd, TCP_BACKLOG) == -1)
	{
		perror("listen is error\r\n");
		exit(1);
	}

	printf("start accept\n");

	
	// 保持任务运行
	while (1)
	{
		// 暂停片刻
		osDelay(1000);
	}
}

static void TCP2in1Demo(void)
{

	osThreadAttr_t attr;

	attr.name = "TCP2in1Task";
	attr.attr_bits = 0U;
	attr.cb_mem = NULL;
	attr.cb_size = 0U;
	attr.stack_mem = NULL;
	attr.stack_size = 10240;
	attr.priority = osPriorityNormal;

	if (osThreadNew((osThreadFunc_t)TCP2in1Task, NULL, &attr) == NULL)
	{
		printf("[bearpi1] Falied to create TCP2in1Task!\n");
	}
}

APP_FEATURE_INIT(TCP2in1Demo);
