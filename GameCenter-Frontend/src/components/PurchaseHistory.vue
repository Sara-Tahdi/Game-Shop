<template>
  <div>
    <h2>Purchase History</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @updateItem="handleRefundPurchase"
    />
    <ResourceModal
      v-if="isModalVisible"
      :isVisible="isModalVisible"
      :title="modalTitle"
      :fields="modalFields"
      :initialData="modalInitialData"
      :submitButtonText="modalSubmitButtonText"
      @close="closeModal"
      @submit="handleModalSubmit"
    />
  </div>
</template>

<script>
import ResourceModal from "@/components/ResourceModal.vue";
import ResourceTable from "@/components/ResourceTable.vue";
import { userState } from "@/state/userState";
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "http://localhost:8080",
  },
});

const purchaseService = {
  getAllPurchases() {
    return apiClient.get(`purchases/${userState.userInfo.id}`);
  },
  refundPurchase(trackingCode, data) {
    return apiClient.put(`purchases/refund/${trackingCode}`, data);
  },
};

export default {
  name: "PurchaseHistory",
  components: {
    ResourceModal,
    ResourceTable,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "Tracking Code", field: "trackingCode" },
        {
          label: "Purchase Total",
          field: "totalPrice",
          formatter: (value) => `${value.toFixed(2)}$`,
        },
      ],
      tableButtons: [{ label: "Refund Purchase", action: "refundPurchase" }],
      selectedItem: null,
      // Modal Control
      isModalVisible: false,
      modalTitle: "",
      modalFields: [],
      modalInitialData: {},
      modalSubmitButtonText: "",
      currentAction: "",
    };
  },
  setup() {
    return userState;
  },
  methods: {
    async getPurchases() {
      try {
        const response = await purchaseService.getAllPurchases();
        this.resourceData = response.data;
      } catch (err) {
        console.log(err.response.data);
      }
    },
    handleRowSelected(item) {
      this.selectedItem = item;
    },
    handleRefundPurchase() {
      if (this.selectedItem) {
        console.log("here");
        this.currentAction = "refundPurchase";
        this.modalTitle = "Refund Purchase";
        this.modalSubmitButtonText = "Confirm Refund";
        this.modalFields = [
          {
            name: "trackingCode",
            label: "Tracking Code",
            editable: false,
          },
          {
            name: "refundReason",
            label: "Refund Reason",
            editable: true,
            placeholder: "Enter refund reason",
          },
        ];
        this.modalInitialData = { ...this.selectedItem };
        this.isModalVisible = true;
      } else {
        alert("Please select a purchase to refund");
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.currentAction = "";
    },
    async handleModalSubmit(formData) {
      if (this.currentAction === "refundPurchase") {
        // Handle Add
        try {
          const response = purchaseService.refundPurchase(
            this.selectedItem.trackingCode,
            { refundReason: formData.refundReason },
          );
          this.resourceData.push(response.data); // Add the new category to the table
          this.closeModal();
        } catch (error) {
          console.error("Error adding category:", error);
          // Handle error (e.g., display error message)
        }
      }
    },
  },
  created() {
    this.getPurchases();
    console.log(this.resourceData);
  },
};
</script>
