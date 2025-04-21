import axiosInstance from "./axiosInstance";

const API_URL = "/financial-target"

export const getProgress = async (budgetId,type) => {
    const response = await axiosInstance.get(`${API_URL}/progress/${type}/${budgetId}`);

    console.log(`PROGRESS is ${response['progress']}`)

    return response.data;
  }