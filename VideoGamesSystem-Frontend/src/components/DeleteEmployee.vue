<template>
    <div class="delete-employee-container">
      <h1>Manage Staff Members</h1>
      
      <div v-if="loading" class="loading">
        Loading...
      </div>
  
      <div v-else-if="error" class="error-message">
        {{ error }}
      </div>
  
      <!-- Success Message Popup -->
      <div v-if="showSuccess" class="success-message">
        Successfully deleted staff member!
      </div>
  
      <div v-else class="employees-list">
        <div v-if="filteredEmployees.length === 0" class="no-employees">
          No staff members found
        </div>
  
        <div v-for="employee in filteredEmployees" :key="employee.id" class="employee-card">
          <div class="employee-info">
            <h3>{{ employee.userName }}</h3>
            <p>{{ employee.email }}</p>
            <p class="employee-type">Staff Member</p>
          </div>
          <button 
            @click="showDeleteConfirmation(employee)" 
            class="delete-btn"
            :disabled="isDeleting"
          >
            Delete
          </button>
        </div>
      </div>
  
      <!-- Delete Confirmation Modal -->
      <div v-if="showModal" class="modal-overlay">
        <div class="modal-content">
          <h2>Confirm Delete</h2>
          <p>Are you sure you want to delete "{{ selectedEmployee?.userName }}"?</p>
          <p class="warning-text">This action cannot be undone!</p>
          <div class="modal-buttons">
            <button 
              @click="confirmDelete" 
              class="confirm-btn"
              :disabled="isDeleting"
            >
              Delete
            </button>
            <button 
              @click="cancelDelete" 
              class="cancel-btn"
              :disabled="isDeleting"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import { store } from "../store.js";
  
  const axiosEmployee = axios.create({
    baseURL: "http://localhost:8081",
  });
  
  export default {
    name: "DeleteEmployee",
    data() {
      return {
        employees: [],
        loading: true,
        error: null,
        isDeleting: false,
        showModal: false,
        selectedEmployee: null,
        showSuccess: false
      };
    },
    computed: {
      filteredEmployees() {
        // Filter out admin users (owners)
        return this.employees.filter(employee => !employee.admin);
      }
    },
    async created() {
      await this.fetchEmployees();
    },
    methods: {
      async fetchEmployees() {
        try {
          const response = await axiosEmployee.get("/staff");
          this.employees = response.data;
          this.loading = false;
        } catch (error) {
          console.error("Error fetching employees:", error);
          this.error = "Failed to load staff members";
          this.loading = false;
        }
      },
  
      showDeleteConfirmation(employee) {
        this.selectedEmployee = employee;
        this.showModal = true;
      },
  
      async confirmDelete() {
        if (!this.selectedEmployee) return;
        
        this.isDeleting = true;
        try {
          await axiosEmployee.delete(`/staff/${this.selectedEmployee.id}`);
          this.employees = this.employees.filter(emp => emp.id !== this.selectedEmployee.id);
          this.showModal = false;
          this.selectedEmployee = null;
          
          // Show success message
          this.showSuccess = true;
          setTimeout(() => {
            this.showSuccess = false;
          }, 3000); // Hide after 3 seconds
          
        } catch (error) {
          console.error("Error deleting employee:", error);
          if (error.response?.data?.message) {
            this.error = error.response.data.message;
          } else {
            this.error = "Failed to delete staff member";
          }
        } finally {
          this.isDeleting = false;
        }
      },
  
      cancelDelete() {
        this.showModal = false;
        this.selectedEmployee = null;
      },
    },
  };
  </script>
  
  <style scoped>
  .delete-employee-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background-color: #2c2c2c;
    color: #e0d6cc;
    min-height: 100vh;
  }
  
  h1 {
    color: #e8b67d;
    text-align: center;
    margin-bottom: 30px;
    font-family: 'Helvetica Neue', sans-serif;
  }
  
  .employees-list {
    display: grid;
    gap: 20px;
    margin-top: 20px;
  }
  
  .employee-card {
    border: 1px solid #4a4a4a;
    padding: 20px;
    border-radius: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #363636;
    box-shadow: 0 4px 6px rgba(0,0,0,0.2);
    transition: transform 0.2s;
  }
  
  .employee-card:hover {
    transform: translateY(-2px);
  }
  
  .employee-info h3 {
    margin: 0;
    color: #ffa054;
    font-size: 1.2em;
  }
  
  .employee-info p {
    color: #bdb4aa;
    margin: 5px 0;
  }
  
  .employee-type {
    color: #8b8178;
    font-size: 0.9em;
  }
  
  .delete-btn {
    background-color: #695c52;
    color: #e0d6cc;
    border: none;
    padding: 10px 20px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .delete-btn:hover {
    background-color: #7d6e62;
  }
  
  .delete-btn:disabled {
    background-color: #4a4a4a;
    cursor: not-allowed;
  }
  
  /* Modal styles */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background-color: #363636;
    padding: 25px;
    border-radius: 12px;
    max-width: 400px;
    width: 90%;
    text-align: center;
    color: #e0d6cc;
    border: 1px solid #4a4a4a;
  }
  
  .warning-text {
    color: #ffa054;
    font-weight: bold;
    margin: 15px 0;
  }
  
  .modal-buttons {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-top: 25px;
  }
  
  .confirm-btn {
    background-color: #8b5e3c;
    color: #e0d6cc;
    border: none;
    padding: 10px 20px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .confirm-btn:hover {
    background-color: #a06e47;
  }
  
  .cancel-btn {
    background-color: #4a4a4a;
    color: #e0d6cc;
    border: none;
    padding: 10px 20px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .cancel-btn:hover {
    background-color: #5a5a5a;
  }
  
  .error-message {
    color: #ffa054;
    padding: 15px;
    border: 1px solid #8b5e3c;
    border-radius: 6px;
    margin: 10px 0;
    text-align: center;
    background-color: #363636;
  }
  
  .success-message {
    color: #a8c69f;
    padding: 15px;
    border: 1px solid #6b8a63;
    border-radius: 6px;
    margin: 10px 0;
    text-align: center;
    background-color: #363636;
    animation: fadeIn 0.3s ease-in;
  }
  
  .loading {
    text-align: center;
    padding: 20px;
    color: #bdb4aa;
  }
  
  .no-employees {
    text-align: center;
    color: #bdb4aa;
    padding: 20px;
  }
  
  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(-10px); }
    to { opacity: 1; transform: translateY(0); }
  }
  </style>