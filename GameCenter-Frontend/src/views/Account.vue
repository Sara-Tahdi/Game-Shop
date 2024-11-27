<template>
  <div class="account-container">
    <h1>Update your account</h1>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
    <form @submit.prevent="updateAccount" class="update-form">
      <div class="form-group">
        <label for="username">Username:</label>
        <input
          type="text"
          id="username"
          v-model="formData.username"
          :placeholder="userState.userInfo.username"
        />
      </div>
      <div class="form-group">
        <label for="new-password">New Password:</label>
        <input type="text" id="new-password" v-model="formData.newPassword" />
      </div>
      <div class="form-group">
        <label for="confirm-password">Confirm Password:</label>
        <input
          type="text"
          id="confirm-password"
          v-model="formData.confirmPassword"
        />
      </div>
      <div v-if="userState.userInfo.userType === 'Client'" class="form-group">
        <label for="phone-number">Phone Number:</label>
        <input
          type="text"
          id="phone-number"
          v-model="formData.phoneNumber"
          :placeholder="userState.userInfo.phoneNumber"
        />
      </div>
      <div v-if="userState.userInfo.userType === 'Client'" class="form-group">
        <label for="delivery-address">Delivery Address:</label>
        <input
          type="text"
          id="delivery-address"
          v-model="formData.deliveryAddress"
          :placeholder="userState.userInfo.deliveryAddress"
        />
      </div>
      <div class="form-group">
        <label for="phone-number">Password to Confirm:</label>
        <input type="text" id="phone-number" v-model="formData.password" />
      </div>
      <button type="submit" class="update-button">Submit</button>
    </form>
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
  name: "Account",
  data() {
    return {
      formData: {
        username: "",
        newPassword: "",
        confirmPassword: "",
        deliveryAddress: "",
        phoneNumber: "",
        password: "",
      },
      error: null,
    };
  },
  setup() {
    return { userState };
  },
  methods: {
    async updateAccount() {
      this.error = null;

      if (
        this.formData.newPassword &&
        this.formData.confirmPassword &&
        this.formData.newPassword !== this.formData.confirmPassword
      ) {
        this.error = "Passwords do not match!";
        return;
      }

      const body = {};
      body.username = this.formData.username
        ? this.formData.username
        : userState.userInfo.username;
      body.email = userState.userInfo.email;
      body.password = this.formData.password;
      body.newPassword = this.formData.newPassword
        ? this.formData.newPassword
        : this.formData.password;
      if (userState.userInfo.userType === "Client") {
        body.deliveryAddress = this.formData.deliveryAddress
          ? this.formData.deliveryAddress
          : "";
        body.phoneNumber = this.formData.phoneNumber
          ? this.formData.deliveryAddress
          : "";
      }

      try {
        const data = await apiClient
          .put(
            `/users/${userState.userInfo.userType.toLowerCase()}/update`,
            body,
          )
          .then((res) => res.data);
        if (userState.userInfo.userType === "Client") {
          userState.setUser({
            id: data.id,
            email: data.email,
            username: data.username,
            deliveryAddress: data.deliveryAddress,
            phoneNumber: data.phoneNumber,
          });
        } else {
          userState.setUser({
            id: data.id,
            email: data.email,
            username: data.username,
          });
        }
        console.log("success");
      } catch (err) {
        this.error = err;
        console.log(this.error);
      }
    },
  },
};
</script>

<style scoped>
.account-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  background-color: white;
  border-radius: 8px;
}

.update-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin-top: 2rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  font-weight: 500;
  color: #333;
}

input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

input:focus {
  outline: none;
  border-color: #007bff;
}

.update-button {
  background-color: #007bff;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.update-button:hover {
  background-color: #0056b3;
}

.error-message {
  color: #dc3545;
  padding: 0.75rem;
  background-color: #ffe6e6;
  border-radius: 4px;
  margin-top: 1rem;
}

.success-message {
  color: #28a745;
  padding: 0.75rem;
  background-color: #e6ffe6;
  border-radius: 4px;
  margin-top: 1rem;
}

@media (max-width: 768px) {
  .account-container {
    padding: 1rem;
  }
}
</style>
