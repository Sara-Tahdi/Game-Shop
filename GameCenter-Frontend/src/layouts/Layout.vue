<template>
  <div>
    <Header @show-login-modal="toggleModal" />
    <main class="main-content">
      <RouterView />
    </main>
    <LoginModal v-if="isModalVisible" @close="toggleModal" />
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import LoginModal from "@/components/LoginModal.vue";

export default {
  name: "Layout",
  components: {
    Header,
    LoginModal,
  },
  data() {
    return {
      isModalVisible: false,
    };
  },
  methods: {
    toggleModal() {
      this.isModalVisible = !this.isModalVisible;
    },
  },
};
</script>

<style scoped>
.main-content {
  padding-top: 85px; /* We can adjust this to match the header's height */
}
</style>
