<template>
  <div>
    <h2>{{ cartData.length > 1 ? "Games" : "Game " }}</h2>
    <div v-if="!loading" v-for="game in cartData">
      <CheckoutElement :game="game" />
    </div>
    <h2>Select Payment Info</h2>
    <PaymentInfoSelection
      ref="paymentSelection"
      @payment-select="updateSelectedPayment"
    />
    <div class="purchase-button-container">
      <button class="purchase-button" @click="handlePurchase">
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
    "Access-Control-Allow-Origin": "http://localhost:8080",
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
    };
  },
  methods: {
    async fetchCart() {
      this.loading = true;
      try {
        const response = await apiClient.get(
          `/carts/client/${userState.userInfo.id}`,
        );
        console.log(response.data);
        response.data.forEach(async (item) => {
          console.log(item);
          const itemData = await apiClient.get(`/games/id/${item.gameId}`);
          this.cartData.push(itemData.data);
        });
        this.loading = false;
      } catch (err) {
        this.error = "Failed to load cart. Please try again.";
        console.error(err);
      } finally {
        this.loading = false;
      }
    },
    updateSelectedPayment(payment) {
      this.selectedPayment = payment;
    },
    async handlePurchase() {
      const paymentSelection = this.$refs.paymentSelection;

      if (paymentSelection.newPaymentInfo.cardNumber) {
        if (paymentSelection.savePaymentInfo) {
          try {
            await apiClient.post(
              `paymentInfo/${userState.userInfo.id}`,
              paymentSelection.newPaymentInfo,
            );
          } catch (err) {
            console.log(err);
          }
        }
        this.selectedPayment = paymentSelection.newPaymentInfo;
      } else if (!this.selectedPayment) {
        alert("Please select or enter payment information");
        return;
      }

      let purchaseData = [];

      this.cartData.forEach((element) => {
        let tmp = {
          clientId: userState.userInfo.id,
          gameId: element.id,
          copies: 1,
        };
        purchaseData.push(tmp);
      });

      console.log(purchaseData);

      try {
        await apiClient.post(`purchases/place/${userState.userInfo.id}`);
      } catch (err) {
        console.log(err);
      }
    },
  },
  created() {
    this.fetchCart();
  },
};
</script>

<style scoped>
.checkout-container {
  position: relative;
  min-height: 100vh;
  padding-bottom: 80px; /* Space for the checkout button */
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

.purchase-button:hover {
  background-color: #0056b3;
}
</style>
