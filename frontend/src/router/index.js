import { createRouter, createWebHistory } from 'vue-router'
import AuthPage from '@/Auth/AuthPage.vue'

const routes = [
  {
    path: '/',
    redirect: '/auth'
  },
  {
    path: '/auth',
    component: AuthPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router