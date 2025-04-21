import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getBudgetById, updateBudget } from "../api/budgetService";
import DashboardLayout from "../components/DashBoard";

const UpdateBudget = () => {
  const { id } = useParams(); // gets :id from route
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    budgetId: "", // important: this will be sent in request body
    categoryName: "",
    description: "",
    name: "",
    startDate: "",
    endDate: "",
    allocatedAmount: ""
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchBudget = async () => {
      try {
        const budget = await getBudgetById(id);

        console.log(`BUDGET IN UPDATE BUDGET ${budget}`);

        setFormData({
          budgetId: budget.budgetId,
          categoryName: budget.categoryName,
          description: budget.description,
          name: budget.name,
          startDate: budget.startDate.slice(0, 16),
          endDate: budget.endDate.slice(0, 16),
          allocatedAmount: budget.allocatedAmount
        });
      } catch (err) {
        console.error("Error loading budget:", err);
        setError("Failed to load budget data.");
      } finally {
        setLoading(false);
      }
    };

    fetchBudget();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateBudget(formData);  // sending the object with budgetId
      navigate("/view-budgets");
    } catch (err) {
      console.error("Update failed:", err);
      setError("Failed to update budget. Please try again.");
    }
  };

  return (
    <DashboardLayout>
      <div className="flex justify-center items-center min-h-screen bg-gradient-to-br from-green-50 to-teal-100 p-6">
        <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-2xl">
          <h2 className="text-2xl font-bold text-green-600 mb-6">Update Budget</h2>

          {loading ? (
            <div className="text-center">Loading...</div>
          ) : error ? (
            <div className="text-red-600 mb-4">{error}</div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-gray-700">Name</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-gray-700">Category</label>
                <input
                  type="text"
                  name="categoryName"
                  value={formData.categoryName}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-gray-700">Description</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  rows="3"
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-gray-700">Start Date</label>
                  <input
                    type="datetime-local"
                    name="startDate"
                    value={formData.startDate}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
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
                    className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
              </div>

              <div>
                <label className="block text-gray-700">Allocated Amount</label>
                <input
                  type="number"
                  name="allocatedAmount"
                  value={formData.allocatedAmount}
                  onChange={handleChange}
                  step="0.01"
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <button
                type="submit"
                className="w-full text-black py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
              >
                Update Budget
              </button>
            </form>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default UpdateBudget;
