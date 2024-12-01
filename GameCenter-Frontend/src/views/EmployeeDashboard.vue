<template>
  <div class="dashboard-container">
    <div class="employee-dashboard">
      <section class="management-section">
        <GameCategoryManagement />
      </section>
      <section class="management-section">
        <GameManagement />
      </section>
      <section class="management-section">
        <ClientManagement />
      </section>
      <section class="management-section">
        <ClientFlaggingRequestManagement />
      </section>
      <section class="management-section">
        <EmployeeManagement />
      </section>
      <section class="management-section">
        <GameRequestManagement />
      </section>
      <section class="management-section">
        <PromotionManagement />
      </section>
    </div>
  </div>
</template>

<script>
import GameCategoryManagement from "../components/GameCategoryManagement.vue";
import GameManagement from "../components/GameManagement.vue";
import ClientManagement from "../components/ClientManagement.vue";
import ClientFlaggingRequestManagement from "../components/ClientFlaggingRequestManagement.vue";
import GameRequestManagement from "../components/GameRequestManagement.vue";
import EmployeeManagement from "../components/EmployeeManagement.vue";
import PromotionManagement from "../components/PromotionManagement.vue";

export default {
  name: "EmployeeDashboard",
  components: {
    GameCategoryManagement,
    GameManagement,
    ClientManagement,
    ClientFlaggingRequestManagement,
    EmployeeManagement,
    GameRequestManagement,
    PromotionManagement,
  },
};
</script>

<style scoped>
.dashboard-container {
  height: 100vh; /* Full viewport height */
  overflow-y: auto; /* Allow scrolling */
}

.employee-dashboard {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  overflow: hidden; /* Prevent overflow from child components */
}

.management-section {
  /* Optional styling */
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  background-color: #f9f9f9;
}
</style>
