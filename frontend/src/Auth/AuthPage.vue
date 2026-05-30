<template>
  <div class="page">
    <div class="auth-card">

      <!-- LEFT -->
      <div class="left">
        <div>
          <h1>🎬 PolyCinema</h1>
          <p class="sub">Đặt vé phim nhanh chóng và tiện lợi</p>
          <div class="feature-list">
            <div class="feature">🎟 Đặt vé online</div>
            <div class="feature">💺 Chọn ghế trực tiếp</div>
            <div class="feature">🕒 Lịch sử đặt vé</div>
            <div class="feature">🎁 Voucher ưu đãi</div>
          </div>
        </div>
      </div>

      <!-- RIGHT -->
      <div class="right">
        <div class="form-box">

          <h2>
            {{
              isRegister ? 'Tạo tài khoản' :
              isVerify ? 'Xác thực Email' :
              isForgot ? 'Quên mật khẩu' : 'Đăng nhập'
            }}
          </h2>

          <!-- ================= LOGIN ================= -->
          <div v-if="!isRegister && !isForgot && !isVerify">
            <input v-model="loginForm.email" placeholder="Email" type="email" />
            <input v-model="loginForm.password" type="password" placeholder="Mật khẩu" />

            <button @click="login" :disabled="isLoading.login">
              {{ isLoading.login ? 'Đang đăng nhập...' : 'Đăng nhập' }}
            </button>

            <div class="social">
              <button class="google" @click="loginGoogle" :disabled="isLoading.google">
                <span class="google-icon">🔐</span> Google
              </button>
              <button class="facebook" @click="loginFacebook">
                <span class="fb-icon">📱</span> Facebook
              </button>
            </div>

            <div class="bottom-link">
              <span @click="switchToForgot">Quên mật khẩu?</span>
            </div>
            <div class="bottom-link">
              Chưa có tài khoản?
              <span @click="switchToRegister">Đăng ký</span>
            </div>
          </div>

          <!-- ================= REGISTER ================= -->
          <div v-if="isRegister">
            <input v-model="registerForm.hoTen" placeholder="Họ tên" />
            <input v-model="registerForm.email" placeholder="Email" type="email" />
            <input v-model="registerForm.soDienThoai" placeholder="Số điện thoại" />
            <input v-model="registerForm.password" type="password" placeholder="Mật khẩu (tối thiểu 6 ký tự)" />

            <button @click="register" :disabled="isLoading.register">
              {{ isLoading.register ? 'Đang xử lý...' : 'Đăng ký' }}
            </button>

            <div class="bottom-link">
              Đã có tài khoản?
              <span @click="backToLogin">Đăng nhập</span>
            </div>
          </div>

          <!-- ================= VERIFY EMAIL ================= -->
          <div v-if="isVerify">
            <p class="info-text">
              Chúng tôi đã gửi mã OTP đến email:<br>
              <strong>{{ verifyForm.email }}</strong>
            </p>
            
            <input 
              v-model="verifyForm.otp" 
              placeholder="Nhập mã OTP 6 số" 
              maxlength="6"
              type="number"
            />
            
            <button @click="verifyOtp" :disabled="isLoading.verify">Xác thực Email</button>
            <button class="secondary" @click="resendOtp" :disabled="isLoading.resend">
              {{ isLoading.resend ? 'Đang gửi...' : 'Gửi lại OTP' }}
            </button>

            <div class="bottom-link">
              <span @click="backToLogin">Quay lại đăng nhập</span>
            </div>
          </div>

          <!-- ================= FORGOT PASSWORD ================= -->
          <div v-if="isForgot">
            <!-- STEP 1 -->
            <div v-if="!otpSent">
              <input v-model="forgotForm.email" placeholder="Nhập email của bạn" type="email" />
              <button @click="sendOtp" :disabled="isLoading.forgot">Gửi OTP</button>
            </div>
            <!-- STEP 2 -->
            <div v-else>
              <input v-model="forgotForm.otp" placeholder="Nhập OTP" maxlength="6" type="number" />
              <input v-model="forgotForm.newPassword" type="password" placeholder="Mật khẩu mới (tối thiểu 6 ký tự)" />
              <button @click="resetPassword" :disabled="isLoading.reset">Đổi mật khẩu</button>
            </div>

            <div class="bottom-link">
              <span @click="backToLogin">Quay lại đăng nhập</span>
            </div>
          </div>

          <!-- ================= MESSAGE ================= -->
          <transition name="slide">
            <div v-if="error" class="msg error">
              <span class="icon">⚠️</span>
              <span class="text">{{ error }}</span>
            </div>
          </transition>
          <transition name="slide">
            <div v-if="success" class="msg success">
              <span class="icon">✓</span>
              <span class="text">{{ success }}</span>
            </div>
          </transition>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const API = 'http://localhost:8080/api/auth'

// ================= STATE =================
const isRegister = ref(false)
const isForgot = ref(false)
const isVerify = ref(false)
const otpSent = ref(false)

const error = ref('')
const success = ref('')

const isLoading = ref({
  login: false,
  register: false,
  verify: false,
  resend: false,
  forgot: false,
  reset: false,
  google: false
})

// ================= FORM =================
const loginForm = ref({
  email: '',
  password: ''
})

const registerForm = ref({
  hoTen: '',
  email: '',
  soDienThoai: '',
  password: ''
})

const forgotForm = ref({
  email: '',
  otp: '',
  newPassword: ''
})

const verifyForm = ref({
  email: '',
  otp: ''
})

// ================= VALIDATION FUNCTIONS =================
function isValidEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(email)
}

function isValidPhone(phone) {
  // Định dạng: 10 chữ số bắt đầu từ 0, hoặc +84
  const regex = /^(0\d{9}|\+84\d{9})$/
  return regex.test(phone.replace(/\s/g, ''))
}

function hasNoNumbers(text) {
  return !/\d/.test(text)
}

function clearMsg() {
  error.value = ''
  success.value = ''
}

function backToLogin() {
  isRegister.value = false
  isForgot.value = false
  isVerify.value = false
  otpSent.value = false
  clearMsg()

  // Reset form
  loginForm.value = { email: '', password: '' }
  registerForm.value = { hoTen: '', email: '', soDienThoai: '', password: '' }
  forgotForm.value = { email: '', otp: '', newPassword: '' }
  verifyForm.value = { email: '', otp: '' }
}

function switchToRegister() {
  backToLogin()
  isRegister.value = true
}

function switchToForgot() {
  backToLogin()
  isForgot.value = true
}

// ================= LOGIN =================
async function login() {
  clearMsg()

  // Validation
  if (!loginForm.value.email.trim()) {
    error.value = 'Email không được để trống'
    return
  }

  if (!loginForm.value.password.trim()) {
    error.value = 'Mật khẩu không được để trống'
    return
  }

  if (!isValidEmail(loginForm.value.email)) {
    error.value = 'Email không đúng định dạng'
    return
  }

  isLoading.value.login = true

  try {
    const res = await fetch(`${API}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginForm.value)
    })

    const text = await res.text()

    if (!res.ok) {
      if (text === "Email chưa xác thực") {
        verifyForm.value.email = loginForm.value.email
        isVerify.value = true
        error.value = "Email của bạn chưa được xác thực. Vui lòng xác thực ngay bây giờ."
      } else {
        // Hiển thị lỗi chung cho tất cả các trường hợp sai
        error.value = 'Sai email hoặc mật khẩu'
      }
      return
    }

    localStorage.setItem('token', text)
    success.value = 'Đăng nhập thành công!'

    setTimeout(() => {
      window.location.href = '/'
    }, 1200)

  } catch {
    error.value = 'Không kết nối được server'
  } finally {
    isLoading.value.login = false
  }
}

// ================= REGISTER =================
async function register() {
  clearMsg()

  // Validation
  if (!registerForm.value.hoTen.trim()) {
    error.value = 'Họ tên không được để trống'
    return
  }

  if (!hasNoNumbers(registerForm.value.hoTen)) {
    error.value = 'Họ tên không được chứa số'
    return
  }

  if (!registerForm.value.email.trim()) {
    error.value = 'Email không được để trống'
    return
  }

  if (!isValidEmail(registerForm.value.email)) {
    error.value = 'Email không đúng định dạng'
    return
  }

  if (!registerForm.value.soDienThoai.trim()) {
    error.value = 'Số điện thoại không được để trống'
    return
  }

  if (!isValidPhone(registerForm.value.soDienThoai)) {
    error.value = 'Số điện thoại không đúng định dạng Việt Nam (0xxxxxxxxx)'
    return
  }

  if (!registerForm.value.password.trim()) {
    error.value = 'Mật khẩu không được để trống'
    return
  }

  if (registerForm.value.password.length < 6) {
    error.value = 'Mật khẩu tối thiểu 6 ký tự'
    return
  }

  isLoading.value.register = true

  try {
    const res = await fetch(`${API}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(registerForm.value)
    })

    const text = await res.text()

    if (!res.ok) {
      error.value = text || 'Đăng ký thất bại'
      return
    }

    success.value = text
    verifyForm.value.email = registerForm.value.email
    isRegister.value = false
    isVerify.value = true

  } catch {
    error.value = 'Không kết nối được server'
  } finally {
    isLoading.value.register = false
  }
}

// ================= VERIFY EMAIL =================
async function verifyOtp() {
  clearMsg()

  if (!verifyForm.value.otp.trim()) {
    error.value = 'OTP không được để trống'
    return
  }

  if (verifyForm.value.otp.length !== 6) {
    error.value = 'OTP phải có 6 chữ số'
    return
  }

  isLoading.value.verify = true

  try {
    const res = await fetch(`${API}/verify`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: verifyForm.value.email,
        otp: verifyForm.value.otp
      })
    })

    const text = await res.text()

    if (!res.ok) {
      error.value = text || 'Xác thực thất bại'
      return
    }

    success.value = 'Xác thực email thành công!'
    
    setTimeout(() => {
      backToLogin()
    }, 1800)

  } catch {
    error.value = 'Không kết nối được server'
  } finally {
    isLoading.value.verify = false
  }
}

// ================= RESEND OTP =================
async function resendOtp() {
  clearMsg()
  isLoading.value.resend = true

  try {
    const res = await fetch(`${API}/resend-verify`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: verifyForm.value.email
      })
    })

    const text = await res.text()

    if (!res.ok) {
      error.value = text
    } else {
      success.value = text
    }

  } catch {
    error.value = 'Không kết nối được server'
  } finally {
    isLoading.value.resend = false
  }
}

// ================= FORGOT PASSWORD =================
async function sendOtp() {
  clearMsg()

  if (!forgotForm.value.email.trim()) {
    error.value = 'Email không được để trống'
    return
  }

  if (!isValidEmail(forgotForm.value.email)) {
    error.value = 'Email không đúng định dạng'
    return
  }

  isLoading.value.forgot = true

  try {
    const res = await fetch(`${API}/forgot`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: forgotForm.value.email
      })
    })

    const text = await res.text()

    if (!res.ok) {
      error.value = text
      return
    }

    success.value = text
    otpSent.value = true

  } catch {
    error.value = 'Không kết nối được server'
  } finally {
    isLoading.value.forgot = false
  }
}

async function resetPassword() {
  clearMsg()

  if (!forgotForm.value.otp.trim()) {
    error.value = 'OTP không được để trống'
    return
  }

  if (forgotForm.value.otp.length !== 6) {
    error.value = 'OTP phải có 6 chữ số'
    return
  }

  if (!forgotForm.value.newPassword.trim()) {
    error.value = 'Mật khẩu mới không được để trống'
    return
  }

  if (forgotForm.value.newPassword.length < 6) {
    error.value = 'Mật khẩu tối thiểu 6 ký tự'
    return
  }

  isLoading.value.reset = true

  try {
    const res = await fetch(`${API}/reset`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(forgotForm.value)
    })

    const text = await res.text()

    if (!res.ok) {
      error.value = text
      return
    }

    success.value = text
    setTimeout(() => {
      backToLogin()
    }, 1500)

  } catch {
    error.value = 'Không kết nối được server'
  } finally {
    isLoading.value.reset = false
  }
}

// ================= SOCIAL LOGIN =================
function loginGoogle() {
  isLoading.value.google = true
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}

function loginFacebook() {
  error.value = 'Facebook login chưa được triển khai'
}
</script>

<style scoped>
* { box-sizing: border-box; }

.page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #0f172a, #1e293b);
  padding: 20px;
}

.auth-card {
  width: 1000px;
  min-height: 650px;
  background: #fff;
  border-radius: 25px;
  display: flex;
  overflow: hidden;
  box-shadow: 0 25px 50px rgba(0,0,0,.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.auth-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 30px 60px rgba(0,0,0,.4);
}

.left {
  width: 42%;
  color: #fff;
  padding: 50px;
  background: linear-gradient(180deg, #ff7300, #ff4d00);
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.left::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 500px;
  height: 500px;
  background: rgba(255,255,255,.1);
  border-radius: 50%;
}

.left > div {
  position: relative;
  z-index: 1;
}

.left h1 { 
  font-size: 42px; 
  margin-bottom: 10px;
  text-shadow: 0 2px 4px rgba(0,0,0,.1);
}

.sub { 
  opacity: .95; 
  margin-bottom: 30px;
  font-size: 16px;
}

.feature-list { 
  display: flex; 
  flex-direction: column; 
  gap: 14px; 
}

.feature {
  background: rgba(255,255,255,.15);
  padding: 14px;
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255,255,255,.2);
  transition: all 0.3s ease;
  font-size: 15px;
}

.feature:hover {
  background: rgba(255,255,255,.25);
  transform: translateX(5px);
}

.right {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  background: #f9fafb;
}

.form-box { 
  width: 100%; 
  max-width: 400px; 
}

h2 { 
  font-size: 32px; 
  margin-bottom: 25px;
  color: #1f2937;
  font-weight: 700;
}

input {
  width: 100%;
  padding: 12px 14px;
  margin-bottom: 12px;
  border-radius: 10px;
  border: 2px solid #e5e7eb;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
}

input:focus {
  outline: none;
  border-color: #ff7300;
  box-shadow: 0 0 0 3px rgba(255, 115, 0, 0.1);
  background: #fffbf7;
}

input::placeholder {
  color: #9ca3af;
}

button {
  width: 100%;
  padding: 12px 14px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #ff7300, #ff6b00);
  color: white;
  font-weight: 600;
  font-size: 15px;
  cursor: pointer;
  margin-bottom: 10px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(255, 115, 0, 0.3);
}

button:hover:not(:disabled) { 
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 115, 0, 0.4);
}

button:active:not(:disabled) {
  transform: translateY(0);
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.secondary {
  background: linear-gradient(135deg, #6b7280, #4b5563);
  box-shadow: 0 4px 12px rgba(107, 114, 128, 0.3);
}

.secondary:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(107, 114, 128, 0.4);
}

.social {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.google {
  background: linear-gradient(135deg, #ea4335, #d33c27);
  box-shadow: 0 4px 12px rgba(234, 67, 53, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.google:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(234, 67, 53, 0.4);
}

.google-icon {
  font-size: 16px;
}

.facebook {
  background: linear-gradient(135deg, #1877f2, #0c63e4);
  box-shadow: 0 4px 12px rgba(24, 119, 242, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.facebook:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(24, 119, 242, 0.4);
}

.fb-icon {
  font-size: 16px;
}

.bottom-link {
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
  color: #6b7280;
}

.bottom-link span {
  color: #ff7300;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s ease;
}

.bottom-link span:hover {
  color: #ff6b00;
  text-decoration: underline;
}

.info-text {
  text-align: center;
  margin-bottom: 15px;
  color: #555;
  font-size: 14px;
  line-height: 1.6;
}

/* ================= MESSAGE STYLES ================= */
.msg {
  margin-top: 15px;
  padding: 12px 14px;
  border-radius: 10px;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  font-weight: 500;
  animation: slideIn 0.3s ease-out;
}

.msg .icon {
  font-size: 18px;
  flex-shrink: 0;
}

.msg .text {
  flex: 1;
  word-break: break-word;
}

.error {
  background: linear-gradient(135deg, #fee2e2, #fecaca);
  color: #991b1b;
  border: 1px solid #fca5a5;
  box-shadow: 0 4px 12px rgba(220, 38, 38, 0.1);
}

.success {
  background: linear-gradient(135deg, #dcfce7, #bbf7d0);
  color: #166534;
  border: 1px solid #86efac;
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.1);
}

/* ================= ANIMATIONS ================= */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.slide-enter-active, .slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* ================= RESPONSIVE ================= */
@media(max-width:1024px){
  .auth-card {
    width: 95%;
    min-height: 600px;
  }
  
  h2 {
    font-size: 28px;
  }
  
  .left h1 {
    font-size: 36px;
  }
}

@media(max-width:900px){
  .auth-card { 
    flex-direction: column;
    min-height: auto;
  }
  
  .left { 
    width: 100%; 
    padding: 30px;
    min-height: 200px;
  }
  
  .right {
    padding: 30px 20px;
  }
  
  .left h1 {
    font-size: 32px;
    margin-bottom: 8px;
  }
  
  .sub {
    font-size: 14px;
  }
  
  .feature-list {
    gap: 10px;
  }
  
  .feature {
    padding: 10px 12px;
    font-size: 13px;
  }
}

@media(max-width:640px){
  .page {
    padding: 10px;
  }
  
  .auth-card {
    border-radius: 15px;
    width: 100%;
  }
  
  .left {
    padding: 20px;
    min-height: 150px;
  }
  
  .left h1 {
    font-size: 24px;
    margin-bottom: 5px;
  }
  
  .sub {
    font-size: 12px;
    margin-bottom: 15px;
  }
  
  .feature-list {
    gap: 8px;
  }
  
  .feature {
    padding: 8px 10px;
    font-size: 12px;
  }
  
  .right {
    padding: 20px 15px;
  }
  
  .form-box {
    max-width: 100%;
  }
  
  h2 {
    font-size: 22px;
    margin-bottom: 15px;
  }
  
  input {
    padding: 10px 12px;
    font-size: 13px;
  }
  
  button {
    padding: 10px 12px;
    font-size: 14px;
  }
  
  .social {
    gap: 8px;
    margin-top: 10px;
  }
  
  .msg {
    padding: 10px 12px;
    font-size: 12px;
    gap: 8px;
  }
  
  .msg .icon {
    font-size: 16px;
  }
}

@media(max-width:480px){
  .left h1 {
    font-size: 20px;
  }
  
  h2 {
    font-size: 20px;
  }
  
  button {
    padding: 9px 10px;
    font-size: 13px;
  }
}
</style>