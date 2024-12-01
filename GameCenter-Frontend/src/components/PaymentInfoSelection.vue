<template>
  <div class="payment-selection-container">
    <!-- Existing Payment Methods -->
    <div v-if="paymentInfos.length > 0" class="saved-payments">
      <h3>Saved Payment Methods</h3>
      <div v-for="info in paymentInfos" :key="info.id" class="payment-option">
        <label class="radio-container">
          <input
            type="radio"
            :value="info"
            v-model="selectedPaymentInfo"
            name="payment"
            :checked="selectedPaymentInfo === info"
            @click="handleRadioClick(info)"
            class="radio-custom"
          />
          <span class="payment-details">
            ****{{ info.lastFourDigits }} (Expires: {{ info.expiryMonth }}/{{
              info.expiryYear
            }})
          </span>
        </label>
      </div>
    </div>

    <!-- New Payment Form -->
    <div class="new-payment-form">
      <h3>
        {{
          paymentInfos.length > 0
            ? "Or Add New Payment Method"
            : "Add Payment Method"
        }}
      </h3>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>Card Number</label>
          <input
            v-model="newPaymentInfo.cardNumber"
            type="text"
            maxlength="16"
            placeholder="Card Number"
            required
          />
        </div>

        <div class="form-group">
          <label>Card Holder Name</label>
          <input
            v-model="newPaymentInfo.cardHolderName"
            type="text"
            placeholder="Card Holder Name"
            required
          />
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Expiry Month</label>
            <input
              v-model="newPaymentInfo.expiryMonth"
              type="text"
              maxlength="2"
              placeholder="MM"
              required
            />
          </div>
          <div class="form-group">
            <label>Expiry Year</label>
            <input
              v-model="newPaymentInfo.expiryYear"
              type="text"
              maxlength="4"
              placeholder="YYYY"
              required
            />
          </div>
          <div class="form-group">
            <label>CVV</label>
            <input
              v-model="newPaymentInfo.cvv"
              type="text"
              maxlength="3"
              placeholder="CVV"
              required
            />
          </div>
        </div>

        <div class="save-payment">
          <label>
            <input type="checkbox" v-model="savePaymentInfo" /> Save this
            payment method for future use
          </label>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { userState } from "@/state/userState";
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "http://localhost:8080",
  },
});

export default {
  name: "PaymentInfoSelection",
  data() {
    return {
      paymentInfos: [],
      selectedPaymentInfo: null,
      savePaymentInfo: false,
      newPaymentInfo: {
        cardNumber: "",
        cardHolderName: "",
        expiryMonth: "",
        expiryYear: "",
        cvv: "",
      },
    };
  },
  methods: {
    async getPaymentInfos() {
      try {
        let response = await apiClient.get(
          `paymentInfo/${userState.userInfo.id}`,
        );
        this.paymentInfos = response.data;
      } catch (err) {
        console.log(err);
      }
    },
    async handleSubmit() {
      const paymentInfo = {
        ...this.newPaymentInfo,
      };

      if (this.savePaymentInfo) {
        try {
          await apiClient.post(
            `paymentInfo/${userState.userInfo.id}`,
            paymentInfo,
          );
          await this.getPaymentInfos();
        } catch (err) {
          console.log(err);
        }
      }
      this.selectedPaymentInfo = paymentInfo;
      this.$emit("payment-select", this.selectedPaymentInfo);
      console.log(paymentInfo);
    },
    handleRadioClick(info) {
      if (this.selectedPaymentInfo === info) {
        this.selectedPaymentInfo = null;
        this.$emit("payment-select", null);
      } else {
        this.selectedPaymentInfo = info;
        this.$emit("payment-select", info);
        this.newPaymentInfo = {
          cardNumber: "",
          cardHolderName: "",
          expiryMonth: "",
          expiryYear: "",
          cvv: "",
        };
        this.savePaymentInfo = false;
      }
      console.log(this.selectedPaymentInfo);
    },
    clearRadioSelection() {
      this.selectedPaymentInfo = null;
      this.$emit("payment-select", null);
    },
  },
  created() {
    this.getPaymentInfos();
  },
  watch: {
    selectedPaymentInfo(newValue) {
      this.$emit("payment-select", newValue);
    },

    newPaymentInfo: {
      handler(newVal) {
        const hasValue = Object.values(newVal).some((val) => val !== "");
        if (hasValue) {
          this.clearRadioSelection();
        }
      },
      deep: true,
    },

    savePaymentInfo(newVal) {
      if (newVal) {
        this.clearRadioSelection();
      }
    },
  },
};
</script>

<style scoped>
.payment-selection-container {
  padding: 2px;
}

.saved-payments {
  margin-bottom: 30px;
}

.payment-option {
  margin: 10px 0;
}

.radio-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.radio-custom {
  width: 20px;
  height: 20px;
  border: 2px solid #ccc;
  border-radius: 50%;
  margin-right: 10px;
  position: relative;
}

input[type="radio"]:checked + .radio-custom::after {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background-color: #007bff;
  border-radius: 50%;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-row {
  display: flex;
  gap: 15px;
}

.save-payment {
  margin: 20px 0;
}

button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
