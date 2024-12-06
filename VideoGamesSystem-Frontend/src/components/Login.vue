<template>
  <div class="login-container">
    <h1>Login</h1>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="userName">Username:</label>
        <input type="text" v-model="userName" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" v-model="password" required />
      </div>
      <div class="form-group">
        <label for="userType">User Type:</label>
        <select v-model="userType" required>
          <option value="customer">Customer</option>
          <option value="staff">Staff</option>
        </select>
      </div>
      <button type="submit">Login</button>
    </form>
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
    <div class="auth-links">
      <p class="signup-link">
        Don't have an account?
        <a @click="goToSignup">Create one here</a>
      </p>
      <div class="divider">or</div>
      <p class="guest-link">
        Want to browse as guest?
        <a @click="continueAsGuest">Continue without logging in</a>
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { store } from "../store.js";

const axiosClient = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "Login",
  data() {
    return {
      userName: "",
      password: "",
      userType: "customer",
      errorMessage: "",
    };
  },
  methods: {
    async login() {
  try {
    const response = await axiosClient.post("/login", {
      userName: this.userName,
      password: this.password,
      userType: this.userType,
    });
    
    localStorage.setItem("user", JSON.stringify(response.data));
    localStorage.setItem("userType", this.userType);
    store.user = response.data;
    store.userType = this.userType;

    // Redirect based on user type
    switch(this.userType) {
      case 'customer':
        this.$router.push("/home");
        break;
      case 'staff':
        this.$router.push("/homeOwner");
        break;
      case 'owner':
        this.$router.push("/homeOwner");
        break;
      default:
        this.$router.push("/home");
      }

      } catch (e) {
        console.error("Login failed:", e.response);
        if (e.response && e.response.status === 401) {
          this.errorMessage = "Invalid username or password";
        } else {
          this.errorMessage = "An error occurred during login";
        }
      }
    },
    goToSignup() {
      this.$router.push("/signup");
    },
    continueAsGuest() {
      this.$router.push("/homeGuest");
    },
  },
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input, select {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 14px;
}

button {
  width: 100%;
  padding: 12px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
  font-size: 16px;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #45a049;
}

.error-message {
  color: #dc3545;
  text-align: center;
  margin-top: 10px;
  padding: 10px;
  background-color: #f8d7da;
  border-radius: 4px;
}

.auth-links {
  text-align: center;
  margin-top: 20px;
}

.signup-link, .guest-link {
  margin: 10px 0;
}

.signup-link a, .guest-link a {
  color: #4CAF50;
  cursor: pointer;
  text-decoration: none;
  font-weight: 500;
}

.signup-link a:hover, .guest-link a:hover {
  text-decoration: underline;
}

.divider {
  margin: 15px 0;
  color: #666;
  position: relative;
}

.divider::before,
.divider::after {
  content: "";
  position: absolute;
  top: 50%;
  width: 45%;
  height: 1px;
  background-color: #ddd;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

/* Optional: Add animation for smoother transitions */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.auth-links {
  animation: fadeIn 0.3s ease-out;
}
</style>
