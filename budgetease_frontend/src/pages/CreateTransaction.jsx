import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createTransaction } from "../api/transactionService";
import { getGoals } from "../api/goalService";
import { getBudgets } from "../api/budgetService";
import DashboardLayout from "../components/DashBoard";

const CreateTransaction = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    transactionName: "",
    amount: "",
    date: "",
    type: "",
    status:"",
    description: "",
    merchant: "",
    budgetId: [],
    goalId: []
  });

  const [allGoals, setAllGoals] = useState([]);
  const [allBudgets, setAllBudgets] = useState([]);
  const [validationError, setValidationError] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (formData.type === "INCOME") {
      getGoals().then(setAllGoals).catch(console.error);
    } else if (formData.type === "EXPENSE") {
      getBudgets().then(setAllBudgets).catch(console.error);
    }
  }, [formData.type]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleLinkedSelection = (e) => {
    const selectedId = e.target.value;
    if (formData.type === "INCOME") {
      setFormData((prev) => ({ ...prev, goalId: [selectedId], budgetId: [] }));
    } else if (formData.type === "EXPENSE") {
      setFormData((prev) => ({ ...prev, budgetId: [selectedId], goalId: [] }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (
      (formData.type === "INCOME" && formData.goalId.length === 0) ||
      (formData.type === "EXPENSE" && formData.budgetId.length === 0)
    ) {
      setValidationError("Please select a valid " + (formData.type === "INCOME" ? "goal" : "budget") + ".");
      setLoading(false);
      return;
    }

    try {
      await createTransaction(formData);
      navigate("/view-transactions");
    } catch (err) {
      console.error("Transaction creation failed:", err);
      setError("Failed to create transaction. Please check your input.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <DashboardLayout>
      <div className="flex justify-center items-center min-h-screen bg-gradient-to-br from-green-50 to-teal-100 p-6">
        <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-2xl">
          <h2 className="text-2xl font-bold text-green-600 mb-6">Create Transaction</h2>

          {loading ? (
            <div className="text-center">Processing...</div>
          ) : error ? (
            <div className="text-red-600 mb-4">{error}</div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">

              <div>
                <label className="block text-gray-700">Transaction Name</label>
                <input
                  type="text"
                  name="transactionName"
                  value={formData.transactionName}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-gray-700">Amount</label>
                <input
                  type="number"
                  name="amount"
                  value={formData.amount}
                  onChange={handleChange}
                  step="0.01"
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-gray-700">Merchant</label>
                <input
                  type="text"
                  name="merchant"
                  value={formData.merchant}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-gray-700">Transaction Type</label>
                <select
                  name="type"
                  value={formData.type}
                  onChange={(e) => {
                    handleChange(e);
                    setValidationError("");
                    setFormData((prev) => ({ ...prev, goalId: [], budgetId: [] }));
                  }}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="">Select type</option>
                  <option value="INCOME">INCOME</option>
                  <option value="EXPENSE">EXPENSE</option>
                </select>
              </div>

              {formData.type && (
                <div>
                  <label className="block text-gray-700">
                    {formData.type === "INCOME" ? "Select Goal" : "Select Budget"}
                  </label>
                  <select
                    onChange={handleLinkedSelection}
                    className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                    required
                  >
                    <option value="">-- Choose One --</option>
                    {(formData.type === "INCOME" ? allGoals : allBudgets).map((item) => (
                      <option key={item.goalId || item.budgetId} value={item.goalId || item.budgetId}>
                        {item.name}
                      </option>
                    ))}
                  </select>
                  {validationError && <p className="text-red-600 mt-1">{validationError}</p>}
                </div>
              )}

<div>
                <label className="block text-gray-700">Transaction Type</label>
                <select
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="">Select type</option>
                  <option value="FAILED">FAILED</option>
                  <option value="SUCCESS">SUCCESS</option>
                  <option value="PENDING">PENDING</option>
                </select>
              </div>

              <div>
                <label className="block text-gray-700">Transaction Date</label>
                <input
                  type="datetime-local"
                  name="date"
                  value={formData.date}
                  onChange={handleChange}
                  required
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
{/* 
              <div>
                <label className="block text-gray-700">Description</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  rows="3"
                  className="w-full text-black mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div> */}

              <button
                type="submit"
                className="w-full py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
              >
                Create Transaction
              </button>
            </form>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CreateTransaction;
