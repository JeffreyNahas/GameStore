<template>
  <div class="home">
    <h1>Video Games System</h1>

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
    <div v-if="loading" class="loading">Loading games...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="games-list">
      <div v-for="game in sortedGames" :key="game.id" class="game-card">
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
          <router-link to="/login" class="login-btn">
            {{ game.stockQuantity === 0 ? "Out of Stock" : "Log in to Purchase" }}
          </router-link>
          <router-link to="/login" class="wishlist-btn">Log in to Save</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import debounce from "lodash/debounce";

const axiosGame = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "GuestHome",
  data() {
    return {
      sortOption: "price",
      searchQuery: "",
      games: [],
      loading: false,
      error: null,
      debouncedSearch: null,
    };
  },
  computed: {
    sortedGames() {
      const query = this.searchQuery.toLowerCase().trim();
      let filteredGames = this.games;

      if (query) {
        filteredGames = this.games.filter((game) => {
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
    async fetchGames() {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosGame.get("/games");
        this.games = response.data;

        // Fetch the stock quantity for each game
        for (const game of this.games) {
          const stockResponse = await axiosGame.get(`/games/${game.id}/stock`);
          game.stockQuantity = stockResponse.data;
        }
      } catch (err) {
        console.error("Error fetching games:", err);
        this.error = "Failed to load games. Please try again later.";
      } finally {
        this.loading = false;
      }
    },
  },
  created() {
    this.fetchGames();
    this.debouncedSearch = debounce(() => {
      // Currently, the computed property handles filtering
    }, 300);
  },
};
</script>

<style scoped>
/* Global Styles */
.home {
  padding: 20px;
  max-width: 2000px;
  width: 95%;
  margin: 0 auto;
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

/* Game Actions */
.game-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  justify-content: center;
}

.login-btn,
.wishlist-btn {
  padding: 12px 20px;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
  max-width: 200px;
  text-decoration: none;
  text-align: center;
  display: inline-block;
}

.login-btn {
  background-color: #28a745;
  color: white;
}

.login-btn:hover {
  background-color: #218838;
}

.login-btn[disabled] {
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

.loading,
.error {
  text-align: center;
  padding: 20px;
  font-size: 1.1rem;
}

.error {
  color: #dc3545;
}

/* Media Queries */
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

  .login-btn,
  .wishlist-btn {
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

  .login-btn,
  .wishlist-btn {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>