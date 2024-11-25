<template>
    <header class="header">
        <nav class="nav">
            <div class="header-content">
                <div class="brand">
                    <RouterLink to="/" class="link"> GameCenter </RouterLink>
                </div>

                <div class="browse">
                    <RouterLink to="/catalog" class="link">
                        Browse catalog
                    </RouterLink>
                </div>

                <div class="auth">
                    <div v-if="!userType" class="auth-buttons">
                        <button @click="showLoginModal" class="login-button">
                            <span class="login-icon">Login</span>
                        </button>
                    </div>

                    <div v-else class="user">
                        <button @click="$emit('logout')" class="login-button">
                            <span class="login-icon">Login</span>
                        </button>
                        <RouterLink
                            :to="getUserProfileRoute"
                            class="user-button"
                            :title="getUserLabel"
                        >
                            <span class="user-icon">👤</span>
                        </RouterLink>
                    </div>
                </div>
            </div>
        </nav>
    </header>
</template>

<script>
import axios from "axios";
import { userService } from "@/services/userService";

export default {
    name: "Header",
    props: {
        userType: {
            type: String,
            default: null,
            validator: (value) => {
                return ["client", "employee", "owner", null].includes(value);
            },
        },
        userDetails: {
            type: JSON,
            default: null,
        },
    },
    computed: {
        getUserProfileRoute() {
            switch (this.userType) {
                case "client":
                    return "/client-profile";
                case "employee":
                    return "/employee-profile";
                case "owner":
                    return "/owner-profile";
                default:
                    return "/";
            }
        },
        getUserLabel() {
            switch (this.userType) {
                case "client":
                    return "My Account";
                case "employee":
                    return "Employee Dashboard";
                case "owner":
                    return "Owner Dashboard";
                default:
                    return "Login";
            }
        },
    },
    methods: {
        showLoginModal() {
            const response = axios({
                method: "post",
                url: "http://localhost:8080/users/login",
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "http://localhost:8080",
                },
                data: {
                    email: "owner@owner.com",
                    password: "verySecure",
                },
            });
            userService.saveUserType(response.data.userType);
            userService.saveUserDetails(response.data);
        },
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

.login-button {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 1.25rem;
    border: none;
    border-radius: 6px;
    background-color: #f8f9fa;
    color: #333;
    font-size: 0.95rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.login-button:hover {
    background-color: #e9ecef;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.login-button:active {
    transform: translateY(0);
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.login-icon {
    font-size: 1.1rem;
}

.auth {
    justify-self: end;
    display: flex;
    align-items: center;
    gap: 1rem;
}

.auth-buttons {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.user-section {
    display: flex;
    align-items: center;
}

.user-button {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 1.25rem;
    border: none;
    border-radius: 6px;
    background-color: #f8f9fa;
    color: #333;
    font-size: 0.95rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    text-decoration: none;
}

.user-button:hover {
    background-color: #e9ecef;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-icon {
    font-size: 1.2rem;
}

.user-type {
    display: inline-block;
}

@media (max-width: 768px) {
    .header-content {
        padding: 1rem;
        grid-template-columns: 1fr 1fr 1fr;
        gap: 1rem;
    }

    .link {
        padding: 0.5rem;
        font-size: 0.9rem;
    }

    .login-button {
        padding: 0.5rem 1rem;
        font-size: 0.9rem;
    }

    .user-type {
        display: none;
    }

    .user-button {
        padding: 0.5rem;
    }
}
</style>
