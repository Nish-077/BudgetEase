import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import DashboardLayout from "../components/DashBoard";

const Profile = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const pfp_url = localStorage.getItem("pfp_url");

  useEffect(() => {
    const token = localStorage.getItem("jwt");
    if (!token) navigate("/login");
    else {
      try {
        const decoded = jwtDecode(token);
        setUser({ username: decoded.username, email: decoded.email });
      } catch (err) {
        console.error("Invalid token:", err);
        navigate("/login");
      }
    }
  }, [navigate]);

  if (!user) {
    return (
      <div className="flex justify-center items-center h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
        <p className="text-gray-600 text-lg">Loading profile...</p>
      </div>
    );
  }

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto">
        <div className="bg-white rounded-2xl shadow-lg p-8 h-full">
          <div className="flex flex-col items-center text-center">
            {pfp_url && (
              <img 
                src={pfp_url} 
                alt="Profile" 
                className="w-32 h-32 rounded-full object-cover mb-4 border-4 border-indigo-100"
              />
            )}
            <h1 className="text-3xl font-bold text-indigo-600 mb-2">Welcome, {user.username}!</h1>
            <p className="text-gray-700 mb-6">{user.email}</p>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default Profile;