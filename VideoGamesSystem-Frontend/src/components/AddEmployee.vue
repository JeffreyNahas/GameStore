<template>
  <div class="AddEmployee-container">
    <h1>Add Employee</h1>
    <form @submit.prevent="createStaff">
      <div>
        <label for="userName">Username:</label>
        <input type="text" id="userName" v-model="userName" required />
      </div>

      <div>
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required />
      </div>

      <button type="submit">Create Staff</button>
    </form>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>

</template>

<script>
import axios from "axios";
import { store } from "../store.js";

const axiosEmployee = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "CreateStaff",
  data() {
    return {
      userName: "",
      email: "",
      password: "",
      errorMessage: "",
    };
  },
  methods: {
    async createStaff() {
      console.log("Form data:", {
        userName: this.userName,
        email: this.email,
        password: this.password,
      });

      const newEmployee = {
        userName: this.userName,
        email: this.email,
        password: this.password,
        admin: false,
      };

      console.log("Request payload:", newEmployee); // Debug log

      try {
        // localStorage.setItem("Employee", JSON.stringify(response.data));

        const response = await axiosEmployee.post("/staff", newEmployee, {
          headers: {
            "Content-Type": "application/json",
          },
        });
        console.log("Response:", response.data);

        // Redirect to a different page (e.g., employee list) after successful submission
        this.$router.push("/HomeOwner"); // a ajouter
      } catch (e) {
        console.error("Error adding employee:", e);
        if (
          e.response &&
          e.response.data &&
          e.response.data.errors &&
          e.response.data.errors[0]
        ) {
          this.errorMessage = e.response.data.errors[0];
        } else {
          this.errorMessage = "An error occurred while adding the employee.";
        }
      }
    },
  },
};
</script>

<style scoped>
.AddEmployee-container {
  max-width: 400px;
  margin: 0 auto;
}

.error-message {
  color: red;
}
</style>
