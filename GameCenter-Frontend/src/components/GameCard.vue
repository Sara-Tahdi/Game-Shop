<template>
  <div class="game-card">
    <div class="game-image-placeholder">
      <div class="placeholder-text">Game Image Coming Soon</div>
    </div>
    <div class="game-content">
      <h3>{{ game.title }}</h3>
      <p class="description">{{ game.description }}</p>
      <div class="game-details">
        <span class="price">${{ game.price.toFixed(2) }}</span>
        <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5</span>
      </div>
      <div
        class="stock-status"
        :class="{ 'in-stock': game.remainingQuantity > 0 }"
      >
        {{ game.remainingQuantity > 0 ? "In Stock" : "Out of Stock" }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "GameCard",
  props: {
    game: {
      type: Object,
      required: true,
    },
  },
  methods: {
    addToWishlist() {
      this.$emit("add-to-wishlist", this.game);
    },
    addToCart() {
      this.$emit("add-to-cart", this.game);
    },
  },
};
</script>

<style scoped>
.game-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
  transition: transform 0.2s ease-in-out;
}

.game-card:hover {
  transform: translateY(-5px);
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
  margin-bottom: 10px;
}

.stock-status.in-stock {
  background-color: #2ecc71;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: auto;
}
</style>
