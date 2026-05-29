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

            <input v-model="loginForm.email" placeholder="Email" />
            <input v-model="loginForm.password" type="password" placeholder="Mật khẩu" />

            <button @click="login">Đăng nhập</button>

            <div class="social">
              <button class="google" @click="loginGoogle">Google</button>
              <button class="facebook" @click="loginFacebook">Facebook</button>
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
            <input v-model="registerForm.email" placeholder="Email" />
            <input v-model="registerForm.soDienThoai" placeholder="Số điện thoại" />
            <input v-model="registerForm.password" type="password" placeholder="Mật khẩu" />

            <button @click="register">Đăng ký</button>

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
            />
            
            <button @click="verifyOtp">Xác thực Email</button>
            <button class="secondary" @click="resendOtp">Gửi lại OTP</button>

            <div class="bottom-link">
              <span @click="backToLogin">Quay lại đăng nhập</span>
            </div>
          </div>

          <!-- ================= FORGOT PASSWORD ================= -->
          <div v-if="isForgot">

            <!-- STEP 1: Nhập email -->
            <div v-if="!otpSent">
              <input v-model="forgotForm.email" placeholder="Nhập email của bạn" />
              <button @click="sendOtp">Gửi OTP</button>
            </div>

            <!-- STEP 2: Nhập OTP và mật khẩu mới -->
            <div v-else>
              <input v-model="forgotForm.otp" placeholder="Nhập OTP" maxlength="6" />
              <input v-model="forgotForm.newPassword" type="password" placeholder="Mật khẩu mới" />
              <button @click="resetPassword">Đổi mật khẩu</button>
            </div>

            <div class="bottom-link">
              <span @click="backToLogin">Quay lại đăng nhập</span>
            </div>
          </div>

          <!-- ================= MESSAGE ================= -->
          <div v-if="error" class="msg error">
            {{ error }}
          </div>

          <div v-if="success" class="msg success">
            {{ success }}
          </div>

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

// ================= HELPERS =================
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
        error.value = text
      }
      return
    }

    localStorage.setItem('token', text)
    success.value = 'Đăng nhập thành công'

  } catch {
    error.value = 'Không kết nối được server'
  }
}

// ================= REGISTER =================
async function register() {
  clearMsg()

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
      error.value = text
      return
    }

    // Chuyển sang màn xác thực sau khi đăng ký
    success.value = text
    verifyForm.value.email = registerForm.value.email
    isRegister.value = false
    isVerify.value = true

  } catch {
    error.value = 'Không kết nối được server'
  }
}

// ================= VERIFY EMAIL =================
async function verifyOtp() {
  clearMsg()

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
      error.value = text
      return
    }

    success.value = 'Xác thực email thành công!'
    
    setTimeout(() => {
      backToLogin()
    }, 1800)

  } catch {
    error.value = 'Không kết nối được server'
  }
}

// ================= RESEND OTP =================
async function resendOtp() {
  clearMsg()

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
  }
}

// ================= FORGOT PASSWORD =================
async function sendOtp() {
  clearMsg()

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
  }
}

async function resetPassword() {
  clearMsg()

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
  }
}

// ================= SOCIAL LOGIN =================
function loginGoogle() {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}

function loginFacebook() {
  alert('Facebook login chưa được triển khai')
}
</script>

<style scoped>
/* Giữ nguyên style như cũ của bạn */
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
  border-radius: 30px;
  display: flex;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0,0,0,.35);
}

.left {
  width: 42%;
  color: #fff;
  padding: 50px;
  background: linear-gradient(180deg, #ff7300, #ff4d00);
  display: flex;
  align-items: center;
}

.left h1 { font-size: 42px; margin-bottom: 10px; }

.sub { opacity: .9; margin-bottom: 30px; }

.feature-list { display: flex; flex-direction: column; gap: 14px; }

.feature {
  background: rgba(255,255,255,.15);
  padding: 14px;
  border-radius: 12px;
}

.right {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.form-box { width: 100%; max-width: 400px; }

h2 { font-size: 34px; margin-bottom: 20px; }

input {
  width: 100%;
  padding: 14px;
  margin-bottom: 12px;
  border-radius: 12px;
  border: 1px solid #ddd;
}

input:focus {
  outline: none;
  border-color: #ff7300;
}

button {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 12px;
  background: #ff6b00;
  color: white;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 8px;
}

button:hover { transform: translateY(-2px); }

.secondary {
  background: #666;
}

.social {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.google { background: #ea4335; }
.facebook { background: #1877f2; }

.bottom-link {
  margin-top: 15px;
  text-align: center;
}

.bottom-link span {
  color: #ff6b00;
  cursor: pointer;
  font-weight: 600;
}

.info-text {
  text-align: center;
  margin-bottom: 15px;
  color: #555;
}

.msg {
  margin-top: 15px;
  padding: 12px;
  border-radius: 10px;
  text-align: center;
}

.error {
  background: #ffe1e1;
  color: #d90000;
}

.success {
  background: #dcffe5;
  color: #008f3c;
}

@media(max-width:900px){
  .auth-card { flex-direction: column; }
  .left { width: 100%; }
}
</style>