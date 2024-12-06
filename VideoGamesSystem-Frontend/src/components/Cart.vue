<template>
  <div class="cart-page">
    <h1>My Cart</h1>
    <div v-if="store.cartSpecificGames.length === 0">Your cart is empty.</div>
    <div v-else class="games-list">
      <div
        v-for="game in store.cartSpecificGames"
        :key="game.serialNumber"
        class="game-item"
      >
        <h3>{{ game.title }}</h3>
        <p>{{ game.description }}</p>
        <p>Price: ${{ game.price }}</p>
        <button @click="removeFromCart(game)">Remove</button>
      </div>
    </div>
    <div class="checkout" v-if="store.cartSpecificGames.length > 0">
      <p>Total Price: ${{ totalPrice }}</p>
      <div class="promise-checkbox">
        <label>
          <input 
            type="checkbox" 
            v-model="promiseChecked"
          >
          I pinky promise that I will e-transfer ${{ totalPrice }} to jeffnahas4@gmail.com
        </label>
      </div>
      <button 
        @click="checkout" 
        :disabled="!promiseChecked"
        :class="{ 'button-disabled': !promiseChecked }"
      >
        Checkout
      </button>
    </div>
  </div>
</template>

<script>
import { store } from "../store.js";
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8081",
});

export default {
  name: "Cart",
  setup() {
    return { store };
  },
  data() {
    return {
      totalPrice: 0,
      promiseChecked: false
    };
  },
  created() {
    if (store.user) {
      this.calculateTotalPrice();
    } else {
      this.$router.push("/login");
    }
  },
  methods: {
    calculateTotalPrice() {
      this.totalPrice = store.cartSpecificGames.reduce(
        (sum, game) => sum + game.price,
        0
      );
    },
    removeFromCart(game) {
      const index = store.cartSpecificGames.findIndex(
        (g) => g.serialNumber === game.serialNumber
      );
      if (index !== -1) {
        store.cartSpecificGames.splice(index, 1);
        localStorage.setItem(
          "cartSpecificGames",
          JSON.stringify(store.cartSpecificGames)
        );
        this.calculateTotalPrice();
      }
    },
    async checkout() {
      try {
        if (!this.promiseChecked) {
          alert("Please confirm your e-transfer promise before checking out.");
          return;
        }

        const orderDate = new Date().toISOString().split('T')[0];
        
        // Create a new specific order
        const orderRequest = {
          orderDate: orderDate,
          cardNumber: store.user.cardNumber || 123456789,
          customerId: store.user.id
        };

        const orderResponse = await axiosClient.post('/orders', orderRequest);
        const orderNumber = orderResponse.data.orderNumber;

        // Initialize orderedGames if it doesn't exist
        if (!store.orderedGames) {
          store.orderedGames = [];
        }

        // Add each game to the order
        for (const game of store.cartSpecificGames) {
          await axiosClient.put(
            `/specificGames/${game.serialNumber}/addToOrder?orderId=${orderNumber}`
          );

          // Add the game to orderedGames with additional information
          store.orderedGames.push({
            ...game,
            orderNumber,
            orderDate,
            purchaseDate: new Date().toLocaleString()
          });
        }

        // Persist orderedGames to localStorage
        localStorage.setItem("orderedGames", JSON.stringify(store.orderedGames));

        // Clear cart
        store.cartSpecificGames = [];
        localStorage.removeItem("cartSpecificGames");
        this.totalPrice = 0;
        this.promiseChecked = false;

        alert("Checkout successful! Please don't forget to send the e-transfer!");
        this.$router.push("/my-games");
      } catch (error) {
        console.error("Checkout failed:", error);
        alert("Checkout failed. Please try again.");
      }
    },
  },
};
</script>

<style scoped>
.cart-page {
  padding: 20px;
}

.games-list {
  display: flex;
  flex-wrap: wrap;
}

.game-item {
  border: 1px solid #ccc;
  padding: 10px;
  margin: 10px;
  width: 200px;
}

.checkout {
  margin-top: 20px;
}

.promise-checkbox {
  margin: 20px 0;
  padding: 15px;
  background-color: black;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.promise-checkbox label {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.promise-checkbox input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover:not(.button-disabled) {
  background-color: #0056b3;
}

.button-disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.button-disabled:hover {
  background-color: #cccccc;
}
</style>