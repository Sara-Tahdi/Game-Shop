<template>
  <div>
    <h2>Employee Management</h2>
    <ResourceTable
      :data="resourceData"
      :columns="tableColumns"
      :buttons="tableButtons"
      :height="'400px'"
      :width="'100%'"
      itemKey="id"
      @rowSelected="handleRowSelected"
      @addEmployee="handleAddEmployee"
      @fireEmployee="handleFireEmployee"
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

const employeeService = {
  getAllEmployees() {
    return apiClient.get("/users/employee");
  },
  addEmployee(employeeData) {
    return apiClient.post("/users/employee/create", employeeData);
  },

  fireEmployee(employeeUsername) {
    return apiClient.put(`/users/employee/fire/${employeeUsername}`);
  },
};

export default {
  name: "EmployeeManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "ID", field: "id" },
        { label: "Username", field: "username" },
        { label: "Email", field: "email" },
        { label: "Active", field: "isActive" },
      ],
      tableButtons: [
        { label: "Add Employee", action: "addEmployee" },
        { label: "Fire Employee", action: "fireEmployee" },
      ],
      selectedItem: null,
      // Modal Control
      isModalVisible: false,
      modalTitle: "",
      modalFields: [],
      modalInitialData: {},
      modalSubmitButtonText: "",
      currentAction: "", // "add" or "fire"
      error: null,
    };
  },
  methods: {
    async fetchEmployees() {
      try {
        const response = await employeeService.getAllEmployees();
        this.resourceData = response.data;
        console.log("Employees", this.resourceData);
      } catch (error) {
        console.error("Error fetching employees", error);
      }
    },

    // Handle row selection
    handleRowSelected(item) {
      this.selectedItem = item;
    },

    handleAddEmployee() {
      this.currentAction = "add";
      this.modalTitle = "Add New Employee";
      this.modalSubmitButtonText = "Create Employee";
      this.modalFields = [
        {
          name: "email",
          label: "Email",
          editable: true,
          type: "email",
          placeholder: "Enter email",
        },
        {
          name: "username",
          label: "Username",
          editable: true,
          placeholder: "Enter username",
        },
        {
          name: "password",
          label: "Password",
          editable: true,
          placeholder: "Enter password",
        },
        {
          name: "confirmPassword",
          label: "Confirm Password",
          editable: true,
          placeholder: "Confirm password",
        },
      ];
      this.modalInitialData = {};
      this.isModalVisible = true;
    },

    handleFireEmployee() {
      if (this.selectedItem && this.selectedItem.isActive) {
        this.currentAction = "fire";
        this.modalTitle = "Fire Employee";
        this.modalSubmitButtonText = "Fire Employee";
        this.modalFields = [
          {
            name: "id",
            label: "ID",
            editable: false,
          },
          {
            name: "email",
            label: "Email",
            editable: true,
            placeholder: "Enter email",
          },
          {
            name: "username",
            label: "Username",
            editable: true,
            placeholder: "Enter username",
          },
        ];
        this.modalInitialData = { ...this.selectedItem };
        this.isModalVisible = true;
      } else if (this.selectedItem && !this.selectedItem.isActive) {
        alert("Employee is already inactive.");
      } else {
        alert("Please select an employee to fire.");
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.currentAction = "";
      this.error = null;
    },
    async handleModalSubmit(formData) {
      if (this.currentAction === "add") {
        if (formData.password !== formData.confirmPassword) {
          this.error = "Passwords do not match.";
          return;
        }
        try {
          const requestPayload = {
            email: formData.email,
            username: formData.username,
            password: formData.password,
          };
          const response = await employeeService.addEmployee(requestPayload);
          this.resourceData.push(response.data);
          this.fetchEmployees();
          this.closeModal();
        } catch (error) {
          this.error =
            error.response?.data || "Failed to submit employee firing.";
        }
      } else if (this.currentAction === "fire") {
        try {
          await employeeService.fireEmployee(formData.username);
          this.selectedItem = {
            ...this.selectedItem,
            isActive: false,
          };
          this.fetchEmployees();
          this.closeModal();
        } catch (error) {
          this.error =
            error.response?.data || "Failed to submit employee firing.";
        }
      }
    },
  },
  created() {
    this.fetchEmployees();
    console.log("EmployeeManagement component created.");
  },
};
</script>
