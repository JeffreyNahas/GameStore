<!-- src/components/Navbar.vue -->
<template>
  <nav class="navbar" v-if="store.user">
    <ul>
      <li><router-link to="/home">Home</router-link></li>
      <li><router-link to="/wishlist">Wishlist</router-link></li>
      <li><router-link to="/cart">Cart</router-link></li>
      <li><router-link to="/my-games">My Games</router-link></li>
      <li @click="logout">Logout</li>
    </ul>
  </nav>
</template>

<script>
import { store } from "../store.js";

export default {
  name: "Navbar",
  setup() {
    return { store };
  },
  methods: {
    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("userType");
      localStorage.removeItem("cartSpecificGames");
      store.user = null;
      store.userType = null;
      store.cartSpecificGames = [];
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.navbar {
  background-color: #333;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.navbar ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center; /* Center the navigation items horizontally */
  align-items: center; /* Center the items vertically */
  height: 50px; /* Reduced height for a more compact navbar */
}

.navbar li {
  padding: 8px 15px; /* Reduced padding to bring items closer together */
}

.navbar a {
  color: #f2f2f2;
  text-decoration: none;
  display: block;
  padding: 5px 10px; /* Adjusted padding to keep items closer */
}

.navbar a:hover {
  background-color: #ddd;
  color: black;
}

/* Mobile view: Make navbar items even closer */
@media (max-width: 768px) {
  .navbar ul {
    flex-direction: column;
    text-align: center;
  }

  .navbar li {
    padding: 6px 10px; /* Even less padding on mobile */
    width: 100%;
  }

  .navbar a {
    padding: 8px 0; /* Tighter padding for mobile screens */
  }
}

</style>
