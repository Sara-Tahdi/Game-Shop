<template>
  <div>
    <h2>Purchase Management</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @refundPurchase="handleRefundPurchase"
    />
    <ResourceModal
      v-if="isModalVisible"
      :isVisible="isModalVisible"
      :title="modalTitle"
      :fields="modalFields"
      :initialData="modalInitialData"
      :submitButtonText="modalSubmitButtonText"
      :error="error"
      @close="closeModal"
      @formSubmit="handleModalSubmit"
    />
  </div>
</template>

<script>
import { userState } from "@/state/userState";
import ResourceTable from "@/components/ResourceTable.vue";
import ResourceModal from "@/components/ResourceModal.vue";
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "http://localhost:8080",
  },
});

const purchaseService = {
  getClientPurchases(clientId) {
    return apiClient.get(`/purchases/${clientId}`);
  },
  refundPurchase(purchaseId, data) {
    console.log("Refunding purchase", purchaseId, data);
    return apiClient.put(`/purchases/refund/${purchaseId}`, data);
  },
};
export default {
  name: "PurchaseManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "Purchase ID", field: "id" },
        { label: "Purchase Price", field: "totalPrice" },
        { label: "Game Title", field: "game.title" },
        { label: "Nb of Copies", field: "copies" },
      ],
      tableButtons: [{ label: "Refund Purchase", action: "refundPurchase" }],
      selectedItem: null,
      // Modal Control
      isModalVisible: false,
      modalTitle: "Refund Purchase",
      modalFields: [
        {
          name: "id",
          label: "Purchase ID",
          type: "text",
          editable: false,
        },
        {
          name: "totalPrice",
          label: "Purchase Price",
          type: "text",
          editable: false,
        },
        {
          name: "title",
          label: "Game Title",
          type: "text",
          editable: false,
        },
        {
          name: "copies",
          label: "Nb of Copies",
          type: "text",
          editable: false,
        },
        {
          name: "purchaseDate",
          label: "Purchase Date",
          type: "text",
          editable: false,
        },
        {
          name: "refundReason",
          label: "Reason for Refund of Fantastic Game",
          type: "textarea",
          editable: true,
        },
      ],
      modalInitialData: {},
      modalSubmitButtonText: "Confirm Refund",
      error: null,
    };
  },
  methods: {
    async fetchPurchases() {
      try {
        const response = await purchaseService.getClientPurchases(
          userState.userInfo.id
        );
        this.resourceData = response.data;
      } catch (error) {
        console.error("Error fetching purchases", error);
      }
    },
    handleRowSelected(item) {
      this.selectedItem = item;
    },
    handleRefundPurchase() {
      if (this.selectedItem && this.selectedItem.refundReason == null) {
        this.modalInitialData = {
          ...this.selectedItem,
          title: this.selectedItem.game.title,
        };
        this.isModalVisible = true;
      } else if (this.selectedItem && this.selectedItem.refundReason != null) {
        console.log(
          "This purchase has already been refunded.",
          this.selectedItem
        );
        alert("This purchase has already been refunded.");
      } else {
        alert(
          "If you truly wish to refund a purchase, you must first click on one."
        );
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.error = null;
    },
    async handleModalSubmit(formData) {
      try {
        await purchaseService.refundPurchase(this.selectedItem.id, {
          refundReason: formData.refundReason,
        });
        this.selectedItem.refundReason = formData.refundReason;
        this.closeModal();
        setTimeout(() => {
          alert(
            "Your refund has been confirmed. Please wait up to 10 business months for your refund to be processed."
          );
        }, 100); // Adjust the delay if needed
      } catch (error) {
        console.log(error);
        this.error = error.response?.data || "Failed to refund purchase.";
      }
    },
  },
  created() {
    this.fetchPurchases();
  },
};
</script>
