import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../views/HomePage.vue";
import GameDetails from "@/views/GameDetails.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "Homepage",
      component: HomePage,
    },
    {
      path: "/catalog",
      name: "Catalog",
      component: () => import("../views/Catalog.vue"),
    },
    {
      path: "/game/:id",
      name: "Game Details",
      component: GameDetails,
      props: true,
    },
    {
      path: "/client-profile",
      component: () => import("@/views/ClientProfile.vue"),
      meta: { requiresAuth: true, userType: "client" },
    },
    {
      path: "/employee-dashboard",
      component: () => import("@/views/EmployeeDashboard.vue"),
      meta: { requiresAuth: true, userType: "employee" },
    },
    {
      path: "/owner-dashboard",
      component: () => import("@/views/OwnerDashboard.vue"),
      meta: { requiresAuth: true, userType: "owner" },
    },
    {
      path: "/account",
      component: () => import("@/views/Account.vue"),
    },
  ],
});

export default router;
