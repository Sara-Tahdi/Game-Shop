<template>
  <div class="game-card">
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
          @click="addToWishlist"
          class="wishlist-button"
          :disabled="game.remainingQuantity === 0">
        Add to Wishlist
      </button>
      <button
          @click="addToCart"
          class="cart-button"
          :disabled="game.remainingQuantity === 0">
        Add to Cart
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GameCard',
  props: {
    game: {
      type: Object,
      required: true
    }
  },
  methods: {
    addToWishlist() {
      this.$emit('add-to-wishlist', this.game);
    },
    addToCart() {
      this.$emit('add-to-cart', this.game);
    }
  }
};
</script>

<style scoped>
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
</style>