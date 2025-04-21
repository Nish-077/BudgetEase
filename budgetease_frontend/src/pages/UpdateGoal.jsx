import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getGoalById, updateGoal } from "../api/goalService";
import DashboardLayout from "../components/DashBoard";

const UpdateGoal = () => {
  const { id } = useParams();  
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    goalId: "",
    categoryName: "",
    description: "",
    name: "",
    targetAmount: ""
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchGoal = async () => {
      try {
        const goal = await getGoalById(id);
        console.log(`GOAL IN UPDATE GOAL:`, goal);

        setFormData({
          goalId: goal.goalId,
          categoryName: goal.categoryName,
          description: goal.description,
          name: goal.name,
          targetAmount: goal.targetAmount
        });
      } catch (err) {
        console.error("Error loading goal:", err);
        setError("Failed to load goal data.");
      } finally {
        setLoading(false);
      }
    };

    fetchGoal();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let goal = await updateGoal(id,formData);
      console.log(`UPDATED GOAL`,goal);
      navigate("/view-goals");
    } catch (err) {
      console.error("Update failed:", err);
      setError("Failed to update goal. Please try again.");
    }
  };

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 bg-gradient-to-br from-purple-50 to-pink-100 min-h-screen overflow-auto">
        <div className="grid place-items-center h-full">
          <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-2xl">
            <h2 className="text-3xl font-bold text-purple-600 mb-8">Update Goal</h2>

            {loading ? (
              <div className="text-center text-gray-600">Loading...</div>
            ) : error ? (
              <div className="text-red-600 mb-4">{error}</div>
            ) : (
              <form onSubmit={handleSubmit} className="space-y-6">

                <div>
                  <label className="block text-gray-700 font-medium">Name</label>
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Category</label>
                  <input
                    type="text"
                    name="categoryName"
                    value={formData.categoryName}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Description</label>
                  <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    rows="4"
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Target Amount</label>
                  <input
                    type="number"
                    name="targetAmount"
                    value={formData.targetAmount}
                    onChange={handleChange}
                    step="0.01"
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500"
                  />
                </div>

                <button
                  type="submit"
                  className="w-full py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
                >
                  Update Goal
                </button>
              </form>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default UpdateGoal;
