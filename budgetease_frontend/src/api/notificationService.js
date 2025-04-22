import axiosInstance from "./axiosInstance";

const API_URL = "/notifications";

export const getAllNotifications = async () => {
    const response = await axiosInstance.get(`${API_URL}/getNotifications`);
    return response.data;
}

export const markAsRead = async (id) => {
    const response = await axiosInstance.post(`${API_URL}/mark-read/${id}`);
    return response.data;
}