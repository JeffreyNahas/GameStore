<template>
  <div class="game-detail" v-if="game">
    <h1 class="game-title">{{ game.title }}</h1>
    <p><strong>Description:</strong> {{ game.description }}</p>
    <p><strong>Price:</strong> ${{ game.price }}</p>
    <p><strong>Status:</strong> {{ game.stockQuantity > 0 ? 'Available' : 'Out of Stock' }}</p>

<button
  @click="addToCart(game)"
  :disabled="game.stockQuantity === 0"
  class="btn btn-primary"
>
  Add to Cart
</button>

    <!-- Action Buttons - Only show for customers -->
    <template v-if="store.userType === 'customer'">
      <button
        @click="addToCart(game)"
        :disabled="game.stockQuantity === 0"
        class="btn btn-primary"
      >
        Add to Cart
      </button>
      <button @click="addToWishlist(game)" class="btn btn-secondary">
        Add to Wishlist
      </button>
    </template>

    <!-- Back to Home Button - Different routes based on user type -->
    <router-link 
      :to="store.userType === 'CUSTOMER' ? '/home' : '/homeowner'" 
      class="btn btn-back"
    >
      Back to Home
    </router-link>

    <!-- Reviews Section -->
    <div class="reviews-section">
      <h2>Reviews</h2>
      <ReviewList :gameId="game.id" :isStaff="store.userType === 'staff'" :staffId="store.user.id" />
    </div>
  </div>

  <div v-else>
    <p>Game not found. Please make sure the game exists in the list.</p>
    <!-- Back button for error state -->
    <router-link 
      :to="store.userType === 'CUSTOMER' ? '/home' : '/homeowner'" 
      class="btn btn-back"
    >
      Back to Home
    </router-link>
  </div>
</template>

<script>
import axios from "axios";
import { store } from "../store.js";
import ReviewList from "../components/ReviewList.vue";

export default {
  name: "GameDetails",
  components: { ReviewList },
  props: ["gameId"],
  data() {
    return {
      game: null,
      store,
    };
  },
  created() {
    this.fetchGame();
  },
  methods: {
    async fetchGame() {
  const gameId = this.gameId;
  try {
    const response = await axios.get(`http://localhost:8081/games/${gameId}`);
    this.game = response.data;
    
    // Fetch stock quantity
    const stockResponse = await axios.get(`http://localhost:8081/games/${gameId}/stock`);
    this.game.stockQuantity = stockResponse.data;
  } catch (error) {
    console.error("Error fetching game data:", error);
    alert("Failed to load game details. Please try again.");
      }
    },
    addToCart(game) {
      if (store.userType !== 'customer') {
        alert('Only customers can add items to cart');
        return;
      }
      alert(`${game.title} added to the cart!`);
    },
    addToWishlist(game) {
      if (store.userType !== 'customer') {
        alert('Only customers can add items to wishlist');
        return;
      }
      alert(`${game.title} added to the wishlist!`);
    },
  },
  computed: {
    isCustomer() {
      return store.userType === 'customer';
    }
  }
};
</script>

<style scoped>
.game-detail {
  padding: 20px;
  border: 1px solid #ccc;
  background-color: #f9f9f9;
  max-width: 800px;
  margin: 20px auto;
  border-radius: 8px;
}

.game-title {
  font-size: 2rem;
  color: #333;
  margin-bottom: 10px;
}

p {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 10px;
}

button {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  font-size: 16px;
}

.btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  border: none;
}

button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.btn-back {
  display: inline-block;
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  text-align: center;
  cursor: pointer;
}

.btn-back:hover {
  background-color: #218838;
}

.reviews-section {
  margin-top: 30px;
  border-top: 1px solid #ddd;
  padding-top: 20px;
}
</style>
