<!-- src/components/NavbarOwn.vue -->
<template>
    <nav class="navbarOwn" v-if="store.user">
      <ul>
        <li><router-link to="/homeOwner">Home</router-link></li>
        <li><router-link to="/addGames">Add Games</router-link></li>
        <li><router-link to="/addPromotions">Add Promotions</router-link></li>
        <li><router-link to="/addEmployee">Add Employee</router-link></li>
        <li><router-link to="/deleteEmployee">Delete Employee</router-link></li>
        <li @click="logout">Logout</li>
      </ul>
    </nav>
  </template>
  
  <script>
  import { store } from "../store.js";
  
  export default {
    name: "NavbarOwn",
    setup() {
      return { store };
    },
    methods: {
      logout() {
        localStorage.removeItem("user");
        localStorage.removeItem("userType");
        localStorage.removeItem("cartSpecificGames");// maybe more items to remove , to determine
        store.user = null;
        store.userType = null;
        this.$router.push("/login");
      },
    },
  };
  </script>
  
  <style scoped>
.navbarOwn {
  background-color: #333;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.navbarOwn ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center; /* Center the navigation items horizontally */
  align-items: center; /* Center the items vertically */
  height: 50px; /* Reduced height for a more compact navbar */
}

.navbarOwn li {
  padding: 8px 15px; /* Reduced padding to bring items closer together */
}

.navbarOwn a {
  color: #f2f2f2;
  text-decoration: none;
  display: block;
  padding: 5px 10px; /* Adjusted padding to keep items closer */
}

.navbarOwn a:hover {
  background-color: #ddd;
  color: black;
}

/* Mobile view: Make navbar items even closer */
@media (max-width: 768px) {
  .navbarOwn ul {
    flex-direction: column;
    text-align: center;
  }

  .navbarOwn li {
    padding: 6px 10px; /* Even less padding on mobile */
    width: 100%;
  }

  .navbarOwn a {
    padding: 8px 0; /* Tighter padding for mobile screens */
  }
}

  </style>
  