import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createBudget } from "../api/budgetService";
import DashboardLayout from "../components/DashBoard";

const CreateBudget = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    amount: "",
    startDate: "",
    endDate: "",
    categoryName: ""
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = {
        ...form,
        amount: parseFloat(form.amount),
        startDate: new Date(form.startDate).toISOString(),
        endDate: new Date(form.endDate).toISOString()
      };
      const response = await createBudget(payload);
      console.log("BUDGET CREATE: ",response);
      
      alert("Budget created successfully!");
      navigate("/view-budgets");
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to create budget. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <DashboardLayout>
      <div className="flex min-h-full w-full bg-gradient-to-br from-green-50 to-teal-100">
        {/* Left Panel */}
        <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-r from-teal-500 to-green-600 p-8">
          <div className="m-auto max-w-md text-center">
            <h1 className="text-4xl font-bold text-black mb-4">Create a Budget</h1>
            <p className="text-xl text-green-100">Plan ahead and track your expenses</p>
          </div>
        </div>

        {/* Right Panel */}
        <div className="w-full lg:w-1/2 flex items-center justify-center p-6 overflow-auto">
          <div className="w-full max-w-md">
            <div className="lg:hidden text-center mb-8">
              <h1 className="text-3xl font-bold text-green-600">Create a Budget</h1>
              <p className="text-gray-600 mt-2">Plan ahead and track your expenses</p>
            </div>

            {error && (
              <div className="mb-6 p-3 bg-red-50 text-red-600 rounded-lg flex items-center">
                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit} className="bg-white p-8 rounded-2xl shadow-lg">
              <div className="space-y-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Budget Name</label>
                  <input
                    type="text"
                    name="name"
                    value={form.name}
                    onChange={handleChange}
                    className="block text-black w-full pl-3 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                    placeholder="January Savings"
                    required
                    maxLength="30"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Amount</label>
                  <input
                    type="number"
                    name="amount"
                    value={form.amount}
                    onChange={handleChange}
                    min="0"
                    step="0.01"
                    className="block text-black w-full pl-3 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                    placeholder="0.00"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Start Date</label>
                  <input
                    type="datetime-local"
                    name="startDate"
                    value={form.startDate}
                    onChange={handleChange}
                    className="block text-black w-full pl-3 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">End Date</label>
                  <input
                    type="datetime-local"
                    name="endDate"
                    value={form.endDate}
                    onChange={handleChange}
                    className="block text-black w-full pl-3 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Category Name</label>
                  <input
                    type="text"
                    name="categoryName"
                    value={form.categoryName}
                    onChange={handleChange}
                    className="block text-black w-full pl-3 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-green-500 focus:border-green-500"
                    placeholder="Groceries"
                    required
                    maxLength="50"
                  />
                </div>

                <button
                  type="submit"
                  disabled={loading}
                  className={`w-full py-3 px-4 rounded-lg shadow-sm text-white font-medium ${loading ? "bg-green-300" : "bg-green-600 hover:bg-green-700"}`}
                >
                  {loading ? "Creating Budget..." : "Create Budget"}
                </button>
              </div>

              <div className="mt-6 text-center">
                <p className="text-sm text-gray-600">
                  Want to manage your budgets?{" "}
                  <button onClick={() => navigate("/budgets")} className="text-green-600 hover:text-green-500 font-medium">
                    View Budgets
                  </button>
                </p>
              </div>
            </form>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CreateBudget;
