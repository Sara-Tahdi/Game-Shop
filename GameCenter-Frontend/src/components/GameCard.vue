<template>
  <div class="game-card">
    <RouterLink
      :to="{
        name: 'GameDetails',
        params: { id: game.id },
      }"
      class="game-link"
    >
      <div class="game-image-placeholder">
        <img
          :src="game.imageUrl"
          alt="Problem fetching game image"
          class="game-image"
        />
      </div>
      <div class="game-content">
        <h3>{{ game.title }}</h3>
        <p class="description">{{ game.description }}</p>
        <div class="game-details">
          <div class="price-container">
            <span
              class="original-price"
              :class="{ 'has-promotion': promotionalPrices[game.id] }"
            >
              ${{ game.price.toFixed(2) }}
            </span>
            <span v-if="promotionalPrices[game.id]" class="promotional-price">
              ${{ promotionalPrices[game.id].toFixed(2) }}
            </span>
          </div>
          <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5.0</span>
        </div>
        <div
          class="stock-status"
          :class="{ 'in-stock': game.remainingQuantity > 0 }"
        >
          {{ game.remainingQuantity > 0 ? "In Stock" : "Out of Stock" }}
        </div>
      </div>
    </RouterLink>
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
  name: "GameCard",
  props: {
    game: {
      type: Object,
      required: true,
    },
    isInWishlist: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      promotionalPrices: {},
    };
  },
  methods: {
    async fetchPromotions() {
      try {
        console.log(`Fetching promotions for game ${this.game.id}`);
        const response = await apiClient.get(
          `/promotions/game/${this.game.id}`,
        );
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
          this.promotionalPrices[this.game.id] = lowestPrice;
        }
      } catch (error) {
        console.error(
          `Error fetching promotions for game ${this.game.id}:`,
          error,
        );
      }
    },
  },
  created() {
    this.fetchPromotions();
  },
};
</script>

<style scoped>
.game-card {
  background: white;
  width: 300px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 450px;
  transition: transform 0.2s ease-in-out;
  margin: 10px;
}

.game-card:hover {
  transform: translateY(-5px);
}

.game-link {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.game-image-placeholder {
  width: 100%;
  height: 200px;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 2px dashed #ddd;
}

.game-image {
  width: 100%;
  height: 100%;
  object-fit: fill;
  object-position: center;
}

.game-content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.game-card h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.description {
  color: #666;
  margin-bottom: 10px;
  font-size: 0.9em;
  flex: 1;
}

.game-details {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.price-container {
  display: flex;
  gap: 10px;
  align-items: center;
  height: 1.5em; /* Fixed height for price container */
}

.original-price {
  color: #2c3e50;
}

.original-price.has-promotion {
  text-decoration: line-through;
  color: #999;
}

.promotional-price {
  color: #dc3545;
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
  margin-top: 10px;
  height: 2em; /* Fixed height for status */
  display: flex;
  align-items: center;
  justify-content: center;
}

.stock-status.in-stock {
  background-color: #2ecc71;
}
</style>
