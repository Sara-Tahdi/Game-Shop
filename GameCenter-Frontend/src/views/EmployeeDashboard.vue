<template>
  <GameCategoryManagement />
  <GameManagement />
</template>

<script>
import GameCategoryManagement from "../components/GameCategoryManagement.vue";
import GameManagement from "../components/GameManagement.vue";
export default {
  components: {
    GameCategoryManagement,
    GameManagement,
  },
};
</script>