/*
 * Copyright (c) 2020 Nanjing Xiaoxiongpai Intelligent Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "lwip/netif.h"
#include "lwip/netifapi.h"
#include "lwip/ip4_addr.h"
#include "lwip/api_shell.h"

#include "cmsis_os2.h"
#include "hos_types.h"
#include "wifi_device.h"
#include "wifiiot_errno.h"
#include "ohos_init.h"

#define DEF_TIMEOUT 15
#define ONE_SECOND 1

#define SELECT_WIFI_SECURITYTYPE WIFI_SEC_TYPE_PSK  

static void WiFiInit(void);
static void WaitSacnResult(void);
static int WaitConnectResult(void);
static void OnWifiScanStateChangedHandler(int state, int size);
static void OnWifiConnectionChangedHandler(int state, WifiLinkedInfo *info);
static void OnHotspotStaJoinHandler(StationInfo *info);
static void OnHotspotStateChangedHandler(int state);
static void OnHotspotStaLeaveHandler(StationInfo *info);

static int g_staScanSuccess = 0;
static int g_ConnectSuccess = 0;
static int ssid_count = 0;
WifiEvent g_wifiEventHandler = {0};
WifiErrorCode error;

#define SELECT_WLAN_PORT "wlan0"

int WifiConnect(const char *ssid, const char *psk)
{
    WifiScanInfo *info = NULL;
    unsigned int size = WIFI_SCAN_HOTSPOT_LIMIT;
    static struct netif *g_lwip_netif = NULL;

    osDelay(200);
    printf("<--System Init-->\r\n");

    // 初始化 WIFI
    WiFiInit();

    // 使能 WIFI
    if (EnableWifi() != WIFI_SUCCESS)
    {
        printf("EnableWifi failed, error = %d\r\n", error);
        return -1;
    }

    // 判断 WIFI 是否激活
    if (IsWifiActive() == 0)
    {
        printf("Wifi station is not actived.\r\n");
        return -1;
    }

    // 分配空间，保存 WiFi 信息
    info = malloc(sizeof(WifiScanInfo) * WIFI_SCAN_HOTSPOT_LIMIT);
    if (info == NULL)
    {
        return -1;
    }
    // 轮询查找 WiFi 列表
    do{
        // 重置标志位
        ssid_count = 0;
        g_staScanSuccess = 0;

        // 开始扫描
        Scan();

        // 等待扫描结果
        WaitSacnResult();

        // 获取扫描列表
        error = GetScanInfoList(info, &size);

    }while(g_staScanSuccess != 1);

    // 打印 WiFi 列表
    printf("********************\r\n");
    for(uint8_t i = 0; i < ssid_count; i++)
    {
        printf("no:%03d, ssid:%-30s, rssi:%5d\r\n", i+1, info[i].ssid, info[i].rssi/100);
    }
    printf("********************\r\n");
    
    // 连接指定的 WiFi 热点
    for(uint8_t i = 0; i < ssid_count; i++)
    {
        if (strcmp(ssid, info[i].ssid) == 0)
        {
            int result;

            printf("Select:%3d wireless, Waiting...\r\n", i+1);

            // 拷贝要连接的热点信息
            WifiDeviceConfig select_ap_config = {0};
            strcpy(select_ap_config.ssid, info[i].ssid);
            strcpy(select_ap_config.preSharedKey, psk);
            select_ap_config.securityType = SELECT_WIFI_SECURITYTYPE;

            if (AddDeviceConfig(&select_ap_config, &result) == WIFI_SUCCESS)
            {
                if (ConnectTo(result) == WIFI_SUCCESS && WaitConnectResult() == 1)
                {
                    printf("WiFi connect succeed!\r\n");
                    g_lwip_netif = netifapi_netif_find(SELECT_WLAN_PORT);
                    break;
                }
            }
        }

        if(i == ssid_count-1)
        {
            printf("ERROR: No wifi as expected\r\n");
            while(1) osDelay(100);
        }
    }

     // 启动 DHCP
    if (g_lwip_netif)
    {
        dhcp_start(g_lwip_netif);
        printf("begain to dhcp\r\n");
    }


    // 等待 DHCP
    for(;;)
    {
        if(dhcp_is_bound(g_lwip_netif) == ERR_OK)
        {
            printf("<-- DHCP state:OK -->\r\n");

            // 打印获取到的 IP 信息
            netifapi_netif_common(g_lwip_netif, dhcp_clients_info_show, NULL);
            break;
        }

        printf("<-- DHCP state:Inprogress -->\r\n");
        osDelay(100);
    }

    osDelay(100);

    return 0;
}

// WiFi 初始化
static void WiFiInit(void)
{
    // 打印初始化开始信息
    printf("<--Wifi Init-->\r\n");
    // 设置 WiFi 扫描状态改变时的处理函数
    g_wifiEventHandler.OnWifiScanStateChanged = OnWifiScanStateChangedHandler;
    // 设置 WiFi 连接状态改变时的处理函数
    g_wifiEventHandler.OnWifiConnectionChanged = OnWifiConnectionChangedHandler;
    // 设置热点有新设备加入时的处理函数
    g_wifiEventHandler.OnHotspotStaJoin = OnHotspotStaJoinHandler;
    // 设置热点有设备离开时的处理函数
    g_wifiEventHandler.OnHotspotStaLeave = OnHotspotStaLeaveHandler;
    // 设置热点状态改变时的处理函数
    g_wifiEventHandler.OnHotspotStateChanged = OnHotspotStateChangedHandler;
    // 注册 WiFi 事件处理器
    error = RegisterWifiEvent(&g_wifiEventHandler);
    // 检查注册是否成功
    if (error != WIFI_SUCCESS)
    {
        printf("register wifi event fail!\r\n");
    }
    else
    {
        printf("register wifi event succeed!\r\n");
    }
}

// 当 WiFi 扫描状态改变时的处理函数
static void OnWifiScanStateChangedHandler(int state, int size)
{
    if (size > 0)
    {
        ssid_count = size;
        g_staScanSuccess = 1;
    }
    printf("callback function for wifi scan:%d, %d\r\n", state, size);
    return;
}

// 当 WiFi 连接状态改变时的处理函数
static void OnWifiConnectionChangedHandler(int state, WifiLinkedInfo *info)
{
    if (info == NULL)
    {
        printf("WifiConnectionChanged:info is null, stat is %d.\n", state);
    }
    else
    {
        if (state == WIFI_STATE_AVALIABLE)
        {
            g_ConnectSuccess = 1;
        }
        else
        {
            g_ConnectSuccess = 0;
        }
    }
}

// 当有设备加入热点时的处理函数
static void OnHotspotStaJoinHandler(StationInfo *info)
{
    (void)info;
    printf("STA join AP\n");
    return;
}

// 当有设备离开热点时的处理函数
static void OnHotspotStaLeaveHandler(StationInfo *info)
{
    (void)info;
    printf("HotspotStaLeave:info is null.\n");
    return;
}

// 当热点状态改变时的处理函数
static void OnHotspotStateChangedHandler(int state)
{
    printf("HotspotStateChanged:state is %d.\n", state);
    return;
}

// 等待扫描结果的函数
static void WaitSacnResult(void)
{
    int scanTimeout = DEF_TIMEOUT;
    while (scanTimeout > 0)
    {
        sleep(ONE_SECOND);
        scanTimeout--;
        if (g_staScanSuccess == 1)
        {
            printf("WaitSacnResult:wait success[%d]s\n", (DEF_TIMEOUT - scanTimeout));
            break;
        }
    }
    if (scanTimeout <= 0)
    {
        printf("WaitSacnResult:timeout!\n");
    }
}

// 等待连接结果的函数
static int WaitConnectResult(void)
{
    int ConnectTimeout = DEF_TIMEOUT;
    while (ConnectTimeout > 0)
    {
        sleep(ONE_SECOND);
        ConnectTimeout--;
        if (g_ConnectSuccess == 1)
        {
            printf("WaitConnectResult:wait success[%d]s\n", (DEF_TIMEOUT - ConnectTimeout));
            break;
        }
    }
    if (ConnectTimeout <= 0)
    {
        printf("WaitConnectResult:timeout!\n");
        return 0;
    }

    return 1;
}
