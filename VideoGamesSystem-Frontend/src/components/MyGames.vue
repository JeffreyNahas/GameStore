<template>
  <div class="my-games-page">
    <h1 class="page-title">My Games</h1>
    
    <!-- Empty state -->
    <div v-if="!hasGames" class="empty-state">
      You haven't purchased any games yet.
    </div>
    
    <!-- Games list -->
    <div v-else class="games-list">
      <div v-for="game in sortedGames" :key="game.serialNumber" class="game-card">
        <div class="game-info">
          <h3 class="game-title">{{ game.title }}</h3>
          <p class="game-description">{{ game.description }}</p>
          <p class="price">Price: ${{ game.price }}</p>
        </div>
        <div class="purchase-details">
          <div class="detail-item">
            <span class="detail-label">Serial Number:</span>
            <span class="detail-value">{{ game.serialNumber }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">Order #:</span>
            <span class="detail-value">{{ game.orderNumber }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">Purchased:</span>
            <span class="detail-value">{{ formatDate(game.purchaseDate) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { store } from "../store.js";
import { computed } from 'vue';

export default {
  name: "MyGames",
  
  setup() {
    if (!store.orderedGames) {
      store.orderedGames = [];
    }

    const hasGames = computed(() => {
      return store.orderedGames && store.orderedGames.length > 0;
    });

    const sortedGames = computed(() => {
      if (!store.orderedGames) return [];
      return [...store.orderedGames].sort((a, b) => 
        new Date(b.purchaseDate) - new Date(a.purchaseDate)
      );
    });

    return {
      store,
      hasGames,
      sortedGames
    };
  },
  
  created() {
    if (!store.user) {
      this.$router.push("/login");
      return;
    }
    
    const savedGames = localStorage.getItem("orderedGames");
    if (savedGames) {
      store.orderedGames = JSON.parse(savedGames);
    }
  },
  
  methods: {
    formatDate(dateString) {
      if (!dateString) return 'N/A';
      return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
/* Variables for consistent colors */
:root {
  --primary-color: black;
  --secondary-color: #3498db;
  --accent-color: #e74c3c;
  --background-color: #f8f9fa;
  --text-color: rgb(67, 10, 10);
  --border-color: #dde1e3;
  --shadow-color: rgba(0, 0, 0, 0.1);
}

.my-games-page {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
  background-color: var(--background-color);
  min-height: 100vh;
}

.page-title {
  text-align: center;
  color: var(--primary-color);
  margin-bottom: 2rem;
  font-size: 2.5rem;
  font-weight: 600;
}

.games-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

.game-card {
  background-color: darkslategray;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px var(--shadow-color);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.game-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 12px var(--shadow-color);
}

.game-title {
  color: var(--primary-color);
  font-size: 1.5rem;
  margin-bottom: 1rem;
  font-weight: 600;
}

.game-description {
  color: var(--text-color);
  line-height: 1.6;
  margin-bottom: 1rem;
}

.price {
  font-size: 1.25rem;
  color: var(--accent-color);
  font-weight: 600;
  margin: 1rem 0;
}

.purchase-details {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 2px solid var(--border-color);
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
}

.detail-label {
  color: var(--text-color);
  font-weight: 600;
}

.detail-value {
  color: var(--secondary-color);
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--text-color);
  background-color: black;
  border-radius: 12px;
  box-shadow: 0 4px 6px var(--shadow-color);
  font-size: 1.2rem;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .my-games-page {
    padding: 1rem;
  }

  .games-list {
    grid-template-columns: 1fr;
  }

  .game-card {
    padding: 1rem;
  }

  .page-title {
    font-size: 2rem;
  }
}
</style>