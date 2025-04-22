import axiosInstance from "./axiosInstance";

const API_URL="/rewards"

export const getRewards = async () => {
    const response = await axiosInstance.get(`${API_URL}/user`);
    return response.data;
}