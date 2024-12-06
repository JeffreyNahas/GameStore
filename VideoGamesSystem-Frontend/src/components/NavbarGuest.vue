<!-- src/components/NavbarGuest.vue -->
<template>
  <nav class="navbar">
    <ul>
      <li><router-link to="/homeGuest">Home</router-link></li>
      <li><router-link to="/login">Login</router-link></li>
      <li><router-link to="/signup">Sign Up</router-link></li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'NavbarGuest'
}
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
  justify-content: center;
  align-items: center;
  height: 50px;
}

.navbar li {
  padding: 8px 15px;
}

.navbar a {
  color: #f2f2f2;
  text-decoration: none;
  display: block;
  padding: 5px 10px;
}

.navbar a:hover {
  background-color: #ddd;
  color: black;
}

/* Mobile view */
@media (max-width: 768px) {
  .navbar ul {
    flex-direction: column;
    text-align: center;
  }

  .navbar li {
    padding: 6px 10px;
    width: 100%;
  }

  .navbar a {
    padding: 8px 0;
  }
}
</style>