<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-box" @click.stop>
      <div class="modal-header">
        <h3>{{ title }}</h3>
        <button class="close-button" @click="closeModal">×</button>
      </div>
      <div class="modal-body">
        <!-- Error Message -->
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

        <!-- Dynamic Form -->
        <form @submit.prevent="handleFormSubmit">
          <div class="form-group" v-for="(field, index) in fields" :key="index">
            <label :for="field.name">{{ field.label }}</label>
            <input
              v-if="field.type !== 'textarea'"
              :type="field.type || 'text'"
              :id="field.name"
              v-model="formData[field.name]"
              :readonly="!field.editable"
              :placeholder="field.placeholder"
              @input="clearError"
              required
            />
            <textarea
              v-else
              :id="field.name"
              v-model="formData[field.name]"
              :readonly="!field.editable"
              :placeholder="field.placeholder"
              @input="clearError"
              required
            ></textarea>
          </div>
          <button type="submit" class="submit-button">
            {{ submitButtonText }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ResourceModal",
  props: {
    isVisible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: "Modal Title",
    },
    fields: {
      type: Array,
      required: true,
      // Each field: { name: 'fieldName', label: 'Field Label', editable: true/false, type: 'text', placeholder: 'Enter value' }
    },
    initialData: {
      type: Object,
      default: () => ({}),
    },
    submitButtonText: {
      type: String,
      default: "Submit",
    },
    error: {
      type: String,
      default: null, // Accept error from parent
    },
  },
  data() {
    return {
      formData: { ...this.initialData },
      errorMessage: this.error, // Local copy of the error prop
    };
  },
  watch: {
    error(newError) {
      this.errorMessage = newError; // Sync with the parent prop
    },
    initialData(newData) {
      this.formData = { ...newData };
    },
    isVisible(newVal) {
      if (newVal) {
        this.formData = { ...this.initialData };
        this.errorMessage = this.error; // Reset error when modal opens
      }
    },
  },
  methods: {
    closeModal() {
      this.$emit("close");
    },
    handleFormSubmit() {
      this.$emit("formSubmit", { ...this.formData });
    },
    clearError() {
      this.errorMessage = null; // Reset local error message
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body .form-group {
  margin-bottom: 1rem;
}

.modal-body label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.modal-body input,
.modal-body textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.modal-body input[readonly],
.modal-body textarea[readonly] {
  background-color: #f5f5f5;
}

.submit-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #0056b3;
}

.error-message {
  color: red;
  margin-top: 0.5rem;
  font-size: 0.9rem;
  text-align: center;
}
</style>
