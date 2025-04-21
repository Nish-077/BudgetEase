import axiosInstance from "./axiosInstance";

const API_URL = "/goals";

// Create a new goal
export const createGoal = async (goalData) => {
    const response = await axiosInstance.post(`${API_URL}/createGoal`, goalData);
    return response.data;
};

// Get all goals
export const getGoals = async () => {
    const response = await axiosInstance.get(API_URL);
    console.log("GOALS:",response.data);

    return response.data;
};

// Get a goal by ID
export const getGoalById = async (goalId) => {
    const response = await axiosInstance.get(`${API_URL}/${goalId}`);
    return response.data;
};

// Get only the purpose of a goal by ID
export const getGoalPurpose = async (goalId) => {
    const response = await axiosInstance.get(`${API_URL}/${goalId}/purpose`);
    return response.data;
};

// Update a goal by ID
export const updateGoal = async (goalId, goalData) => {
    const response = await axiosInstance.put(`${API_URL}/${goalId}`, goalData);
    return response.data;
};

// Delete a goal by ID
export const deleteGoal = async (goalId) => {
    const response = await axiosInstance.delete(`${API_URL}/${goalId}`);
    return response.data;
};

export const fetchGoalByName = async (goalName) => {
    const response = await axiosInstance.get(`${API_URL}/findGoalByName/${goalName}`);
    console.log("RESPONSE ",response.data);

    return response.data;
}
