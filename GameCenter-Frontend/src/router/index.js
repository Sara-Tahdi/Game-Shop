import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomePage.vue'
import CatalogView from '../views/Catalog.vue'
import GameDetails from '../views/GameDetails.vue'
import Cart from '../views/Cart.vue'
import Checkout from '../views/Checkout.vue'
import Wishlist from '../views/Wishlist.vue'
import Account from '../views/Account.vue'
import EmployeeDashboard from '../views/EmployeeDashboard.vue'
import OwnerDashboard from '../views/OwnerDashboard.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/catalog',
      name: 'catalog',
      component: CatalogView
    },
    {
      path: '/games/:id',
      name: 'GameDetails',
      component: GameDetails,
      props: true // Enable props for component
    },
    {
      path: '/cart',
      name: 'cart',
      component: Cart
    },
    {
      path: '/checkout',
      name: 'checkout',
      component: Checkout
    },
    {
      path: '/wishlist',
      name: 'wishlist',
      component: Wishlist
    },
    {
      path: '/account',
      name: 'account',
      component: Account
    },
    {
      path: '/employee-dashboard',
      name: 'employee-dashboard',
      component: EmployeeDashboard
    },
    {
      path: '/owner-dashboard',
      name: 'owner-dashboard',
      component: OwnerDashboard
    },
    // Redirect /game/:id to /games/:id
    {
      path: '/game/:id',
      redirect: to => {
        return { path: `/games/${to.params.id}` }
      }
    },
    // Catch-all route for 404
    {
      path: '/:pathMatch(.*)*',
      redirect: '/catalog'
    }
  ]
})

// Navigation guard for debugging
router.beforeEach((to, from, next) => {
  console.log('Navigation:', {
    from: from.fullPath,
    to: to.fullPath,
    params: to.params
  })
  next()
})

export default router