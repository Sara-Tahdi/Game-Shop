<template>
  <div class="cart">
    <h1 class="cart-title">Your Cart</h1>

    <div v-if="loading" class="loading">
      Loading your cart...
      <div class="spinner"></div>
    </div>

    <div v-if="error" class="error">
      {{ error }}
      <button @click="fetchCart" class="retry-button">Retry</button>
    </div>

    <div v-if="!loading && !error && cart.length > 0" class="cart-container">
      <section class="games-list">
        <div v-for="game in cart" :key="game.id" class="game-card">
          <h3>{{ game.title }}</h3>
          <p class="description">{{ game.description }}</p>
          <div class="game-details">
            <div class="price-container">
              <span class="original-price" :class="{ 'has-promotion': promotionalPrices[game.id] }">
                ${{ game.price.toFixed(2) }}
              </span>
              <span v-if="promotionalPrices[game.id]" class="promotional-price">
                ${{ promotionalPrices[game.id].toFixed(2) }}
              </span>
            </div>
            <span class="rating">⭐ {{ game.rating.toFixed(1) }}/5</span>
          </div>
          <div
              class="stock-status"
              :class="{ 'in-stock': game.remainingQuantity > 0 }"
          >
            {{ game.remainingQuantity > 0 ? "In Stock" : "Out of Stock" }}
          </div>
          <button @click="removeFromCart(game.id)" class="remove-btn">
            Remove from Cart
          </button>
        </div>
      </section>

      <div class="cart-summary">
        <div class="total">
          <span>Total:</span>
          <span>${{ calculateTotal().toFixed(2) }}</span>
        </div>
        <button
            @click="proceedToCheckout"
            class="checkout-btn"
            :disabled="cart.some(game => game.remainingQuantity === 0)"
        >
          Proceed to Checkout
        </button>
      </div>
    </div>

    <div v-if="!loading && !error && cart.length === 0" class="no-items">
      Your cart is empty.
    </div>
  </div>
</template>

<script>
import { userState } from "@/state/userState";
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 5000,
});

export default {
  name: "Cart",
  data() {
    return {
      cart: [],
      loading: false,
      error: null,
      promotionalPrices: {},
    };
  },
  methods: {
    calculateTotal() {
      return this.cart.reduce((total, game) => {
        const price = this.promotionalPrices[game.id] || game.price;
        return total + price;
      }, 0);
    },

    async fetchPromotions(gameId) {
      try {
        console.log(`Fetching promotions for game ${gameId}`);
        const response = await apiClient.get(`/promotions/game/${gameId}`);
        const promotions = response.data;
        const currentDate = new Date();

        const activePromotions = promotions.filter(promo => {
          const endDate = new Date(promo.endDate);
          return endDate >= currentDate;
        });

        if (activePromotions.length > 0) {
          const lowestPrice = Math.min(...activePromotions.map(p => p.newPrice));
          this.promotionalPrices[gameId] = lowestPrice;
        }
      } catch (error) {
        console.error(`Error fetching promotions for game ${gameId}:`, error);
      }
    },

    async fetchCart() {
      if (!userState.userInfo?.id) {
        this.error = "Please log in to view your cart";
        return;
      }

      this.loading = true;
      this.error = null;

      try {
        const response = await apiClient.get(`/carts/client/${userState.userInfo.id}`);
        this.cart = [];

        for (const item of response.data) {
          try {
            const gameResponse = await apiClient.get(`/games/id/${item.gameId}`);
            this.cart.push(gameResponse.data);
            await this.fetchPromotions(item.gameId);
          } catch (err) {
            console.error(`Error fetching game ${item.gameId}:`, err);
          }
        }
      } catch (err) {
        console.error("Error fetching cart:", err);
        this.error = "Failed to load cart. Please try again.";
      } finally {
        this.loading = false;
      }
    },

    async removeFromCart(gameId) {
      try {
        await apiClient.delete(`/carts/${userState.userInfo.id}/game/${gameId}`);
        this.cart = this.cart.filter(game => game.id !== gameId);
      } catch (err) {
        console.error("Error removing from cart:", err);
        alert("Failed to remove game from cart. Please try again.");
      }
    },

    proceedToCheckout() {
      this.$router.push("/checkout");
    },
  },
  created() {
    this.fetchCart();
  },
};
</script>

<style scoped>
.cart {
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.cart-title {
  color: #000000;
  font-size: 2.5em;
  text-align: center;
  margin: 20px 0;
  font-weight: bold;
}

.cart-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.games-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.game-card {
  background: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.game-card h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
  font-size: 1.2em;
}

.description {
  color: #2c3e50;
  margin-bottom: 10px;
  font-size: 0.9em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  flex: 1;
}

.game-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.price-container {
  display: flex;
  gap: 10px;
  align-items: center;
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
  margin: 10px 0;
}

.stock-status.in-stock {
  background-color: #2ecc71;
}

.remove-btn {
  width: 100%;
  padding: 8px 16px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
  transition: background-color 0.3s;
}

.remove-btn:hover {
  background-color: #c0392b;
}

.cart-summary {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.2em;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 15px;
}

.checkout-btn {
  width: 100%;
  padding: 12px 24px;
  background-color: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1em;
  transition: background-color 0.3s;
}

.checkout-btn:hover:not(:disabled) {
  background-color: #27ae60;
}

.checkout-btn:disabled {
  background-color: #95a5a6;
  cursor: not-allowed;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #666;
  background-color: white;
  margin: 20px;
  border-radius: 8px;
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
  margin: 20px;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #dc3545;
}

.retry-button {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.retry-button:hover {
  background-color: #c82333;
}

.no-items {
  text-align: center;
  padding: 40px;
  color: #2c3e50;
  background-color: white;
  border-radius: 8px;
  margin: 20px;
  font-size: 1.2em;
}

@media (max-width: 1024px) {
  .games-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .games-list {
    grid-template-columns: 1fr;
  }

  .cart-title {
    font-size: 2em;
  }
}
</style>
