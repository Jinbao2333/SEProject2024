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

// number of Message Queue Objects
#define MSGQUEUE_OBJECTS 16

char recvbuf[512];

static void TCP2in1Task(void) // 从电脑收
{
	// 连接Wifi
	WifiConnect("dacongming", "79#zB791");

	// 在sock_fd 进行监听， 在 new_fd 接收新的链接
	int sock_fd, new_fd;

	char *buf = "GET!!";

	// 服务端地址信息
	struct sockaddr_in server_sock;

	// 客户端地址信息
	struct sockaddr_in client_sock;
	int sin_size;

	struct sockaddr_in *cli_addr;

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

	// 从队列中调用 accept 函数
	while (1)
	{
		sin_size = sizeof(struct sockaddr_in);

		if ((new_fd = accept(sock_fd, (struct sockaddr *)&client_sock, (socklen_t *)&sin_size)) == -1)
		{
			perror("accept");
			continue;
		}

		cli_addr = malloc(sizeof(struct sockaddr));

		printf("accept addr\r\n");

		if (cli_addr != NULL)
		{
			memcpy(cli_addr, &client_sock, sizeof(struct sockaddr));
		}

		// 在sock_fd 进行监听，在 new_fd 接收新的链接
		int sock_fd_pi2;

		static const char *send_data_pi2 = recvbuf;

		// 服务器的地址信息
		struct sockaddr_in send_addr_pi2;
		socklen_t addr_length_pi2 = sizeof(send_addr_pi2);
		char recvBuf_pi2[512];

		// 创建socket
		if ((sock_fd_pi2 = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		{
			perror("create socket failed!\r\n");
			exit(1);
		}

		// 初始化预连接的服务端地址
		send_addr_pi2.sin_family = AF_INET;
		send_addr_pi2.sin_port = htons(_PROT2_);
		send_addr_pi2.sin_addr.s_addr = inet_addr("192.168.68.90");
		addr_length_pi2 = sizeof(send_addr_pi2);

		connect(sock_fd_pi2, (struct sockaddr *)&send_addr_pi2, addr_length_pi2);
		printf("connect success\n");
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
