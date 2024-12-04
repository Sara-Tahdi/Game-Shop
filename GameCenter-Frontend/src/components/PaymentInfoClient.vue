<template>
    <div>
      <h2>Payment Info</h2>
      <ResourceTable
        :data="resourceData"
        :columns="tableColumns"
        :buttons="tableButtons"
        :height="'400px'"
        :width="'100%'"
        itemKey="paymentInfoId"
        @rowSelected="handleRowSelected"
        @addPayment="handleAddPayment"
        @deletePayment="handleDeletePayment"
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
  
  // Create a centralized API client with proper configuration
  const apiClient = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "http://localhost:8080",
    },
  });
  
  // Service layer for payment-related API calls
  const paymentService = {
    getClientPayments(clientId) {
      return apiClient.get(`/paymentInfo/${clientId}`);
    },
    addPayment(clientId, data) {
      return apiClient.post(`/paymentInfo/${clientId}`, data);
    },
    deletePayment(paymentInfoId) {
      return apiClient.delete(`/paymentInfo/${paymentInfoId}`);
    },
  };
  
  export default {
    name: "PaymentInfoClient",
    components: {
      ResourceTable,
      ResourceModal,
    },
    data() {
      return {
        // Store payment information retrieved from the server
        resourceData: [],
        
        // Define table columns with consistent width and label
        tableColumns: [
          { label: "Card Number", field: "lastFourDigits", width: "25%" },
          { label: "Expiry Month", field: "expiryMonth", width: "15%" },
          { label: "Expiry Year", field: "expiryYear", width: "15%" },
          { label: "CVV", field: "cvv", width: "10%" },
        ],
        
        // Define action buttons for the table
        tableButtons: [
          { label: "Add Payment", action: "addPayment" },
          { label: "Delete Payment", action: "deletePayment" },
        ],
        
        // State management for modal and selections
        selectedItem: null,
        isModalVisible: false,
        modalTitle: "",
        modalFields: [],
        modalInitialData: {},
        modalSubmitButtonText: "",
        currentAction: "",
        error: null,
      };
    },
    methods: {
      // Fetch payment information for the current user
      async fetchPayments() {
        try {
          const clientId = userState.userInfo.id;
          const response = await paymentService.getClientPayments(clientId);
          this.resourceData = response.data;
          console.log("Payment information retrieved:", this.resourceData);
        } catch (error) {
          console.error("Error fetching payment information:", error);
          this.error = error.response?.data || error.message || "Failed to fetch payment information.";
        }
      },
  
      // Handle row selection in the table
      handleRowSelected(selectedItem) {
        this.selectedItem = selectedItem;
      },
  
      // Prepare and show modal for adding a new payment
      handleAddPayment() {
        this.currentAction = "add";
        this.modalTitle = "Add Payment";
        this.modalSubmitButtonText = "Add";
        
        // Define fields for payment information input
        this.modalFields = [
          { name: "cardNumber", label: "Card Number", type: "text", editable: true },
          { name: "expiryMonth", label: "Expiry Month", type: "number", editable: true },
          { name: "expiryYear", label: "Expiry Year", type: "number", editable: true },
          { name: "cvv", label: "CVV", type: "text", editable: true },
        ];
        
        this.modalInitialData = {};
        this.isModalVisible = true;
      },
  
      // Handle payment deletion with confirmation
     // In your ResourceTable component or methods
handleDeletePayment() {
  if (this.selectedItem) {
    // Use a more robust way to get the ID
    const paymentInfoId = this.selectedItem.id || 
                          this.selectedItem.paymentInfoId || 
                          this.selectedItem.paymentInfoId;
    
    if (paymentInfoId) {
      if (confirm(`Are you sure you want to delete the card details?`)) {
        this.deletePayment(paymentInfoId);
      }
    } else {
      console.error("No valid ID found for deletion", this.selectedItem);
      alert("Unable to identify payment method for deletion");
    }
  } else {
    alert("Please select a payment to delete.");
  }
},
  
      // Centralized method for deleting a payment
      async deletePayment(paymentInfoId) {
  try {
    // Make the delete request to the backend
    await paymentService.deletePayment(paymentInfoId);
    
    // Remove the deleted item from the local state
    this.resourceData = this.resourceData.filter(
      (item) => item.paymentInfoId !== paymentInfoId
    );
    
    // Refetch the payment information from the backend
    await this.fetchPayments();
    
    alert("Payment information deleted successfully!");
  } catch (error) {
    // Handle any errors
    console.error("Error deleting payment:", error);
    alert(`Deletion failed: ${error.message}`);
  }
},
  
      // Handle form submission in the modal
      async handleModalSubmit(formData) {
        // Input validation could be added here
        try {
          if (this.currentAction === "add") {
            const clientId = userState.userInfo.id;
            
            // Convert form data to appropriate types
            const payload = {
              cardNumber: formData.cardNumber,
              expiryMonth: parseInt(formData.expiryMonth, 10),
              expiryYear: parseInt(formData.expiryYear, 10),
              cvv: formData.cvv,
            };
            
            // Call service to add payment
            const response = await paymentService.addPayment(clientId, payload);
            
            // Update local state with new payment
            this.resourceData.push(response.data);
            
            alert("Payment information added successfully!");
            this.closeModal();
          }
        } catch (error) {
          console.error("Error in modal submit:", error);
          this.error = error.response?.data || error.message || "Action failed.";
          alert("Failed to add payment. Please check your information and try again.");
        }
      },
  
      // Close and reset modal state
      closeModal() {
        this.isModalVisible = false;
        this.modalInitialData = {};
        this.currentAction = "";
        this.error = null;
      },
    },
    
    // Fetch payments when component is created
    created() {
      this.fetchPayments();
    },
  };
  </script>