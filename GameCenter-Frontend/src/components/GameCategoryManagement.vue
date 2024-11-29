<template>
  <div>
    <h2>Game Category Management</h2>
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
const categoryService = {
  getAllCategories() {
    return apiClient.get("/gameCategory");
  },
  createCategory(data) {
    console.log("So far so good");
    return apiClient.post("/gameCategory/create", data);
  },
  updateCategory(id, data) {
    console.log("So far so good 2");
    return apiClient.put(`/gameCategory/${id}`, data);
  },
};

export default {
  name: "GameCategoryManagement",
  components: {
    ResourceTable,
    ResourceModal,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "ID", field: "id" },
        { label: "Category", field: "category" },
      ],
      tableButtons: [
        { label: "Add Category", action: "addItem" },
        { label: "Update Category", action: "updateItem" },
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
    // Fetch all categories from the backend using categoryService
    async fetchCategories() {
      try {
        const response = await categoryService.getAllCategories();
        this.resourceData = response.data;
      } catch (error) {
        console.error("Error fetching categories:", error);
        // Handle error (e.g., display a notification)
      }
    },

    // Handle row selection
    handleRowSelected(item) {
      this.selectedItem = item;
    },

    // Handle adding a new category
    handleAddItem() {
      this.currentAction = "add";
      this.modalTitle = "Add New Category";
      this.modalSubmitButtonText = "Create Category";
      this.modalFields = [
        {
          name: "category",
          label: "Category Name",
          editable: true,
          placeholder: "Enter category name",
        },
      ];
      this.modalInitialData = {};
      this.isModalVisible = true;
    },

    // Handle updating an existing category
    handleUpdateItem() {
      if (this.selectedItem) {
        this.currentAction = "update";
        this.modalTitle = "Update Category";
        this.modalSubmitButtonText = "Update Category";
        this.modalFields = [
          {
            name: "id",
            label: "ID",
            editable: false,
          },
          {
            name: "category",
            label: "Category Name",
            editable: true,
          },
        ];
        this.modalInitialData = { ...this.selectedItem };
        this.isModalVisible = true;
      } else {
        alert("Please select a category to update.");
      }
    },

    // Close the modal
    closeModal() {
      this.isModalVisible = false;
      this.modalInitialData = {};
      this.currentAction = "";
      this.error = null;
    },

    // Handle form submission from the modal
    async handleModalSubmit(formData) {
      if (this.currentAction === "add") {
        // Handle Add
        try {
          const response = await categoryService.createCategory(formData);
          this.resourceData.push(response.data); // Add the new category to the table
          this.closeModal();
        } catch (error) {
          console.error("Error adding category:", error.response.data);
          // Handle error (e.g., display error message)
          this.error = error.response?.data || "Failed to add category.";
        }
      } else if (this.currentAction === "update") {
        // Handle Update
        try {
          const response = await categoryService.updateCategory(formData.id, {
            category: formData.category,
          });
          const index = this.resourceData.findIndex(
            (item) => item.id === formData.id
          );
          if (index !== -1) {
            this.resourceData.splice(index, 1, response.data); // Reactive update
          }
          this.selectedItem = response.data; // Reassign selectedItem to the updated row
          this.closeModal();
        } catch (error) {
          console.error("Error updating category:", error.response.data);
          // Handle error (e.g., display error message)
          this.error = error.response?.data || "Failed to update category.";
        }
      }
    },
  },
  created() {
    this.fetchCategories();
  },
};
</script>
