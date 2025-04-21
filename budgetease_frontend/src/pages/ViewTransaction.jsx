import React, { useState, useEffect } from "react";
import { getAllTransactions,deleteTransaction } from "../api/transactionService";
import DashboardLayout from "../components/DashBoard";
import { useNavigate, useParams } from "react-router-dom";

const ViewTransactions = () => {
  const { goalId } = useParams();
  const navigate = useNavigate();

  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await getAllTransactions();
        setTransactions(response);
      } catch (err) {
        setError("Failed to load transactions. Please try again.");
        console.error("Transaction fetch error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, []);

  const handleDelete = async (transactionId) => {
    try {
      await deleteTransaction(transactionId); // Call the delete API
      setTransactions(transactions.filter((transaction) => transaction.transactionId !== transactionId)); // Remove it from the local state
    } catch (err) {
      setError("Failed to delete the transaction. Please try again.");
      console.error("Delete transaction error:", err);
    }
  };

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-purple-50 to-pink-100">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-full">
          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-3 overflow-y-auto">
            <h2 className="text-2xl font-bold text-purple-600 mb-6">Transactions</h2>

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
              <div className="text-center py-8 text-gray-500">
                <p>No transactions found</p>
              </div>
            ) : (
              <ul className="space-y-3">
                {transactions.map((transaction) => (
                  <li key={transaction.transactionId} className="p-4 rounded-lg border bg-white hover:bg-gray-50">
                    <div className="flex justify-between items-center">
                      <div>
                        <h3 className="font-bold text-lg">{transaction.categoryName}</h3>
                        <p className="text-sm text-gray-600">{transaction.description}</p>
                        <p className="text-sm text-gray-500">{new Date(transaction.date).toLocaleDateString()}</p>
                      </div>
                      <div className="flex flex-col items-end">
                        <span className="font-semibold text-purple-600">${transaction.amount}</span>
                        <span className={`mt-2 text-sm px-2 py-1 rounded-full ${transaction.status === 'SUCCESS' ? 'bg-green-100 text-green-700' : transaction.status === 'FAILED' ? 'bg-red-100 text-red-700' : 'bg-yellow-100 text-yellow-700'}`}>
                          {transaction.status}
                        </span>
                        <p className="text-sm text-gray-600 mt-1">{transaction.merchant}</p>
                      </div>
                    </div>

                    <div className="flex space-x-4 mt-4">
                      <button
                        onClick={() => navigate(`/update-transaction/${transaction.transactionId}`)}
                        className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(transaction.transactionId)}
                        className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                      >
                        Delete
                      </button>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ViewTransactions;
