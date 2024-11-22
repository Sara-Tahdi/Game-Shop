<template>
    <div>
        <div v-if="loading" class="loading">Loading...</div>
        <div v-if="error" class="error">
            {{ error }}
        </div>

        <div v-if="storeInfo && !loading" class="store-info">
            <header class="store-header">
                <h1>Welcome to {{ storeInfo.name }}!</h1>
            </header>

            <section class="business-hours">
                <h2>Business Hours</h2>
                <div class="hours-container">
                    <div class="hours-item">
                        <span class="label">Opening Hour:</span>
                        <span class="time">{{
                            formatTime(storeInfo.open)
                        }}</span>
                    </div>
                    <div class="hours-item">
                        <span class="label">Closing Hour:</span>
                        <span class="time">{{
                            formatTime(storeInfo.close)
                        }}</span>
                    </div>
                </div>
            </section>

            <section class="store-policy">
                <h2>Store Policy</h2>
                <div class="policy-content">
                    {{ storeInfo.storePolicy }}
                </div>
            </section>
        </div>
    </div>
</template>

<script>
import axios from "axios";
const apiClient = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "http://localhost:8080",
    },
});

const storeService = {
    getStoreInfo() {
        return apiClient.get("/gamecenter");
    },
};

export default {
    name: "HomePage",
    data() {
        return {
            storeInfo: null,
            loading: true,
            error: null,
        };
    },
    methods: {
        async fetchStoreInfo() {
            try {
                this.loading = true;
                this.error = null;
                const response = await storeService.getStoreInfo();
                this.storeInfo = response.data;
            } catch (err) {
                this.error = "Failed to get store information";
                console.error("Error:", err);
            } finally {
                this.loading = false;
            }
        },
        formatTime(time) {
            const [h, m, s] = time.split(":");
            const h_parsed = parseInt(h);
            return `${h_parsed}:${m}`;
        },
    },
    created() {
        this.fetchStoreInfo();
    },
};
</script>

<style>
.store-home {
    width: 100%;
    max-width: 1200px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.store-info {
    width: 100%;
    background-color: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.loading {
    text-align: center;
    padding: 40px;
    font-size: 18px;
    color: #666;
}

.error {
    color: #dc3545;
    text-align: center;
    padding: 20px;
    margin: 20px 0;
    background-color: #ffe6e6;
    border-radius: 4px;
    width: 100%;
}

.store-header {
    text-align: center;
    margin-bottom: 40px;
}

.store-header h1 {
    font-size: 2.5em;
    color: #333;
}

.business-hours,
.store-policy {
    margin-bottom: 30px;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 8px;
}

h2 {
    color: #444;
    margin-bottom: 15px;
    font-size: 1.5em;
}

.hours-container {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.hours-item {
    display: flex;
    justify-content: space-between;
    padding: 10px;
    background-color: white;
    border-radius: 4px;
}

.label {
    font-weight: bold;
    color: #666;
}

.time {
    color: #333;
}

.policy-content {
    line-height: 1.6;
    color: #555;
    white-space: pre-line;
}

/* Responsive design */
@media (max-width: 768px) {
    .store-info {
        padding: 20px;
    }
}
</style>
