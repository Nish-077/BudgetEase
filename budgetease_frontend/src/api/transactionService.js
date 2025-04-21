import axiosInstance from "./axiosInstance";

const API_URL = "/transactions"

// Get all transactions for the current user
export const getAllTransactions = async () => {
    const response = await axiosInstance.get(`${API_URL}/getTransactions`);
    return response.data;
};

// Get transactions by Budget ID
export const getTransactionsByBudgetId = async (budgetId) => {
    const response = await axiosInstance.get(`${API_URL}/getTransactionsByBudgetId/${budgetId}`);
    return response.data;
};

// Get transactions by Goal ID
export const getTransactionsByGoalId = async (goalId) => {
    const response = await axiosInstance.get(`${API_URL}/getTransactionsByGoalId/${goalId}`);
    return response.data;
};

// Create a normal transaction
export const createTransaction = async (transactionData) => {
    const response = await axiosInstance.post(`${API_URL}/create-normal-transaction`, transactionData);
    return response.data;
};

// Create transactions from image (Gemini Vision)
export const createTransactionsFromImage = async (file) => {
    const formData = new FormData();
    formData.append("file", file);
    const response = await axiosInstance.post(`${API_URL}/create-transaction-gemini`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
};

// Add transaction to a specific Budget
export const addTransactionToBudget = async (budgetId, transactionData) => {
    const response = await axiosInstance.post(`${API_URL}/addTransactionToBudget/${budgetId}`, transactionData);
    return response.data;
};

// Add transaction to a specific Goal
export const addTransactionToGoal = async (goalId, transactionData) => {
    const response = await axiosInstance.post(`${API_URL}/addTransactionToGoal/${goalId}`, transactionData);
    return response.data;
};

// Update a transaction by ID
export const updateTransaction = async (transactionId, updateData) => {
    const response = await axiosInstance.post(`${API_URL}/updateTransaction/${transactionId}`, updateData);
    return response.data;
};

// Delete a transaction by ID
export const deleteTransaction = async (transactionId) => {
    const response = await axiosInstance.delete(`${API_URL}/deleteTransaction/${transactionId}`);
    return response.data;
};

export const getTransactionById = async (transactionId) => {
    const response = await axiosInstance.get(`${API_URL}/getTransactionById/${transactionId}`);
    return response.data;
}
