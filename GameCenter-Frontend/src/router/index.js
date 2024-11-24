import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../views/HomePage.vue";

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
  ],
});

export default router;
