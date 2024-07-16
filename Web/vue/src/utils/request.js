import axios from 'axios'
import router from "@/router";
import config from "../config"

const baseUrl = process.env.NODE_ENV === 'development' ? config.baseUrl.dev : config.baseUrl.pro
const request = axios.create({
    //baseURL: "/api",
    timeout: 30000
})

// 请求白名单，如果请求在白名单里面，将不会被拦截校验权限
const whiteUrls = ["/api/login", '/api/register','/api/register/sendemail']
// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    console.log("拦截器1")
    // 取出sessionStorage里面缓存的用户信息
    let userJson = sessionStorage.getItem("user")
    if (!whiteUrls.includes(config.url)) {
        if (!userJson) {
            console.log("tiaozhuanla")
            router.push("/login")
        }
    }
    return config
}, error => {
    console.error("拦截器before request")

    return Promise.reject(error)
});
// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        //console.log("拦截器打印")

        //console.log(response)
        let res = response.data;
        // 如果是返回的文件
        //console.log(res)
        if (response.config.responseType === 'blob') {
            console.error("blob")
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            console.error("拦截器string")

            res = res ? JSON.parse(res) : res
        }
        console.log(res.code)
        if (res.code === 3) {
            console.log("print"+ res.code)
            sessionStorage.removeItem("user")
            sessionStorage.removeItem("userPermission")
            console.error("token过期，重新登录")
            router.push("/login")
        }
        return res;
    },
    error => {
        console.log('err!!' + error) // for debug
        return Promise.reject(error)
    }
)
export default request

