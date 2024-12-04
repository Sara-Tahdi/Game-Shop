<template>
  <div class="game-details-page">
    <div class="back-link">
      <router-link to="/catalog" class="back-button">← Back to Catalog</router-link>
    </div>

    <div v-if="loading" class="loading">
      Loading game details...
      <div class="spinner"></div>
    </div>

    <div v-else-if="error" class="error">
      <p>Error loading game details: {{ error }}</p>
    </div>

    <div v-else-if="game" class="game-details-container">
      <div class="content-wrapper">
        <div class="game-image-placeholder">
          <img
            :src="game.imageUrl"
            alt="Problem fetching image"
            class="game-image"
          />
        </div>

        <div class="game-info">
          <h1 class="game-title">{{ game.title }}</h1>

          <div class="game-details">
            <div class="detail-row">
              <span class="label">Regular Price:</span>
              <span class="value" :class="{ 'original-price': hasActivePromotions }">
                ${{ game.price.toFixed(2) }}
              </span>
            </div>

            <!-- Promotions Section -->
            <div v-if="activePromotions.length > 0" class="promotions-section">
              <h3>Active Promotions</h3>
              <div v-for="promo in activePromotions" :key="promo.id" class="promotion-item">
                <div class="promotion-price">
                  <span class="promotional-price">${{ promo.newPrice.toFixed(2) }}</span>
                  <span class="promotion-end-date">
                    Ends on {{ formatDate(promo.endDate) }}
                  </span>
                </div>
              </div>
            </div>

            <div class="detail-row">
              <span class="label">Category:</span>
              <span class="value">{{ game.category.category }}</span>
            </div>

            <div class="detail-row">
              <span class="label">Rating:</span>
              <span class="value">⭐ {{ game.rating.toFixed(1) }}/5.0</span>
            </div>

            <div class="detail-row">
              <span class="label">Status:</span>
              <span class="value stock-status" :class="{ 'in-stock': game.remainingQuantity > 0 }">
                {{ game.remainingQuantity > 0 ? "In Stock" : "Out of Stock" }}
              </span>
            </div>

            <div class="description-section">
              <h2>Description</h2>
              <p>{{ game.description }}</p>
            </div>

            <div class="opinion-section" :class="feelingClass">
              <p>{{ formattedFeeling }}</p>
            </div>
          </div>

          <div class="action-buttons">
            <button
                @click="addToWishlist"
                class="wishlist-button"
                :disabled="isGuest || game.remainingQuantity === 0"
                :title="!userState?.userInfo ? 'Please log in to add to wishlist' : ''"
            >
              Add to Wishlist
            </button>
            <button
                @click="addToCart"
                class="cart-button"
                :disabled="isGuest || game.remainingQuantity === 0"
                :title="!userState?.userInfo ? 'Please log in to add to cart' : ''"
            >
              Add to Cart
            </button>
          </div>
        </div>
      </div>

      <!-- Reviews Section -->
      <div class="reviews-section">
        <div v-if="reviews && reviews.length > 0">
          <button class="accordion-toggle" @click="toggleReviewsVisibility">
            {{ areReviewsVisible ? "Hide Reviews" : "Show Reviews" }}
          </button>

          <div v-if="areReviewsVisible" class="reviews-container">
            <h2>Reviews</h2>
            <div v-for="review in reviews" :key="review.id" class="review-item">
              <GameReview :review="review" />
            </div>
          </div>
        </div>
        <div v-else class="no-reviews">
          <p>No reviews available for this game.</p>
        </div>
      </div>

      <!-- Write a Review Section -->
      <div class="write-review-section-container">
        <h2>Write a Review</h2>
        <form @submit.prevent="submitReview">
          <div class="write-review-section">
            <div class="write-review-section-top">
              <div>
                <label for="rating">Rating:</label>
                <select id="rating" v-model.number="newReview.rating">
                  <option value="" disabled selected>Select rating</option>
                  <option value="ONE">1</option>
                  <option value="TWO">2</option>
                  <option value="THREE">3</option>
                  <option value="FOUR">4</option>
                  <option value="FIVE">5</option>
                </select>
              </div>
            </div>

            <div class="write-review-section-bottom">
              <label for="reviewMessage">Review:</label>
              <textarea
                  class="write-review-text-area"
                  id="reviewMessage"
                  v-model="newReview.reviewMessage"
                  required
              ></textarea>
            </div>

            <button type="submit">Submit Review</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { userState } from "@/state/userState";
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  name: "GameDetails",
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      game: null,
      loading: true,
      error: null,
      isInWishlist: false,
      promotionalPrice: null,
      activePromotions: [],
      reviews: [],
      areReviewsVisible: false,
      newReview: {
        rating: 5,
        reviewMessage: ''
      }
    };
  },
  computed: {
    isGuest() {
      return !userState?.userInfo;
    },
    hasActivePromotions() {
      return this.activePromotions.length > 0;
    },
    feelingClass() {
      // Add your feeling class logic here if needed
      return '';
    },
    formattedFeeling() {
      // Add your formatted feeling logic here if needed
      return '';
    }
  },
  methods: {
    async fetchGameDetails() {
      this.loading = true;
      this.error = null;

      try {
        const gameId = this.id || this.$route.params.id;
        console.log('Fetching game details for ID:', gameId);
        const response = await apiClient.get(`/games/id/${gameId}`);
        console.log('Game details response:', response.data);

        this.game = response.data;

        if (this.game) {
          await this.fetchPromotions();
          if (userState?.userInfo) {
            await this.checkWishlistStatus();
          }
        } else {
          this.error = "Game not found";
        }
      } catch (error) {
        console.error("Error fetching game details:", error);
        this.error = "Failed to load game details: " + (error.response?.data || error.message);
      } finally {
        this.loading = false;
      }
    },

    async checkWishlistStatus() {
      if (!userState?.userInfo) return;

      try {
        const response = await apiClient.get(
            `/wishlists/client/${userState.userInfo.id}/game/${this.game.id}`
        );
        this.isInWishlist = response.data;
      } catch (error) {
        console.error("Error checking wishlist status:", error);
      }
    },

    async fetchPromotions() {
      try {
        const response = await apiClient.get(`/promotions/game/${this.game.id}`);
        const promotions = response.data;
        const currentDate = new Date();

        this.activePromotions = promotions.filter(promo => {
          const endDate = new Date(promo.endDate);
          return endDate >= currentDate;
        });
      } catch (error) {
        console.error("Error fetching promotions:", error);
      }
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString();
    },

    async addToWishlist() {
      if (!userState?.userInfo) {
        alert("Please log in before adding to your wishlist");
        return;
      }

      try {
        const response = await apiClient.post(`/wishlists/create`, {
          clientId: userState.userInfo.id,
          gameId: this.game.id,
        });
        console.log(response);
      } catch (e) {
        console.log(e);
        alert("Failed to add game to wishlist. Please try again.");
      }
    },

    async addToCart() {
      if (!userState?.userInfo) {
        alert("Please log in before adding to your wishlist");
        return;
      }
      
      try {
        const response = await apiClient.post(`/carts/create`, {
          clientId: userState.userInfo.id,
          gameId: this.game.id,
        });
        console.log(response);
      } catch (e) {
        console.log(e);
        alert("Failed to add game to cart. Please try again.");
      }
    },

    toggleReviewsVisibility() {
      this.areReviewsVisible = !this.areReviewsVisible;
    },

    async submitReview() {
      // Implement review submission logic here
      console.log("Review submitted:", this.newReview);
    }
  },
  created() {
    this.fetchGameDetails();
  },
  watch: {
    '$route.params.id': {
      immediate: true,
      handler(newId) {
        if (newId) {
          this.fetchGameDetails();
        }
      }
    }
  }
};
</script>

<style scoped>
.game-details-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: white;
  min-height: 100vh;
  color: #2c3e50;
}

.back-link {
  margin-bottom: 20px;
}

.back-button {
  display: inline-block;
  padding: 8px 16px;
  color: #2c3e50;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.back-button:hover {
  background-color: #f5f5f5;
}

.game-details-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
  padding-top: 180px;  /* Increased from 120px to 180px */
}

.content-wrapper {
  display: flex;
  gap: 30px;
  padding: 20px;
  align-items: flex-start;
  margin-top: 150px;  /* Increased from 100px to 150px */
}

.game-image-placeholder {
  width: 400px;
  height: 400px;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  border: 2px dashed #ddd;
  overflow: hidden;
}

.game-image {
  width: 100%;
  height: 100%;
  object-fit: scale-down;
  object-position: center;
 }

.game-info {
  flex: 1;
  min-width: 0;
}

.game-title {
  margin: 0 0 20px 0;
  color: #2c3e50;
  font-size: 2em;
}

.game-details {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.label {
  color: #2c3e50;
  min-width: 100px;
  font-weight: 500;
}

.value {
  color: #2c3e50;
  font-weight: 500;
}

.original-price {
  text-decoration: line-through;
  color: #999;
  font-size: 0.9em;
}

.promotions-section {
  margin: 20px 0;
  padding: 15px;
  background-color: #fff8f8;
  border-radius: 8px;
  border: 1px solid #ffecec;
}

.promotions-section h3 {
  color: #e74c3c;
  margin-bottom: 10px;
  font-size: 1.2em;
}

.promotion-item {
  margin: 10px 0;
  padding: 10px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.promotion-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.promotional-price {
  color: #e74c3c;
  font-size: 1.2em;
  font-weight: bold;
}

.promotion-end-date {
  color: #2c3e50;
  font-size: 0.9em;
}

.opinion-section {
  display: inline-block;
  border-radius: 5px;
  padding: 10px;
  color: white;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
  white-space: nowrap;
}

.stock-status {
  padding: 5px 10px;
  border-radius: 4px;
  background-color: #e74c3c;
  color: white;
  font-size: 0.9em;
}

.stock-status.in-stock {
  background-color: #2ecc71;
}

.description-section,
.opinion-section {
  margin-top: 30px;
}

.description-section h2,
.opinion-section h2 {
  color: #2c3e50;
  margin-bottom: 10px;
  font-size: 1.4em;
}

.description-section p,
.opinion-section p {
  color: #2c3e50;
  line-height: 1.6;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.wishlist-button,
.cart-button {
  flex: 1;
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
  transition: all 0.2s ease;
  font-weight: 500;
}

.wishlist-button {
  background-color: #e0e0e0;
  color: #2c3e50;
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

.reviews-section {
  margin-top: 40px;
  padding: 0 20px 20px;
  color: #2c3e50;
}

.accordion-toggle {
  width: 100%;
  padding: 12px;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  cursor: pointer;
  text-align: left;
  font-size: 1em;
  margin-bottom: 20px;
  transition: background-color 0.2s ease;
  color: #2c3e50;
}

.accordion-toggle:hover {
  background-color: #e9ecef;
}

.reviews-container {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.reviews-container h2 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.review-item {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  color: #2c3e50;
}

.review-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.no-reviews {
  text-align: center;
  padding: 40px;
  color: #2c3e50;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.write-review-section-container {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.write-review-section-container h2 {
  margin-bottom: 10px;
}

.write-review-section-container label {
  font-weight: bold;
  margin-top: 10px;
}

.write-review-section-container input,
textarea,
select {
  margin-top: 5px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.write-review-section-container button {
  margin-top: 15px;
  padding: 10px 15px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.write-review-section {
  display: flex;
  flex-direction: column;
}

.write-review-section-top {
  display: flex;
  flex-direction: row;
  gap: 10em;
}

.write-review-section-bottom {
  display: flex;
}

.write-review-text-area {
  width: 100%;
  resize: none;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #2c3e50;
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
  background-color: #f8d7da;
  border-radius: 8px;
  margin: 20px 0;
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }

  .game-image-placeholder {
    width: 100%;
    height: 300px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .game-title {
    font-size: 1.6em;
  }

  .detail-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }

  .label {
    min-width: auto;
  }
}

@media (max-width: 480px) {
  .game-details-page {
    padding: 10px;
  }

  .game-image-placeholder {
    height: 200px;
  }

  .promotion-price {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
}
</style>