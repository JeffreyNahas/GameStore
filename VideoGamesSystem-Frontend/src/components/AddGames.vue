<!-- src/components/AddGames.vue -->
<template>
  <div class="AddGames-container">
    <h1>Add a New Game</h1>
    <form @submit.prevent="addGame">
      <div>
        <label for="title">Title:</label>
        <input type="text" v-model="title" required />
      </div>
      <div>
        <label for="description">Description:</label>
        <input type="text" v-model="description" required />
      </div>
      <div>
        <label for="price">Price:</label>
        <input type="number" v-model="price" required />
      </div>
      <div>
        <label for="category">Category:</label>
        <select v-model="category" required>
          <option value="">Select a category</option>
          <option value="Adventure">Adventure</option>
          <option value="Action">Action</option>
          <option value="Arcade">Arcade</option>
          <option value="Puzzle">Puzzle</option>
          <option value="Party">Party</option>
          <option value="Sports">Sports</option>
          <option value="Strategy">Strategy</option>
          <option value="Survival">Survival</option>
          <option value="Other">Survival</option>


        </select>
      </div>
      <div>
        <label for="consoleType">Console Type:</label>
        <select v-model="consoleType" required>
          <option value="">Select a console type</option>
          <option value="PS4">PS4</option>
          <option value="XBOX">Xbox Series X</option>
          <option value="Switch">Nintendo Switch</option>
          <option value="PC">PC</option>
          <option value="Wii">Wii U</option>
          <option value="Other">Other Console</option>
        </select>
      </div>
      <div>
        <label for="Stock Quantity">Stock Quantity:</label>
        <input type="number" v-model.number="stockQuantity" required />
      </div>
      <button type="submit">Add Game</button>
    </form>
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { store } from "../store.js";

const axiosGame = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "AddGame",
  data() {
    return {
      title: "",
      description: "",
      price: null,
      category: "",
      consoleType: "",
      errorMessage: "",
      stockQuantity: 2,
    };
  },
  methods: {
    async addGame() {
  const newGame = {
    description: this.description,
    price: this.price,
    title: this.title,
    category: this.category,
    consoleType: this.consoleType,
    stockQuantity: parseInt(this.stockQuantity),
  };

  try {
    console.log('Sending game data:', newGame);
    const response = await axiosGame.post("/games", newGame);

    // Generate specific games based on the stock quantity
    const specificGamesResponse = await axiosGame.post(`/games/${response.data.id}/specific-games?stockQuantity=${this.stockQuantity}`);

    // Store the game and specific games in the local storage and the store
    localStorage.setItem("game", JSON.stringify(response.data));
    if (!store.games) store.games = [];
    store.games.push(response.data);
    store.specificGames = specificGamesResponse.data;

    alert('Game and specific games added successfully!');

    // Redirect the user based on their role
    const userType = store.userType || localStorage.getItem("userType");
    if (userType === "OWNER" || userType === "staff") {
      this.$router.push("/homeOwner");
    } else {
      this.$router.push("/login");
    }
  } catch (e) {
    console.error('Error details:', e.response?.data);
    this.errorMessage = e.response?.data?.message || "An error occurred while adding the game.";
  }
},
  },
};
</script>

<style scoped>
.AddGames-container {
  max-width: 400px;
  margin: 0 auto;
}
.error-message {
  color: red;
}
select {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
button {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #45a049;
}
</style>
