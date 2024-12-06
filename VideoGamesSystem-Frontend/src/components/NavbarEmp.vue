<!-- src/components/NavbarEmp.vue -->
<template>
  <nav class="navbarEmp" v-if="store.user">
    <ul>
      <li><router-link to="/homeOwner">Home</router-link></li>
      <li><router-link to="/addGames">Add Games</router-link></li>
      <li><router-link to="/addPromotions">Add Promotions</router-link></li>
      <li @click="logout">Logout</li>
    </ul>
  </nav>
</template>

<script>
import { store } from "../store.js";

export default {
  name: "NavbarEmp",
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
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.navbarEmp {
  background-color: #333;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.navbarEmp ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center; /* Center the navigation items horizontally */
  align-items: center; /* Center the items vertically */
  height: 50px; /* Reduced height for a more compact navbar */
}

.navbarEmp li {
  padding: 8px 15px; 
}

</style>
