<template>
  <div class="catalog">
    <!-- No changes to the header and search sections -->
    <h1 class="catalog-title">Game Catalog</h1>

    <div class="search-bar-container">
      <!-- No changes to search bar content -->
      <div class="search-bar-wrapper">
        <div class="search-input">
          <input
              type="text"
              v-model="searchQuery"
              placeholder="Search games by title or description..."
              @input="filterGames"
          >
        </div>

        <div class="filter-group">
          <div class="price-filter">
            <input
                type="number"
                v-model.number="minPrice"
                placeholder="Min $"
                min="0"
                step="0.01"
                @input="validatePriceRange"
            >
            <span class="separator">-</span>
            <input
                type="number"
                v-model.number="maxPrice"
                placeholder="Max $"
                min="0"
                step="0.01"
                @input="validatePriceRange"
            >
          </div>

          <div class="rating-filter">
            <input
                type="number"
                v-model.number="minRating"
                placeholder="Min ⭐"
                min="0"
                max="5"
                step="0.5"
                @input="validateRatingRange"
            >
            <span class="separator">-</span>
            <input
                type="number"
                v-model.number="maxRating"
                placeholder="Max ⭐"
                min="0"
                max="5"
                step="0.5"
                @input="validateRatingRange"
            >
          </div>
        </div>
      </div>
      <div class="error-messages">
        <span v-if="priceError" class="error-message">{{ priceError }}</span>
        <span v-if="ratingError" class="error-message">{{ ratingError }}</span>
      </div>
    </div>

    <div v-if="loading" class="loading">
      Loading games...
      <div class="spinner"></div>
    </div>

    <div v-if="error" class="error">
      {{ error }}
      <button @click="fetchGames" class="retry-button">Retry</button>
    </div>

    <div v-if="!loading && !error" class="catalog-container">
      <section class="games-grid">
        <div v-if="filteredGames.length === 0" class="no-results">
          No games found matching your criteria.
        </div>
        <div v-else class="game-cards">
          <div v-for="game in filteredGames" :key="game.id" class="game-card">
            <h3>{{ game.title }}</h3>
            <p class="description">{{ game.description }}</p>
            <div class="game-details">
              <span class="price">${{ game.price.toFixed(2) }}</span>
              <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5</span>
            </div>
            <div class="stock-status" :class="{ 'in-stock': game.remainingQuantity > 0 }">
              {{ game.remainingQuantity > 0 ? 'In Stock' : 'Out of Stock' }}
            </div>
            <div class="action-buttons">
              <button
                  @click="addToWishlist(game)"
                  class="wishlist-button"
                  :disabled="game.remainingQuantity === 0">
                Add to Wishlist
              </button>
              <button
                  @click="addToCart(game)"
                  class="cart-button"
                  :disabled="game.remainingQuantity === 0">
                Add to Cart
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  name: 'Catalog',
  data() {
    return {
      games: [],
      filteredGames: [],
      loading: true,
      error: null,
      searchQuery: '',
      minPrice: null,
      maxPrice: null,
      minRating: null,
      maxRating: null,
      priceError: '',
      ratingError: ''
    };
  },
  methods: {
    async fetchGames() {
      try {
        this.loading = true;
        this.error = null;
        console.log('Fetching games...');
        const response = await apiClient.get('/games/available');
        console.log('Response:', response.data);
        this.games = response.data;
        this.filteredGames = this.games;
      } catch (err) {
        console.error('Error details:', err);
        this.error = 'Failed to load games catalog. Please try again.';
      } finally {
        this.loading = false;
      }
    },
    validatePriceRange() {
      this.priceError = '';

      if (this.minPrice !== null && this.minPrice < 0) {
        this.minPrice = 0;
      }

      if (this.minPrice !== null && this.maxPrice !== null && this.minPrice > this.maxPrice) {
        this.priceError = 'Minimum price cannot be greater than maximum price';
        return false;
      }

      this.filterGames();
      return true;
    },
    validateRatingRange() {
      this.ratingError = '';

      if (this.minRating !== null) {
        if (this.minRating < 0) this.minRating = 0;
        if (this.minRating > 5) this.minRating = 5;
      }

      if (this.maxRating !== null) {
        if (this.maxRating < 0) this.maxRating = 0;
        if (this.maxRating > 5) this.maxRating = 5;
      }

      if (this.minRating !== null && this.maxRating !== null && this.minRating > this.maxRating) {
        this.ratingError = 'Minimum rating cannot be greater than maximum rating';
        return false;
      }

      this.filterGames();
      return true;
    },
    filterGames() {
      if (!this.validatePriceRange() || !this.validateRatingRange()) {
        return;
      }

      this.filteredGames = this.games.filter(game => {
        const matchesSearch = !this.searchQuery ||
            game.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            game.description.toLowerCase().includes(this.searchQuery.toLowerCase());

        const matchesPrice =
            (!this.minPrice || game.price >= this.minPrice) &&
            (!this.maxPrice || game.price <= this.maxPrice);

        const matchesRating =
            (!this.minRating || game.rating >= this.minRating) &&
            (!this.maxRating || game.rating <= this.maxRating);

        return matchesSearch && matchesPrice && matchesRating;
      });
    },
    addToWishlist(game) {
      // TODO: Implement wishlist functionality
      console.log('Adding to wishlist:', game.title);
    },
    addToCart(game) {
      // TODO: Implement cart functionality
      console.log('Adding to cart:', game.title);
    }
  },
  created() {
    this.fetchGames();
  }
};
</script>

<style scoped>
/* ... Keep all existing styles ... */

/* Add new styles for the buttons */
.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.wishlist-button,
.cart-button {
  flex: 1;
  padding: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.2s;
}

.wishlist-button {
  background-color: #e0e0e0;
  color: #333;
}

.wishlist-button:hover:not(:disabled) {
  background-color: #d0d0d0;
}

.cart-button {
  background-color: #4CAF50;
  color: white;
}

.cart-button:hover:not(:disabled) {
  background-color: #45a049;
}

.wishlist-button:disabled,
.cart-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Keep all existing styles below */
.catalog-title {
  color: #000000;
  font-size: 2.5em;
  text-align: center;
  margin: 20px 0;
  font-weight: bold;
}

.catalog {
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.search-bar-container {
  position: sticky;
  top: 0;
  background: white;
  padding: 15px 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 100;
  margin-bottom: 20px;
  border-radius: 8px;
  width: 100%;
}

.search-bar-wrapper {
  width: 100%;
  margin: 0 auto;
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.search-input {
  flex: 1;
  min-width: 200px;
}

.search-input input {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1em;
}

.filter-group {
  display: flex;
  gap: 15px;
  align-items: center;
}

.price-filter, .rating-filter {
  display: flex;
  align-items: center;
  gap: 5px;
}

.price-filter input, .rating-filter input {
  width: 80px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9em;
}

.separator {
  color: #666;
  margin: 0 2px;
}

.error-messages {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: 5px;
}

.error-message {
  color: #dc3545;
  font-size: 0.8em;
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

.games-grid {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  width: 100%;
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

.no-results {
  text-align: center;
  padding: 40px;
  color: #666;
  grid-column: 1 / -1;
  background-color: white;
  border-radius: 8px;
}

@media (max-width: 768px) {
  .search-bar-wrapper {
    flex-direction: column;
  }

  .filter-group {
    width: 100%;
    justify-content: space-between;
  }

  .search-input {
    width: 100%;
  }
}
</style>