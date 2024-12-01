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
        <!-- Future image placeholder -->
        <div class="game-image-placeholder">
          <div class="placeholder-text">Game Image Coming Soon</div>
        </div>

        <div class="game-info">
          <h1 class="game-title">{{ game.title }}</h1>

          <div class="game-details">
            <div class="detail-row">
              <span class="label">Price:</span>
              <span class="value">${{ game.price.toFixed(2) }}</span>
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
              >
                Add to Wishlist
              </button>
              <button
                @click="addToCart"
                class="cart-button"
                :disabled="isGuest || game.remainingQuantity === 0"
              >
                Add to Cart
              </button>
            </div>
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
      <div class="write-review-section">
        <h2>Write a Review</h2>
        <form @submit.prevent="submitReview">
          <label for="author">Your Name:</label>
          <input type="text" id="author" v-model="newReview.author" required />

          <label for="rating">Rating:</label>
          <select id="rating" v-model="newReview.rating" required>
            <option value="" disabled>Select Rating</option>
            <option value="1">1 - Very Negative</option>
            <option value="2">2 - Negative</option>
            <option value="3">3 - Neutral</option>
            <option value="4">4 - Positive</option>
            <option value="5">5 - Very Positive</option>
          </select>

          <label for="reviewMessage">Review:</label>
          <textarea
            id="reviewMessage"
            v-model="newReview.reviewMessage"
            required
          ></textarea>

          <button type="submit">Submit Review</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import GameReview from "../components/GameReview.vue";
import { userState } from "@/state/userState";

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
      loading: true,
      error: null,
      areReviewsVisible: false,
      isGuest: null,
      isClient: null,
      newReview: {
        author: "",
        reviewMessage: "",
        rating: "",
      },
    };
  },
  computed: {
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
    async fetchGameDetails() {
      try {
        this.loading = true;
        const response = await apiClient.get(
          `/games/id/${this.$route.params.id}`
        );
        this.game = response.data;
        console.log("Game details:", this.game);
      } catch (err) {
        this.error = err.response?.data || "Error loading game details";
        console.error("Error fetching game details:", err);
      } finally {
        this.loading = false;
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
      console.log("Adding to wishlist:", this.game.title);
    },
    async addToCart() {
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
    submitReview() {
      if (
        !this.newReview.author ||
        !this.newReview.reviewMessage ||
        !this.newReview.rating
      ) {
        alert("Please fill in all fields before submitting.");
        return;
      }

      const review = {
        ...this.newReview,
        game: this.game,
        id: Date.now(), // Simulate unique ID
        managerReply: null, // No manager reply for new reviews
      };

      // Simulate sending the review to the server
      this.$emit("add-review", review);

      // Clear the form
      this.newReview = {
        author: "",
        reviewMessage: "",
        rating: "",
      };
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
  min-height: 100vh;
  background-color: white;
  color: black;
  padding: 20px;
}

.back-link {
  margin-bottom: 20px;
}

.back-button {
  color: #4caf50;
  text-decoration: none;
  font-weight: 500;
}

.back-button:hover {
  text-decoration: underline;
}

.content-wrapper {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.game-image-placeholder {
  flex: 0 0 400px;
  height: 400px;
  background-color: #f5f5f5;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px dashed #ddd;
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
  font-size: 2.5em;
  margin-bottom: 20px;
  color: #333;
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
  font-weight: bold;
  min-width: 100px;
}

.description-section {
  margin-top: 20px;
}

.description-section h2 {
  font-size: 1.5em;
  margin-bottom: 10px;
  color: #333;
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
  padding: 4px 8px;
  border-radius: 4px;
  background-color: #dc3545;
  color: white;
}

.stock-status.in-stock {
  background-color: #28a745;
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
}

.reviews-container {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.review-item {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.review-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.no-reviews {
  text-align: center;
  padding: 40px;
  color: #666;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.write-review-section {
  margin-top: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.write-review-section h2 {
  margin-bottom: 10px;
}

.loading {
  text-align: center;
  padding: 40px;
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
}
</style>
