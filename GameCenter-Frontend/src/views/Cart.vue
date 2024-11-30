<template>
  <div class="cart">
    <h1 class="cart-title">Your Cart</h1>

    <div v-if="loading" class="loading">
      Loading your cart...
      <div class="spinner"></div>
    </div>

    <div v-if="error" class="error">
      {{ error }}
      <button @click="fetchCart" class="retry-button">Retry</button>
    </div>

    <div v-if="!loading && !error && cart.length > 0" class="cart-container">
      <section class="games-list">
        <div v-for="game in cart" :key="game.id" class="game-card">
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
          <!-- Remove from Cart Button -->
          <button @click="removeFromCart(game)" class="remove-btn">Remove from Cart</button>
          <!-- Add to Wishlist Button -->
          <button @click="addToWishlist(game)" class="add-to-wishlist-btn" :disabled="game.remainingQuantity === 0">Add to Wishlist</button>
        </div>
      </section>
    </div>

    <!-- Empty Cart State -->
    <div v-if="clientId && cart.length === 0" class="no-items">
      Your cart is empty.
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
  name: "Cart",
  data() {
    return {
      cart: [],
      loading: false,
      error: null,
      clientId: null
    };
  },
  methods: {
    async fetchCart() {
      console.log('Attempting to fetch cart for client:', this.clientId);
      
      if (!this.clientId) {
        this.error = 'No client ID available. Please log in.';
        this.loading = false;
        return;
      }
      
      this.loading = true;
      this.error = null;
      
      try {
        const response = await axiosClient.get(
          `/carts/client/${userState.userInfo.id}`,
        );
        console.log(response.data);
        response.data.forEach(async (item) => {
          console.log(item);
          const itemData = await axiosClient.get(`/games/id/${item.gameId}`);
          this.cart.push(itemData.data);
          console.log(this.cart);
        });
        this.loading = false;
      } catch (err) {
        this.error = "Failed to load cart. Please try again.";
        console.error(err);
      } finally {
        this.loading = false;
      }
    },

    async removeFromCart(game) {
      console.log('Removing game from cart:', game);

      if (!this.clientId) {
        this.error = 'Please log in to remove games from your cart.';
        return;
      }

      try {

        const response = await axios.delete('http://localhost:8080/carts/remove', {
          params: {
            clientId: this.clientId,
            gameId: game.id
          }
        });

        if (response.status === 204) {
          console.log('Game successfully removed from cart:', game);
          this.cart = this.cart.filter(item => item.id !== game.id); // Remove the game from local cart
        }
      } catch (err) {
        console.error('Error removing game from cart:', err);
        this.error = 'Failed to remove game from cart. Please try again.';
      }
    },

    async addToWishlist(game) {
      console.log('Adding game to wishlist:', game);

      if (!this.clientId) {
        this.error = 'Please log in to add games to your wishlist.';
        return;
      }

      try {
        const response = await axios.post('http://localhost:8080/wishlists/create', {
          clientId: this.clientId,
          gameId: game.id
        });

        if (response.status === 200) {
          console.log('Game successfully added to wishlist:', game);
          // this.wishlist.push(game); ??
        }
      } catch (err) {
        console.error('Error adding game to wishlist:', err);
        this.error = 'Failed to add game to wishlist. Please try again.';
      }
    }
  },
  created() {
    this.fetchCart();
  }
};
</script>

<style scoped>
.cart {
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.cart-title {
  color: #000000;
  font-size: 2.5em;
  text-align: center;
  margin: 20px 0;
  font-weight: bold;
}

.cart-container {
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
}

.game-card h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.description {
  color: #666;
  margin-bottom: 10px;
  font-size: 0.9em;
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
}

.stock-status.in-stock {
  background-color: #2ecc71;
}

.remove-btn,
.add-to-wishlist-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

.remove-btn {
  background-color: #e74c3c;
  color: white;
}

.remove-btn:hover {
  background-color: #c0392b;
}

.add-to-wishlist-btn {
  background-color: #f39c12;
  color: white;
}

.add-to-wishlist-btn:hover {
  background-color: #e67e22;
}

.add-to-wishlist-btn:disabled {
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
}
</style>
