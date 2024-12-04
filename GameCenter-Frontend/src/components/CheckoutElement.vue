<template>
  <div class="checkout-element">
    <h3 class="game-title">{{ game.title }}</h3>
    <div class="price-section">
      <span class="original-price" :class="{ 'has-promotion': finalPrice < game.price }">
        ${{ game.price.toFixed(2) }}
      </span>
      <span v-if="finalPrice < game.price" class="promotional-price">
        ${{ finalPrice.toFixed(2) }}
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CheckoutElement',
  props: {
    game: {
      type: Object,
      required: true
    },
    finalPrice: {
      type: Number,
      required: true
    }
  }
};
</script>

<style scoped>
.checkout-element {
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
  background-color: #fff;
}

.game-title {
  color: #2c3e50;  /* Dark color for better readability */
  margin-bottom: 10px;
  font-size: 1.2em;
}

.price-section {
  display: flex;
  gap: 10px;
  align-items: center;
}

.original-price {
  color: #2c3e50;  /* Dark color for regular price */
}

.original-price.has-promotion {
  text-decoration: line-through;
  color: #999;
}

.promotional-price {
  color: #dc3545;
  font-weight: bold;
}
</style>