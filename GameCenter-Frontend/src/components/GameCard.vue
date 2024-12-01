<template>
  <div class="game-card">
    <div class="game-image-placeholder">
      <div class="placeholder-text">Game Image Coming Soon</div>
    </div>
    <div class="game-content">
      <h3>{{ game.title }}</h3>
      <p class="description">{{ game.description }}</p>
      <div class="game-details">
        <div class="price-container">
          <span class="price" :class="{ 'original-price': hasPromotion }">
            ${{ game.price.toFixed(2) }}
          </span>
          <span v-if="hasPromotion" class="promotional-price">
            ${{ lowestPrice.toFixed(2) }}
          </span>
        </div>
        <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5</span>
      </div>
      <div v-if="hasPromotion" class="promotion-badge">
        PROMOTION
      </div>
      <div class="stock-status" :class="{ 'in-stock': game.remainingQuantity > 0 }">
        {{ game.remainingQuantity > 0 ? 'In Stock' : 'Out of Stock' }}
      </div>
      <div class="action-buttons">
        <button
            @click.stop="addToWishlist"
            class="wishlist-button"
            :disabled="game.remainingQuantity === 0"
            :title="!userState.userInfo ? 'Please log in to add to wishlist' : ''"
        >
          Add to Wishlist
        </button>
        <button
            @click.stop="addToCart"
            class="cart-button"
            :disabled="game.remainingQuantity === 0"
            :title="!userState.userInfo ? 'Please log in to add to cart' : ''"
        >
          Add to Cart
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { userState } from '@/state/userState';
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  name: 'GameCard',
  props: {
    game: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      userState: userState,
      promotions: [],
      lowestPrice: null,
      hasPromotion: false
    };
  },
  async created() {
    await this.fetchPromotions();
  },
  methods: {
    async fetchPromotions() {
      try {
        const response = await apiClient.get(`/promotions/game/${this.game.id}`);
        this.promotions = response.data;

        // Filter current promotions
        const currentDate = new Date();
        const activePromotions = this.promotions.filter(promo => {
          const endDate = new Date(promo.endDate);
          return endDate >= currentDate;
        });

        if (activePromotions.length > 0) {
          this.hasPromotion = true;
          this.lowestPrice = Math.min(...activePromotions.map(p => p.newPrice));
        } else {
          this.lowestPrice = this.game.price;
          this.hasPromotion = false;
        }
      } catch (error) {
        console.error('Error fetching promotions:', error);
        this.lowestPrice = this.game.price;
      }
    },
    addToWishlist() {
      if (!userState.userInfo) {
        alert('Please log in to add items to your wishlist');
        return;
      }
      this.$emit('add-to-wishlist', this.game);
    },
    addToCart() {
      if (!userState.userInfo) {
        alert('Please log in to add items to your cart');
        return;
      }
      this.$emit('add-to-cart', this.game);
    }
  }
};
</script>

<style scoped>
.game-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  position: relative;
}

.game-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
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

.placeholder-text {
  color: #666;
  font-style: italic;
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
  font-size: 1.2em;
  line-height: 1.3;
}

.description {
  color: #666;
  margin-bottom: 10px;
  font-size: 0.9em;
  line-height: 1.4;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.game-details {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.price-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
}

.price {
  color: #2c3e50;
  font-weight: bold;
  font-size: 1.1em;
  transition: all 0.2s ease;
}

.original-price {
  text-decoration: line-through;
  color: #999;
  font-size: 0.9em;
  font-weight: normal;
}

.promotional-price {
  color: #e74c3c;
  font-weight: bold;
  font-size: 1.2em;
}

.promotion-badge {
  background-color: #e74c3c;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8em;
  font-weight: bold;
  display: inline-block;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.rating {
  color: #f1c40f;
  font-weight: bold;
}

.stock-status {
  padding: 5px 10px;
  border-radius: 4px;
  text-align: center;
  background-color: #e74c3c;
  color: white;
  margin-bottom: 10px;
  font-size: 0.9em;
  transition: background-color 0.2s ease;
}

.stock-status.in-stock {
  background-color: #2ecc71;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.wishlist-button,
.cart-button {
  flex: 1;
  padding: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9em;
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

@media (max-width: 768px) {
  .game-card {
    font-size: 0.95em;
  }

  .game-image-placeholder {
    height: 180px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .wishlist-button,
  .cart-button {
    width: 100%;
    padding: 10px;
  }
}

@media (max-width: 480px) {
  .game-card h3 {
    font-size: 1.1em;
  }

  .description {
    -webkit-line-clamp: 2;
  }

  .game-image-placeholder {
    height: 150px;
  }
}
</style>