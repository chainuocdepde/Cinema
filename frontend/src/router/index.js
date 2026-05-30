import { createRouter, createWebHistory } from 'vue-router'
import AuthPage from '@/Auth/AuthPage.vue'
import Home from '@/view/home.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
    meta: { requiresAuth: false }
  },
  {
    path: '/auth',
    name: 'auth',
    component: AuthPage,
    meta: { requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Auth guard
router.beforeEach((to, from, next) => {
  const oauthToken = to.query?.token

  if (oauthToken) {
    localStorage.setItem('token', oauthToken)
    next({ path: '/', replace: true })
    return
  }

  const token = localStorage.getItem('token')
  const requiresAuth = to.meta?.requiresAuth !== false

  if (requiresAuth && !token) {
    next('/auth')
  } else if (!requiresAuth && token && to.path === '/auth') {
    next('/')
  } else {
    next()
  }
})

export default router