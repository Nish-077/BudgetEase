import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createGoal } from "../api/goalService";
import DashboardLayout from "../components/DashBoard";

const CreateGoal = () => {
  const [formData, setFormData] = useState({
    targetAmount: "",
    purpose: "",
    categoryName: "",
    description: "",
    name: "",
    startDate: "",
    endDate: "",
    rewardPoints: ""
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formattedGoal = {
        ...formData,
        targetAmount: parseFloat(formData.targetAmount),
        rewardPoints: parseInt(formData.rewardPoints),
        startDate: new Date(formData.startDate),
        endDate: new Date(formData.endDate)
      };

      await createGoal(formattedGoal);
      navigate("/view-goals");
    } catch (err) {
      console.error("Goal creation failed:", err);
      setError("Failed to create goal. Please check your input and try again.");
    }
  };

  return (
    <DashboardLayout>
      <div className="flex justify-center items-center min-h-screen bg-gradient-to-br from-indigo-100 to-purple-200 p-6">
        <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-2xl">
          <h2 className="text-2xl font-bold text-purple-600 mb-6">Create New Goal</h2>

          {error && <div className="text-red-600 mb-4">{error}</div>}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-gray-700">Goal Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
                className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div>

            <div>
              <label className="block text-gray-700">Purpose</label>
              <input
                type="text"
                name="purpose"
                value={formData.purpose}
                onChange={handleChange}
                required
                className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div>

            {/* <div>
              <label className="block text-gray-700">Category Name</label>
              <input
                type="text"
                name="categoryName"
                value={formData.categoryName}
                onChange={handleChange}
                required
                className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div> 

            <div>
              <label className="block text-gray-700">Description</label>
              <textarea
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows="3"
                className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div> */}

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-gray-700">Start Date</label>
                <input
                  type="datetime-local"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>

              <div>
                <label className="block text-gray-700">End Date</label>
                <input
                  type="datetime-local"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>
            </div>

            <div>
              <label className="block text-gray-700">Target Amount</label>
              <input
                type="number"
                name="targetAmount"
                value={formData.targetAmount}
                onChange={handleChange}
                step="0.01"
                required
                className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div>

            {/* <div>
              <label className="block text-gray-700">Reward Points</label>
              <input
                type="number"
                name="rewardPoints"
                value={formData.rewardPoints}
                onChange={handleChange}
                required
                className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div> */}

            <button
              type="submit"
              className="w-full py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
            >
              Create Goal
            </button>
          </form>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CreateGoal;
