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

    <div v-if="!loading && !error && wishlist.length > 0" class="wishlist-container">
      <section class="games-list">
        <div v-for="game in wishlist" :key="game.id" class="game-card">
          <h3>{{ game.title }}</h3>
          <p class="description">{{ game.description }}</p>
          <div class="game-details">
            <span class="price">${{ game.price.toFixed(2) }}</span>
            <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5</span>
          </div>
          <div class="stock-status" :class="{ 'in-stock': game.remainingQuantity > 0 }">
            {{ game.remainingQuantity > 0 ? 'In Stock' : 'Out of Stock' }}
          </div>
          <!-- Remove from Wishlist Button -->
          <button @click="removeFromWishlist(game.id)" class="remove-btn">Remove from Wishlist</button>
          <!-- Add to Cart Button -->
          <button @click="addToCart(game)" class="add-to-cart-btn" :disabled="game.remainingQuantity === 0">Add to Cart</button>
        </div>
      </section>
    </div>

    <div v-if="clientId && wishlist.length === 0" class="no-items">
      Your wishlist is empty.
    </div>
  </div>
</template>

<script>

import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:3000',
  timeout: 5000
});

export default {
  name: 'Wishlist',
  data() {
    return {
      wishlist: [],
      loading: false,
      error: null,
      clientId: null
    };
  },
  methods: {
    async fetchWishlist() {
      console.log('Attempting to fetch wishlist for client:', this.clientId);
      
      if (!this.clientId) {
        this.error = 'No client ID available. Please log in.';
        this.loading = false;
        return;
      }
      
      this.loading = true;
      this.error = null;
      
      try {
        const response = await axiosClient.get(`/wishlists/client/${this.clientId}`);
        
        console.log('Wishlist fetch response:', response.data);
        
        if (!response.data || response.data.length === 0) {
          this.error = 'No wishlist items found.';
        } else {
          this.wishlist = response.data;
        }
      } catch (err) {
        console.error('Detailed Wishlist fetch error:', err);
        
        if (err.response) {
          this.error = err.response.data.message || 'Server error while fetching wishlist';
        } else if (err.request) {
          this.error = 'No response received from server. Check your network connection.';
        } else {
          this.error = 'Error setting up the request. Please try again.';
        }
      } finally {
        this.loading = false;
      }
    },

    async removeFromWishlist(game) {
      console.log('Removing game from wishlist:', game);

      if (!this.clientId) {
        this.error = 'Please log in to remove games from your wishlist.';
        return;
      }

      try {
        const response = await axios.delete('http://localhost:8080/wishlists/remove', {
          params: {
            clientId: this.clientId,
            gameId: game.id
          }
        });

        if (response.status === 204) {
          console.log('Game successfully removed from wishlist:', game);
          this.wishlist = this.wishlist.filter(item => item.id !== game.id);
        }
      } catch (err) {
        console.error('Error removing game from wishlist:', err);
        this.error = 'Failed to remove game from wishlist. Please try again.';
      }
    },

    async addToCart(game) {
      console.log('Adding game to cart:', game);

      if (!this.clientId) {
        this.error = 'Please log in to add games to your cart.';
        return;
      }

      try {
        const response = await axios.post('http://localhost:8080/carts/create', {
          clientId: this.clientId,
          gameId: game.id
        });

        if (response.status === 200) {
          console.log('Game successfully added to cart:', game);
          this.cart.push(game);
        }
      } catch (err) {
        console.error('Error adding game to cart:', err);
        this.error = 'Failed to add game to cart. Please try again.';
      }
    }

  },
  created() {
    this.fetchWishlist();
  }
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
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
.add-to-cart-btn {
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

.add-to-cart-btn {
  background-color: #4CAF50;
  color: white;
}

.add-to-cart-btn:hover {
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
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
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
