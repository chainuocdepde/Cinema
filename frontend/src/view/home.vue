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
const darkMode = ref(localStorage.getItem('poly_theme') === 'dark' || true)

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
  if (rating === 'P' || rating === 'G') return 'badge-green'
  if (rating.includes('13')) return 'badge-yellow'
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
  document.getElementById('phim').scrollIntoView({ behavior: 'smooth' })
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
  
  document.addEventListener('click', (e) => {
    if (!e.target.closest('.user-menu')) {
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
}

.home {
  font-family: 'Nunito', system-ui, -apple-system, sans-serif;
  line-height: 1.6;
  background: var(--bg);
  color: var(--text);
  min-height: 100vh;
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
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 0 40px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.home.dark .nav {
  background: rgba(15, 23, 42, 0.95);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: inherit;
}

.logo-icon {
  width: 42px;
  height: 42px;
  background: var(--primary);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text {
  font-size: 26px;
  font-weight: 900;
  letter-spacing: -0.8px;
}

.logo-text span {
  color: var(--primary);
}

.nav-links {
  display: flex;
  gap: 32px;
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.nav-link:hover {
  color: var(--primary);
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
  height: 560px;
  position: relative;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  color: white;
  margin-top: -2px;
  border-radius: 0 0 24px 24px;
  overflow: hidden;
  box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1);
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(45deg, rgba(0,0,0,0.75), rgba(15,23,42,0.45));
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 40px;
  width: 100%;
}

.hero-text h1 {
  font-size: 48px;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 16px;
  text-shadow: 0 4px 12px rgba(0,0,0,0.6);
}

.hero-text p {
  font-size: 18px;
  max-width: 520px;
  margin-bottom: 32px;
  opacity: 0.95;
}

.btn {
  padding: 12px 28px;
  border-radius: 8px;
  font-weight: 700;
  font-size: 15px;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn-primary {
  background: var(--primary);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(255, 107, 0, 0.3);
}

.btn-outline {
  background: transparent;
  border: 2px solid white;
  color: white;
}

.btn-outline:hover {
  background: white;
  color: var(--primary);
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
  padding: 60px 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.section-title {
  font-size: 28px;
  font-weight: 800;
}

.section-title span {
  color: var(--primary);
}

.tabs {
  display: flex;
  gap: 4px;
  border-bottom: 2px solid var(--border);
  margin-bottom: 32px;
}

.tab {
  position: relative;
  padding: 14px 36px;
  font-weight: 700;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
}

.tab.active {
  color: var(--primary);
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100%;
  height: 4px;
  background: var(--primary);
  border-radius: 4px;
}

/* MOVIE GRID */
.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 24px;
}

.movie-card {
  background: var(--surface);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--card-shadow);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--border);
  cursor: pointer;
}

.movie-card:hover {
  transform: translateY(-14px) scale(1.04);
  box-shadow: 0 25px 50px -12px rgb(255 107 0 / 0.25);
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
  transition: transform 0.5s ease;
}

.movie-card:hover .movie-thumb img {
  transform: scale(1.12);
}

.movie-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 10px;
  font-size: 12px;
  font-weight: 700;
  border-radius: 6px;
  color: white;
}

.badge-red { background: #ef4444; }
.badge-yellow { background: #eab308; }
.badge-green { background: #22c55e; }

.movie-info {
  padding: 16px;
}

.movie-title {
  font-weight: 700;
  font-size: 15px;
  margin-bottom: 6px;
  line-height: 1.3;
}

.movie-meta {
  font-size: 13px;
  color: var(--text-secondary);
}

.movie-rating {
  color: var(--primary);
  font-weight: 700;
  margin-top: 8px;
  font-size: 14px;
}

/* SKELETON */
.skeleton {
  animation: pulse 1.6s infinite;
}

.skeleton-thumb {
  height: 100%;
  background: #e2e8f0;
}

.home.dark .skeleton-thumb {
  background: #334155;
}

.skeleton-text {
  height: 18px;
  background: #e2e8f0;
  margin-bottom: 8px;
  border-radius: 4px;
}

.home.dark .skeleton-text {
  background: #475569;
}

.skeleton-text.short {
  width: 60%;
}

/* PROMO */
.promo-banner {
  background: linear-gradient(135deg, var(--primary), #ff8c38);
  border-radius: 20px;
  padding: 48px 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: white;
  margin: 0 40px;
  box-shadow: 0 20px 25px -5px rgb(255 107 0 / 0.3);
}

/* CINEMA */
.cinema-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 20px;
}

.cinema-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  gap: 20px;
  transition: all 0.3s;
}

.cinema-card:hover {
  border-color: var(--primary);
  transform: translateY(-4px);
}

.cinema-icon {
  width: 48px;
  height: 48px;
  background: var(--primary);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.cinema-name {
  font-weight: 700;
  font-size: 17px;
}

.cinema-addr {
  color: var(--text-secondary);
  font-size: 14px;
}

/* FOOTER */
.footer {
  background: #0f172a;
  color: #cbd5e1;
  padding: 80px 40px 40px;
  margin-top: 80px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 60px;
  max-width: 1200px;
  margin: 0 auto;
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

.footer-col h4 {
  color: white;
  margin-bottom: 16px;
}

.footer-col a {
  display: block;
  color: #94a3b8;
  text-decoration: none;
  margin-bottom: 8px;
}

.footer-col a:hover {
  color: white;
}

.footer-bottom {
  text-align: center;
  padding-top: 40px;
  border-top: 1px solid #334155;
  margin-top: 60px;
}

/* RESPONSIVE */
@media (max-width: 1024px) {
  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}

@media (max-width: 768px) {
  .nav {
    padding: 0 20px;
  }
  .nav-links {
    display: none;
  }
  .hero {
    height: 420px;
  }
  .hero-text h1 {
    font-size: 36px;
  }
  .promo-banner {
    margin: 0 20px;
    padding: 32px 40px;
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
}
</style>