<!-- src/App.vue -->
<template>
  <div id="app">
    <!-- Hide navbar on login and signup pages -->
    <template v-if="!isAuthPage">
      <!-- Guest Navbar -->
      <NavbarGuest v-if="!store.user" />
      
      <!-- Customer Navbar -->
      <Navbar 
        v-else-if="store.user && store.userType === 'customer'"
      />
      <!-- Owner Navbar -->
            <NavbarOwn 
        v-else-if="store.user && store.userType === 'staff' && store.user.admin === true"
      />
      <!-- Employee Navbar -->
      <NavbarEmp 
        v-else-if="store.user && store.userType === 'staff'"
      />

    </template>
    
    <router-view />
  </div>
</template>

<script>
import Navbar from "./components/Navbar.vue";
import NavbarGuest from "./components/NavbarGuest.vue";
import NavbarOwn from "./components/NavbarOwn.vue"
import NavbarEmp from "./components/NavbarEmp.vue";
import { store } from "./store.js";
import { computed } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: "App",
  components: {
    Navbar,
    NavbarOwn,
    NavbarEmp,
    NavbarGuest,
  },
  setup() {
    const route = useRoute();
    
    // Computed property to check if current route is login or signup
    const isAuthPage = computed(() => {
      return ['/login', '/signup'].includes(route.path);
    });

    return { 
      store,
      isAuthPage
    };
  },
};
</script>

<style>
/* Your existing styles */
</style>