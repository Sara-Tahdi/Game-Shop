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
  </div>
</template>
<script>
import ResourceTable from "../components/ResourceTable.vue";
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
    return apiClient.post("/gameCategory/create", data);
  },
  updateCategory(id, data) {
    return apiClient.put(`/gameCategory/${id}`, data);
  },
};

export default {
  name: "GameCategoryManagement",
  components: {
    ResourceTable,
  },
  data() {
    return {
      resourceData: [],
      tableColumns: [
        { label: "ID", field: "id" },
        { label: "Category", field: "category" },
      ],
      tableButtons: [
        { label: "Update Category", action: "updateItem" },
        { label: "Add Category", action: "addItem" },
      ],
      selectedItem: null,
    };
  },
  methods: {
    // Fetch all categories from the backend
    async fetchCategories() {
      try {
        const response = await categoryService.getAllCategories();
        this.resourceData = response.data;
      } catch (error) {
        console.error("Error fetching categories1:", error);
        // Handle error (e.g., display a notification)
      }
    },

    // Handle row selection
    handleRowSelected(item) {
      this.selectedItem = item;
    },

    // Handle adding a new category
    async handleAddItem() {
      // Prompt the user for the new category name (e.g., using a modal)
      const categoryName = prompt("Enter new category name:");
      if (categoryName) {
        try {
          const response = await categoryService.createCategory({
            category: categoryName,
          });
          this.resourceData.push(response.data); // Add the new category to the table
        } catch (error) {
          console.error("Error adding category:", error);
          // Handle error
        }
      }
    },

    async handleUpdateItem() {
      if (this.selectedItem) {
        const newCategoryName = prompt(
          "Enter new category name:",
          this.selectedItem.category
        );
        if (newCategoryName && newCategoryName !== this.selectedItem.category) {
          try {
            const response = await categoryService.updateCategory(
              this.selectedItem.id,
              {
                category: newCategoryName,
              }
            );

            const index = this.resourceData.findIndex(
              (item) => item.id === this.selectedItem.id
            );
            if (index !== -1) {
              this.resourceData.splice(index, 1, response.data); // Reactive update
            }
            this.selectedItem = null;
          } catch (error) {
            console.error("Error updating category:", error);
          }
        }
      } else {
        alert("Please select a category to update.");
      }
    },
  },
  created() {
    this.fetchCategories();
  },
};
</script>
