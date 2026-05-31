import { createRouter, createWebHistory } from 'vue-router'
import AuthPage from '@/Auth/AuthPage.vue'
import Home from '@/view/home.vue'
import AdminDashboard from '@/admin/AdminDashboard.vue'

function decodeToken(token) {
  try {
    const payload = token.split('.')[1]
    const normalizedPayload = payload.replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(decodeURIComponent(escape(atob(normalizedPayload))))
  } catch {
    return null
  }
}

function getUserRole() {
  const token = localStorage.getItem('token')
  const payload = token ? decodeToken(token) : null
  return payload?.role?.replace('ROLE_', '').toUpperCase() || ''
}

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
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminDashboard,
    meta: { requiresAuth: true, role: 'ADMIN' }
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
    const role = getUserRole()
    next({ path: role === 'ADMIN' ? '/admin' : '/', replace: true })
    return
  }

  const token = localStorage.getItem('token')
  const requiresAuth = to.meta?.requiresAuth !== false
  const role = getUserRole()

  if (requiresAuth && !token) {
    next('/auth')
  } else if (to.meta?.role && role !== to.meta.role) {
    next('/')
  } else if (!requiresAuth && token && to.path === '/auth') {
    next(role === 'ADMIN' ? '/admin' : '/')
  } else {
    next()
  }
})

export default router