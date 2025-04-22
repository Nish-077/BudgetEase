import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashBoard";
import { getTransactionsByGoalId } from "../api/transactionService";  // Make sure this is implemented
import { deleteGoal } from "../api/goalService";  // Ensure you have this function

const ViewTransactionsByGoalId = () => {
  const { id } = useParams();  // goalId from URL
  const navigate = useNavigate();

  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [deleteError, setDeleteError] = useState("");

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await getTransactionsByGoalId(id);
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

  const handleDelete = async () => {
    try {
      await deleteGoal(id);  // Deleting the goal using its ID
      alert("Goal deleted successfully");
      navigate("/view-goals");
    } catch (err) {
      console.error("Error deleting goal:", err);
      setDeleteError("Failed to delete goal. Please try again.");
    }
  };

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-indigo-50 to-purple-100">
        <div className="bg-white rounded-2xl shadow-lg p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-purple-600">Transactions for Goal</h2>
            <button
              onClick={() => navigate(-1)}
              className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
            >
              Back to Goal
            </button>
          </div>

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
              <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-purple-500"></div>
            </div>
          ) : transactions.length === 0 ? (
            <div className="text-center text-gray-500 py-8">
              <p>No transactions found for this goal.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-purple-100 text-purple-700">
                    <th className="text-black p-3">Date</th>
                    <th className="text-black p-3">Description</th>
                    <th className="text-black p-3">Amount</th>
                    <th className="text-black p-3">Attachment</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((txn) => (
                    <tr key={txn.transactionId} className="hover:bg-gray-50 border-b">
                      <td className="text-black p-3">{new Date(txn.date).toLocaleDateString()}</td>
                      <td className="text-black p-3">{txn.description || "-"}</td>
                      <td className="p-3 text-black font-semibold">${txn.amount}</td>
                      <td className="text-black p-3">
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

          <div className="mt-6 flex justify-end">
            <button
              onClick={handleDelete}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
            >
              Delete Goal
            </button>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ViewTransactionsByGoalId;
