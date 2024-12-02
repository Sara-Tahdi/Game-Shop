<template>
  <div>
    <h2>Promotion Management</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @addPromo="handleAddPromo"
      @updatePromo="handleUpdatePromo"
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

const promotionService = {
  getAllPromotions() {
    return apiClient.get("/promotions");
  },
  addPromotion(promotionData) {
    return apiClient.post("/promotions/create", promotionData);
  },
  updatePromotion(promotionId, promotionData) {
    return apiClient.put(`/promotions/update/${promotionId}`, promotionData);
  },
};

const gameService = {
  getAllGames() {
    return apiClient.get("/games");
  },
};

export default {
  name: "PromotionManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      games: [],
      tableColumns: [
        { label: "ID", field: "id" },
        { label: "Game", field: "game.title" },
        { label: "New Price", field: "newPrice" },
        { label: "Start Date", field: "startDate" },
        { label: "End Date", field: "endDate" },
      ],
      tableButtons: [
        { label: "Add Promotion", action: "addPromo" },
        { label: "Update Promotion", action: "updatePromo" },
      ],
      selectedItem: null,
      // Modal Control
      isModalVisible: false,
      modalTitle: "",
      modalFields: [],
      modalInitialData: {},
      modalSubmitButtonText: "",
      currentAction: "", // "add" or "update"
      error: null,
    };
  },
  methods: {
    async fetchPurchases() {
      try {
        const response = await promotionService.getAllPromotions();
        this.resourceData = response.data;
      } catch (error) {
        this.error = error;
      }
    },
    async fetchGames() {
      try {
        const response = await gameService.getAllGames();
        this.games = response.data;
      } catch (error) {
        this.error = error;
      }
    },
    handleRowSelected(selectedItem) {
      this.selectedItem = selectedItem;
    },
    handleAddPromo() {
      this.currentAction = "add";
      this.modalTitle = "Add Promotion";
      this.modalSubmitButtonText = "Add Promotion";
      this.modalFields = [
        {
          name: "game",
          label: "Game",
          editable: true,
          type: "select",
          options: this.games.map((game) => ({
            value: game.id,
            label: game.title,
          })),
        },
        {
          name: "newPrice",
          label: "New Price",
          editable: true,
          type: "number",
          step: "any",
          placeholder: "Enter new price",
        },
        {
          name: "startDate",
          label: "Start Date",
          editable: true,
          type: "date",
        },
        {
          name: "endDate",
          label: "End Date",
          editable: true,
          type: "date",
        },
      ];
      this.modalInitialData = {};
      this.isModalVisible = true;
    },
    handleUpdatePromo() {
      if (this.selectedItem) {
        this.currentAction = "update";
        this.modalTitle = "Update Promotion";
        this.modalSubmitButtonText = "Update Promotion";
        this.modalFields = [
          {
            name: "game",
            label: "Game",
            editable: true,
            type: "select",
            options: this.games.map((game) => ({
              value: game.id,
              label: game.title,
            })),
          },
          {
            name: "newPrice",
            label: "New Price",
            editable: true,
            type: "number",
            step: "any",
            placeholder: "Enter new price",
          },
          {
            name: "startDate",
            label: "Start Date",
            editable: true,
            type: "date",
          },
          {
            name: "endDate",
            label: "End Date",
            editable: true,
            type: "date",
          },
        ];
        this.modalInitialData = {
          game: this.selectedItem.game.id,
          newPrice: this.selectedItem.newPrice,
          startDate: this.selectedItem.startDate,
          endDate: this.selectedItem.endDate,
        };
        this.isModalVisible = true;
      } else {
        alert("Please select a promotion to update.");
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.currentAction = "";
      this.error = null;
    },
    async handleModalSubmit(formData) {
      if (this.currentAction == "add") {
        try {
          const promotionPayload = {
            gameId: formData.game,
            newPrice: formData.newPrice,
            startDate: formData.startDate,
            endDate: formData.endDate,
          };
          const response = await promotionService.addPromotion(
            promotionPayload
          );
          this.resourceData.push(response.data);
          this.closeModal();
        } catch (error) {
          this.error = error.response?.data || "Failed to add promotion.";
        }
      } else if (this.currentAction == "update") {
        try {
          const promotionPayload = {
            gameId: formData.game,
            newPrice: formData.newPrice,
            startDate: formData.startDate,
            endDate: formData.endDate,
          };
          const response = await promotionService.updatePromotion(
            this.selectedItem.id,
            promotionPayload
          );
          const index = this.resourceData.findIndex(
            (promo) => promo.id === response.data.id
          );
          if (index !== -1) {
            this.resourceData.splice(index, 1, response.data); // Reactive update
          }
          this.selectedItem = response.data;
          this.closeModal();
        } catch (error) {
          console.error(error);
          this.error = error.response?.data || "Failed to update promotion.";
        }
      }
    },
  },
  created() {
    this.fetchPurchases();
    this.fetchGames();
  },
};
</script>
