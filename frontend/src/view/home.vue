<template>
  <div :class="['home', { 'dark': darkMode }]">
    <!-- HEADER -->
    <nav class="nav">
      <a href="#" class="logo" @click.prevent="goHome">
        <div class="logo-icon">
          <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M18 3v2h-2V3H8v2H6V3H4v18h2v-2h2v2h8v-2h2v2h2V3h-2zM8 17H6v-2h2v2zm0-4H6v-2h2v2zm0-4H6V7h2v2zm10 8h-2v-2h2v2zm0-4h-2v-2h2v2zm0-4h-2V7h2v2z"/>
          </svg>
        </div>
        <span class="logo-text">Poly<span>Cinema</span></span>
      </a>

      <div class="nav-links">
        <a href="#" class="nav-link" @click.prevent="goHome">{{ t('home') }}</a>
        <a href="#" class="nav-link">{{ t('movies') }}</a>
        <a href="#" class="nav-link">{{ t('showtimes') }}</a>
        <a href="#" class="nav-link">{{ t('news') }}</a>
      </div>

      <div class="nav-actions">
        <!-- Language Switch -->
        <div class="lang-switch">
          <button 
            :class="{ active: lang === 'vi' }" 
            @click="setLanguage('vi')">VI</button>
          <button 
            :class="{ active: lang === 'en' }" 
            @click="setLanguage('en')">EN</button>
        </div>

        <!-- Theme Switch -->
        <button class="theme-toggle" @click="toggleTheme">
          {{ darkMode ? '☀️' : '🌙' }}
        </button>

        <!-- Auth Section -->
        <template v-if="!isLoggedIn">
          <router-link to="/auth" class="btn btn-outline">{{ t('login') }}</router-link>
          <router-link :to="{ path: '/auth', query: { mode: 'register' } }" class="btn btn-primary">
            {{ t('register') }}
          </router-link>
        </template>
        <template v-else>
          <div class="user-menu" @click="toggleDropdown">
            <div class="user-avatar">
              <img :src="user?.avatar || 'https://via.placeholder.com/32'" alt="Avatar">
            </div>
            <span class="user-name">{{ user?.fullName || user?.username }}</span>
            <span class="dropdown-arrow">▼</span>
          </div>
          
          <!-- Dropdown -->
          <div v-if="showDropdown" class="user-dropdown">
            <a href="#" class="dropdown-item">{{ t('profile') }}</a>
            <a href="#" class="dropdown-item">{{ t('myTickets') }}</a>
            <a href="#" @click.prevent="logout" class="dropdown-item logout">{{ t('logout') }}</a>
          </div>
        </template>
      </div>
    </nav>

    <!-- HERO / BANNER -->
    <div class="hero" :style="bannerStyle">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <div class="hero-text">
          <h1 v-if="banner">{{ banner.title }}</h1>
          <h1 v-else-if="loading.banner">{{ t('loading') }}...</h1>
          <h1 v-else>{{ t('welcome') }}</h1>
          
          <p v-if="banner">{{ banner.description }}</p>
          <p v-else-if="!loading.banner">{{ t('experienceCinema') }}</p>
          
          <div class="hero-btns">
            <button class="btn btn-primary" @click="bookTicket">
              {{ t('bookNow') }}
            </button>
            <button class="btn btn-outline" @click="scrollToMovies">
              {{ t('nowShowing') }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- PHIM ĐANG CHIẾU -->
    <section id="phim" class="section">
      <div class="section-header">
        <h2 class="section-title">{{ t('nowShowing') }} <span>{{ t('movies') }}</span></h2>
        <a href="#" class="see-all">{{ t('seeAll') }} →</a>
      </div>

      <!-- Tabs -->
      <div class="tabs">
        <div :class="['tab', { active: activeTab === 'dangChieu' }]" @click="activeTab = 'dangChieu'">
          {{ t('nowShowing') }}
        </div>
        <div :class="['tab', { active: activeTab === 'sapChieu' }]" @click="activeTab = 'sapChieu'">
          {{ t('comingSoon') }}
        </div>
      </div>

      <!-- Loading -->
      <div v-if="loading.dangChieu || loading.sapChieu" class="movie-grid">
        <div v-for="n in 5" :key="n" class="movie-card skeleton">
          <div class="movie-thumb skeleton-thumb"></div>
          <div class="movie-info">
            <div class="skeleton-text"></div>
            <div class="skeleton-text short"></div>
          </div>
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="error.dangChieu || error.sapChieu" class="error-message">
        {{ error.dangChieu || error.sapChieu }}
      </div>

      <!-- Content -->
      <div v-else class="movie-grid">
        <div 
          v-for="movie in (activeTab === 'dangChieu' ? phimDangChieu : phimSapChieu)" 
          :key="movie.id"
          class="movie-card"
          @click="viewMovie(movie)"
        >
          <div class="movie-thumb">
            <img :src="movie.poster" :alt="movie.title" />
            <span class="movie-badge" :class="getAgeClass(movie.ageRating)">{{ movie.ageRating }}</span>
          </div>
          <div class="movie-info">
            <div class="movie-title">{{ movie.title }}</div>
            <div class="movie-meta">
              {{ movie.duration }} phút • {{ movie.genre }}
            </div>
            <div class="movie-rating">★ {{ movie.rating }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- PROMO -->
    <section class="promo-section">
      <div class="promo-banner">
        <div class="promo-text">
          <h2>🎉 {{ t('promoTitle') }}</h2>
          <p>{{ t('promoDesc') }}</p>
        </div>
        <button class="btn-white" @click="registerMember">{{ t('joinNow') }}</button>
      </div>
    </section>

    <!-- RẠP -->
    <section id="rap" class="section">
      <div class="section-header">
        <h2 class="section-title">{{ t('cinemas') }} <span>{{ t('system') }}</span></h2>
        <a href="#" class="see-all">{{ t('seeAll') }} →</a>
      </div>
      <div class="cinema-list">
        <div v-for="cinema in cinemas" :key="cinema.id" class="cinema-card">
          <div class="cinema-icon">
            <svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>
          </div>
          <div>
            <div class="cinema-name">{{ cinema.name }}</div>
            <div class="cinema-addr">{{ cinema.address }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- FOOTER -->
    <footer>
      <div class="footer-grid">
        <div>
          <div class="footer-logo">Poly<span>Cinema</span></div>
          <p class="footer-desc">{{ t('footerDesc') }}</p>
        </div>
        <div class="footer-col">
          <h4>{{ t('about') }}</h4>
          <a href="#">{{ t('intro') }}</a>
          <a href="#">{{ t('cinemaSystem') }}</a>
          <a href="#">{{ t('recruitment') }}</a>
          <a href="#">{{ t('contact') }}</a>
        </div>
        <div class="footer-col">
          <h4>{{ t('services') }}</h4>
          <a href="#">{{ t('bookOnline') }}</a>
          <a href="#">{{ t('membership') }}</a>
          <a href="#">{{ t('promotions') }}</a>
        </div>
        <div class="footer-col">
          <h4>{{ t('support') }}</h4>
          <a href="#">{{ t('faq') }}</a>
          <a href="#">{{ t('terms') }}</a>
          <a href="#">{{ t('privacy') }}</a>
        </div>
      </div>
      <div class="footer-bottom">
        © 2026 PolyCinema. {{ t('allRights') }}
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// API Base
const API_BASE = 'http://localhost:8080/api'

// Translations
const translations = {
  vi: {
    home: 'Trang chủ',
    movies: 'Phim',
    showtimes: 'Lịch chiếu',
    news: 'Tin tức',
    login: 'Đăng nhập',
    register: 'Đăng ký',
    profile: 'Hồ sơ',
    myTickets: 'Vé của tôi',
    logout: 'Đăng xuất',
    nowShowing: 'Đang chiếu',
    comingSoon: 'Sắp chiếu',
    seeAll: 'Xem tất cả',
    bookNow: 'Đặt vé ngay',
    welcome: 'Trải Nghiệm Điện Ảnh Đỉnh Cao',
    experienceCinema: 'Đặt vé trực tuyến nhanh chóng tại PolyCinema',
    promoTitle: 'Ưu Đãi Thứ 3 Hàng Tuần',
    promoDesc: 'Giảm 30% tất cả vé xem phim vào thứ 3. Áp dụng cho thành viên.',
    joinNow: 'Tham Gia Ngay',
    cinemas: 'Hệ Thống',
    system: 'Rạp Chiếu',
    about: 'Về PolyCinema',
    intro: 'Giới thiệu',
    cinemaSystem: 'Hệ thống rạp',
    recruitment: 'Tuyển dụng',
    contact: 'Liên hệ',
    services: 'Dịch vụ',
    bookOnline: 'Mua vé online',
    membership: 'Thẻ thành viên',
    promotions: 'Khuyến mãi',
    support: 'Hỗ trợ',
    faq: 'Câu hỏi thường gặp',
    terms: 'Điều khoản',
    privacy: 'Bảo mật',
    footerDesc: 'Hệ thống rạp chiếu phim hiện đại với chất lượng âm thanh và hình ảnh đỉnh cao.',
    allRights: 'All rights reserved.',
    loading: 'Đang tải',
  },
  en: {
    home: 'Home',
    movies: 'Movies',
    showtimes: 'Showtimes',
    news: 'News',
    login: 'Login',
    register: 'Register',
    profile: 'Profile',
    myTickets: 'My Tickets',
    logout: 'Logout',
    nowShowing: 'Now Showing',
    comingSoon: 'Coming Soon',
    seeAll: 'See All',
    bookNow: 'Book Now',
    welcome: 'Premium Cinema Experience',
    experienceCinema: 'Book tickets online quickly at PolyCinema',
    promoTitle: 'Tuesday Weekly Offer',
    promoDesc: '30% off all movie tickets every Tuesday. For members only.',
    joinNow: 'Join Now',
    cinemas: 'Cinema',
    system: 'System',
    about: 'About Us',
    intro: 'Introduction',
    cinemaSystem: 'Cinema System',
    recruitment: 'Recruitment',
    contact: 'Contact',
    services: 'Services',
    bookOnline: 'Book Online',
    membership: 'Membership',
    promotions: 'Promotions',
    support: 'Support',
    faq: 'FAQ',
    terms: 'Terms',
    privacy: 'Privacy',
    footerDesc: 'Modern cinema system with top-notch sound and image quality.',
    allRights: 'All rights reserved.',
    loading: 'Loading',
  }
}

// Reactive states
const lang = ref(localStorage.getItem('poly_lang') || 'vi')
const darkMode = ref(localStorage.getItem('poly_theme') !== 'light')

const token = ref(localStorage.getItem('token') || null)
const user = ref(null)
const showDropdown = ref(false)

const banner = ref(null)
const phimDangChieu = ref([])
const phimSapChieu = ref([])
const activeTab = ref('dangChieu')

const loading = reactive({
  profile: false,
  banner: false,
  dangChieu: false,
  sapChieu: false
})

const error = reactive({
  profile: '',
  banner: '',
  dangChieu: '',
  sapChieu: ''
})

const cinemas = [
  { id: 1, name: 'PolyCinema Quận 1', address: '135 Lê Thánh Tôn, Q.1, TP.HCM' },
  { id: 2, name: 'PolyCinema Thủ Đức', address: 'Vincom Thủ Đức, TP.HCM' },
  { id: 3, name: 'PolyCinema Quận 7', address: 'SC VivoCity, Q.7, TP.HCM' },
]

// Computed
const isLoggedIn = computed(() => !!token.value)

const bannerStyle = computed(() => {
  if (!banner.value?.image) return {}
  return {
    backgroundImage: `url(${banner.value.image})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center'
  }
})

// Functions
const t = (key) => translations[lang.value][key] || key

const setLanguage = (newLang) => {
  lang.value = newLang
  localStorage.setItem('poly_lang', newLang)
}

const toggleTheme = () => {
  darkMode.value = !darkMode.value
  localStorage.setItem('poly_theme', darkMode.value ? 'dark' : 'light')
  document.documentElement.classList.toggle('dark', darkMode.value)
}

const loadProfile = async () => {
  if (!token.value) {
    user.value = null
    return
  }
  
  loading.profile = true
  error.profile = ''
  
  try {
    const res = await axios.get(`${API_BASE}/user/profile`, {
      headers: { Authorization: `Bearer ${token.value}` }
    })
    user.value = res.data
    localStorage.setItem('user', JSON.stringify(res.data))
  } catch (err) {
    console.error(err)
    if (err.response?.status === 401) {
      logout()
    } else {
      error.profile = 'Không thể tải thông tin người dùng'
      user.value = null
    }
  } finally {
    loading.profile = false
  }
}

const loadBanner = async () => {
  loading.banner = true
  error.banner = ''
  
  try {
    const res = await axios.get(`${API_BASE}/phim/banner`)
    banner.value = res.data
  } catch (err) {
    console.error('Banner load error:', err)
    error.banner = 'Không tải được banner'
  } finally {
    loading.banner = false
  }
}

const loadDangChieu = async () => {
  loading.dangChieu = true
  error.dangChieu = ''
  
  try {
    const res = await axios.get(`${API_BASE}/phim/dang-chieu`)
    phimDangChieu.value = res.data
  } catch (err) {
    console.error('Dang chieu error:', err)
    error.dangChieu = 'Không tải được phim đang chiếu'
  } finally {
    loading.dangChieu = false
  }
}

const loadSapChieu = async () => {
  loading.sapChieu = true
  error.sapChieu = ''
  
  try {
    const res = await axios.get(`${API_BASE}/phim/sap-chieu`)
    phimSapChieu.value = res.data
  } catch (err) {
    console.error('Sap chieu error:', err)
    error.sapChieu = 'Không tải được phim sắp chiếu'
  } finally {
    loading.sapChieu = false
  }
}

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
}

const getAgeClass = (rating) => {
  if (!rating) return 'badge-green'
  if (rating === 'P' || rating === 'G') return 'badge-green'
  if (String(rating).includes('13')) return 'badge-yellow'
  return 'badge-red'
}

const viewMovie = (movie) => {
  console.log('View movie:', movie)
  alert(`Đang xem chi tiết: ${movie.title}`)
}

const bookTicket = () => {
  if (!isLoggedIn.value) {
    alert('Vui lòng đăng nhập để đặt vé')
    router.push('/auth')
    return
  }

  alert('Chức năng đặt vé sẽ được triển khai sau')
}

const scrollToMovies = () => {
  document.getElementById('phim')?.scrollIntoView({ behavior: 'smooth' })
}

const registerMember = () => {
  if (!isLoggedIn.value) {
    router.push({ path: '/auth', query: { mode: 'register' } })
    return
  }

  alert('Chức năng đăng ký thành viên')
}

const goHome = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const setupAxios = () => {
  axios.interceptors.request.use(config => {
    if (token.value) {
      config.headers.Authorization = `Bearer ${token.value}`
    }
    return config
  })
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  token.value = null
  user.value = null
  showDropdown.value = false
  router.push('/')
}

// Lifecycle
onMounted(() => {
  setupAxios()
  if (token.value) {
    loadProfile()
  }
  loadBanner()
  loadDangChieu()
  loadSapChieu()

  document.documentElement.classList.toggle('dark', darkMode.value)
  
  document.addEventListener('click', (e) => {
    if (!e.target.closest('.user-menu') && !e.target.closest('.user-dropdown')) {
      showDropdown.value = false
    }
  })
})
</script>

<style scoped>
:root {
  --primary: #FF6B00;
  --primary-dark: #E05A00;
  --primary-light: #FF8533;
  --sidebar-w: 0;
}

.home {
  font-family: 'Nunito', system-ui, -apple-system, sans-serif;
  line-height: 1.6;
  background: var(--bg);
  color: var(--text);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: none;
  overflow-x: clip;
}

.home *,
.home *::before,
.home *::after {
  box-sizing: border-box;
}

/* ==================== LIGHT MODE ==================== */
.home {
  --bg: #f8fafc;
  --surface: #ffffff;
  --text: #1e2937;
  --text-secondary: #64748b;
  --border: #e2e8f0;
  --card-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.05);
}

.home.dark {
  --bg: #0f172a;
  --surface: #1e2937;
  --text: #f1f5f9;
  --text-secondary: #94a3b8;
  --border: #334155;
  --card-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.3);
}

/* NAV */
.nav {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(16px);
  border-bottom: 2px solid var(--border);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 0 32px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  box-sizing: border-box;
}

.home.dark .nav {
  background: rgba(15, 23, 42, 0.98);
  border-bottom-color: #334155;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: inherit;
  flex-shrink: 0;
}

.logo-icon {
  width: 44px;
  height: 44px;
  background: var(--primary);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.logo-icon:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(255, 107, 0, 0.3);
}

.logo-icon svg {
  width: 24px;
  height: 24px;
  fill: white;
}

.logo-text {
  font-size: 24px;
  font-weight: 900;
  letter-spacing: -0.8px;
}

.logo-text span {
  color: var(--primary);
}

.nav-links {
  display: flex;
  gap: 40px;
  flex: 1;
  justify-content: center;
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 600;
  font-size: 15px;
  transition: all 0.3s ease;
  position: relative;
  padding: 4px 0;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  width: 0;
  height: 3px;
  background: var(--primary);
  border-radius: 2px;
  transform: translateX(-50%);
  transition: width 0.3s ease;
}

.nav-link:hover {
  color: var(--primary);
}

.nav-link:hover::after {
  width: 100%;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.lang-switch {
  display: flex;
  background: #f1f5f9;
  border-radius: 9999px;
  padding: 2px;
  font-size: 13px;
  font-weight: 700;
}

.home.dark .lang-switch {
  background: #334155;
}

.lang-switch button {
  padding: 4px 12px;
  border-radius: 9999px;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.lang-switch button.active {
  background: white;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.home.dark .lang-switch button.active {
  background: #475569;
  color: white;
}

.theme-toggle {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  border: none;
  background: #f1f5f9;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.home.dark .theme-toggle {
  background: #334155;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 9999px;
  transition: background 0.2s;
}

.user-menu:hover {
  background: #f8fafc;
}

.home.dark .user-menu:hover {
  background: #334155;
}

.user-avatar img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--primary);
}

.user-name {
  font-weight: 600;
  font-size: 14px;
  max-width: 140px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-dropdown {
  position: absolute;
  top: 68px;
  right: 40px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.15);
  min-width: 180px;
  z-index: 200;
  overflow: hidden;
}

.dropdown-item {
  display: block;
  padding: 12px 20px;
  color: var(--text);
  text-decoration: none;
  font-size: 14px;
}

.dropdown-item:hover {
  background: #f8fafc;
}

.home.dark .dropdown-item:hover {
  background: #334155;
}

.dropdown-item.logout {
  color: #ef4444;
  border-top: 1px solid var(--border);
}

/* HERO */
.hero {
  min-height: clamp(520px, calc(100vh - 70px), 780px);
  position: relative;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  color: white;
  margin-top: 0;
  border-radius: 0;
  overflow: hidden;
  box-shadow: none;
  width: 100%;
  isolation: isolate;
}

.hero::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 80% 20%, rgba(255, 107, 0, 0.24), transparent 36%),
              linear-gradient(120deg, #111827, #1f2937 52%, #ea580c);
  z-index: -1;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(0,0,0,0.65), rgba(15,23,42,0.5)), 
              linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.6));
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 60px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
}

.hero-text h1 {
  font-size: 56px;
  font-weight: 800;
  line-height: 1.15;
  margin-bottom: 20px;
  text-shadow: 0 4px 16px rgba(0,0,0,0.7);
  animation: slideInLeft 0.8s ease-out;
}

.hero-text p {
  font-size: 20px;
  max-width: 540px;
  margin-bottom: 40px;
  opacity: 0.95;
  animation: slideInLeft 0.8s ease-out 0.1s both;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.hero-btns {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  animation: slideInLeft 0.8s ease-out 0.2s both;
}

.btn {
  padding: 14px 32px;
  border-radius: 12px;
  font-weight: 800;
  font-size: 15px;
  cursor: pointer;
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary), #ff8c38);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(255, 107, 0, 0.4);
}

.btn-primary:active {
  transform: translateY(-2px);
}

.btn-outline {
  background: transparent;
  border: 2.5px solid white;
  color: white;
  font-weight: 800;
}

.btn-outline:hover {
  background: white;
  color: var(--primary);
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(255, 255, 255, 0.2);
}

.btn-white {
  background: white;
  color: var(--primary);
  padding: 12px 32px;
  border-radius: 9999px;
  font-weight: 700;
}

/* SECTIONS */
.section {
  padding: 80px 60px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 48px;
}

.section-title {
  font-size: 32px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.section-title span {
  color: var(--primary);
}

.see-all {
  color: var(--primary);
  text-decoration: none;
  font-weight: 700;
  font-size: 15px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.see-all:hover {
  transform: translateX(4px);
}

.tabs {
  display: flex;
  gap: 8px;
  border-bottom: 3px solid var(--border);
  margin-bottom: 40px;
  overflow-x: auto;
  padding-bottom: 0;
}

.tab {
  position: relative;
  padding: 16px 28px;
  font-weight: 700;
  font-size: 15px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  border-bottom: 3px solid transparent;
  margin-bottom: -3px;
}

.tab:hover {
  color: var(--text);
}

.tab.active {
  color: var(--primary);
  border-bottom-color: var(--primary);
}

/* MOVIE GRID */
.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 28px;
  width: 100%;
}

.movie-card {
  background: var(--surface);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: var(--card-shadow);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--border);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.movie-card:hover {
  transform: translateY(-16px);
  box-shadow: 0 25px 50px -12px rgb(255 107 0 / 0.3);
  border-color: var(--primary);
}

.movie-thumb {
  position: relative;
  aspect-ratio: 2 / 3;
  overflow: hidden;
}

.movie-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.movie-card:hover .movie-thumb img {
  transform: scale(1.15);
}

.movie-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 6px 12px;
  font-size: 12px;
  font-weight: 800;
  border-radius: 6px;
  color: white;
  backdrop-filter: blur(4px);
  text-transform: uppercase;
}

.badge-red { background: rgba(239, 68, 68, 0.95); }
.badge-yellow { background: rgba(234, 179, 8, 0.95); }
.badge-green { background: rgba(34, 197, 94, 0.95); }

.movie-info {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.movie-title {
  font-weight: 800;
  font-size: 16px;
  margin-bottom: 8px;
  line-height: 1.3;
  color: var(--text);
  flex-grow: 1;
}

.movie-meta {
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.movie-rating {
  color: var(--primary);
  font-weight: 800;
  font-size: 15px;
  display: flex;
  align-items: center;
  gap: 4px;
}

/* SKELETON */
.skeleton {
  animation: pulse 1.8s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

.skeleton-thumb {
  height: 100%;
  background: #e2e8f0;
}

.home.dark .skeleton-thumb {
  background: #334155;
}

.skeleton-text {
  height: 16px;
  background: #e2e8f0;
  margin-bottom: 10px;
  border-radius: 6px;
}

.home.dark .skeleton-text {
  background: #475569;
}

.skeleton-text.short {
  width: 60%;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* ERROR MESSAGE */
.error-message {
  background: #fee2e2;
  border: 2px solid #fecaca;
  color: #991b1b;
  padding: 20px 24px;
  border-radius: 12px;
  font-weight: 600;
  text-align: center;
}

.home.dark .error-message {
  background: #7f1d1d;
  border-color: #991b1b;
  color: #fecaca;
}

/* PROMO */
.promo-section {
  padding: 0 60px 80px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.promo-banner {
  background: linear-gradient(135deg, var(--primary), #ff8c38, #ffa84d);
  border-radius: 24px;
  padding: 64px 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: white;
  box-shadow: 0 20px 40px rgba(255 107 0 / 0.3);
  position: relative;
  overflow: hidden;
}

.promo-banner::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 500px;
  height: 500px;
  background: rgba(255,255,255,0.05);
  border-radius: 50%;
}

.promo-text {
  z-index: 2;
  position: relative;
}

.promo-text h2 {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 12px;
}

.promo-text p {
  font-size: 18px;
  opacity: 0.95;
}

.btn-white {
  background: white;
  color: var(--primary);
  padding: 14px 36px;
  border-radius: 12px;
  font-weight: 800;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 15px;
  z-index: 2;
  position: relative;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.btn-white:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.25);
}

/* CINEMA */
.cinema-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 24px;
  width: 100%;
}

.cinema-card {
  background: var(--surface);
  border: 2px solid var(--border);
  border-radius: 16px;
  padding: 28px;
  display: flex;
  gap: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.cinema-card:hover {
  border-color: var(--primary);
  transform: translateY(-8px);
  box-shadow: 0 16px 32px rgba(255 107 0 / 0.15);
}

.cinema-icon {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, var(--primary), #ff8c38);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.cinema-icon svg {
  width: 28px;
  height: 28px;
}

.cinema-name {
  font-weight: 800;
  font-size: 18px;
  color: var(--text);
}

.cinema-addr {
  color: var(--text-secondary);
  font-size: 14px;
  margin-top: 4px;
}

/* FOOTER */
footer {
  background: linear-gradient(135deg, #0f172a, #1e2937);
  color: #cbd5e1;
  padding: 80px 60px 40px;
  margin-top: 100px;
  border-top: 2px solid #334155;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 60px;
  max-width: 1400px;
  margin: 0 auto 60px;
}

.footer-logo {
  font-size: 28px;
  font-weight: 800;
  color: var(--primary);
  margin-bottom: 16px;
}

.footer-logo span {
  color: white;
}

.footer-desc {
  color: #94a3b8;
  line-height: 1.6;
  margin-bottom: 20px;
}

.footer-col h4 {
  color: white;
  margin-bottom: 20px;
  font-weight: 800;
  font-size: 15px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.footer-col a {
  display: block;
  color: #94a3b8;
  text-decoration: none;
  margin-bottom: 12px;
  transition: all 0.3s ease;
  font-size: 14px;
}

.footer-col a:hover {
  color: var(--primary);
  transform: translateX(4px);
}

.footer-bottom {
  text-align: center;
  padding-top: 40px;
  border-top: 1px solid #334155;
  margin-top: 40px;
  color: #64748b;
  font-size: 14px;
}

/* ================== RESPONSIVE DESIGN ================== */

/* Tablet - iPad landscape (1024px) */
@media (max-width: 1024px) {
  :root {
    --sidebar-w: 0;
  }

  .nav {
    padding: 0 24px;
  }

  .nav-links {
    gap: 24px;
  }

  .hero {
    min-height: 520px;
  }

  .hero-content {
    padding: 0 40px;
  }

  .hero-text h1 {
    font-size: 48px;
  }

  .section {
    padding: 60px 40px;
  }

  .promo-section {
    padding: 0 40px 60px;
  }

  .promo-banner {
    padding: 48px 40px;
    gap: 32px;
  }

  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }

  .footer-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
  }
}

/* Tablet - iPad (768px) */
@media (max-width: 768px) {
  .nav {
    padding: 0 16px;
    height: 64px;
  }

  .logo {
    gap: 8px;
  }

  .logo-icon {
    width: 40px;
    height: 40px;
  }

  .logo-text {
    font-size: 20px;
  }

  .nav-links {
    display: none;
  }

  .nav-actions {
    gap: 8px;
  }

  .lang-switch {
    font-size: 12px;
  }

  .lang-switch button {
    padding: 3px 8px;
  }

  .hero {
    min-height: 440px;
  }

  .hero-content {
    padding: 0 24px;
  }

  .hero-text h1 {
    font-size: 36px;
    margin-bottom: 16px;
  }

  .hero-text p {
    font-size: 16px;
    margin-bottom: 24px;
  }

  .hero-btns {
    gap: 12px;
  }

  .btn {
    padding: 12px 24px;
    font-size: 14px;
  }

  .section {
    padding: 48px 24px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .section-title {
    font-size: 28px;
  }

  .tabs {
    gap: 4px;
    margin-bottom: 32px;
  }

  .tab {
    padding: 12px 16px;
    font-size: 14px;
  }

  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 16px;
  }

  .movie-badge {
    font-size: 10px;
    padding: 4px 8px;
  }

  .movie-info {
    padding: 12px;
  }

  .movie-title {
    font-size: 14px;
  }

  .movie-rating {
    font-size: 13px;
  }

  .promo-section {
    padding: 0 24px 48px;
  }

  .promo-banner {
    padding: 40px 24px;
    border-radius: 16px;
    flex-direction: column;
    text-align: center;
  }

  .promo-text h2 {
    font-size: 28px;
  }

  .promo-text p {
    font-size: 16px;
  }

  .cinema-list {
    grid-template-columns: 1fr;
  }

  .cinema-card {
    padding: 20px;
  }

  .footer-grid {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .footer-grid > div:first-child {
    grid-column: 1 / -1;
  }

  footer {
    padding: 60px 24px 32px;
  }
}

/* Mobile - Small phones (480px) */
@media (max-width: 480px) {
  .nav {
    padding: 0 12px;
  }

  .logo {
    gap: 6px;
  }

  .logo-icon {
    width: 36px;
    height: 36px;
  }

  .logo-text {
    font-size: 18px;
  }

  .nav-actions {
    gap: 4px;
  }

  .nav-actions .btn {
    width: auto;
    padding: 8px 10px;
    font-size: 12px;
  }

  .theme-toggle {
    width: 34px;
    height: 34px;
    font-size: 16px;
  }

  .user-menu {
    padding: 4px 8px;
  }

  .user-name {
    display: none;
  }

  .user-dropdown {
    right: 12px;
  }

  .hero {
    min-height: 420px;
  }

  .hero-content {
    padding: 0 16px;
  }

  .hero-text h1 {
    font-size: 28px;
    line-height: 1.2;
    margin-bottom: 12px;
  }

  .hero-text p {
    font-size: 14px;
    margin-bottom: 20px;
  }

  .hero-btns {
    flex-direction: column;
    gap: 10px;
  }

  .btn {
    width: 100%;
    justify-content: center;
    padding: 12px 20px;
    font-size: 14px;
  }

  .section {
    padding: 32px 16px;
  }

  .section-title {
    font-size: 24px;
  }

  .see-all {
    font-size: 13px;
  }

  .tabs {
    margin-bottom: 24px;
  }

  .tab {
    padding: 10px 12px;
    font-size: 12px;
  }

  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(130px, 1fr));
    gap: 12px;
  }

  .movie-info {
    padding: 10px;
  }

  .movie-title {
    font-size: 12px;
    margin-bottom: 4px;
  }

  .movie-meta {
    font-size: 11px;
  }

  .movie-rating {
    font-size: 12px;
  }

  .promo-section {
    padding: 0 16px 32px;
  }

  .promo-banner {
    padding: 32px 16px;
    border-radius: 12px;
  }

  .promo-banner::before {
    width: 300px;
    height: 300px;
  }

  .promo-text h2 {
    font-size: 24px;
  }

  .promo-text p {
    font-size: 14px;
  }

  .btn-white {
    padding: 12px 24px;
    border-radius: 10px;
    font-size: 14px;
  }

  .cinema-icon {
    width: 48px;
    height: 48px;
  }

  .cinema-icon svg {
    width: 24px;
    height: 24px;
  }

  .cinema-name {
    font-size: 16px;
  }

  .cinema-addr {
    font-size: 12px;
  }

  footer {
    padding: 40px 16px 24px;
    margin-top: 60px;
  }

  .footer-logo {
    font-size: 24px;
  }

  .footer-col h4 {
    font-size: 13px;
  }

  .footer-col a {
    font-size: 13px;
    margin-bottom: 8px;
  }

  .footer-bottom {
    font-size: 12px;
  }
}
</style>