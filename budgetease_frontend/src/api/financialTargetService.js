import axiosInstance from "./axiosInstance";

const API_URL = "/financial-target"

export const getProgress = async (Id,type) => {
    const response = await axiosInstance.get(`${API_URL}/progress/${type}/${Id}`);

    return response.data;
  }

export const getOverdue = async (Id, type) => {
  const response = await axiosInstance.get(`${API_URL}/overdue/${type}/${Id}`);
  console.log(`OVERDUE? `,response.data);

  return response.data;
}