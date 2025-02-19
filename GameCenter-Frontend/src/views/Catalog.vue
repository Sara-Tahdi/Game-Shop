<template>
  <div class="catalog">
    <h1 class="catalog-title">Game Catalog</h1>

    <div class="search-bar-container">
      <div class="search-bar-wrapper">
        <div class="search-input">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="Search games by title or description..."
            @keyup.enter="applyFilters"
          />
          <button @click="applyFilters" class="search-button">Search</button>
        </div>

        <div class="filter-group">
          <div class="category-filter">
            <select v-model="selectedCategory" @change="applyFilters">
              <option value="">All Categories</option>
              <option
                v-for="category in categories"
                :key="category.id"
                :value="category.id"
              >
                {{ category.category }}
              </option>
            </select>
          </div>

          <div class="price-filter">
            <input
              type="number"
              v-model.number="minPrice"
              placeholder="Min $"
              min="0"
              step="0.01"
              @keyup.enter="applyFilters"
            />
            <span class="separator">-</span>
            <input
              type="number"
              v-model.number="maxPrice"
              placeholder="Max $"
              min="0"
              step="0.01"
              @keyup.enter="applyFilters"
            />
          </div>

          <div class="rating-filter">
            <input
              type="number"
              v-model.number="minRating"
              placeholder="Min ⭐"
              min="0"
              max="5"
              step="0.5"
              @keyup.enter="applyFilters"
            />
            <span class="separator">-</span>
            <input
              type="number"
              v-model.number="maxRating"
              placeholder="Max ⭐"
              min="0"
              max="5"
              step="0.5"
              @keyup.enter="applyFilters"
            />
          </div>
        </div>
        <button @click="applyFilters" class="apply-filters-button">
          Apply Filters
        </button>
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
        <div v-else class="games-grid">
          <div
            v-for="game in filteredGames"
            :key="game.id"
            class="game-card-wrapper"
          >
            <div class="game-card-content" @click="navigateToGame(game.id)">
              <GameCard :game="game" />
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import GameCard from "@/components/GameCard.vue";
import { userState } from "@/state/userState";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  name: "Catalog",
  components: {
    GameCard,
  },
  data() {
    return {
      games: [],
      filteredGames: [],
      categories: [],
      loading: true,
      error: null,
      searchQuery: "",
      selectedCategory: "",
      minPrice: null,
      maxPrice: null,
      minRating: null,
      maxRating: null,
      priceError: "",
      ratingError: "",
      userState: userState,
      promotionalPrices: {},
    };
  },
  methods: {
    navigateToGame(gameId) {
      this.$router.push(`/game/${gameId}`);
    },
    async fetchGames() {
      try {
        this.loading = true;
        this.error = null;
        console.log("Fetching games...");
        const response = await apiClient.get("/games/available");
        console.log("Response:", response.data);
        this.games = response.data;
        this.filteredGames = this.games;

        await this.fetchPromotionalPrices();
      } catch (err) {
        console.error("Error details:", err);
        this.error = "Failed to load games catalog. Please try again.";
      } finally {
        this.loading = false;
      }
    },
    async fetchCategories() {
      try {
        const response = await apiClient.get("/gameCategory");
        console.log("Fetched categories:", response.data);
        this.categories = response.data;
      } catch (err) {
        console.error("Error fetching categories:", err);
      }
    },
    async fetchPromotionalPrices() {
      try {
        for (const game of this.games) {
          const response = await apiClient.get(`/promotions/game/${game.id}`);
          const promotions = response.data;
          const currentDate = new Date();

          const activePromotions = promotions.filter((promo) => {
            const endDate = new Date(promo.endDate);
            return endDate >= currentDate;
          });

          if (activePromotions.length > 0) {
            const lowestPrice = Math.min(
              ...activePromotions.map((p) => p.newPrice),
            );
            this.promotionalPrices[game.id] = lowestPrice;
          }
        }
      } catch (error) {
        console.error("Error fetching promotional prices:", error);
      }
    },
    validatePriceRange() {
      this.priceError = "";

      if (this.minPrice !== null && this.minPrice < 0) {
        this.minPrice = 0;
      }
      if (this.maxPrice !== null && this.maxPrice < 0) {
        this.maxPrice = 0;
      }

      if (
        this.minPrice !== null &&
        this.maxPrice !== null &&
        this.minPrice > this.maxPrice
      ) {
        this.priceError = "Minimum price cannot be greater than maximum price";
        return false;
      }
      return true;
    },
    validateRatingRange() {
      this.ratingError = "";

      if (
        this.minRating !== null &&
        (this.minRating < 0 || this.minRating > 5)
      ) {
        this.ratingError = "Rating must be between 0 and 5";
        return false;
      }
      if (
        this.maxRating !== null &&
        (this.maxRating < 0 || this.maxRating > 5)
      ) {
        this.ratingError = "Rating must be between 0 and 5";
        return false;
      }

      if (
        this.minRating !== null &&
        this.maxRating !== null &&
        this.minRating > this.maxRating
      ) {
        this.ratingError =
          "Minimum rating cannot be greater than maximum rating";
        return false;
      }
      return true;
    },
    applyFilters() {
      if (!this.validatePriceRange() || !this.validateRatingRange()) {
        return;
      }

      this.filteredGames = this.games.filter((game) => {
        const matchesSearch =
          !this.searchQuery ||
          game.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          game.description
            .toLowerCase()
            .includes(this.searchQuery.toLowerCase());

        const matchesCategory =
          !this.selectedCategory ||
          game.category.id === parseInt(this.selectedCategory);

        const effectivePrice = this.promotionalPrices[game.id] || game.price;

        const matchesPrice =
          (!this.minPrice || effectivePrice >= this.minPrice) &&
          (!this.maxPrice || effectivePrice <= this.maxPrice);

        const matchesRating =
          (!this.minRating || game.rating >= this.minRating) &&
          (!this.maxRating || game.rating <= this.maxRating);

        return (
          matchesSearch && matchesCategory && matchesPrice && matchesRating
        );
      });
    },
  },
  created() {
    this.fetchGames();
    this.fetchCategories();
  },
};
</script>

<style scoped>
.catalog {
  padding: 100px;
  background-color: white;
  min-height: 112vh;
}

.catalog-title {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
}

.search-bar-container {
  margin-bottom: 30px;
}

.search-bar-wrapper {
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.search-input {
  flex: 1;
  min-width: 200px;
  display: flex;
  gap: 10px;
}

.search-input input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1em;
}

.search-button,
.apply-filters-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-button:hover,
.apply-filters-button:hover {
  background-color: #45a049;
}

.filter-group {
  display: flex;
  gap: 15px;
  align-items: center;
}

.category-filter {
  min-width: 150px;
}

.category-filter select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9em;
  background-color: white;
}

.price-filter,
.rating-filter {
  display: flex;
  align-items: center;
  gap: 5px;
}

.price-filter input,
.rating-filter input {
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

.games-grid {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(4, 300px);
  width: 100%;
  padding: 20px;
}

.game-card-wrapper {
  position: relative;
  max-width: 200px;
  margin: 0 auto;
}

.game-card-content {
  cursor: pointer;
  height: 100%;
  transition: transform 0.2s ease;
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
