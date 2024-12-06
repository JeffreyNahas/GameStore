import { createRouter, createWebHistory } from "vue-router";
import Home from "../components/Home.vue";
import Wishlist from "../components/Wishlist.vue";
import Cart from "../components/Cart.vue";
import MyGames from "../components/MyGames.vue";
import Login from "../components/Login.vue";
import Signup from "../components/Signup.vue";
import HomeOwner from "@/components/HomeOwner.vue";
import AddPromotions from "@/components/AddPromotions.vue";
import AddGames from "@/components/AddGames.vue";
import AddEmployee from "@/components/AddEmployee.vue";
import DeleteEmployee from "@/components/DeleteEmployee.vue";
import HomeGuest from "@/components/HomeGuest.vue";
import GameDetails from "@/components/GameDetails.vue";

const routes = [
  { 
    path: "/", 
    name: "Home", 
    component: Home,
    meta: { requiresAuth: false }
  },
  { 
    path: "/home", 
    name: "HomePage", 
    component: Home,
    meta: { requiresAuth: true, roles: ['customer'] }
  },
  { 
    path: "/game-details/:gameId",  
    name: "GameDetails",
    component: GameDetails,
    props: true,  
  },
  { 
    path: "/homeOwner", 
    name: "HomeOwner", 
    component: HomeOwner,
    meta: { requiresAuth: true, roles: ['staff', 'owner'] }
  },
  { 
    path: "/homeGuest", 
    name: "HomeGuest", 
    component: HomeGuest,
    meta: { requiresAuth: false }
  },
  { path: "/wishlist", name: "Wishlist", component: Wishlist },
  { path: "/cart", name: "Cart", component: Cart },
  { path: "/my-games", name: "MyGames", component: MyGames },
  { path: "/login", name: "Login", component: Login },
  { path: "/signup", name: "Signup", component: Signup },
  { path: "/addPromotions", name: "AddPromotions", component: AddPromotions },
  { path: "/addGames", name: "AddGames", component: AddGames },
  { path: "/addEmployee", name: "AddEmployee", component: AddEmployee },
  { path: "/deleteEmployee", name: "DeleteEmployee", component: DeleteEmployee },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const userType = localStorage.getItem("userType");

  if (to.meta.requiresAuth && !user) {
    next("/login");
    return;
  }

  if (to.path === "/login" && user) {
    if (userType === "customer") {
      next("/home");
    } else if (userType === "staff" || userType === "owner") {
      next("/homeOwner");
    }
    return;
  }
  next();
});

export default router;
