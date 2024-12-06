<template>
  <div class="signup-container">
    <h1>Create an Account</h1>
    <form @submit.prevent="signup">
      <div class="form-group">
        <label for="userName">Username:</label>
        <input type="text" v-model="userName" required />
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" v-model="email" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" v-model="password" required />
      </div>
      <div class="form-group">
        <label for="phoneNumber">Phone Number:</label>
        <input type="number" v-model.number="phoneNumber" required />
      </div>
      <div class="form-group">
        <label for="address">Address:</label>
        <input type="text" v-model="address" required />
      </div>
      <button type="submit">Create Account</button>
    </form>
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
    <p class="login-link">
      Already have an account?
      <a @click="goToLogin">Log in here</a>
    </p>
  </div>
</template>

<script>
import axios from "axios";
import { store } from "../store.js";

const axiosClient = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "Signup",
  data() {
    return {
      userName: "",
      email: "",
      password: "",
      phoneNumber: null,
      address: "",
      errorMessage: "",
    };
  },
  methods: {
    async signup() {
      const newUser = {
        userName: this.userName,
        email: this.email,
        password: this.password,
        phoneNumber: this.phoneNumber,
        address: this.address,
      };
      try {
        const response = await axiosClient.post("/customers", newUser);
        // Optionally log the user in immediately, idk if I liked this feature we can talk more later
        localStorage.setItem("user", JSON.stringify(response.data));
        localStorage.setItem("userType", "customer");
        store.user = response.data;
        store.userType = "customer";
        this.$router.push("/home");
      } catch (e) {
        console.error(e);
        if (
          e.response &&
          e.response.data &&
          e.response.data.errors &&
          e.response.data.errors[0]
        ) {
          this.errorMessage = e.response.data.errors[0];
        } else {
          this.errorMessage = "An error occurred during account creation";
        }
      }
    },
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.signup-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background-color: #45a049;
}

.error-message {
  color: red;
  text-align: center;
  margin-top: 10px;
}

.login-link {
  text-align: center;
  margin-top: 15px;
}

.login-link a {
  color: #4CAF50;
  cursor: pointer;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
