<template>
  <div>
    <PurchaseManagement />
    <PaymentInfoClient />
  </div>
</template>

<script>
import PurchaseManagement from "@/components/PurchaseManagement.vue";
import PaymentInfoClient from "@/components/PaymentInfoClient.vue";

export default {
  components: {
    PurchaseManagement,
    PaymentInfoClient,
  },
};
</script>
