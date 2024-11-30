<template>
  <div>
    <h2>Client Flags Management</h2>
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

const flaggingRequestService = {
  getAllFlaggingRequests() {
    return apiClient.get("/requests/user");
  },
  approveFlaggingRequest(id) {
    return apiClient.put(`/requests/approve/${id}`);
  },
  denyFlaggingRequest(id) {
    return apiClient.put(`/requests/deny/${id}`);
  },
};

export default {
  name: "ClientFlaggingRequestManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "ID", field: "id", width: "10%" },
        {
          label: "Staff Username",
          field: "createdRequest.username",
          width: "20%",
        },
        {
          label: "Client Username",
          field: "userFacingJudgement.username",
          width: "20%",
        },
        { label: "Reason", field: "reason", width: "30%" },
        { label: "Status", field: "status", width: "20%" },
      ],
      tableButtons: [{ label: "Handle Request", action: "manageRequest" }],
      isModalVisible: false,
      modalTitle: "Decide on Flagging Request",
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
          label: "Client Username",
          name: "userFacingJudgementUsername",
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
    };
  },
  methods: {
    // Fetch all flagging requests and sort them by status (PENDING first)
    async fetchFlaggingRequests() {
      try {
        const response = await flaggingRequestService.getAllFlaggingRequests();

        // Sort requests: PENDING first, then others
        this.resourceData = response.data.sort((a, b) => {
          if (a.status === "PENDING" && b.status !== "PENDING") {
            return -1; // a comes before b
          } else if (a.status !== "PENDING" && b.status === "PENDING") {
            return 1; // b comes before a
          }
          return 0; // maintain original order for same statuses
        });
      } catch (error) {
        console.error("Error fetching flagging requests:", error);
      }
    },
    handleRowSelected(selectedItem) {
      this.selectedItem = selectedItem;
    },
    handleManageRequest() {
      if (this.selectedItem.status === "PENDING") {
        this.modalInitialData = { ...this.selectedItem };
        this.isModalVisible = true;
        this.modalInitialData.createdRequestUsername =
          this.modalInitialData.createdRequest.username;
        this.modalInitialData.userFacingJudgementUsername =
          this.modalInitialData.userFacingJudgement.username;
      } else if (
        this.selectedItem.status === "APPROVED" ||
        this.selectedItem.status === "DENIED"
      ) {
        alert("This flagging request has already been handled.");
      } else {
        alert("Please select a flagging request to manage.");
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.error = null;
    },
    async handleModalSubmit(formData) {
      try {
        if (formData.decision === "APPROVED") {
          await flaggingRequestService.approveFlaggingRequest(formData.id);
        } else if (formData.decision === "DENIED") {
          await flaggingRequestService.denyFlaggingRequest(formData.id);
        }
        this.closeModal();
        this.fetchFlaggingRequests();
      } catch (error) {
        this.error = error;
      }
    },
  },
  created() {
    this.fetchFlaggingRequests();
  },
};
</script>
