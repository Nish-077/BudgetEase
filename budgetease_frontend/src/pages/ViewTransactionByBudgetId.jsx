import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashBoard";
import { getTransactionsByBudgetId } from "../api/transactionService";  // You'll need to implement this API call
import { deleteBudget } from "../api/budgetService";  // Ensure you have the deleteBudget function

const ViewTransactionsByBudgetId = () => {
  const { id } = useParams();  // budgetId from URL
  const navigate = useNavigate();

  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [deleteError, setDeleteError] = useState("");

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await getTransactionsByBudgetId(id);
        setTransactions(response);
      } catch (err) {
        console.error("Error fetching transactions:", err);
        setError("Failed to load transactions. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, [id]);

  // Delete budget logic
  const handleDelete = async () => {
    try {
      await deleteBudget(id);  // Deleting the budget using its ID
      alert("Budget deleted successfully");
      navigate("/view-budgets");  // Navigate back to the budget list page
    } catch (err) {
      console.error("Error deleting budget:", err);
      setDeleteError("Failed to delete budget. Please try again.");
    }
  };

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-green-50 to-teal-100">
        <div className="bg-white rounded-2xl shadow-lg p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-green-600">Transactions</h2>
            <button
              onClick={() => navigate(-1)}
              className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
            >
              Back to Budget
            </button>
          </div>

          {/* Delete Error Message */}
          {deleteError && (
            <div className="mb-4 p-3 bg-red-50 text-red-600 rounded-lg flex items-center">
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              {deleteError}
            </div>
          )}

          {error && (
            <div className="mb-4 p-3 bg-red-50 text-red-600 rounded-lg flex items-center">
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              {error}
            </div>
          )}

          {loading ? (
            <div className="flex justify-center items-center h-32">
              <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-green-500"></div>
            </div>
          ) : transactions.length === 0 ? (
            <div className="text-center text-gray-500 py-8">
              <p>No transactions found for this budget.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-green-100 text-green-700">
                    <th className="p-3">Date</th>
                    <th className="p-3">Description</th>
                    <th className="p-3">Amount</th>
                    <th className="p-3">Attachment</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((txn) => (
                    <tr key={txn.transactionId} className="hover:bg-gray-50 border-b">
                      <td className="p-3">{new Date(txn.date).toLocaleDateString()}</td>
                      <td className="p-3">{txn.description || "-"}</td>
                      <td className="p-3 text-green-600 font-semibold">${txn.amount}</td>
                      <td className="p-3">
                        {txn.attachmentUrl ? (
                          <a
                            href={txn.attachmentUrl}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-blue-600 underline hover:text-blue-800"
                          >
                            View
                          </a>
                        ) : (
                          <span className="text-gray-400">No file</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Delete Budget Button */}
          <div className="mt-6 flex justify-end">
            <button
              onClick={handleDelete}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
            >
              Delete Budget
            </button>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ViewTransactionsByBudgetId;
