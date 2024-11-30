<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-box" @click.stop>
      <div class="modal-header">
        <h3 v-if="isSignIn">Sign In</h3>
        <h3 v-else>Sign Up</h3>
        <button class="close-button" @click="closeModal">×</button>
      </div>
      <div class="modal-body">
        <!-- Error Message -->
        <div v-if="error" class="error-message">
          {{ error }}
        </div>

        <!-- Sign In Form -->
        <form v-if="isSignIn" @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              v-model="email"
              @input="clearError"
              placeholder="Enter your email"
              required
            />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              v-model="password"
              @input="clearError"
              placeholder="Enter your password"
              required
            />
          </div>
          <button type="submit" class="submit-button">Login</button>
          <p class="alt-option">
            New to GameCenter?
            <span class="link" @click="switchToSignUp">Create an Account</span>
          </p>
        </form>

        <!-- Sign Up Form -->
        <form v-else @submit.prevent="handleSignUp">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              v-model="email"
              @input="clearError"
              placeholder="Enter your email"
              required
            />
          </div>
          <div class="form-group">
            <label for="username">Username</label>
            <input
              type="text"
              id="username"
              v-model="username"
              @input="clearError"
              placeholder="Choose a username"
              required
            />
          </div>
          <div class="form-group">
            <label for="phone">Phone Number</label>
            <input
              type="tel"
              id="phone"
              v-model="phone"
              @input="clearError"
              placeholder="Enter your phone number"
              required
            />
          </div>
          <div class="form-group">
            <label for="delivery-address">Delivery Address</label>
            <input
              type="text"
              id="delivery-address"
              v-model="deliveryAddress"
              @input="clearError"
              placeholder="Enter your delivery address"
              required
            />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              v-model="password"
              @input="clearError"
              placeholder="Enter a password"
              required
            />
          </div>
          <div class="form-group">
            <label for="verify-password">Verify Password</label>
            <input
              type="password"
              id="verify-password"
              v-model="verifyPassword"
              @input="clearError"
              placeholder="Re-enter your password"
              required
            />
          </div>
          <button type="submit" class="submit-button">Create Account</button>
          <p class="alt-option">
            Already have an account?
            <span class="link" @click="switchToSignIn">Sign In</span>
          </p>
        </form>
      </div>
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
  name: "LoginModal",
  props: ["isVisible"],
  data() {
    return {
      isSignIn: true, // Track whether it's Sign In or Sign Up
      email: "",
      username: "",
      phone: "",
      deliveryAddress: "",
      password: "",
      verifyPassword: "",
      error: null,
    };
  },
  methods: {
    closeModal() {
      this.$emit("close");
    },
    async handleLogin() {
      this.error = null;
      try {
        const response = await apiClient.post("/users/login", {
          email: this.email,
          password: this.password,
        });
        const userData = response.data;
        console.log("Login successful:", response.data);
        console.log(userData.email);
        console.log(userData.username);
        console.log(userData.userType);
        console.log(userData.id);
        // Update the reactive state
        if (userData.userType === "Client") {
          userState.setUser({
            id: userData.id,
            username: userData.username,
            email: userData.email,
            deliveryAddress: userData.deliveryAddress,
            phoneNumber: userData.phoneNumber,
            userType: userData.userType,
          });
        } else {
          userState.setUser({
            id: userData.id,
            username: userData.username,
            email: userData.email,
            userType: userData.userType,
          });
        }
        this.closeModal();
      } catch (err) {
        this.error =
          "Failed to log in. Please check your credentials or try again later.";
        console.error("Error logging in:", err);
      }
    },
    async handleSignUp() {
      this.error = null;
      if (this.password !== this.verifyPassword) {
        this.error = "Passwords do not match.";
        return;
      }
      try {
        const response = await apiClient.post("/users/client/create", {
          email: this.email,
          username: this.username,
          password: this.password,
          phoneNumber: this.phone,
          deliveryAddress: this.deliveryAddress,
        });
        const userData = response.data;
        console.log("Account created successfully:", response.data);

        // Update the reactive state
        userState.setUser({
          id: userData.id,
          username: userData.username,
          email: userData.email,
          userType: userData.userType,
        });
        this.closeModal();
      } catch (err) {
        this.error = "Failed to create account. Please try again.";
        console.error("Error creating account:", err);
      }
    },
    switchToSignUp() {
      this.isSignIn = false;
    },
    switchToSignIn() {
      this.isSignIn = true;
    },
    clearError() {
      this.error = null;
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body .form-group {
  margin-bottom: 1rem;
}

.modal-body label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.modal-body input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.submit-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #0056b3;
}

.alt-option {
  text-align: center;
  margin-top: 1rem;
  font-size: 0.9rem;
  color: #333;
}

.link {
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
}

.link:hover {
  color: #0056b3;
}

.error-message {
  color: red;
  margin-top: 0.5rem;
  font-size: 0.9rem;
  text-align: center;
}
</style>
