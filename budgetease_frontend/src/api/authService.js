// src/api/authService.js
import axiosInstance from "./axiosInstance";
import { jwtDecode } from "jwt-decode";

const API_URL = "/auth";

export const register = async ({ email, username, password, phoneNo }) => {
  return axiosInstance.post("/auth/register", { email, username, password, phoneNo });
};


export const login = async ({ identifier, password }) => {
  const response = await axiosInstance.post("/auth/login", { identifier, password });

  const authHeader = response.headers['authorization'];

  // console.log(authHeader);
  let token=null
  if (authHeader && authHeader.startsWith('Bearer ')) {
    token = authHeader.substring(7); // removes "Bearer "
    localStorage.setItem("jwt", token);
  } else {
    throw new Error("Token not found in response headers");
  }

  console.log(response.data);

  let info=jwtDecode(token)
  localStorage.setItem("pfp_url",response.data['pfp_url'])

  return info;
};


export const logout = async () => {
  localStorage.removeItem("jwt");
  return axiosInstance.post(`${API_URL}/logout`);
};

export const getProfile = async () => {

  const response = await axiosInstance.get(`${API_URL}/profile`);
  return response.data;
};

export const updateProfile = async ({ userName, userProfilePic, email, phoneNo }) => {

  const formData = new FormData();
  formData.append("userName", userName);
  formData.append("userProfilePic", userProfilePic);
  formData.append("email", email);
  formData.append("phoneNo", phoneNo);

  return axiosInstance.post("/auth/profile", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      "Authorization": `Bearer ${token}`
    }
  });
};

export const changePassword = async (passwordData) => {

  // console.log(passwordData);

  return axiosInstance.post(`${API_URL}/changePassword`, passwordData);
};
