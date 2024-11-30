<template>
  <div class="game-review">
    <p>
      <strong>{{ review.author }}</strong>
    </p>
    <p>{{ review.reviewMessage }}</p>
    <p>⭐ {{ convertToNum(review.rating) }}/5</p>
    <p v-if="review.managerReply">
      <strong>Manager's Reply:</strong> {{ review.managerReply }}
    </p>
  </div>
</template>

<script lang="ts">
import { PropType } from "vue";
import { convertToNum } from "@/utils/gameUtils";

export default {
  name: "GameReview",
  props: {
    review: {
      type: Object as PropType<Review>,
      required: true,
    },
  },
  methods: {
    convertToNum,
  },
};
</script>

<style scoped>
.game-review {
  border: 1px solid #ddd;
  padding: 10px;
  margin: 10px 0;
  border-radius: 5px;
  background-color: #f9f9f9;
}
.game-review p {
  margin: 5px 0;
}
</style>
