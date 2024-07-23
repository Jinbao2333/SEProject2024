#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include "ohos_init.h"
#include "cmsis_os2.h"

#include "wifi_connect.h"
#include "lwip/sockets.h"
#include <queue.h>
#include <oc_mqtt_al.h>
#include <oc_mqtt_profile.h>
#include "E53_SC1.h"
#include <dtls_al.h>
#include <mqtt_al.h>

#define _PROT_ 8888
#define TCP_BACKLOG 10

#define CONFIG_WIFI_SSID "dacongming" // WiFi 热点账号

#define CONFIG_WIFI_PWD "79#zB791" // WiFi 热点密码

// #define CONFIG_APP_SERVERIP       "121.36.42.100"                       //基础版平台对接地址

#define CONFIG_APP_SERVERIP "117.78.5.125" // 标准版平台对接地址

#define CONFIG_APP_SERVERPORT "1883"

#define CONFIG_APP_DEVICEID "64784989f4d13061fc88ae1d_BearPi1" // 注册设备后生成的deviceid

#define CONFIG_APP_DEVICEPWD "15914e7e5a1aa34f1a9e9370b9166365" // 注册设备后生成的密钥

#define CONFIG_APP_LIFETIME 60 ///< seconds

#define CONFIG_QUEUE_TIMEOUT (5 * 1000)

#define MSGQUEUE_OBJECTS 16 // number of Message Queue Objects

typedef enum
{
	en_msg_cmd = 0,
	en_msg_report,
} en_msg_type_t;

typedef struct
{
	char *request_id;
	char *payload;
} cmd_t;

typedef struct
{
	int lum;

} report_t;

typedef struct
{
	en_msg_type_t msg_type;
	union
	{
		cmd_t cmd;
		report_t report;
	} msg;
} app_msg_t;

typedef struct
{
	queue_t *app_msg;
	int connected;
	int led;
} app_cb_t;
static app_cb_t g_app_cb;

// 在sock_fd 进行监听，在 new_fd 接收新的链接
int sock_fd, new_fd;

char recvbuf[512];

// use this function to push all the message to the buffer
static int msg_rcv_callback(oc_mqtt_profile_msgrcv_t *msg)
{
	printf("%s", msg->msg);
	return 0;
}

int Pi_to_Cloud(void)
{
	app_msg_t *app_msg;
	uint32_t ret;

	dtls_al_init();
	mqtt_al_init();
	oc_mqtt_init();

	g_app_cb.app_msg = queue_create("queue_rcvmsg", 10, 1);
	if (NULL == g_app_cb.app_msg)
	{
		printf("Create receive msg queue failed");
	}
	oc_mqtt_profile_connect_t connect_para;
	(void)memset(&connect_para, 0, sizeof(connect_para));

	connect_para.boostrap = 0;
	connect_para.device_id = CONFIG_APP_DEVICEID;
	connect_para.device_passwd = CONFIG_APP_DEVICEPWD;
	connect_para.server_addr = CONFIG_APP_SERVERIP;
	connect_para.server_port = CONFIG_APP_SERVERPORT;
	connect_para.life_time = CONFIG_APP_LIFETIME;
	connect_para.rcvfunc = msg_rcv_callback;
	connect_para.security.type = EN_DTLS_AL_SECURITY_TYPE_NONE;
	ret = oc_mqtt_profile_connect(&connect_para);
	if ((ret == (int)en_oc_mqtt_err_ok))
	{
		g_app_cb.connected = 1;
		printf("oc_mqtt_profile_connect succed!\r\n");
	}
	else
	{
		printf("oc_mqtt_profile_connect faild!\r\n");
	}

	oc_mqtt_profile_msgup_t msg;
	msg.device_id = "64784989f4d13061fc88ae1d_BearPi1";
	msg.name = NULL;
	msg.id = NULL;
	msg.msg = (void *)recvbuf;
	msg.msg_len = sizeof(msg.msg) / sizeof(char);
	oc_mqtt_profile_msgup("64784989f4d13061fc88ae1d_BearPi1", &msg);
	return 0;
}

static void TCPServerTask(void)
{
	// 服务端地址信息
	struct sockaddr_in server_sock;

	// 客户端地址信息
	struct sockaddr_in client_sock;
	int sin_size;

	struct sockaddr_in *cli_addr;

	// 连接Wifi
	WifiConnect(CONFIG_WIFI_SSID, CONFIG_WIFI_PWD);

	// 创建socket
	if ((sock_fd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
	{
		perror("socket is error\r\n");
		exit(1);
	}

	bzero(&server_sock, sizeof(server_sock));
	server_sock.sin_family = AF_INET;
	server_sock.sin_addr.s_addr = htonl(INADDR_ANY);
	server_sock.sin_port = htons(_PROT_);

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

	// 调用accept函数从队列中
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

		// 处理目标
		ssize_t ret;

		while (1)
		{
			if ((ret = recv(new_fd, recvbuf, sizeof(recvbuf), 0)) == -1)
			{
				printf("recv error \r\n");
			}
			printf("recv :%s\r\n", recvbuf);
			Pi_to_Cloud();
		}

		close(new_fd);
	}
}

static void TCPServerDemo(void)
{
	osThreadAttr_t attr;

	attr.name = "TCPServerTask";
	attr.attr_bits = 0U;
	attr.cb_mem = NULL;
	attr.cb_size = 0U;
	attr.stack_mem = NULL;
	attr.stack_size = 10240;
	attr.priority = osPriorityNormal;

	if (osThreadNew((osThreadFunc_t)TCPServerTask, NULL, &attr) == NULL)
	{
		printf("[TCPServerDemo] Falied to create TCPServerTask!\n");
	}
}

APP_FEATURE_INIT(TCPServerDemo);