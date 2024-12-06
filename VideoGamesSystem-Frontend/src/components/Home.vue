<template>
  <div class="home">
    <h1>Welcome, {{ store.user.userName }}!</h1>

    <!-- Search Bar -->
    <div class="search-bar">
      <label for="search">Search Games:</label>
      <input
        type="text"
        id="search"
        v-model="searchQuery"
        @input="handleSearchInput"
        placeholder="Search by title, description, etc..."
        aria-label="Search Games"
      />
    </div>

    <!-- Sort Controls -->
    <div class="sort-controls">
      <label for="sort">Sort by:</label>
      <select
        v-model="sortOption"
        @change="sortGames"
        id="sort"
        aria-label="Sort Games"
      >
        <option value="price">Price</option>
        <option value="title">Title</option>
        <option value="category">Category</option>
        <option value="consoleType">Console Type</option>
      </select>
    </div>

    <!-- Games List -->
    <div class="games-list">
      <div
        v-for="game in filteredAndSortedGames"
        :key="game.id"
        class="game-card"
      >
        <div class="game-info">
          <h3>{{ game.title }}</h3>
          <p>{{ game.description }}</p>
          <p>Price: ${{ game.price.toFixed(2) }}</p>
          <p>Category: {{ game.category }}</p>
          <p>Console: {{ game.consoleType }}</p>
          <p>
            Status:
            <span
              :class="{
                available: game.stockQuantity > 0,
                'out-of-stock': game.stockQuantity === 0,
              }"
            >
              {{ game.stockQuantity > 0 ? "Available" : "Out of Stock" }}
            </span>
          </p>
        </div>
        <div class="game-actions">
          <button
            @click="addToCart(game)"
            class="cart-btn"
            :disabled="game.stockQuantity === 0 || isAddingToCart"
            aria-label="Add to Cart"
          >
            {{ game.stockQuantity === 0 ? "Out of Stock" : "Add to Cart" }}
          </button>
          <button
            @click="addToWishlist(game)"
            class="wishlist-btn"
            :disabled="isAddingToWishlist"
            aria-label="Add to Wishlist"
          >
            Add to Wishlist
          </button>

          <!-- View Details Button that leads to GameDetails -->
          <button 
            @click="viewGameDetails(game.id)"
            class="view-details-btn"
          >
            View Details
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { store } from "../store.js";
import axios from "axios";
import debounce from "lodash/debounce";

const axiosGame = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "Home",
  setup() {
    return { store };
  },
  data() {
    return {
      sortOption: "price",
      searchQuery: "",
      isAddingToCart: false,
      isAddingToWishlist: false,
      debouncedSearch: null,
    };
  },
  async created() {
    if (!store.user) {
      this.$router.push("/login");
      return;
    }

    try {
      const response = await axiosGame.get('/games');
      store.games = response.data;

      // Fetch the stock quantity for each game
      for (const game of store.games) {
        const stockResponse = await axiosGame.get(`/games/${game.id}/stock`);
        game.stockQuantity = stockResponse.data;
      }
    } catch (error) {
      console.error('Error fetching games:', error);
      alert('Failed to load games');
    }

    // Initialize debounced search function
    this.debouncedSearch = debounce(() => {
      // If you want to perform any action after debounced search
      // Currently, the computed property handles filtering
    }, 300);
  },
  computed: {
    filteredAndSortedGames() {
      if (!store.games) return [];

      const query = this.searchQuery.toLowerCase().trim();
      let filteredGames = store.games;

      if (query) {
        filteredGames = store.games.filter((game) => {
          return (
            game.title.toLowerCase().includes(query) ||
            game.description.toLowerCase().includes(query) ||
            game.category.toLowerCase().includes(query) ||
            game.consoleType.toLowerCase().includes(query)
          );
        });
      }

      return filteredGames.sort((a, b) => {
        if (this.sortOption === "price") {
          return a.price - b.price;
        } else if (this.sortOption === "title") {
          return a.title.localeCompare(b.title);
        } else if (this.sortOption === "category") {
          return a.category.localeCompare(b.category);
        } else if (this.sortOption === "consoleType") {
          return a.consoleType.localeCompare(b.consoleType);
        }
        return 0;
      });
    },
  },
  methods: {
    handleSearchInput() {
      if (this.debouncedSearch) {
        this.debouncedSearch();
      }
    },
    async addToCart(game) {
      this.isAddingToCart = true;
      try {
        if (game.stockQuantity === 0) {
          alert("No available copies of this game.");
          return;
        }
        // Get available specific games for this game
        const response = await axiosGame.get(`/games/${game.id}/specific-games`);
        const availableSpecificGames = response.data;

        if (availableSpecificGames.length === 0) {
          alert("No available copies of this game.");
          return;
        }

        const specificGameToAdd = availableSpecificGames[0];

        // Check if game is already in cart
        const exists = store.cartSpecificGames.some(
          sg => sg.serialNumber === specificGameToAdd.serialNumber
        );

        if (exists) {
          alert("This game is already in your cart.");
          return;
        }

        // Add game details to cart
        const cartGame = {
          serialNumber: specificGameToAdd.serialNumber,
          title: game.title,
          description: game.description,
          price: game.price,
        };

        store.cartSpecificGames.push(cartGame);
        localStorage.setItem(
          "cartSpecificGames",
          JSON.stringify(store.cartSpecificGames)
        );

        // Update the game's stock quantity in the UI
        game.stockQuantity--;

        alert("Game added to cart!");
      } catch (error) {
        console.error("Error adding game to cart:", error);
        alert("Failed to add game to cart. Please try again.");
      } finally {
        this.isAddingToCart = false;
      }
    },

    viewGameDetails(gameId) {
      this.$router.push(`/game-details/${gameId}`);
    },

    addToWishlist(game) {
  try {
    // Check if game is already in wishlist
    const alreadyInWishlist = store.wishlistGames.some(
      (wishlistGame) => wishlistGame.id === game.id
    );

    if (alreadyInWishlist) {
      alert("This game is already in your wishlist.");
      return;
    }

    // Add game to wishlist
    const gameToAdd = {
      id: game.id,
      title: game.title,
      description: game.description,
      price: game.price,
      category: game.category,
      consoleType: game.consoleType
    };

    store.wishlistGames.push(gameToAdd);
    
    // Update localStorage
    localStorage.setItem("wishlistGames", JSON.stringify(store.wishlistGames));
    
    alert(`"${game.title}" added to wishlist!`);

  } catch (error) {
    console.error("Error adding to wishlist:", error);
    alert("Failed to add game to wishlist. Please try again.");
      }
    },
  },
};
</script>

<style scoped>
/* Global Styles */
body {
  font-family: 'Arial', sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f5f5f5; /* Light background */
  color: #333333; /* Dark text */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  flex-direction: column;
}

.home {
  padding: 20px;
  max-width: 1200px;
  width: 100%;
  box-sizing: border-box;
}

h1 {
  font-size: 2rem;
  color: #222;
  margin-bottom: 20px;
  text-align: center;
}

/* Search Bar */
.search-bar {
  margin-bottom: 15px;
  display: flex;
  justify-content: center;
}

.search-bar label {
  font-weight: bold;
  margin-right: 10px;
}

.search-bar input {
  padding: 8px;
  width: 100%;
  max-width: 300px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
}

/* Sort Controls */
.sort-controls {
  margin-bottom: 25px;
  display: flex;
  justify-content: center;
}

.sort-controls label {
  font-weight: bold;
  margin-right: 10px;
}

.sort-controls select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
  width: 150px;
}

/* Game List */
.games-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  justify-items: center;
}

.game-card {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  width: 100%;
  max-width: 350px;
}

.game-card:hover {
  transform: scale(1.03);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.game-info h3 {
  font-size: 1.5rem;
  color: #222;
  margin-bottom: 10px;
}

.game-info p {
  font-size: 1rem;
  color: #555;
}

.game-info p span {
  font-weight: bold;
}

.available {
  color: green;
}

.out-of-stock {
  color: red;
}

/* Game Actions (Buttons) */
.game-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  justify-content: center;
}

.cart-btn,
.wishlist-btn,
.view-details-btn {
  padding: 12px 20px;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
  max-width: 200px;
}

.cart-btn {
  background-color: #28a745;
  color: white;
}

.cart-btn:hover {
  background-color: #218838;
}

.cart-btn:disabled {
  background-color: #d6d6d6;
  cursor: not-allowed;
}

.wishlist-btn {
  background-color: #ff6600;
  color: white;
}

.wishlist-btn:hover {
  background-color: #e65c00;
}

.view-details-btn {
  background-color: #007bff;
  color: white;
}

.view-details-btn:hover {
  background-color: #0056b3;
}

button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

/* Media Queries for Responsiveness */
@media (max-width: 768px) {
  h1 {
    font-size: 1.5rem;
  }

  .search-bar input,
  .sort-controls select {
    width: 100%;
    max-width: 100%;
  }

  .games-list {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }

  .game-card {
    max-width: 100%;
  }

  .cart-btn,
  .wishlist-btn,
  .view-details-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  h1 {
    font-size: 1.25rem;
  }

  .game-card {
    padding: 15px;
    width: 90%;
  }

  .game-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .cart-btn,
  .wishlist-btn,
  .view-details-btn {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>