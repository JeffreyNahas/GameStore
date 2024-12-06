<!-- src/components/Wishlist.vue -->
<template>
  <div class="wishlist-page">
    <h1>My Wishlist</h1>
    <div v-if="store.wishlistGames.length === 0">Your wishlist is empty.</div>
    <div v-else class="games-list">
      <div v-for="game in store.wishlistGames" :key="game.id" class="game-item">
        <h2>{{ game.title }}</h2>
        <p>{{ game.description }}</p>
        <p>Price: ${{ game.price }}</p>
        <button @click="removeFromWishlist(game)">Remove</button>
        <button @click="addToCart(game)">Add to Cart</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { store } from "../store.js";

const axiosClient = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "Wishlist",
  setup() {
    return { store };
  },
  created() {
    if (store.user) {
      this.fetchWishlist();
    } else {
      this.$router.push("/login");
    }
  },
  methods: {
    async fetchWishlist() {
      try {
        if (!store.user) return;

        const response = await axiosClient.get(
          `/wishlists/customer/${store.user.id}`
        );
        const wishlistId = response.data.id;

        // Fetch games in the wishlist
        const gamesResponse = await axiosClient.get(
          `/wishlists/${wishlistId}/games`
        );
        store.wishlistGames = gamesResponse.data || [];
        localStorage.setItem(
          "wishlistGames",
          JSON.stringify(store.wishlistGames)
        );
      } catch (error) {
        console.error("Error fetching wishlist:", error);
        // Fall back to local storage
        store.wishlistGames =
          JSON.parse(localStorage.getItem("wishlistGames")) || [];
      }
    },

    async removeFromWishlist(game) {
        const index = store.wishlistGames.findIndex((g) => g.id === game.id);
        if (index !== -1) {
          store.wishlistGames.splice(index, 1);
          localStorage.setItem(
            "wishlistGames",
            JSON.stringify(store.wishlistGames)
          );

          // Update wishlist on backend
          await axiosClient.delete(
            `/wishlists/${store.user.id}/games/${game.id}`
          );
        }
    },

    async addToCart(game) {
    this.isAddingToCart = true;
      // Get available specific games for this game
      const response = await axiosClient.get(`/games/${game.id}/specific-games`);
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

      // Add to cart first
      store.cartSpecificGames.push(cartGame);
      localStorage.setItem(
        "cartSpecificGames",
        JSON.stringify(store.cartSpecificGames)
      );

      // Then remove from wishlist
      await this.removeFromWishlist(game);

      alert("Game added to cart and removed from wishlist!");
      this.isAddingToCart = false;
},
  },
};
</script>

<style scoped>
/* Global Styles */
.wishlist-page {
  padding: 20px;
  max-width: 2000px;
  width: 95%;
  margin: 0 auto;
  box-sizing: border-box;
}

h1 {
  font-size: 2rem;
  color: #222;
  margin-bottom: 30px;
  text-align: center;
}

/* Games List Grid */
.games-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 25px;
  width: 100%;
  padding: 20px;
  justify-content: start;
}

.game-item {
  background-color: white;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  width: 100%;
  min-width: 280px;
  margin: 0;
}

.game-item:hover {
  transform: scale(1.03);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

/* Game Content Styling */
.game-item h2 {
  font-size: 1.5rem;
  color: #222;
  margin-bottom: 10px;
}

.game-item p {
  font-size: 1rem;
  color: #555;
  margin: 8px 0;
}

/* Buttons */
button {
  padding: 12px 20px;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
  max-width: 200px;
  margin: 10px auto;
  display: block;
}

button[type="button"]:first-of-type {
  background-color: #ff6600;
  color: white;
}

button[type="button"]:first-of-type:hover {
  background-color: #e65c00;
}

button[type="button"]:last-of-type {
  background-color: #28a745;
  color: white;
}

button[type="button"]:last-of-type:hover {
  background-color: #218838;
}

button:disabled {
  background-color: #d6d6d6;
  cursor: not-allowed;
  opacity: 0.6;
}

/* Empty Wishlist Message */
.wishlist-page > div:first-of-type {
  text-align: center;
  font-size: 1.2rem;
  color: #666;
  margin-top: 50px;
}

/* Media Queries for Responsiveness */
@media (max-width: 1600px) {
  .games-list {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 1200px) {
  .games-list {
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  }
}

@media (max-width: 768px) {
  h1 {
    font-size: 1.5rem;
  }

  .games-list {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    padding: 10px;
  }

  .game-item {
    padding: 15px;
  }

  button {
    width: 100%;
    margin: 5px 0;
  }
}

@media (max-width: 480px) {
  h1 {
    font-size: 1.25rem;
  }

  .game-item {
    padding: 15px;
    width: 90%;
  }

  button {
    width: 100%;
    margin-bottom: 10px;
  }
}

/* Added for better use of wide screens */
@media (min-width: 2000px) {
  .games-list {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}
</style>

