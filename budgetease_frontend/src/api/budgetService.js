import axiosInstance from "./axiosInstance";

const API_URL = "/budget"

// Create a new budget
export const createBudget = async (budgetData) => {
    const response = await axiosInstance.post(`${API_URL}/createBudget`, budgetData);
    return response.data;
  };
  
  // Get all budgets
  export const getBudgets = async () => {
    const response = await axiosInstance.get(`${API_URL}/getBudgets`);

    return response.data;
  };
  
  // Delete a budget by ID
  export const deleteBudget = async (budgetId) => {
    const response = await axiosInstance.delete(`${API_URL}/deleteBudget/${budgetId}`);
    return response.data;
  };
  
  // Get a budget by ID
  export const getBudgetById = async (budgetId) => {
    const response = await axiosInstance.get(`${API_URL}/getBudgetById/${budgetId}`);

    console.log(`BUDGET ID INFO ${response.data}`);

    return response.data;
  };
  
  // Update a budget
  export const updateBudget = async (budgetData) => {
    const response = await axiosInstance.put(`${API_URL}/updateBudget`, budgetData);
    return response.data;
  };
  
  // Get budgets filtered by category
  export const getBudgetsByCategory = async (category) => {
    const response = await axiosInstance.get(`${API_URL}/getBudgetsByCategory/${category}`);
    return response.data;
  };

  export const fetchBudgetByName = async (budgetName) => {
    const response = await axiosInstance.get(`${API_URL}/findBudgetByName/${budgetName}`);
    return response.data;
  }