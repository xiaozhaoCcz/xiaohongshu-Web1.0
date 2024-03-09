import { createRouter, createWebHistory } from "vue-router";
import Login from "@/pages/login.vue";
import Dashboard from "@/pages/dashboard/dashboard.vue";

export const routes = [
  {
    path: "/",
    redirect: "/index",
  },
  {
    name: "login",
    path: "/login",
    component: Login,
  },
  {
    name: "index",
    path: "/index",
    component: () => import("@/pages/index.vue"),
    redirect: "/dashboard",
    children: [
      {
        path: "/dashboard",
        component: Dashboard,
        name: "dashboard",
      },
      {
        path: "/followTrend",
        component: () => import("@/pages/follow-trend/follow-trend.vue"),
        name: "followTrend",
      },
      {
        path: "/notice",
        component: () => import("@/pages/message/index.vue"),
        name: "notice",
      },
      {
        path: "/user",
        component: () => import("@/pages/user/index.vue"),
        name: "user",
      },
      {
        path: "/push",
        component: () => import("@/pages/push/index.vue"),
        name: "push",
      },
    ],
  },
];
const router = createRouter({
  scrollBehavior: () => ({ left: 0, top: 0 }),
  history: createWebHistory(),
  routes,
});
router.beforeEach((to, from, next) => {
  next();
});
export default router;
