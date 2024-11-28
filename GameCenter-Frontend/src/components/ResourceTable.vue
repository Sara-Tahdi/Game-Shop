<template>
  <div class="resource-table" :style="{ height: height, width: width }">
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th
              v-for="(column, index) in columns"
              :key="index"
              :style="{ width: column.width }"
            >
              {{ column.label }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(item, rowIndex) in data"
            :key="itemKey ? item[itemKey] : rowIndex"
            :class="{ selected: selectedItemIndex === rowIndex }"
            @click="selectRow(rowIndex)"
          >
            <td
              v-for="(column, colIndex) in columns"
              :key="colIndex"
              :style="{ width: column.width }"
            >
              {{ item[column.field] }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="button-container">
      <button
        v-for="(button, index) in buttons"
        :key="index"
        @click="buttonClicked(button.action)"
      >
        {{ button.label }}
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "ResourceTable",
  props: {
    data: {
      type: Array,
      required: true,
    },
    columns: {
      type: Array,
      required: true,
      // Each column: { label: 'Column Name', field: 'propertyName', width: '50%' }
    },
    buttons: {
      type: Array,
      default: () => [],
      // Each button: { label: 'Button Label', action: 'functionName' }
    },
    height: {
      type: String,
      default: "400px",
    },
    width: {
      type: String,
      default: "100%",
    },
    itemKey: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      selectedItemIndex: null,
    };
  },
  methods: {
    selectRow(index) {
      this.selectedItemIndex = index;
      const selectedItem = this.data[index];
      this.$emit("rowSelected", selectedItem);
    },
    buttonClicked(action) {
      const selectedItem =
        this.selectedItemIndex !== null
          ? this.data[this.selectedItemIndex]
          : null;
      this.$emit(action, selectedItem);
    },
  },
};
</script>

<style scoped>
.resource-table {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.table-container {
  flex: 1;
  overflow: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed; /* Ensures fixed table layout */
}

thead th,
tbody td {
  padding: 8px;
  border-bottom: 1px solid #ddd;
  text-align: left;
  overflow: hidden; /* Prevents content overflow */
  text-overflow: ellipsis; /* Adds ellipsis if content is too long */
  white-space: nowrap;
}

thead th {
  background-color: #f7f7f7;
  position: sticky;
  top: 0;
  border-bottom: 1px solid #ddd;
  padding: 8px;
  text-align: left;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: bold; /* Makes the header text bold */
}

tbody tr.selected {
  background-color: #e6f7ff;
}

tbody tr:hover {
  background-color: #f5f5f5;
  cursor: pointer;
}

.button-container {
  padding: 8px;
  text-align: right;
  border-top: 1px solid #ddd;
}

.button-container button {
  margin-left: 8px;
  padding: 12px 20px; /* Increase padding for larger buttons */
  font-size: 1rem; /* Increase font size for better readability */
  font-weight: bold; /* Make button text bold */
  cursor: pointer;
  border: none;
  border-radius: 4px;
  background-color: #007bff; /* Blue background color */
  color: white; /* White text */
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.button-container button:hover {
  background-color: #0056b3; /* Darker blue on hover */
  transform: translateY(-2px); /* Add a slight hover effect */
}

.button-container button:active {
  transform: translateY(0); /* Reset transform on click */
}
</style>
