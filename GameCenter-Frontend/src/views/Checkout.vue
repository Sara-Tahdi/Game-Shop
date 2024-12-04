<template>
  <div class="checkout-container">
    <h2>{{ cartData.length > 1 ? "Games" : "Game " }}</h2>
    <div v-if="!loading" v-for="game in cartData" :key="game.id">
      <div class="game-price-info">
        <CheckoutElement
            :game="game"
            :finalPrice="getFinalPrice(game.id)"
        />
        <!-- Debug info -->
        <div class="debug-info" style="display: none;">
          Original Price: ${{ game.price }}
          Promotional Price: ${{ promotionalPrices[game.id] }}
          Final Price: ${{ getFinalPrice(game.id) }}
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading-message">
      Loading cart items...
    </div>

    <h2>Select Payment Info</h2>
    <PaymentInfoSelection
        ref="paymentSelection"
        @payment-select="updateSelectedPayment"
    />

    <div class="total-section">
      <h3>Order Total: ${{ calculateTotal().toFixed(2) }}</h3>
    </div>

    <div class="purchase-button-container">
      <button class="purchase-button" @click="handlePurchase" :disabled="loading">
        Submit Purchase
      </button>
    </div>
  </div>
</template>

<script>
import { userState } from "@/state/userState";
import CheckoutElement from "@/components/CheckoutElement.vue";
import PaymentInfoSelection from "@/components/PaymentInfoSelection.vue";
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  name: "Checkout",
  components: {
    CheckoutElement,
    PaymentInfoSelection,
  },
  data() {
    return {
      cartData: [],
      loading: true,
      selectedPayment: null,
      promotionalPrices: {},
      error: null,
    };
  },
  methods: {
    getFinalPrice(gameId) {
      const originalPrice = this.cartData.find(game => game.id === gameId)?.price || 0;
      const promoPrice = this.promotionalPrices[gameId];
      return promoPrice && promoPrice < originalPrice ? promoPrice : originalPrice;
    },

    calculateTotal() {
      return this.cartData.reduce((total, game) => {
        return total + this.getFinalPrice(game.id);
      }, 0);
    },

    async fetchPromotions(gameId) {
      try {
        console.log(`Fetching promotions for game ${gameId}`);
        const response = await apiClient.get(`/promotions/game/${gameId}`);
        console.log(`Promotions response for game ${gameId}:`, response.data);

        const promotions = response.data;
        const currentDate = new Date();

        const activePromotions = promotions.filter(promo => {
          const endDate = new Date(promo.endDate);
          return endDate >= currentDate;
        });

        console.log(`Active promotions for game ${gameId}:`, activePromotions);

        if (activePromotions.length > 0) {
          const lowestPrice = Math.min(...activePromotions.map(p => p.newPrice));
          console.log(`Lowest price for game ${gameId}:`, lowestPrice);
          this.promotionalPrices[gameId] = lowestPrice;
        }
      } catch (error) {
        console.error(`Error fetching promotions for game ${gameId}:`, error);
      }
    },

    async fetchCart() {
      this.loading = true;
      this.error = null;
      this.cartData = []; // Clear existing data

      try {
        const response = await apiClient.get(
            `/carts/client/${userState.userInfo.id}`
        );
        console.log("Cart response:", response.data);

        for (const item of response.data) {
          try {
            const gameResponse = await apiClient.get(`/games/id/${item.gameId}`);
            console.log(`Game data for ID ${item.gameId}:`, gameResponse.data);
            this.cartData.push(gameResponse.data);
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

    updateSelectedPayment(payment) {
      this.selectedPayment = payment;
    },

    async handlePurchase() {
      if (!this.selectedPayment && !this.$refs.paymentSelection.newPaymentInfo.cardNumber) {
        alert("Please select or enter payment information");
        return;
      }

      try {
        if (this.$refs.paymentSelection.newPaymentInfo.cardNumber &&
            this.$refs.paymentSelection.savePaymentInfo) {
          await apiClient.post(
              `paymentInfo/${userState.userInfo.id}`,
              this.$refs.paymentSelection.newPaymentInfo
          );
        }

        const purchaseData = this.cartData.map(game => ({
          clientId: userState.userInfo.id,
          gameId: game.id,
          copies: 1,
          price: this.getFinalPrice(game.id)
        }));

        console.log("Purchase data:", purchaseData);

        const response = await apiClient.post(
            `purchases/place/${userState.userInfo.id}`,
            purchaseData
        );

        if (response.status === 200) {
          alert("Purchase successful!");
          this.$router.push('/catalog');
        }
      } catch (err) {
        console.error("Purchase error:", err);
        alert("Failed to complete purchase. Please try again.");
      }
    },
  },
  created() {
    if (!userState.userInfo?.id) {
      this.$router.push('/');
      return;
    }
    this.fetchCart();
  },
};
</script>

<style scoped>
.checkout-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
  padding-bottom: 80px;
}

.loading-message {
  text-align: center;
  padding: 20px;
  color: #666;
}

.game-price-info {
  margin-bottom: 20px;
  padding: 15px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.total-section {
  margin: 20px 0;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  text-align: right;
}

.purchase-button-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 20px;
}

.purchase-button {
  background-color: #007bff;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.purchase-button:hover:not(:disabled) {
  background-color: #0056b3;
}

.purchase-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error-message {
  color: #dc3545;
  padding: 10px;
  margin: 10px 0;
  border-radius: 4px;
  background-color: #f8d7da;
}
</style>