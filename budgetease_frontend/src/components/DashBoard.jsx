import { useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode"; // Import jwt-decode
import { useState, useEffect } from "react";

const DashboardLayout = ({ children }) => {
    const navigate = useNavigate();
    const [userId, setUserId] = useState(null); // Initially null

    useEffect(() => {
        const token = localStorage.getItem("jwt");

        if (token) {
            try {
                // Decode the JWT to get user data
                const decodedToken = jwtDecode(token);
                setUserId(decodedToken.sub); // Assuming the userId is part of the JWT payload
            } catch (err) {
                console.error("Invalid JWT token", err);
            }
        } else {
            setUserId(null); // Set to null if there's no token
        }
    }, []);

    return (
      <div className="flex h-screen w-full bg-gradient-to-br from-blue-50 to-indigo-100">
        {/* Sidebar */}
        <img
            src={localStorage.getItem("pfp_url")}
            alt="Profile Picture"
            className="w-24 h-24 rounded-full object-cover border-2 border-indigo-500"
        />
        <div className="w-64 bg-white shadow-lg flex flex-col justify-between p-6">
          <div>
            <h2 className="text-2xl font-bold text-indigo-600 mb-6">Dashboard</h2>
            <nav className="space-y-4">
              <button
                onClick={() => navigate("/profile")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                My Profile
              </button>
              <button
                onClick={() => navigate("/update-profile")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                Update Profile
              </button>
              <button
                onClick={() => navigate("/change-password")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                Change Password
              </button>
              <button
                onClick={() => navigate("/create-budget")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                Create Budget
              </button>
              <button
                onClick={() => navigate("/view-budgets")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                View Budgets
              </button>
              <button
                onClick={() => navigate("/create-goal")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                Create Goals
              </button>
              <button
                onClick={() => navigate("/view-goals")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                View Goals
              </button>
              <button
                onClick={() => navigate("/create-transaction")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                Create Transactions
              </button>
              <button
                onClick={() => navigate("/create-transaction-image")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                Create Transaction from image
              </button>
              <button
                onClick={() => navigate("/view-transactions")}
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                View Transactions
              </button>
              <button
                onClick={() => navigate(`/view-rewards`)} // Passing userId in the route
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
                disabled={!userId} // Disable until userId is available
              >
                View Rewards
              </button>
              <button
                onClick={() => navigate(`/view-notifications`)} // Passing userId in the route
                className="w-full text-left text-indigo-600 hover:text-indigo-800 font-medium transition"
              >
                View Notifications
              </button>
            </nav>
          </div>
          <button
            onClick={() => {
              localStorage.removeItem("jwt");
              navigate("/login");
            }}
            className="mt-6 bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors"
          >
            Logout
          </button>
        </div>
  
        {/* Main Content */}
        <div className="flex-grow flex justify-center items-start p-6">
          {children}
        </div>
      </div>
    );
};

export default DashboardLayout;
