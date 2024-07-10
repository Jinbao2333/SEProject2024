#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"

#include "wifi_connect.h"

#define MSGQUEUE_OBJECTS 16

static void TCP2in1Task(void) // 从电脑收
{
	// 连接Wifi
	WifiConnect("dacongming", "79#zB791");

	// 待添加调试信息...
	printf("WiFi connected\n");

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
