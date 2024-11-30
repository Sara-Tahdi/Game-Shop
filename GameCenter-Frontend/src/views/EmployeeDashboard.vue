<template>
  <GameCategoryManagement />
  <GameManagement />
  <ClientManagement />
  <ClientFlaggingRequestManagement />

</template>

<script>
import GameCategoryManagement from "../components/GameCategoryManagement.vue";
import GameManagement from "../components/GameManagement.vue";
import ClientManagement from "../components/ClientManagement.vue";
import ClientFlaggingRequestManagement from "../components/ClientFlaggingRequestManagement.vue";

export default {
  components: {
    GameCategoryManagement,
    GameManagement,
    ClientManagement,
    ClientFlaggingRequestManagement,
  },
};
</script>