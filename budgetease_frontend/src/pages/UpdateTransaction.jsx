import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { updateTransaction, getTransactionById } from "../api/transactionService";
import DashboardLayout from "../components/DashBoard";

const UpdateTransaction = () => {
  const { id } = useParams();  
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    transactionId: "",
    transactionName: "",
    amount: "",
    type: "",
    date: "",
    description: "",
    merchant: "",
    status: ""
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchTransaction = async () => {
      try {
        const transaction = await getTransactionById(id);
        console.log("TRANSACTION FOUND:", transaction);

        if (!transaction) {
          setError("Transaction not found.");
        } else {
          setFormData({
            transactionId: transaction.transactionId,
            transactionName: transaction.transactionName,
            amount: transaction.amount,
            type: transaction.type,
            date: transaction.date,
            description: transaction.description,
            merchant: transaction.merchant,
            status: transaction.status
          });
        }
      } catch (err) {
        console.error("Error loading transaction:", err);
        setError("Failed to load transaction data.");
      } finally {
        setLoading(false);
      }
    };

    fetchTransaction();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const updated = await updateTransaction(id, formData);
      console.log("UPDATED TRANSACTION:", updated);
      navigate("/view-transactions");
    } catch (err) {
      console.error("Update failed:", err);
      setError("Failed to update transaction. Please try again.");
    }
  };

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 bg-gradient-to-br from-green-50 to-teal-100 min-h-screen overflow-auto">
        <div className="grid place-items-center h-full">
          <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-2xl">
            <h2 className="text-3xl font-bold text-green-600 mb-8">Update Transaction</h2>

            {loading ? (
              <div className="text-center text-gray-600">Loading...</div>
            ) : error ? (
              <div className="text-red-600 mb-4">{error}</div>
            ) : (
              <form onSubmit={handleSubmit} className="space-y-6">

                <div>
                  <label className="block text-gray-700 font-medium">Transaction Name</label>
                  <input
                    type="text"
                    name="transactionName"
                    value={formData.transactionName}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Amount</label>
                  <input
                    type="number"
                    name="amount"
                    value={formData.amount}
                    onChange={handleChange}
                    step="0.01"
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Merchant</label>
                  <input
                    type="text"
                    name="merchant"
                    value={formData.merchant}
                    onChange={handleChange}
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Type</label>
                  <select
                    name="type"
                    value={formData.type}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">Select Type</option>
                    <option value="INCOME">Income</option>
                    <option value="EXPENSE">Expense</option>
                  </select>
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Status</label>
                  <select
                    name="status"
                    value={formData.status}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">Select Status</option>
                    <option value="FAILED">FAILED</option>
                    <option value="SUCCESS">SUCCESS</option>
                    <option value="PENDING">PENDING</option>
                  </select>
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Date</label>
                  <input
                    type="datetime-local"
                    name="date"
                    value={formData.date ? formData.date.slice(0, 16) : ""}
                    onChange={handleChange}
                    required
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <div>
                  <label className="block text-gray-700 font-medium">Description</label>
                  <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    rows="4"
                    className="w-full text-black mt-2 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <button
                  type="submit"
                  className="w-full py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                >
                  Update Transaction
                </button>
              </form>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default UpdateTransaction;
