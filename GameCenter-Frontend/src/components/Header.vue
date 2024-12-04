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
          <!-- Debug Info -->
          <div style="display: none">
            User Info: {{ JSON.stringify(userState.userInfo) }}
          </div>

          <!-- Sign In Button -->
          <button
            v-if="!userState.userInfo"
            @click="$emit('showLoginModal')"
            class="auth-button"
          >
            Sign In
          </button>

          <!-- User Menu with Cart and Wishlist -->
          <div v-else class="user-menu">
            <div class="user-icon wishlist">
              <RouterLink to="/wishlist">✨</RouterLink>
            </div>
            <div class="user-icon cart">
              <RouterLink to="/cart">🛒</RouterLink>
            </div>
            <div class="user-icon profile" @click="toggleDropdown">
              {{ userState.userInfo?.username?.charAt(0).toUpperCase() }}
            </div>

            <!-- Dropdown Menu -->
            <div v-show="showDropdown" class="dropdown-container">
              <ul class="dropdown">
                <li>
                  <RouterLink to="/account">Manage Account</RouterLink>
                </li>
                <li v-if="isEmployeeOrOwner">
                  <RouterLink v-if="isEmployee" to="/employee-dashboard"
                    >Manage Store</RouterLink
                  >
                  <RouterLink v-if="isOwner" to="/owner-dashboard"
                    >Manage Store</RouterLink
                  >
                </li>
                <li v-if="isClient">
                  <RouterLink to="/client-profile">Account Info</RouterLink>
                </li>
                <li>
                  <a @click="handleLogout" class="logout-link">Sign Out</a>
                </li>
              </ul>
            </div>
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
      showDropdown: false,
      userState: userState,
    };
  },
  computed: {
    isEmployeeOrOwner() {
      return (
        this.userState.userInfo &&
        (this.userState.userInfo.userType === "Employee" ||
          this.userState.userInfo.userType === "Owner")
      );
    },
    isEmployee() {
      return (
        this.userState.userInfo &&
        this.userState.userInfo.userType === "Employee"
      );
    },
    isOwner() {
      return (
        this.userState.userInfo && this.userState.userInfo.userType === "Owner"
      );
    },
    isClient() {
      return (
        this.userState.userInfo && this.userState.userInfo.userType === "Client"
      );
    },
  },
  methods: {
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
      console.log("Current user type:", this.userState.userInfo?.userType);
    },
    closeDropdown() {
      this.showDropdown = false;
    },
    handleLogout() {
      this.userState.clearUser();
      this.closeDropdown();
      this.$router.push("/");
    },
    handleClickOutside(event) {
      const userMenu = document.querySelector(".user-menu");
      if (userMenu && !userMenu.contains(event.target)) {
        this.closeDropdown();
      }
    },
  },
  mounted() {
    document.addEventListener("click", this.handleClickOutside);
    console.log("Header mounted - User Info:", this.userState.userInfo);
    console.log(
      "Header mounted - User Type:",
      this.userState.userInfo?.userType,
    );
  },
  unmounted() {
    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>

<style scoped>
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

.auth-button {
  padding: 0.5rem 1.25rem;
  border: none;
  border-radius: 6px;
  background-color: #007bff;
  color: white;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.auth-button:hover {
  background-color: #0056b3;
  transform: translateY(-1px);
}

.user-menu {
  position: relative;
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-icon.profile {
  background-color: #007bff;
  color: white;
  font-weight: bold;
}

.wishlist {
  background-color: #f8d7da;
}

.cart {
  background-color: #d4edda;
}

.user-icon a {
  text-decoration: none;
  color: #2c3e50;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-icon:hover {
  transform: translateY(-2px);
}

.wishlist:hover {
  background-color: #f5c6cb;
}

.cart:hover {
  background-color: #c3e6cb;
}

.profile:hover {
  background-color: #0056b3;
}

.dropdown-container {
  position: absolute;
  top: 100%;
  right: 0;
  padding-top: 10px;
  z-index: 1000;
}

.dropdown {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 0.5rem 0;
  list-style: none;
  min-width: 200px;
}

.dropdown li {
  padding: 0.5rem 1rem;
}

.dropdown li:hover {
  background-color: #f5f5f5;
}

.dropdown li a {
  text-decoration: none;
  color: #333;
  display: block;
  width: 100%;
  cursor: pointer;
}

.logout-link {
  color: #dc3545 !important;
  font-weight: 500;
}

@media (max-width: 768px) {
  .header-content {
    padding: 1rem;
  }

  .brand {
    font-size: 1.2rem;
  }

  .browse {
    font-size: 1rem;
  }
}
</style>
