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
        <!-- Language Switch with Flags -->
        <!-- Hiển thị cờ của ngôn ngữ ĐỐI DIỆN (click vào để chuyển sang ngôn ngữ đó) -->
        <button
          class="flag-btn"
          @click="toggleLanguage"
          :title="lang === 'vi' ? 'Switch to English' : 'Chuyển sang Tiếng Việt'"
          :aria-label="lang === 'vi' ? 'Switch to English' : 'Chuyển sang Tiếng Việt'"
        >
          <!-- Hiện cờ Mỹ khi đang dùng tiếng Anh, cờ VN khi đang dùng tiếng Việt -->
          <span class="flag-icon" v-if="lang === 'en'">
            <!-- Cờ Việt Nam -->
            <svg viewBox="0 0 30 20" xmlns="http://www.w3.org/2000/svg">
              <rect width="30" height="20" fill="#DA251D"/>
              <polygon points="15,4 16.76,9.41 22.5,9.41 17.87,12.59 19.63,18 15,14.82 10.37,18 12.13,12.59 7.5,9.41 13.24,9.41" fill="#FFFF00"/>
            </svg>
          </span>
          <span class="flag-icon" v-else>
            <!-- Cờ Mỹ -->
            <svg viewBox="0 0 30 20" xmlns="http://www.w3.org/2000/svg">
              <rect width="30" height="20" fill="#B22234"/>
              <rect y="1.538" width="30" height="1.538" fill="#FFFFFF"/>
              <rect y="4.615" width="30" height="1.538" fill="#FFFFFF"/>
              <rect y="7.692" width="30" height="1.538" fill="#FFFFFF"/>
              <rect y="10.769" width="30" height="1.538" fill="#FFFFFF"/>
              <rect y="13.846" width="30" height="1.538" fill="#FFFFFF"/>
              <rect y="16.923" width="30" height="1.538" fill="#FFFFFF"/>
              <rect width="12" height="10.769" fill="#3C3B6E"/>
              <g fill="#FFFFFF">
                <circle cx="1.5" cy="1.2" r="0.6"/>
                <circle cx="3" cy="1.2" r="0.6"/>
                <circle cx="4.5" cy="1.2" r="0.6"/>
                <circle cx="6" cy="1.2" r="0.6"/>
                <circle cx="7.5" cy="1.2" r="0.6"/>
                <circle cx="9" cy="1.2" r="0.6"/>
                <circle cx="10.5" cy="1.2" r="0.6"/>
                <circle cx="2.25" cy="2.7" r="0.6"/>
                <circle cx="3.75" cy="2.7" r="0.6"/>
                <circle cx="5.25" cy="2.7" r="0.6"/>
                <circle cx="6.75" cy="2.7" r="0.6"/>
                <circle cx="8.25" cy="2.7" r="0.6"/>
                <circle cx="9.75" cy="2.7" r="0.6"/>
                <circle cx="1.5" cy="4.2" r="0.6"/>
                <circle cx="3" cy="4.2" r="0.6"/>
                <circle cx="4.5" cy="4.2" r="0.6"/>
                <circle cx="6" cy="4.2" r="0.6"/>
                <circle cx="7.5" cy="4.2" r="0.6"/>
                <circle cx="9" cy="4.2" r="0.6"/>
                <circle cx="10.5" cy="4.2" r="0.6"/>
                <circle cx="2.25" cy="5.7" r="0.6"/>
                <circle cx="3.75" cy="5.7" r="0.6"/>
                <circle cx="5.25" cy="5.7" r="0.6"/>
                <circle cx="6.75" cy="5.7" r="0.6"/>
                <circle cx="8.25" cy="5.7" r="0.6"/>
                <circle cx="9.75" cy="5.7" r="0.6"/>
                <circle cx="1.5" cy="7.2" r="0.6"/>
                <circle cx="3" cy="7.2" r="0.6"/>
                <circle cx="4.5" cy="7.2" r="0.6"/>
                <circle cx="6" cy="7.2" r="0.6"/>
                <circle cx="7.5" cy="7.2" r="0.6"/>
                <circle cx="9" cy="7.2" r="0.6"/>
                <circle cx="10.5" cy="7.2" r="0.6"/>
                <circle cx="2.25" cy="8.7" r="0.6"/>
                <circle cx="3.75" cy="8.7" r="0.6"/>
                <circle cx="5.25" cy="8.7" r="0.6"/>
                <circle cx="6.75" cy="8.7" r="0.6"/>
                <circle cx="8.25" cy="8.7" r="0.6"/>
                <circle cx="9.75" cy="8.7" r="0.6"/>
              </g>
            </svg>
          </span>
          <span class="flag-label">{{ lang === 'vi' ? 'EN' : 'VI' }}</span>
        </button>

        <!-- Theme Switch -->
        <button class="theme-toggle" @click="toggleTheme" :aria-label="darkMode ? 'Switch to light mode' : 'Switch to dark mode'">
          <svg v-if="darkMode" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="5"/>
            <path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
          </svg>
        </button>

        <!-- Auth Section -->
        <template v-if="!isLoggedIn">
          <router-link to="/auth" class="btn btn-ghost">{{ t('login') }}</router-link>
          <router-link :to="{ path: '/auth', query: { mode: 'register' } }" class="btn btn-primary">
            {{ t('register') }}
          </router-link>
        </template>
        <template v-else>
          <div class="user-menu" @click="toggleDropdown" :aria-expanded="showDropdown" role="button" tabindex="0">
            <!-- Avatar: initials fallback nếu không có ảnh -->
            <div class="user-avatar">
              <img
                v-if="user?.avatar && !avatarError"
                :src="user.avatar"
                :alt="user?.fullName || user?.username"
                @error="avatarError = true"
              />
              <span v-else class="avatar-initials">{{ getUserInitials() }}</span>
            </div>
            <span class="user-name">{{ user?.fullName || user?.username }}</span>
            <svg class="chevron" :class="{ open: showDropdown }" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </div>

          <!-- Dropdown -->
          <transition name="dropdown">
            <div v-if="showDropdown" class="user-dropdown">
              <!-- Mini profile header -->
              <div class="dropdown-header">
                <div class="dropdown-avatar">
                  <img
                    v-if="user?.avatar && !avatarError"
                    :src="user.avatar"
                    :alt="user?.fullName || user?.username"
                  />
                  <span v-else class="avatar-initials-lg">{{ getUserInitials() }}</span>
                </div>
                <div>
                  <div class="dropdown-name">{{ user?.fullName || user?.username }}</div>
                  <div class="dropdown-email">{{ user?.email || 'member' }}</div>
                </div>
              </div>
              <div class="dropdown-divider"></div>
              <a href="#" class="dropdown-item">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                {{ t('profile') }}
              </a>
              <a href="#" class="dropdown-item">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 12V22H4V12"/><path d="M22 7H2v5h20V7z"/><path d="M12 22V7"/><path d="M12 7H7.5a2.5 2.5 0 0 1 0-5C11 2 12 7 12 7z"/><path d="M12 7h4.5a2.5 2.5 0 0 0 0-5C13 2 12 7 12 7z"/></svg>
                {{ t('myTickets') }}
              </a>
              <div class="dropdown-divider" v-if="isAdmin"></div>
              <a href="#" v-if="isAdmin" @click.prevent="goToAdmin" class="dropdown-item admin-link">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/></svg>
                Quản Lý Admin
              </a>
              <div class="dropdown-divider"></div>
              <a href="#" @click.prevent="logout" class="dropdown-item logout">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                {{ t('logout') }}
              </a>
            </div>
          </transition>
        </template>
      </div>
    </nav>

    <!-- HERO / BANNER -->
    <div class="hero" :style="bannerStyle">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <div class="hero-text">
          <div class="hero-badge">🎬 PolyCinema {{ new Date().getFullYear() }}</div>
          <h1 v-if="banner">{{ banner.title }}</h1>
          <h1 v-else-if="loading.banner">{{ t('loading') }}...</h1>
          <h1 v-else>{{ t('welcome') }}</h1>

          <p v-if="banner">{{ banner.description }}</p>
          <p v-else-if="!loading.banner">{{ t('experienceCinema') }}</p>

          <div class="hero-btns">
            <button class="btn btn-primary btn-lg" @click="bookTicket">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M15 5v14M3 5h18v14H3z"/><path d="M3 10h4M3 14h4M17 10h4M17 14h4"/></svg>
              {{ t('bookNow') }}
            </button>
            <button class="btn btn-outline btn-lg" @click="scrollToMovies">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polygon points="23 7 16 12 23 17 23 7"/><rect x="1" y="5" width="15" height="14" rx="2" ry="2"/></svg>
              {{ t('nowShowing') }}
            </button>
          </div>

          <!-- Quick stats -->
          <div class="hero-stats">
            <div class="hero-stat"><span class="stat-num">50+</span><span class="stat-label">{{ t('moviesCount') }}</span></div>
            <div class="hero-stat-divider"></div>
            <div class="hero-stat"><span class="stat-num">3</span><span class="stat-label">{{ t('cinemasCount') }}</span></div>
            <div class="hero-stat-divider"></div>
            <div class="hero-stat"><span class="stat-num">4K</span><span class="stat-label">{{ t('quality') }}</span></div>
          </div>
        </div>
      </div>
    </div>

    <!-- PHIM ĐANG CHIẾU -->
    <section id="phim" class="section">
      <div class="section-header">
        <div>
          <h2 class="section-title">{{ t('nowShowing') }} <span>{{ t('movies') }}</span></h2>
          <p class="section-sub">{{ t('sectionSubMovies') }}</p>
        </div>
        <a href="#" class="see-all">{{ t('seeAll') }} <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg></a>
      </div>

      <!-- Tabs -->
      <div class="tabs" role="tablist">
        <button :class="['tab', { active: activeTab === 'dangChieu' }]" @click="activeTab = 'dangChieu'" role="tab">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polygon points="10 8 16 12 10 16 10 8"/></svg>
          {{ t('nowShowing') }}
        </button>
        <button :class="['tab', { active: activeTab === 'sapChieu' }]" @click="activeTab = 'sapChieu'" role="tab">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
          {{ t('comingSoon') }}
        </button>
      </div>

      <!-- Loading Skeleton -->
      <div v-if="loading.dangChieu || loading.sapChieu" class="movie-grid">
        <div v-for="n in 5" :key="n" class="movie-card skeleton">
          <div class="movie-thumb skeleton-thumb"></div>
          <div class="movie-info">
            <div class="skeleton-text"></div>
            <div class="skeleton-text short"></div>
            <div class="skeleton-text shorter"></div>
          </div>
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="error.dangChieu || error.sapChieu" class="error-message">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
        {{ error.dangChieu || error.sapChieu }}
      </div>

      <!-- Content -->
      <div v-else class="movie-grid">
        <div
          v-for="movie in (activeTab === 'dangChieu' ? phimDangChieu : phimSapChieu)"
          :key="movie.id"
          class="movie-card"
          @click="viewMovie(movie)"
          :tabindex="0"
          :aria-label="movie.title"
          @keydown.enter="viewMovie(movie)"
        >
          <div class="movie-thumb">
            <img :src="movie.poster" :alt="movie.title" loading="lazy" />
            <div class="movie-overlay">
              <button class="play-btn" aria-label="Xem chi tiết">
                <svg viewBox="0 0 24 24" fill="white"><polygon points="5 3 19 12 5 21 5 3"/></svg>
              </button>
            </div>
            <span class="movie-badge" :class="getAgeClass(movie.ageRating)">{{ movie.ageRating }}</span>
          </div>
          <div class="movie-info">
            <div class="movie-title">{{ movie.title }}</div>
            <div class="movie-meta">
              <span><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>{{ movie.duration }} phút</span>
              <span class="genre-chip">{{ movie.genre }}</span>
            </div>
            <div class="movie-rating">
              <svg viewBox="0 0 24 24" fill="currentColor"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
              {{ movie.rating }}
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- PROMO -->
    <section class="promo-section">
      <div class="promo-banner">
        <div class="promo-content">
          <div class="promo-icon">🎉</div>
          <div class="promo-text">
            <h2>{{ t('promoTitle') }}</h2>
            <p>{{ t('promoDesc') }}</p>
          </div>
        </div>
        <button class="btn-white" @click="registerMember">{{ t('joinNow') }}</button>
      </div>
    </section>

    <!-- RẠP -->
    <section id="rap" class="section">
      <div class="section-header">
        <div>
          <h2 class="section-title">{{ t('cinemas') }} <span>{{ t('system') }}</span></h2>
          <p class="section-sub">{{ t('sectionSubCinemas') }}</p>
        </div>
        <a href="#" class="see-all">{{ t('seeAll') }} <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg></a>
      </div>
      <div class="cinema-list">
        <div v-for="cinema in cinemas" :key="cinema.id" class="cinema-card">
          <div class="cinema-icon">
            <svg viewBox="0 0 24 24" fill="white"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>
          </div>
          <div class="cinema-info">
            <div class="cinema-name">{{ cinema.name }}</div>
            <div class="cinema-addr">{{ cinema.address }}</div>
            <a href="#" class="cinema-link">{{ t('viewShowtimes') }} →</a>
          </div>
        </div>
      </div>
    </section>

    <!-- FOOTER -->
    <footer>
      <div class="footer-grid">
        <div class="footer-brand">
          <div class="footer-logo">Poly<span>Cinema</span></div>
          <p class="footer-desc">{{ t('footerDesc') }}</p>
          <div class="footer-social">
            <a href="#" class="social-btn" aria-label="Facebook">
              <svg viewBox="0 0 24 24" fill="currentColor"><path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"/></svg>
            </a>
            <a href="#" class="social-btn" aria-label="Instagram">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="2" width="20" height="20" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" y1="6.5" x2="17.51" y2="6.5"/></svg>
            </a>
            <a href="#" class="social-btn" aria-label="YouTube">
              <svg viewBox="0 0 24 24" fill="currentColor"><path d="M22.54 6.42a2.78 2.78 0 0 0-1.94-1.96C18.88 4 12 4 12 4s-6.88 0-8.6.46a2.78 2.78 0 0 0-1.94 1.96A29 29 0 0 0 1 12a29 29 0 0 0 .46 5.58 2.78 2.78 0 0 0 1.94 1.96C5.12 20 12 20 12 20s6.88 0 8.6-.46a2.78 2.78 0 0 0 1.94-1.96A29 29 0 0 0 23 12a29 29 0 0 0-.46-5.58z"/><polygon points="9.75 15.02 15.5 12 9.75 8.98 9.75 15.02" fill="white"/></svg>
            </a>
          </div>
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
        <span>© 2026 PolyCinema. {{ t('allRights') }}</span>
        <div class="footer-bottom-links">
          <a href="#">{{ t('terms') }}</a>
          <a href="#">{{ t('privacy') }}</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const API_BASE = 'http://localhost:8080/api'

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
    moviesCount: 'Bộ phim',
    cinemasCount: 'Rạp chiếu',
    quality: 'Chất lượng',
    sectionSubMovies: 'Khám phá những bộ phim đang chiếu và sắp ra mắt',
    sectionSubCinemas: 'Tìm rạp gần bạn nhất',
    viewShowtimes: 'Xem lịch chiếu',
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
    moviesCount: 'Movies',
    cinemasCount: 'Cinemas',
    quality: 'Quality',
    sectionSubMovies: 'Explore movies now showing and coming soon',
    sectionSubCinemas: 'Find the nearest cinema to you',
    viewShowtimes: 'View showtimes',
  }
}

// State
const lang = ref(localStorage.getItem('poly_lang') || 'vi')
const darkMode = ref(localStorage.getItem('poly_theme') !== 'light')
const token = ref(localStorage.getItem('token') || null)
const user = ref(null)
const avatarError = ref(false)
const showDropdown = ref(false)
const banner = ref(null)
const phimDangChieu = ref([])
const phimSapChieu = ref([])
const activeTab = ref('dangChieu')

const loading = reactive({ profile: false, banner: false, dangChieu: false, sapChieu: false })
const error = reactive({ profile: '', banner: '', dangChieu: '', sapChieu: '' })

const cinemas = [
  { id: 1, name: 'PolyCinema Quận 1', address: '135 Lê Thánh Tôn, Q.1, TP.HCM' },
  { id: 2, name: 'PolyCinema Thủ Đức', address: 'Vincom Thủ Đức, TP.HCM' },
  { id: 3, name: 'PolyCinema Quận 7', address: 'SC VivoCity, Q.7, TP.HCM' },
]

// Computed
const isLoggedIn = computed(() => !!token.value)
const bannerStyle = computed(() => {
  if (!banner.value?.image) return {}
  return { backgroundImage: `url(${banner.value.image})`, backgroundSize: 'cover', backgroundPosition: 'center' }
})

// Helpers
const t = (key) => translations[lang.value][key] || key

const getUserInitials = () => {
  const name = user.value?.fullName || user.value?.username || '?'
  return name.split(' ').map(w => w[0]).join('').toUpperCase().slice(0, 2)
}

const toggleLanguage = () => {
  lang.value = lang.value === 'vi' ? 'en' : 'vi'
  localStorage.setItem('poly_lang', lang.value)
}

const toggleTheme = () => {
  darkMode.value = !darkMode.value
  localStorage.setItem('poly_theme', darkMode.value ? 'dark' : 'light')
}

const toggleDropdown = () => { showDropdown.value = !showDropdown.value }

const decodeToken = (tokenStr) => {
  try {
    const payload = tokenStr.split('.')[1]
    const normalizedPayload = payload.replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(decodeURIComponent(escape(atob(normalizedPayload))))
  } catch {
    return null
  }
}

const getUserRole = () => {
  const payload = token.value ? decodeToken(token.value) : null
  return payload?.role?.replace('ROLE_', '').toUpperCase() || ''
}

const isAdmin = computed(() => getUserRole() === 'ADMIN')

const getAgeClass = (rating) => {
  if (!rating) return 'badge-green'
  if (rating === 'P' || rating === 'G') return 'badge-green'
  if (String(rating).includes('13')) return 'badge-yellow'
  return 'badge-red'
}

const viewMovie = (movie) => { alert(`Đang xem chi tiết: ${movie.title}`) }

const bookTicket = () => {
  if (!isLoggedIn.value) { alert('Vui lòng đăng nhập để đặt vé'); router.push('/auth'); return }
  alert('Chức năng đặt vé sẽ được triển khai sau')
}

const scrollToMovies = () => { document.getElementById('phim')?.scrollIntoView({ behavior: 'smooth' }) }

const registerMember = () => {
  if (!isLoggedIn.value) { router.push({ path: '/auth', query: { mode: 'register' } }); return }
  alert('Chức năng đăng ký thành viên')
}

const goHome = () => { window.scrollTo({ top: 0, behavior: 'smooth' }) }

const goToAdmin = () => {
  showDropdown.value = false
  router.push('/admin')
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  token.value = null
  user.value = null
  showDropdown.value = false
  router.push('/')
}

// API calls
const loadProfile = async () => {
  if (!token.value) { user.value = null; return }
  loading.profile = true
  try {
    const res = await axios.get(`${API_BASE}/user/profile`, { headers: { Authorization: `Bearer ${token.value}` } })
    user.value = res.data
  } catch (err) {
    if (err.response?.status === 401) logout()
    else user.value = null
  } finally { loading.profile = false }
}

const loadBanner = async () => {
  loading.banner = true
  try { const res = await axios.get(`${API_BASE}/phim/banner`); banner.value = res.data }
  catch { error.banner = 'Không tải được banner' }
  finally { loading.banner = false }
}

const loadDangChieu = async () => {
  loading.dangChieu = true
  try { const res = await axios.get(`${API_BASE}/phim/dang-chieu`); phimDangChieu.value = res.data }
  catch { error.dangChieu = 'Không tải được phim đang chiếu' }
  finally { loading.dangChieu = false }
}

const loadSapChieu = async () => {
  loading.sapChieu = true
  try { const res = await axios.get(`${API_BASE}/phim/sap-chieu`); phimSapChieu.value = res.data }
  catch { error.sapChieu = 'Không tải được phim sắp chiếu' }
  finally { loading.sapChieu = false }
}

onMounted(() => {
  axios.interceptors.request.use(config => {
    if (token.value) config.headers.Authorization = `Bearer ${token.value}`
    return config
  })
  if (token.value) loadProfile()
  loadBanner()
  loadDangChieu()
  loadSapChieu()

  document.addEventListener('click', (e) => {
    if (!e.target.closest('.user-menu') && !e.target.closest('.user-dropdown')) {
      showDropdown.value = false
    }
  })
})
</script>

<style scoped>
/* ==================== CSS VARIABLES ==================== */
.home {
  --primary: #FF6B00;
  --primary-dark: #E05A00;
  --primary-light: #FF8533;

  --bg: #f8fafc;
  --bg-secondary: #f1f5f9;
  --surface: #ffffff;
  --surface-hover: #f1f5f9;
  --text: #1e2937;
  --text-secondary: #64748b;
  --text-muted: #94a3b8;
  --border: #e2e8f0;
  --card-shadow: 0 4px 16px rgba(0,0,0,0.06);
  --nav-bg: rgba(255,255,255,0.96);
  --nav-shadow: 0 1px 0 var(--border);
  --nav-border: #e2e8f0;
  --skeleton-base: #e2e8f0;
  --skeleton-alt: #cbd5e1;
  --footer-bg: #111827;
  --footer-text: #e2e8f0;
  --footer-link: #9ca3af;
  --footer-border: #1f2937;

  /* Avatar */
  --avatar-size: 36px;
  --avatar-font: 14px;
  --avatar-bg: linear-gradient(135deg, var(--primary), #ff8c38);

  font-family: 'Nunito', system-ui, -apple-system, sans-serif;
  line-height: 1.6;
  background: var(--bg);
  color: var(--text);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  width: 100%;
  overflow-x: clip;
}

/* ==================== DARK MODE ==================== */
.home.dark {
  --bg: #0f172a;
  --bg-secondary: #1a2540;
  --surface: #1e2937;
  --surface-hover: #263348;
  --text: #f1f5f9;
  --text-secondary: #94a3b8;
  --text-muted: #64748b;
  --border: #334155;
  --card-shadow: 0 4px 16px rgba(0,0,0,0.3);
  --nav-bg: rgba(15,23,42,0.96);
  --nav-shadow: 0 1px 0 var(--border);
  --nav-border: #334155;
  --skeleton-base: #334155;
  --skeleton-alt: #475569;
  --footer-bg: #060d1a;
  --footer-text: #94a3b8;
  --footer-link: #6b7280;
  --footer-border: #1e2937;
}

.home *, .home *::before, .home *::after { box-sizing: border-box; }

/* ==================== NAV ==================== */
.nav {
  background: var(--nav-bg);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--nav-border);
  box-shadow: var(--nav-shadow);
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 0 clamp(16px, 4vw, 40px);
  height: 68px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
  transition: background 0.3s, border-color 0.3s;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: inherit;
  flex-shrink: 0;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: var(--primary);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.logo-icon svg { width: 22px; height: 22px; fill: white; }
.logo-text { font-size: 22px; font-weight: 900; letter-spacing: -0.5px; }
.logo-text span { color: var(--primary); }

.nav-links {
  display: flex;
  gap: 32px;
  flex: 1;
  justify-content: center;
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
  transition: color 0.2s;
  position: relative;
  padding: 4px 0;
  white-space: nowrap;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  width: 0;
  height: 2px;
  background: var(--primary);
  border-radius: 2px;
  transform: translateX(-50%);
  transition: width 0.25s ease;
}

.nav-link:hover { color: var(--primary); }
.nav-link:hover::after { width: 100%; }

.nav-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

/* ==================== FLAG BUTTON ==================== */
/*
 * Nút chuyển ngôn ngữ: hiển thị cờ của ngôn ngữ ĐỐI DIỆN
 * Click → chuyển sang ngôn ngữ đó
 * Layout: [cờ] [tên ngắn] — gọn, rõ ràng, không bựa
 */
.flag-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--surface);
  cursor: pointer;
  transition: all 0.2s;
  color: var(--text);
}

.flag-btn:hover {
  border-color: var(--primary);
  background: var(--surface-hover);
}

.flag-icon {
  width: 22px;
  height: 15px;
  border-radius: 3px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 0 0 1px rgba(0,0,0,0.1);
}

.flag-icon svg { width: 100%; height: 100%; display: block; }

.flag-label {
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

/* ==================== THEME TOGGLE ==================== */
.theme-toggle {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--surface);
  color: var(--text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.theme-toggle svg { width: 18px; height: 18px; }
.theme-toggle:hover { border-color: var(--primary); color: var(--primary); }

/* ==================== BUTTONS ==================== */
.btn {
  padding: 9px 20px;
  border-radius: 9px;
  font-weight: 700;
  font-size: 14px;
  cursor: pointer;
  border: none;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 7px;
  white-space: nowrap;
}

.btn svg { width: 16px; height: 16px; flex-shrink: 0; }

.btn-lg {
  padding: 14px 28px;
  font-size: 15px;
  border-radius: 12px;
}

.btn-lg svg { width: 18px; height: 18px; }

.btn-primary {
  background: linear-gradient(135deg, var(--primary), #ff8c38);
  color: white;
  box-shadow: 0 4px 12px rgba(255,107,0,0.3);
}

.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(255,107,0,0.4); }
.btn-primary:active { transform: translateY(0); }

.btn-ghost {
  background: transparent;
  color: var(--text);
  border: 1px solid var(--border);
}

.btn-ghost:hover { background: var(--surface-hover); border-color: var(--primary); color: var(--primary); }

/* Hero outline btn */
.btn-outline {
  background: rgba(255,255,255,0.12);
  border: 2px solid rgba(255,255,255,0.6);
  color: white;
  backdrop-filter: blur(8px);
}

.btn-outline:hover {
  background: white;
  color: var(--primary);
  border-color: white;
  transform: translateY(-2px);
}

/* ==================== USER AVATAR (thống nhất) ==================== */
/*
 * FIX CHÍNH: Avatar luôn là hình vuông bo tròn đều (border-radius: 50%)
 * Không bao giờ thành bầu dục vì width = height cố định.
 * img có object-fit: cover để ảnh không bị méo.
 * Khi không có ảnh → initials với gradient background.
 */
.user-avatar {
  width: var(--avatar-size);
  height: var(--avatar-size);
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid var(--primary);
  background: var(--avatar-bg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  display: block;
  /* FIX: Không để img có intrinsic size ảnh hưởng layout */
  flex-shrink: 0;
}

.avatar-initials {
  font-size: var(--avatar-font);
  font-weight: 800;
  color: white;
  line-height: 1;
  user-select: none;
  letter-spacing: 0.5px;
}

/* ==================== USER MENU ==================== */
.user-menu {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 5px 10px 5px 5px;
  border-radius: 50px;
  border: 1px solid var(--border);
  background: var(--surface);
  transition: all 0.2s;
  user-select: none;
  max-width: 220px;
}

.user-menu:hover { border-color: var(--primary); background: var(--surface-hover); }

.user-name {
  font-weight: 700;
  font-size: 13px;
  max-width: 100px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: var(--text);
}

.chevron {
  width: 14px;
  height: 14px;
  color: var(--text-muted);
  transition: transform 0.2s;
  flex-shrink: 0;
}

.chevron.open { transform: rotate(180deg); }

/* ==================== DROPDOWN ==================== */
.user-dropdown {
  position: absolute;
  top: 74px;
  right: clamp(12px, 4vw, 40px);
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  box-shadow: 0 16px 40px rgba(0,0,0,0.15);
  min-width: 220px;
  z-index: 200;
  overflow: hidden;
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
}

/* Avatar lớn trong dropdown — nhất quán với avatar nav */
.dropdown-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid var(--primary);
  background: var(--avatar-bg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.dropdown-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-initials-lg {
  font-size: 18px;
  font-weight: 800;
  color: white;
  user-select: none;
}

.dropdown-name { font-weight: 700; font-size: 14px; color: var(--text); }
.dropdown-email { font-size: 12px; color: var(--text-muted); margin-top: 2px; }

.dropdown-divider { height: 1px; background: var(--border); margin: 0; }

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 11px 16px;
  color: var(--text);
  text-decoration: none;
  font-size: 14px;
  font-weight: 600;
  transition: background 0.15s;
}

.dropdown-item svg { width: 16px; height: 16px; color: var(--text-secondary); flex-shrink: 0; }
.dropdown-item:hover { background: var(--surface-hover); }
.dropdown-item.logout { color: #ef4444; }
.dropdown-item.logout svg { color: #ef4444; }

/* Dropdown transition */
.dropdown-enter-active, .dropdown-leave-active { transition: opacity 0.15s, transform 0.15s; }
.dropdown-enter-from, .dropdown-leave-to { opacity: 0; transform: translateY(-8px); }

/* ==================== HERO ==================== */
.hero {
  min-height: clamp(480px, calc(100vh - 68px), 760px);
  position: relative;
  display: flex;
  align-items: center;
  color: white;
  overflow: hidden;
  isolation: isolate;
}

.hero::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(120deg, #0f172a 0%, #1e293b 50%, #7c2d12 100%);
  z-index: -1;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to right, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.2) 60%, transparent 100%);
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 clamp(20px, 5vw, 80px);
  width: 100%;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(255,107,0,0.2);
  border: 1px solid rgba(255,107,0,0.4);
  color: #ffb380;
  padding: 6px 14px;
  border-radius: 50px;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 20px;
  backdrop-filter: blur(8px);
}

.hero-text h1 {
  font-size: clamp(32px, 5vw, 60px);
  font-weight: 900;
  line-height: 1.1;
  margin-bottom: 16px;
  text-shadow: 0 2px 20px rgba(0,0,0,0.5);
}

.hero-text p {
  font-size: clamp(15px, 2vw, 19px);
  max-width: 500px;
  margin-bottom: 36px;
  opacity: 0.9;
  line-height: 1.6;
}

.hero-btns {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 48px;
}

/* Hero stats */
.hero-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.hero-stat { text-align: center; }
.stat-num { display: block; font-size: 24px; font-weight: 900; color: white; }
.stat-label { display: block; font-size: 12px; color: rgba(255,255,255,0.6); margin-top: 2px; }
.hero-stat-divider { width: 1px; height: 36px; background: rgba(255,255,255,0.2); }

/* ==================== SECTIONS ==================== */
.section {
  padding: clamp(40px, 6vw, 80px) clamp(16px, 5vw, 60px);
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 40px;
  gap: 16px;
}

.section-title {
  font-size: clamp(24px, 3vw, 32px);
  font-weight: 800;
  letter-spacing: -0.5px;
  color: var(--text);
  margin: 0 0 4px;
}

.section-title span { color: var(--primary); }

.section-sub {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
}

.see-all {
  color: var(--primary);
  text-decoration: none;
  font-weight: 700;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
  flex-shrink: 0;
}

.see-all svg { width: 16px; height: 16px; transition: transform 0.2s; }
.see-all:hover svg { transform: translateX(4px); }

/* ==================== TABS ==================== */
.tabs {
  display: flex;
  gap: 4px;
  border-bottom: 2px solid var(--border);
  margin-bottom: 32px;
}

.tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  font-weight: 700;
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  border: none;
  background: none;
  border-bottom: 3px solid transparent;
  margin-bottom: -2px;
  transition: color 0.2s, border-color 0.2s;
  white-space: nowrap;
}

.tab svg { width: 16px; height: 16px; }
.tab:hover { color: var(--text); }
.tab.active { color: var(--primary); border-bottom-color: var(--primary); }

/* ==================== MOVIE GRID ==================== */
.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
}

.movie-card {
  background: var(--surface);
  border-radius: 14px;
  overflow: hidden;
  box-shadow: var(--card-shadow);
  border: 1px solid var(--border);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s, box-shadow 0.3s, border-color 0.3s;
}

.movie-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(255,107,0,0.15);
  border-color: var(--primary);
}

.movie-thumb {
  position: relative;
  aspect-ratio: 2 / 3;
  overflow: hidden;
  background: var(--bg-secondary);
}

.movie-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
  display: block;
}

.movie-card:hover .movie-thumb img { transform: scale(1.08); }

/* Hover overlay với play button */
.movie-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.movie-card:hover .movie-overlay { opacity: 1; }

.play-btn {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: var(--primary);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.play-btn:hover { transform: scale(1.1); }
.play-btn svg { width: 22px; height: 22px; margin-left: 3px; }

.movie-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 10px;
  font-size: 11px;
  font-weight: 800;
  border-radius: 6px;
  color: white;
  text-shadow: 0 1px 2px rgba(0,0,0,0.3);
}

.badge-red    { background: rgba(239,68,68,0.92); }
.badge-yellow { background: rgba(202,138,4,0.92); }
.badge-green  { background: rgba(22,163,74,0.92); }

.movie-info { padding: 14px 16px; display: flex; flex-direction: column; gap: 6px; }

.movie-title {
  font-weight: 800;
  font-size: 14px;
  line-height: 1.3;
  color: var(--text);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.movie-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: var(--text-secondary);
}

.movie-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.movie-meta svg { width: 12px; height: 12px; }

.genre-chip {
  background: var(--bg-secondary);
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 700;
  white-space: nowrap;
  color: var(--text-secondary) !important;
}

.movie-rating {
  color: var(--primary);
  font-weight: 800;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.movie-rating svg { width: 13px; height: 13px; fill: var(--primary); }

/* ==================== SKELETON ==================== */
.skeleton { pointer-events: none; }
.skeleton-thumb {
  height: 100%;
  background: linear-gradient(90deg, var(--skeleton-base) 0%, var(--skeleton-alt) 50%, var(--skeleton-base) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-text {
  height: 14px;
  background: var(--skeleton-base);
  border-radius: 6px;
  animation: pulse 1.6s ease-in-out infinite;
}

.skeleton-text.short { width: 65%; background: var(--skeleton-alt); }
.skeleton-text.shorter { width: 40%; background: var(--skeleton-alt); }

@keyframes shimmer { to { background-position: -200% 0; } }
@keyframes pulse { 0%,100% { opacity: 1; } 50% { opacity: 0.5; } }

/* ==================== ERROR ==================== */
.error-message {
  display: flex;
  align-items: center;
  gap: 10px;
  background: color-mix(in srgb, #ef4444 8%, var(--surface));
  border: 1px solid color-mix(in srgb, #ef4444 25%, var(--border));
  color: #dc2626;
  padding: 16px 20px;
  border-radius: 12px;
  font-weight: 600;
  font-size: 14px;
}

.error-message svg { width: 18px; height: 18px; flex-shrink: 0; }
.home.dark .error-message { color: #fca5a5; }

/* ==================== PROMO ==================== */
.promo-section {
  padding: 0 clamp(16px, 5vw, 60px) clamp(40px, 6vw, 80px);
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.promo-banner {
  background: linear-gradient(135deg, #c2410c, var(--primary), #f97316);
  border-radius: 20px;
  padding: clamp(36px, 5vw, 56px) clamp(24px, 5vw, 60px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  color: white;
  position: relative;
  overflow: hidden;
}

.promo-banner::after {
  content: '';
  position: absolute;
  right: -60px;
  top: -60px;
  width: 280px;
  height: 280px;
  background: rgba(255,255,255,0.06);
  border-radius: 50%;
  pointer-events: none;
}

.promo-content { display: flex; align-items: center; gap: 20px; z-index: 1; position: relative; }
.promo-icon { font-size: 40px; flex-shrink: 0; }
.promo-text h2 { font-size: clamp(22px, 3vw, 32px); font-weight: 800; margin: 0 0 8px; }
.promo-text p { font-size: clamp(14px, 1.5vw, 17px); margin: 0; opacity: 0.9; }

.btn-white {
  background: white;
  color: var(--primary);
  padding: 13px 28px;
  border-radius: 12px;
  font-weight: 800;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.25s;
  font-size: 14px;
  white-space: nowrap;
  flex-shrink: 0;
  z-index: 1;
  position: relative;
}

.btn-white:hover {
  background: transparent;
  color: white;
  border-color: white;
  transform: translateY(-2px);
}

/* ==================== CINEMA ==================== */
.cinema-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.cinema-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 24px;
  display: flex;
  gap: 16px;
  transition: all 0.25s;
}

.cinema-card:hover {
  border-color: var(--primary);
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(255,107,0,0.1);
}

.cinema-icon {
  width: 52px;
  height: 52px;
  background: linear-gradient(135deg, var(--primary), #ff8c38);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.cinema-icon svg { width: 26px; height: 26px; fill: white; }

.cinema-info { display: flex; flex-direction: column; gap: 4px; }
.cinema-name { font-weight: 800; font-size: 16px; color: var(--text); }
.cinema-addr { color: var(--text-secondary); font-size: 13px; }
.cinema-link { color: var(--primary); font-size: 13px; font-weight: 700; text-decoration: none; margin-top: 6px; display: inline-block; }
.cinema-link:hover { text-decoration: underline; }

/* ==================== FOOTER ==================== */
footer {
  background: var(--footer-bg);
  color: var(--footer-text);
  padding: clamp(48px, 6vw, 80px) clamp(16px, 5vw, 60px) clamp(24px, 3vw, 40px);
  margin-top: 80px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: clamp(32px, 5vw, 60px);
  max-width: 1400px;
  margin: 0 auto 48px;
}

.footer-logo {
  font-size: 26px;
  font-weight: 900;
  color: var(--primary);
  margin-bottom: 12px;
}

.footer-logo span { color: white; }
.footer-desc { color: var(--footer-link); line-height: 1.7; font-size: 14px; margin-bottom: 20px; }

.footer-social { display: flex; gap: 10px; }

.social-btn {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(255,255,255,0.1);
  color: var(--footer-link);
  display: flex;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  transition: all 0.2s;
}

.social-btn svg { width: 16px; height: 16px; }
.social-btn:hover { background: var(--primary); border-color: var(--primary); color: white; }

.footer-col h4 {
  color: white;
  margin-bottom: 16px;
  font-weight: 800;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.8px;
}

.footer-col a {
  display: block;
  color: var(--footer-link);
  text-decoration: none;
  margin-bottom: 10px;
  font-size: 14px;
  transition: color 0.2s, transform 0.2s;
}

.footer-col a:hover { color: var(--primary); transform: translateX(4px); }

.footer-bottom {
  max-width: 1400px;
  margin: 0 auto;
  padding-top: 32px;
  border-top: 1px solid var(--footer-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  color: var(--footer-link);
  font-size: 13px;
  flex-wrap: wrap;
}

.footer-bottom-links { display: flex; gap: 16px; }
.footer-bottom-links a { color: var(--footer-link); text-decoration: none; }
.footer-bottom-links a:hover { color: var(--primary); }

/* ==================== RESPONSIVE ==================== */
@media (max-width: 1100px) {
  .footer-grid { grid-template-columns: 1fr 1fr; }
  .footer-brand { grid-column: 1 / -1; }
}

@media (max-width: 900px) {
  .nav-links { display: none; }
  .movie-grid { grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 16px; }
}

@media (max-width: 640px) {
  /* Nav */
  .user-name { display: none; }
  .flag-label { display: none; }
  .flag-btn { padding: 6px 8px; }

  /* Hero */
  .hero-stats { gap: 16px; }
  .stat-num { font-size: 20px; }
  .hero-btns { flex-direction: column; }
  .btn-lg { width: 100%; justify-content: center; }

  /* Movie grid */
  .movie-grid { grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); gap: 12px; }
  .movie-info { padding: 10px 12px; }
  .movie-title { font-size: 13px; }

  /* Promo */
  .promo-banner { flex-direction: column; text-align: center; }
  .promo-content { flex-direction: column; text-align: center; }
  .btn-white { width: 100%; }

  /* Cinema */
  .cinema-list { grid-template-columns: 1fr; }

  /* Footer */
  .footer-grid { grid-template-columns: 1fr; gap: 24px; }
  .footer-brand { grid-column: auto; }
  .footer-bottom { flex-direction: column; text-align: center; }
}

@media (max-width: 400px) {
  .movie-grid { grid-template-columns: repeat(2, 1fr); }
  .tabs { overflow-x: auto; }
  .tab { padding: 10px 12px; font-size: 13px; }

  /* Avatar nhỏ hơn trên màn hình rất nhỏ */
  .home { --avatar-size: 30px; --avatar-font: 12px; }
  .user-menu { padding: 4px 8px 4px 4px; }
  .chevron { display: none; }
}
</style>