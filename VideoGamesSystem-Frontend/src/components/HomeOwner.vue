<template>
  <div class="home">
    <h1>Game Inventory</h1>
    <div class="sort-controls">
      <label for="sort">Sort by:</label>
      <select v-model="sortOption">
        <option value="price">Price</option>
        <option value="title">Title</option>
      </select>
    </div>
    <div class="games-list">
      <div v-for="game in sortedGames" :key="game.id" class="game-card">
        <div class="game-info">
          <h3>{{ game.title }}</h3>
          <p>{{ game.description }}</p>
          <p>Price: ${{ game.price }}</p>
          <p>Category: {{ game.category }}</p>
          <p>Console: {{ game.consoleType }}</p>
        </div>
        <div class="game-actions">
          <router-link :to="{ name: 'GameDetails', params: { gameId: game.id } }" class="details-btn">
            View Details
          </router-link>
          <button 
            @click="showDeleteConfirmation(game)" 
            class="delete-btn"
            :disabled="isDeleting"
          >
            {{ isDeleting ? "Deleting..." : "Delete Game" }}
          </button>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h2>Confirm Delete</h2>
        <p>Are you sure you want to delete "{{ gameToDelete?.title }}"?</p>
        <p class="warning-text">This action cannot be undone!</p>
        <div class="modal-buttons">
          <button
            @click="confirmDelete"
            class="confirm-btn"
            :disabled="isDeleting"
          >
            Delete
          </button>
          <button
            @click="cancelDelete"
            class="cancel-btn"
            :disabled="isDeleting"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { store } from "../store.js";
import axios from "axios";

const axiosGame = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "HomeOwner",
  setup() {
    return { store };
  },
  data() {
    return {
      sortOption: "price",
      isDeleting: false,
      showModal: false,
      gameToDelete: null,
    };
  },
  async created() {
    try {
      const response = await axiosGame.get("/games");
      store.games = response.data;
    } catch (error) {
      console.error("Error fetching games:", error);
      alert("Failed to load games");
    }
  },
  computed: {
    sortedGames() {
      if (!store.games) return [];
      return [...store.games].sort((a, b) => {
        if (this.sortOption === "price") {
          return a.price - b.price;
        } else {
          return a.title.localeCompare(b.title);
        }
      });
    },
  },
  methods: {
    showDeleteConfirmation(game) {
      this.gameToDelete = game;
      this.showModal = true;
    },
    async confirmDelete() {
      if (!this.gameToDelete) return;

      this.isDeleting = true;
      try {
        await axiosGame.delete(`/games/${this.gameToDelete.id}`);
        store.games = store.games.filter(
          (game) => game.id !== this.gameToDelete.id
        );
        alert("Game deleted successfully");
      } catch (error) {
        console.error("Error deleting game:", error);
        alert("Failed to delete game. Please try again.");
      } finally {
        this.isDeleting = false;
        this.showModal = false;
        this.gameToDelete = null;
      }
    },
    cancelDelete() {
      this.showModal = false;
      this.gameToDelete = null;
    },
  },
};
</script>

<style scoped>
.home {
  padding: 20px;
}

.sort-controls {
  margin-bottom: 20px;
}

.games-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.game-card {
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  background-color: grey;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.game-info h3 {
  margin-top: 0;
  color: #333;
}

.game-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.details-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  text-decoration: none;
  cursor: pointer;
}

.details-btn:hover {
  background-color: #0056b3;
}

.delete-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #c82333;
}

.delete-btn:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}

/* Modal styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: grey;
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  width: 90%;
  text-align: center;
}

.warning-text {
  color: #dc3545;
  font-weight: bold;
  margin: 15px 0;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.confirm-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.confirm-btn:hover {
  background-color: #c82333;
}

.cancel-btn {
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #5a6268;
}

.confirm-btn:disabled,
.cancel-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
</style>
