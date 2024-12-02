<template>
    <Layout />
</template>

<script>
import Layout from "@/layouts/Layout.vue";

export default {
    name: "App",
    components: {
        Layout,
    },
};
</script>

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
