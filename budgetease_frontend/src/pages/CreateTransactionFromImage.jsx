import React, { useState } from "react";
import { createTransactionsFromImage } from "../api/transactionService";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashBoard";

const CreateTransactionsFromImage = () => {
  const [file, setFile] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setError("");
    setTransactions([]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      setError("Please upload an image file.");
      return;
    }

    setLoading(true);
    setError("");
    try {
      const result = await createTransactionsFromImage(file);
      setTransactions(result);
    } catch (err) {
      console.error("Error creating transactions from image:", err);
      setError("Failed to process the image. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <DashboardLayout>
      <div className="flex justify-center items-center min-h-screen bg-gradient-to-br from-green-50 to-teal-100 p-6">
        <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-2xl">
          <h2 className="text-2xl font-bold text-green-600 mb-6">Extract Transactions from Image</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-gray-700">Upload Receipt / Image</label>
              <input
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                className="w-full mt-2 p-3 border rounded-lg text-black focus:outline-none focus:ring-2 focus:ring-green-500"
              />
            </div>

            {error && <p className="text-red-600">{error}</p>}

            <button
              type="submit"
              className="w-full py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
              disabled={loading}
            >
              {loading ? "Processing..." : "Extract Transactions"}
            </button>
          </form>

          {transactions.length > 0 && (
            <div className="mt-8">
              <h3 className="text-xl font-semibold text-green-700 mb-3">Extracted Transactions:</h3>
              <ul className="space-y-2">
                {transactions.map((tx, index) => (
                  <li key={index} className="p-4 text-black bg-green-50 rounded-lg shadow">
                    <div><strong>Name:</strong> {tx.transactionName}</div>
                    <div><strong>Amount:</strong> ${tx.amount}</div>
                    <div><strong>Type:</strong> {tx.type}</div>
                    <div><strong>Date:</strong> {new Date(tx.date).toLocaleString()}</div>
                    <div><strong>Description:</strong> {tx.description}</div>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CreateTransactionsFromImage;
