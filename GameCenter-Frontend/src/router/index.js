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

    {
      path: '/wishlist',
      name: 'wishlist',
      component: () => import("../views/Wishlist.vue"), 
    },

    {
      path: "/cart",
      name: "Cart",
      component: () => import("../views/Cart.vue"),
    }
  ],
});

export default router;
