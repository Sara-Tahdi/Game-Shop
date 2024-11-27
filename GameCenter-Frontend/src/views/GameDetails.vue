<template>
  <div>
    <div v-if="loading">Loading game details...</div>
    <div v-else-if="error">
      <p>Error loading game details: {{ error }}</p>
    </div>
    <div v-else class="game-details-container">
      <div class="game-info">
        <h2>Game Details</h2>
        <h3>{{ game.title }}</h3>
        <p><strong>Price:</strong> ${{ game.price }}</p>
        <p><strong>Description:</strong> {{ game.description }}</p>
        <p><strong>General Feeling:</strong> {{ game.publicOpinion }}</p>
        <p><strong>Category:</strong> {{ game.category.category }}</p>
        <p>⭐ {{ game.rating.toFixed(1) }}/5</p>
      </div>

      <!-- Reviews Section -->
      <div v-if="reviews && reviews.length > 0" class="reviews">
        <button class="accordion-toggle" @click="toggleReviewsVisibility">
          {{ areReviewsVisible ? "Hide Reviews" : "Show Reviews" }}
        </button>

        <div v-if="areReviewsVisible">
          <h2>Reviews:</h2>
          <div v-for="review in reviews" :key="review.id">
            <GameReview :review="review" />
          </div>
        </div>
      </div>
      <div v-else>
        <p>No reviews available for this game.</p>
      </div>

      <div
        class="stock-status"
        :class="{ 'in-stock': game.remainingQuantity > 0 }"
      >
        {{ game.remainingQuantity > 0 ? "In Stock" : "Out of Stock" }}
      </div>

      <!-- Buttons -->
      <div class="action-buttons">
        <button
          @click="addToWishlist"
          class="wishlist-button"
          :disabled="game.remainingQuantity === 0"
        >
          Add to Wishlist
        </button>
        <button
          @click="addToCart"
          class="cart-button"
          :disabled="game.remainingQuantity === 0"
        >
          Add to Cart
        </button>
      </div>
    </div>
    <router-link to="/">Back to Catalog</router-link>
  </div>
</template>

<script lang="ts">
import { ref, onMounted, type Ref } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";

import GameReview from "../components/GameReview.vue";

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
  methods: {
    addToWishlist() {
      this.$emit("add-to-wishlist", this.game);
    },
    addToCart() {
      this.$emit("add-to-cart", this.game);
    },
  },
  setup() {
    const route = useRoute();
    const game: Ref<Game> = ref(null);
    const reviews: Ref<Review[]> = ref(null);
    const loading = ref(true);
    const error = ref(null);
    const areReviewsVisible = ref(false);

    const fetchGameDetails = async () => {
      try {
        const response = await apiClient.get(`/games/id/${route.params.id}`);
        game.value = response.data;
      } catch (e) {
        error.value = e.response?.data?.message || e.message || "Unknown error";
      } finally {
        loading.value = false;
      }
    };

    const fetchGameReviews = async () => {
      try {
        const response = await apiClient.get(`/reviews/${route.params.id}`);
        reviews.value = response.data;
      } catch (err) {
        // error.value =
        //   err.message || "Something went wrong while fetching reviews.";
        // TODO: Split error messages?
      }
    };

    const toggleReviewsVisibility = () => {
      areReviewsVisible.value = !areReviewsVisible.value;
    };

    const fetch = async () => {
      fetchGameDetails();
      fetchGameReviews();
    };

    onMounted(fetch);

    return {
      game,
      reviews,
      loading,
      error,
      areReviewsVisible,
      toggleReviewsVisibility,
    };
  },
};
</script>

<style>
h3 {
  margin-top: 0;
}

.game-details-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* .game-info {
}

.game-reviews {
} */

.accordion-toggle {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  padding: 0.5rem 1rem;
  width: 100%;
  border-radius: 4px;
  text-align: left;
  cursor: pointer;
  transition: background-color 0.2s;
}

.accordion-toggle:hover:not(:disabled) {
  background-color: #efefef;
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
</style>
