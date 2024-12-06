<!-- src/components/AddPromotions.vue -->
<template>
  <div class="AddPromotions-container">
    <h1>Add a Promotion to Games</h1>
    <form @submit.prevent="addPromotion">
      <div class="form-group">
        <label for="discount">Discount (%):</label>
        <input
          type="number"
          id="discount"
          v-model.number="percentage"
          min="1"
          max="100"
          required
        />
      </div>

      <div class="form-group">
        <label for="startDate">Start Date:</label>
        <input type="date" id="startDate" v-model="startDate" required />
      </div>

      <div class="form-group">
        <label for="endDate">End Date:</label>
        <input type="date" id="endDate" v-model="endDate" required />
      </div>

      <div class="form-group">
        <label for="game">Select Game(s):</label>
        <select id="game" v-model="selectedGames" multiple required>
          <option value="" disabled>Select one or more games</option>
          <option v-for="game in games" :key="game.id" :value="game.id">
            {{ game.title }}
          </option>
        </select>
        <small
          >Select multiple games by holding the Ctrl (Windows) or Command (Mac)
          key.</small
        >
      </div>

      <button type="submit">Add Promotion</button>
    </form>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { store } from "../store.js";
import debounce from "lodash/debounce"; // Ensure Lodash is installed

const axiosPromotion = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "AddPromotion",
  data() {
    return {
      percentage: null,
      startDate: null,
      endDate: null,
      selectedGames: [], // Holds the selected game IDs
      games: [], // List of games to populate the dropdown
      errorMessage: "",
      isSubmitting: false, // Flag to prevent multiple submissions
    };
  },
  methods: {
    /**
     * Fetches all games to populate the dropdown.
     */
    async fetchGames() {
      try {
        const response = await axiosPromotion.get("/games");
        this.games = response.data;
      } catch (error) {
        console.error("Failed to load games:", error);
        this.errorMessage = "Failed to fetch games for promotion.";
      }
    },

    /**
     * Handles the form submission to add a promotion.
     */
    async addPromotion() {
      // Prevent multiple submissions
      if (this.isSubmitting) return;
      this.isSubmitting = true;
      this.errorMessage = "";

      // Basic Validation
      if (this.percentage <= 0 || this.percentage > 100) {
        this.errorMessage = "Discount percentage must be between 1 and 100.";
        this.isSubmitting = false;
        return;
      }

      if (new Date(this.startDate) > new Date(this.endDate)) {
        this.errorMessage = "Start date cannot be after end date.";
        this.isSubmitting = false;
        return;
      }

      if (this.selectedGames.length === 0) {
        this.errorMessage = "Please select at least one game.";
        this.isSubmitting = false;
        return;
      }

      // Create the promotion
      const newPromotion = {
        percentage: this.percentage,
        startDate: this.startDate,
        endDate: this.endDate,
      };

      try {
        const promotionResponse = await axiosPromotion.post(
          "/promotions",
          newPromotion
        );
        const promotionId = promotionResponse.data.id;

        // Associate the promotion with each selected game
        const associationPromises = this.selectedGames.map((gameId) =>
          axiosPromotion.put(`/games/${gameId}/promotion/${promotionId}`)
        );

        await Promise.all(associationPromises);

        // Update local store and localStorage if needed
        if (!store.promotions) store.promotions = [];
        store.promotions.push(promotionResponse.data);
        localStorage.setItem("promotions", JSON.stringify(store.promotions));

        alert(
          "Promotion added and associated with selected game(s) successfully!"
        );

        // Reset the form
        this.percentage = null;
        this.startDate = null;
        this.endDate = null;
        this.selectedGames = [];
      } catch (error) {
        console.error("Error adding promotion:", error);
        if (
          error.response &&
          error.response.data &&
          error.response.data.errors &&
          error.response.data.errors.length > 0
        ) {
          this.errorMessage = error.response.data.errors[0];
        } else {
          this.errorMessage = "An error occurred while adding the promotion.";
        }
      } finally {
        this.isSubmitting = false;
      }
    },
  },
  mounted() {
    this.fetchGames();
  },
};
</script>

<style scoped>
.AddPromotions-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 1.5rem;
}

form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  font-weight: bold;
  margin-bottom: 0.5rem;
  display: block;
}

input[type="number"],
input[type="date"],
select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  padding: 10px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #1976d2;
}

button:disabled {
  background-color: #90caf9;
  cursor: not-allowed;
}

.error-message {
  margin-top: 1rem;
  color: red;
  font-weight: bold;
  text-align: center;
}
</style>
