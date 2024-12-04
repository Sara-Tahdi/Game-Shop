<template>
  <div class="wishlist">
    <h1 class="wishlist-title">Your Wishlist</h1>

    <div v-if="loading" class="loading">
      Loading your wishlist...
      <div class="spinner"></div>
    </div>

    <div v-if="error" class="error">
      {{ error }}
      <button @click="fetchWishlist" class="retry-button">Retry</button>
    </div>

    <div
        v-if="!loading && !error && wishlist.length > 0"
        class="wishlist-container"
    >
      <section class="games-list">
        <div v-for="game in wishlist" :key="game.id" class="game-card">
          <h3>{{ game.title }}</h3>
          <p class="description">{{ game.description }}</p>
          <div class="game-details">
            <span class="price">${{ game.price.toFixed(2) }}</span>
            <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5</span>
          </div>
          <div
              class="stock-status"
              :class="{ 'in-stock': game.remainingQuantity > 0 }"
          >
            {{ game.remainingQuantity > 0 ? "In Stock" : "Out of Stock" }}
          </div>
          <!-- Remove from Wishlist Button -->
          <button @click="removeFromWishlist(game)" class="remove-btn">
            Remove from Wishlist
          </button>
          <!-- Add to Cart Button -->
          <button
              @click="addToCart(game)"
              class="add-to-cart-btn"
              :disabled="game.remainingQuantity === 0"
          >
            Add to Cart
          </button>
        </div>
      </section>
    </div>

    <div v-if="wishlist.length === 0" class="no-items">
      Your wishlist is empty.
    </div>
  </div>
</template>

<script>
import { userState } from "@/state/userState";
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "http://localhost:8080",
  },
});

export default {
  name: "Wishlist",
  data() {
    return {
      wishlist: [],
      loading: false,
      error: null,
      clientId: null,
    };
  },
  methods: {
    async fetchWishlist() {
      if (!userState.userInfo?.id) {
        this.error = "No client ID available. Please log in.";
        this.loading = false;
        return;
      }

      this.loading = true;
      this.error = null;

      try {
        const response = await axiosClient.get(
            `/wishlists/client/${userState.userInfo.id}`
        );
        console.log("Wishlist fetch response:", response.data);
        const wishlistPromises = response.data.map((item) =>
          axiosClient.get(`/games/id/${item.gameId}`),
        );
        const wishlistResponses = await Promise.all(wishlistPromises);
        this.wishlist = wishlistResponses.map((response) => response.data);
        if (this.wishlist.length === 0) {
          this.error = "No wishlist items found.";
        }
      } catch (err) {
        this.error = "Failed to load wishlist. Please try again.";
        console.error(err);
      } finally {
        this.loading = false;
      }
    },

    async removeFromWishlist(gameId) {
      if (!userState.userInfo?.id) {
        this.error = "Please log in to remove games from your wishlist.";
        return;
      }

      try {
        const response = await axiosClient.delete("/wishlists/remove", {
          params: {
            clientId: userState.userInfo.id,
            gameId: gameId,
          },
        });

        if (response.status === 200) {
          console.log("Game successfully removed from wishlist:", game);
          this.wishlist = this.wishlist.filter((item) => item.id !== game.id);
        }
      } catch (err) {
        console.error("Error removing game from wishlist:", err);
        this.error = "Failed to remove game from wishlist. Please try again.";
      }
    },

    async addToCart(game) {
      if (!userState.userInfo?.id) {
        this.error = "Please log in to add games to your cart.";
        return;
      }

      try {
        const response = await axiosClient.post("/carts/create", {
          clientId: userState.userInfo.id,
          gameId: game.id,
        });

        if (response.status === 200) {
          console.log("Game successfully added to cart:", game);
          this.wishlist.push(game);
        }
      } catch (err) {
        console.error("Error adding game to cart:", err);
        alert("Failed to add game to cart. Please try again.");
      }
    },
  },
  created() {
    if (userState.userInfo?.id) {
      this.clientId = userState.userInfo.id;
      this.fetchWishlist();
    }
  },
};
</script>

<style scoped>
.wishlist {
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.wishlist-title {
  color: #000000;
  font-size: 2.5em;
  text-align: center;
  margin: 20px 0;
  font-weight: bold;
}

.wishlist-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.games-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3 columns */
  gap: 20px; /* spacing between items */
}

.game-card {
  background: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.game-card h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.description {
  color: #666;
  margin-bottom: 10px;
  font-size: 0.9em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  flex: 1;
}

.game-details {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.price {
  color: #2c3e50;
  font-weight: bold;
}

.rating {
  color: #f1c40f;
}

.stock-status {
  padding: 5px 10px;
  border-radius: 4px;
  text-align: center;
  background-color: #e74c3c;
  color: white;
  margin-bottom: 10px;
}

.stock-status.in-stock {
  background-color: #2ecc71;
}

.remove-btn,
.add-to-cart-btn {
  width: 100%;
  padding: 8px 16px;
  width: 100%;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.remove-btn {
  background-color: #e74c3c;
  color: white;
}

.remove-btn:hover {
  background-color: #c0392b;
}

.add-to-cart-btn {
  background-color: #4caf50;
  color: white;
}

.add-to-cart-btn:hover:not(:disabled) {
  background-color: #45a049;
}

.add-to-cart-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #666;
  background-color: white;
  margin: 20px;
  border-radius: 8px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 20px auto;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.error {
  color: #dc3545;
  text-align: center;
  padding: 20px;
  margin: 20px;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #dc3545;
}

.retry-button {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.retry-button:hover {
  background-color: #c82333;
}

.no-items {
  text-align: center;
  padding: 40px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  margin: 20px;
}

.button-container {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
