<template>
  <div class="game-details-page">
    <div class="back-link">
      <router-link to="/catalog" class="back-button"
        >← Back to Catalog</router-link
      >
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
          <div class="placeholder-text">Game Image Coming Soon</div>
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
              <span class="value">⭐ {{ game.rating.toFixed(1) }}/5</span>
            </div>

            <div class="detail-row">
              <span class="label">Status:</span>
              <span
                class="value stock-status"
                :class="{ 'in-stock': game.remainingQuantity > 0 }"
              >
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

          <div v-if="isGuest || isClient" class="action-buttons">
            <div class="action-buttons">
              <button
                @click="addToWishlist"
                class="wishlist-button"
                :disabled="isGuest || game.remainingQuantity === 0"
                :title="!userState.userInfo ? 'Please log in to add to wishlist' : ''"
            >
              Add to Wishlist
            </button>
            <button
                @click="addToCart"
                class="cart-button"
                :disabled="isGuest || game.remainingQuantity === 0"
                :title="!userState.userInfo ? 'Please log in to add to cart' : ''"
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
    </div>
  </div>
</template>

<script>

import axios from 'axios';
import GameReview from '../components/GameReview.vue';
import { userState } from '@/state/userState';

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  name: "GameDetails",
  components: {
    GameReview,
  },
  data() {
    return {
      game: null,
      reviews: [],
      promotions: [],
      loading: true,
      error: null,
      areReviewsVisible: false,
      userState: userState,
      isGuest: null,
      isClient: null,
    };
  },
  computed: {
    activePromotions() {
      const currentDate = new Date();
      return this.promotions.filter(promo => {
        const endDate = new Date(promo.endDate);
        return endDate >= currentDate;
      }).sort((a, b) => a.newPrice - b.newPrice); // Sort by price, lowest first
    },
    hasActivePromotions() {
      return this.activePromotions.length > 0;
    },
    feelingClass() {
      if (!this.game.publicOpinion) return "";
      const feeling = this.game.publicOpinion;
      return {
        "very-positive": feeling === "VERYPOSITIVE",
        positive: feeling === "POSITIVE",
        neutral: feeling === "NEUTRAL",
        negative: feeling === "NEGATIVE",
        "very-negative": feeling === "VERYNEGATIVE",
      };
    },
    formattedFeeling() {
      const feelings = {
        VERYPOSITIVE: "Very Positive",
        POSITIVE: "Positive",
        NEGATIVE: "Negative",
        VERYNEGATIVE: "Very Negative",
        NEUTRAL: "Neutral",
      };
      return feelings[this.game.publicOpinion];
    },

    isGuest() {
      return userState.userInfo === null;
    },
    isClient() {
      return userState.userInfo?.userType === "Client";
    },
  },
  methods: {
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    },
    async fetchGameDetails() {
      try {
        this.loading = true;
        const response = await apiClient.get(
          `/games/id/${this.$route.params.id}`
        );
        this.game = response.data;
        await this.fetchPromotions();
        console.log('Game details:', this.game);
      } catch (err) {
        this.error = err.response?.data || "Error loading game details";
        console.error("Error fetching game details:", err);
      } finally {
        this.loading = false;
      }
    },
    async fetchPromotions() {
      try {
        const response = await apiClient.get(`/promotions/game/${this.$route.params.id}`);
        this.promotions = response.data;
        console.log('Promotions:', this.promotions);
      } catch (err) {
        console.error('Error fetching promotions:', err);
      }
    },
    async fetchGameReviews() {
      try {
        const response = await apiClient.get(
          `/reviews/${this.$route.params.id}`
        );
        this.reviews = response.data;
        console.log("Reviews:", this.reviews);
      } catch (err) {
        console.error("Error fetching reviews:", err);
      }
    },
    toggleReviewsVisibility() {
      this.areReviewsVisible = !this.areReviewsVisible;
    },
    addToWishlist() {
      if (!userState.userInfo) {
        alert('Please log in to add items to your wishlist');
        return;
      }
      console.log('Adding to wishlist:', this.game.title);
   },
    async addToCart() {
      if (!userState.userInfo) {
        alert('Please log in to add items to your cart');
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
      }
    },
  },
  created() {
    this.fetchGameDetails();
    this.fetchGameReviews();
  },
};
</script>

<style scoped>
.game-details-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: white;
  min-height: 100vh;
}

.back-link {
  margin-bottom: 20px;
}

.back-button {
  display: inline-block;
  padding: 8px 16px;
  color: #666;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.back-button:hover {
  color: #333;
  background-color: #f5f5f5;
}

.game-details-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.content-wrapper {
  display: flex;
  gap: 30px;
  padding: 20px;
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
  flex-shrink: 0;
}

.placeholder-text {
  color: #666;
  font-style: italic;
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
  color: #666;
  min-width: 100px;
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

.description-section {
  margin-top: 20px;
}

.description-section h2 {
  font-size: 1.5em;
  
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
  color: #666;
  font-size: 0.9em;
}

.opinion-section {
  border-radius: 5px;
  padding: 10px;
  color: white;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
  max-width: 20%;
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

.very-positive {
  background-color: #a5d6a7; /* Pastel Green */
  color: #2e7d32; /* Darker Green for contrast */
}

.positive {
  background-color: #c5e1a5; /* Lighter Pastel Green */
  color: #558b2f; /* Medium Green for contrast */
}

.negative {
  background-color: #ef9a9a; /* Pastel Red */
  color: #c62828; /* Darker Red for contrast */
}

.very-negative {
  background-color: #e57373; /* Darker Pastel Red */
  color: #b71c1c; /* Deep Red for contrast */
}

.neutral {
  background-color: #b0bec5; /* Pastel Gray */
  color: #37474f; /* Darker Gray for contrast */
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
  color: #333;
}

.wishlist-button:hover:not(:disabled) {
  background-color: #d0d0d0;
}

.cart-button {
  background-color: #4caf50;
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
