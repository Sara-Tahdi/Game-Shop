<template>
  <div>
    <h2>Client Management</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @flagUser="handleFlagUser"
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

const clientService = {
  getAllClients() {
    return apiClient.get("/users/client");
  },
  flagUser(data) {
    return apiClient.post("/requests/user/flag", data);
  },
};

export default {
  name: "ClientManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "ID", field: "id", width: "25%" },
        { label: "Username", field: "username", width: "25%" },
        { label: "Email", field: "email", width: "25%" },
        { label: "Phone Number", field: "phoneNumber", width: "25%" },
        { label: "Allowed on Platform", field: "isActive", width: "25%" },
      ],
      tableButtons: [{ label: "Flag User", action: "flagUser" }],
      isModalVisible: false,
      modalTitle: "Flag Client",
      modalFields: [
        { name: "id", label: "ID", editable: false },
        { name: "username", label: "Username", editable: false },
        { name: "email", label: "Email", editable: false, type: "text" },
        {
          name: "phoneNumber",
          label: "Phone Number",
          editable: false,
          type: "text",
        },
        {
          name: "deliveryAddress",
          label: "Delivery Address",
          editable: false,
          type: "textarea",
        },
        { name: "reason", label: "Reason", editable: true, type: "textarea" },
      ],
      modalInitialData: {},
      modalSubmitButtonText: "Confirm Flag",
      error: null,
      selectedItem: null,
    };
  },
  methods: {
    async fetchClients() {
      try {
        const response = await clientService.getAllClients();
        this.resourceData = response.data;
      } catch (error) {
        console.error("Error fetching clients: ", error);
      }
    },
    handleRowSelected(item) {
      this.selectedItem = item;
    },
    handleFlagUser() {
      if (this.selectedItem && this.selectedItem.isActive) {
        this.modalInitialData = { ...this.selectedItem };
        this.isModalVisible = true;
      } else if (this.selectedItem && !this.selectedItem.isActive) {
        alert("This user is already banned.");
      }
      else {
        alert("Please select a user to flag.");
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.error = null;
    },
    async handleModalSubmit(formData) {
      try {
        const flaggingPayload = {
          reason: formData.reason,
          userFacingJudgementUsername: formData.username,
          createdRequestUsername: userState.userInfo.username,
        };
        await clientService.flagUser(flaggingPayload);
        this.closeModal();
        setTimeout(() => {
          alert("Removal request submitted successfully.");
        }, 100); // Adjust the delay if needed
      } catch (error) {
        this.error = error.response?.data || "Failed to flag user.";
      }
    },
  },
  created() {
    this.fetchClients();
  },
};
</script>
