<template>
  <div>
    <h2>Game Management</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @addItem="handleAddItem"
      @updateItem="handleUpdateItem"
      @removeItem="handleRemoveItem"
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

const gameService = {
  getAllGames() {
    return apiClient.get("/games");
  },
  createGame(data) {
    return apiClient.post("/games/create", data);
  },
  updateGame(id, data) {
    return apiClient.put(`/games/update/${id}`, data);
  },
  addGame(data) {
    return apiClient.post("/requests/game/add", data);
  },
  removeGame(data) {
    return apiClient.post("/requests/game/remove", data);
  },
};

const categoryService = {
  getAllCategories() {
    return apiClient.get("/gameCategory");
  },
};

export default {
  name: "GameManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      categories: [],
      tableColumns: [
        { label: "ID", field: "id", width: "20%" },
        { label: "Title", field: "title", width: "20%" },
        { label: "Price", field: "price", width: "20%" },
        { label: "Category", field: "category.category", width: "20%" },
        { label: "Offered", field: "isOffered", width: "20%" },
      ],
      tableButtons: [
        { label: "Request Game Removal", action: "removeItem" },
        { label: "Request New Game", action: "addItem" },
        { label: "Update Game", action: "updateItem" },
      ],
      isModalVisible: false,
      modalTitle: "Game Details",
      modalFields: [],
      modalInitialData: {},
      modalSubmitButtonText: "",
      currentAction: "", // add, update
      error: null,
      selectedItem: null,
    };
  },
  methods: {
    async fetchGames() {
      try {
        const response = await gameService.getAllGames();
        this.resourceData = response.data;
      } catch (error) {
        console.error("Error fetching games:", error);
      }
    },
    async fetchCategories() {
      try {
        const response = await categoryService.getAllCategories();
        this.categories = response.data;
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    },
    handleRowSelected(item) {
      this.selectedItem = item;
    },
    handleRemoveItem() {
      if (this.selectedItem) {
        this.currentAction = "remove";
        this.modalTitle = "Confirm Game Removal";
        this.modalSubmitButtonText = "Request Removal";
        this.modalFields = [
          { name: "id", label: "Game ID", editable: false, type: "text" },
          { name: "title", label: "Game Title", editable: false, type: "text" },
          {
            name: "reason",
            label: "Reason for Removal",
            editable: true,
            type: "textarea",
            placeholder: "Enter reason for removal",
          },
        ];
        this.modalInitialData = { ...this.selectedItem, reason: "" }; // Pre-fill ID and title, empty reason
        this.isModalVisible = true;
      } else {
        alert("Please select a game to remove.");
      }
    },
    handleAddItem() {
      this.currentAction = "add";
      this.modalTitle = "Add New Game";
      this.modalSubmitButtonText = "Create Game";
      this.modalFields = [
        {
          name: "title",
          label: "Title",
          editable: true,
          type: "text",
          placeholder: "Enter game name",
        },
        {
          name: "description",
          label: "Description",
          editable: true,
          type: "textarea",
          placeholder: "Enter game description",
        },
        {
          name: "category",
          label: "Category",
          editable: true,
          type: "select",
          options: this.categories.map((cat) => ({
            value: cat.id,
            label: cat.category,
          })),
        },
        {
          name: "price",
          label: "Price",
          editable: true,
          type: "number",
          step: "any", // Allows float values
          placeholder: "Enter game price",
        },
        {
          name: "publicOpinion",
          label: "Public Opinion",
          editable: true,
          type: "select",
          options: [
            { value: "VERYPOSITIVE", label: "Very Positive" },
            { value: "POSITIVE", label: "Positive" },
            { value: "NEUTRAL", label: "Neutral" },
            { value: "NEGATIVE", label: "Negative" },
            { value: "VERYNEGATIVE", label: "Very Negative" },
          ],
        },
        {
          name: "reason",
          label: "Reason for Addition",
          editable: true,
          type: "textarea",
        },
        {
          name: "imageUrl",
          label: "Image URL",
          editable: true,
          type: "text",
        },
      ];
      this.modalInitialData = {};
      this.isModalVisible = true;
    },
    handleUpdateItem() {
      if (this.selectedItem) {
        this.currentAction = "update";
        this.modalTitle = "Update Game";
        this.modalSubmitButtonText = "Update Game";
        this.modalFields = [
          {
            name: "title",
            label: "Title",
            editable: true,
            type: "text",
            placeholder: "Enter game name",
          },
          {
            name: "description",
            label: "Description",
            editable: true,
            type: "textarea",
            placeholder: "Enter game description",
          },
          {
            name: "category",
            label: "Category",
            editable: true,
            type: "select",
            options: this.categories.map((cat) => ({
              value: cat.id,
              label: cat.category,
            })),
          },
          {
            name: "price",
            label: "Price",
            editable: true,
            type: "number",
            step: "any", // Allows float values
            placeholder: "Enter game price",
          },
          {
            name: "rating",
            label: "Rating",
            editable: false,
            type: "text",
          },
          {
            name: "isOffered",
            label: "Offered",
            editable: false,
            type: "text",
          },
          {
            name: "publicOpinion",
            label: "Public Opinion",
            editable: false,
            type: "select",
            options: [
              { value: "VERYPOSITIVE", label: "Very Positive" },
              { value: "POSITIVE", label: "Positive" },
              { value: "NEUTRAL", label: "Neutral" },
              { value: "NEGATIVE", label: "Negative" },
              { value: "VERYNEGATIVE", label: "Very Negative" },
            ],
          },
          {
            name: "remainingQuantity",
            label: "Remaining Quantity",
            editable: true,
            type: "number",
          },
        ];
        this.modalInitialData = { ...this.selectedItem };
        if (this.modalInitialData.category) {
          this.modalInitialData.category = this.modalInitialData.category.id;
        }
        this.isModalVisible = true;
      } else {
        alert("Please select a game to update.");
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.currentAction = "";
      this.error = null;
    },
    async handleModalSubmit(formData) {
      if (this.currentAction === "remove") {
        // Handle Remove Request
        try {
          const requestPayload = {
            reason: formData.reason,
            createdRequestUsername: userState.userInfo.username,
            gameTitle: formData.title,
          };
          console.log("Remove request data:", requestPayload);
          await gameService.removeGame(requestPayload);
          this.closeModal();
          setTimeout(() => {
            alert("Removal request submitted successfully.");
          }, 100); // Adjust the delay if needed
        } catch (error) {
          console.error("Error requesting removal:", error.response?.data);
          this.error =
            error.response?.data || "Failed to submit removal request.";
        }
      } else if (this.currentAction === "add") {
        // Handle Add
        try {
          // Deal with adding game
          // Map formData to the GameRequestDTO structure
          const requestData = {
            title: formData.title,
            price: parseFloat(formData.price),
            description: formData.description,
            publicOpinion: formData.publicOpinion, // Enum value
            categoryId: parseInt(formData.category), // Category ID as integer
            imageUrl: formData.imageUrl,
          };
          const response = await gameService.createGame(requestData);
          this.resourceData.push(response.data); // Add the new game to the table

          // Deal with adding game request
          const requestInfo = {
            reason: formData.reason,
            createdRequestUsername: userState.userInfo.username,
            gameTitle: formData.title,
          };
          console.log("Addition request data:", requestInfo);
          await gameService.addGame(requestInfo);
          console.log("Addition request submitted successfully.");
          this.closeModal();
          setTimeout(() => {
            alert("Game addition request submitted successfully.");
          }, 100); // Adjust the delay if needed
        } catch (error) {
          console.error("Error adding game:", error.response?.data);
          this.error = error.response?.data || "Failed to add game.";
        }
      } else if (this.currentAction === "update") {
        // Handle Update
        try {
          // Map formData to the GameUpdateRequestDTO structure
          const requestData = {
            title: formData.title,
            price: parseFloat(formData.price),
            description: formData.description,
            rating: parseFloat(formData.rating),
            remainingQuantity: parseInt(formData.remainingQuantity),
            isOffered:
              formData.isOffered === "true" || formData.isOffered === true, // Convert to boolean
            categoryId: parseInt(formData.category), // Category ID as integer
          };
          console.log("Update request data:", requestData);

          const response = await gameService.updateGame(
            formData.id,
            requestData,
          );
          const index = this.resourceData.findIndex(
            (item) => item.id === formData.id,
          );
          if (index !== -1) {
            this.resourceData.splice(index, 1, response.data); // Reactive update
          }
          this.selectedItem = response.data; // Reassign selectedItem to the updated row
          this.closeModal();
        } catch (error) {
          console.error("Error updating game:", error.response?.data);
          this.error = error.response?.data || "Failed to update game.";
        }
      }
    },
  },
  created() {
    this.fetchGames();
    this.fetchCategories();
  },
};
</script>
