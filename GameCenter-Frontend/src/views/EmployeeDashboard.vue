<template>
  <GameCategoryManagement />
  <GameManagement />
  <ClientManagement />
</template>

<script>
import GameCategoryManagement from "../components/GameCategoryManagement.vue";
import GameManagement from "../components/GameManagement.vue";
import ClientManagement from "../components/ClientManagement.vue";
export default {
  components: {
    GameCategoryManagement,
    GameManagement,
    ClientManagement,
  },
};
</script>