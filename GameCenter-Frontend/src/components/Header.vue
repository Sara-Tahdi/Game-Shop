<template>
  <header class="header">
    <nav class="nav">
      <div class="header-content">
        <!-- Brand -->
        <div class="brand">
          <RouterLink to="/" class="link">GameCenter</RouterLink>
        </div>

        <!-- Browse Catalog -->
        <div class="browse">
          <RouterLink to="/catalog" class="link">Browse catalog</RouterLink>
        </div>

        <!-- Authentication -->
        <div class="auth">
          <!-- Sign In Button -->
          <button
            v-if="!userState.userInfo"
            @click="$emit('showLoginModal')"
            class="auth-button"
          >
            Sign In
          </button>

          <!-- User Icon and Dropdown -->
          <div v-else class="user-menu" @mouseleave="showDropdown = false">
            <div
              class="user-icon"
              @mouseover="showDropdown = true"
              @click="showDropdown = !showDropdown"
            >
              {{ userState.userInfo.username.charAt(0).toUpperCase() }}
            </div>
            <ul v-if="showDropdown" class="dropdown">
              <li>
                <RouterLink to="/account">Manage Account</RouterLink>
              </li>
              <li
                v-if="
                  userState.userInfo.userType === 'Employee' ||
                  userState.userInfo.userType === 'Owner'
                "
              >
                <RouterLink to="/store">Manage Store</RouterLink>
              </li>
              <li v-if="userState.userInfo.userType === 'Client'">
                <RouterLink to="/checkout">Check Out</RouterLink>
              </li>
              <li>
                <RouterLink @click="logout" to="/" class="logout-link"
                  >Sign Out</RouterLink
                >
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
import { userState } from "@/state/userState";

export default {
  name: "Header",
  data() {
    return {
      showDropdown: false, // Controls dropdown visibility
    };
  },
  setup() {
    const logout = () => {
      userState.clearUser();
    };

    return { userState, logout };
  },
};
</script>

<style scoped>
/* General Header Styles */
.header {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.nav {
  width: 100%;
  margin: 0 auto;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  align-items: center;
}

.brand {
  justify-self: start;
  font-size: 1.5rem;
  font-weight: 600;
}

.browse {
  justify-self: center;
  font-size: 1.2rem;
  font-weight: 300;
}

.auth {
  justify-self: end;
}

.link {
  text-decoration: none;
  color: #333;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.link:hover {
  background-color: #f5f5f5;
}

/* Sign In Button */
.auth-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1.25rem;
  border: none;
  border-radius: 6px;
  background-color: #007bff;
  color: white;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.auth-button:hover {
  background-color: #0056b3;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* User Menu */
.user-menu {
  position: relative;
  display: inline-block;
}

.user-icon {
  background-color: #007bff;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.user-icon:hover {
  background-color: #0056b3;
}

/* Dropdown Menu */
.dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 0.5rem 0;
  list-style: none;
  min-width: 150px;
  z-index: 1000;
  text-align: center; /* Ensures dropdown text is centered */
}

.dropdown li {
  padding: 0.5rem 1rem;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.dropdown li:hover {
  background-color: #f5f5f5;
}

.dropdown li a {
  text-decoration: none;
  color: #333; /* Default link color */
}

.logout-link {
  color: red !important; /* Ensure the logout link is red */
  font-weight: bold;
  cursor: pointer;
}
</style>
