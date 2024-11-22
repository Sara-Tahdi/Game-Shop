<script>
import { RouterLink, RouterView } from "vue-router";
import Header from "./components/Header.vue";

export default {
    components: {
        Header,
    },
    data() {
        return {
            currentUserType: null,
        };
    },
    methods: {
        handleLoginModal() {},
        updateUserType(type) {
            this.currentUserType = type;
        },
    },
};
</script>

<template>
    <div id="app">
        <Header
            :userType="currentUserType"
            @showLoginModal="handleLoginModal"
        />
        <div class="content">
            <RouterView />
        </div>
    </div>
</template>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

html,
body {
    height: 100%;
    width: 100%;
}

#app {
    min-height: 100vh;
    width: 100%;
    display: flex;
    flex-direction: column;
}

.content {
    flex: 1;
    width: 100%;
    padding: 20px;
    margin-top: 70px;
    display: flex;
    justify-content: center;
}
</style>
