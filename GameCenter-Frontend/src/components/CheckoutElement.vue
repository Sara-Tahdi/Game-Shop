<template>
  <div class="checkout-item">
    <div class="item-info">
      <div class="game-title">{{ game.title }}</div>
      <div class="game-price">{{ game.price.toFixed(2) }}$</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CheckoutElement",
  props: {
    game: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.checkout-item {
  width: 100%;
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 0.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.item-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.game-title {
  font-size: 1.1rem;
  font-weight: 500;
  color: #333;
}

.game-price {
  font-size: 1.1rem;
  font-weight: 600;
  color: #007bff;
}
</style>
