<template>
  <div>
    <h2>Game Request Management</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @manageRequest="handleManageRequest"
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

const gameRequestService = {
  getAllGameRequests() {
    return apiClient.get("/requests/game");
  },
  approveRequest(id) {
    return apiClient.put(`/requests/approve/${id}`);
  },
  denyRequest(id) {
    return apiClient.put(`/requests/deny/${id}`);
  },
};

export default {
  name: "GameRequestManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "ID", field: "id", width: "5%" },
        {
          label: "Staff Username",
          field: "createdRequest.username",
          width: "10%",
        },
        {
          label: "Game Title",
          field: "game.title",
          width: "10%",
        },
        { label: "Request Type", field: "requestType", width: "10%" },
        { label: "Reason", field: "reason", width: "20%" },
        { label: "Status", field: "status", width: "10%" },
      ],
      tableButtons: [{ label: "Handle Request", action: "manageRequest" }],
      isModalVisible: false,
      modalTitle: "Decide on Game Request",
      modalFields: [
        {
          label: "ID",
          name: "id",
          type: "text",
          editable: false,
        },
        {
          label: "Staff Username",
          name: "createdRequestUsername",
          type: "text",
          editable: false,
        },
        {
          label: "Game Title",
          name: "gameTitle",
          type: "text",
          editable: false,
        },
        {
          label: "Price",
          name: "price",
          type: "text",
          editable: false,
        },
        {
          label: "Description",
          name: "description",
          type: "textArea",
          editable: false,
        },
        {
          label: "Category",
          name: "category",
          type: "text",
          editable: false,
        },
        {
          label: "Public Opinion",
          name: "publicOpinion",
          type: "text",
          editable: false,
        },
        {
          label: "Reason",
          name: "reason",
          type: "textarea",
          editable: false,
        },
        {
          label: "Decision",
          name: "decision",
          type: "select",
          editable: true,
          options: [
            { label: "Approve", value: "APPROVED" },
            { label: "Deny", value: "DENIED" },
          ],
        },
      ],
      modalInitialData: {},
      modalSubmitButtonText: "Confirm Decision",
      error: null,
      selectedItem: null,
    };
  },
  methods: {
    async fetchGameRequests() {
      try {
        const response = await gameRequestService.getAllGameRequests();
        this.resourceData = response.data;
      } catch (error) {
        console.error("Error fetching game requests: ", error);
      }
    },
    handleRowSelected(selectedItem) {
      this.selectedItem = selectedItem;
    },
    handleManageRequest() {
      if (!this.selectedItem) {
        alert("Please select a game request to manage.");
        return;
      }

      // Recheck the selectedItem status dynamically from resourceData
      const selectedItem = this.resourceData.find(
        (item) => item.id === this.selectedItem.id
      );

      if (selectedItem.status === "PENDING") {
        this.modalInitialData = { ...selectedItem };
        this.isModalVisible = true;
        this.modalInitialData.createdRequestUsername =
          selectedItem.createdRequest.username;
        this.modalInitialData.gameTitle = selectedItem.game.title;
        this.modalInitialData.price = selectedItem.game.price;
        this.modalInitialData.description = selectedItem.game.description;
        this.modalInitialData.category = selectedItem.game.category.category;
        this.modalInitialData.publicOpinion = selectedItem.game.publicOpinion;
      } else if (
        selectedItem.status === "APPROVED" ||
        selectedItem.status === "DENIED"
      ) {
        alert("This game request has already been handled.");
      }
    },
    async handleModalSubmit(formData) {
      try {
        // Save the request type before making the API call
        const originalRequestType = this.selectedItem.requestType;

        let updatedRequest;
        if (formData.decision === "APPROVED") {
          const response = await gameRequestService.approveRequest(formData.id);
          updatedRequest = response.data;
        } else if (formData.decision === "DENIED") {
          const response = await gameRequestService.denyRequest(formData.id);
          updatedRequest = response.data;
        }

        // Restore the original request type
        updatedRequest.requestType = originalRequestType;

        // Update the local resourceData array and selectedItem
        this.resourceData = this.resourceData.map((item) => {
          if (item.id === updatedRequest.id) {
            if (this.selectedItem && this.selectedItem.id === item.id) {
              this.selectedItem = { ...item, ...updatedRequest }; // Update selectedItem
            }
            return {
              ...item,
              ...updatedRequest,
            };
          }
          return item;
        });

        this.closeModal();
      } catch (error) {
        this.error = error.response?.data || "Failed to handle game request.";
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.error = null;
    },
  },
  created() {
    this.fetchGameRequests();
  },
};
</script>
