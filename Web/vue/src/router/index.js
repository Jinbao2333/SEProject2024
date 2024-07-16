import {createRouter, createWebHistory} from 'vue-router'
import Layout from "@/layout/Layout";

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/home",
        children: [
            {
                path: 'home',
                name: 'Home',
                component: () => import("@/views/Home"),
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("@/views/Register")
    },


]

const router = createRouter({
    //base: process.env.BASE_URL,
    history: createWebHistory(process.env.BASE_URL),
    routes
})

activeRouter()

function activeRouter() {
    const userStr = sessionStorage.getItem("userPermission")
    if (userStr) {
        const userPermission = JSON.parse(userStr)
        let root = {
            path: '/',
            name: 'Layout',
            component: Layout,
            redirect: "/home",
            children: []
        }
        userPermission.forEach(p => {
            let obj = {
                path: p.path,
                name: p.name,
                component: () => import("@/views/" + p.name)
            };
            root.children.push(obj)


        })
        let obj2 = {
            path: '/changePaper',
            name: 'ChangePaper',
            component: () => import("@/views/" + 'ChangePaper')
        };
        root.children.push(obj2)

        if (router) {
            router.addRoute(root)
        }
    }
}

router.beforeEach((to, from, next) => {
    if (to.path === '/login' || to.path === '/register' || to.path === '/changePaper'||from.path ==='/detail') {
        next()
        return
    }
    let userPermission = sessionStorage.getItem("userPermission") ? JSON.parse(sessionStorage.getItem("userPermission")) : {}
    if (!userPermission || !userPermission.length) {
        next('/login')
    } else if (!userPermission.find(p => p.path === to.path)) {
        next('/login')
    } else {
        next()
    }
})
export default router
