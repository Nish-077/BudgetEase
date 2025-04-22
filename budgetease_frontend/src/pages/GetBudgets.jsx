import React, { useState, useEffect } from "react";
import { getBudgets } from "../api/budgetService";
import { getOverdue, getProgress } from "../api/financialTargetService";
import DashboardLayout from "../components/DashBoard";
import { useNavigate } from "react-router-dom";

const ViewBudgets = () => {
  const [budgets, setBudgets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedBudget, setSelectedBudget] = useState(null);
  const [progress, setProgress] = useState(0);
  const [overdueMap, setOverdueMap] = useState({});
  const [progressLoading, setProgressLoading] = useState(false);
  const [progressError, setProgressError] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const fetchBudgets = async () => {
      try {
        const response = await getBudgets();
        setBudgets(response);
        if (response.length > 0) setSelectedBudget(response[0]);

        const overdueStatus = {};
        for (const budget of response) {
          try {
            const res = await getOverdue(budget.budgetId, "budget");
            console.log("IS IT OVERDUE? ",res.isOverdue);
            
            overdueStatus[budget.budgetId] = res.isOverdue;
          } catch (err) {
            console.error(`Error checking overdue for budget ${budget.budgetId}:`, err);
            overdueStatus[budget.budgetId] = false;
          }
        }
        setOverdueMap(overdueStatus);
      } catch (err) {
        console.error("Budget fetch error:", err);
        setError("Failed to load budgets. Please try again.");
      } finally {
        setLoading(false);
      }
    };
    fetchBudgets();
  }, []);

  useEffect(() => {
    const fetchProgress = async () => {
      if (!selectedBudget?.budgetId) return;

      setProgressLoading(true);
      setProgressError("");

      try {
        const response = await getProgress(selectedBudget.budgetId, "budget");
        setProgress(response.progress);
      } catch (err) {
        console.error("Progress fetch error:", err);
        setProgressError("Failed to load progress data");
      } finally {
        setProgressLoading(false);
      }
    };
    fetchProgress();
  }, [selectedBudget]);

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-green-50 to-teal-100">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-full">
          {/* Budget List Panel */}
          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-1 overflow-y-auto">
            <h2 className="text-2xl font-bold text-green-600 mb-6">Your Budgets</h2>
            {error && (
              <div className="mb-4 p-3 bg-red-50 text-red-600 rounded-lg flex items-center">
                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                    d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                {error}
              </div>
            )}

            {loading ? (
              <div className="flex justify-center items-center h-32">
                <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-green-500"></div>
              </div>
            ) : budgets.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                <p>No budgets found</p>
                <button
                  onClick={() => navigate("/create-budget")}
                  className="mt-4 text-green-600 hover:text-green-800 font-medium"
                >
                  Create your first budget
                </button>
              </div>
            ) : (
              <ul className="space-y-3">
                {budgets.map((budget) => {
                  const isSelected = selectedBudget?.budgetId === budget.budgetId;
                  const isOverdue = overdueMap[budget.budgetId];

                  console.log("INSIDE COMPONENT ISOVERDUE ",isOverdue);

                  return (
                    <li
                      key={budget.budgetId}
                      className={`p-4 rounded-lg cursor-pointer transition-colors border-l-4 ${
                        isSelected
                          ? "bg-green-100 border-green-500"
                          : isOverdue
                          ? "bg-red-100 border-red-500"
                          : "hover:bg-gray-50 border-transparent"
                      }`}
                      onClick={() => setSelectedBudget(budget)}
                    >
                      <div className="flex justify-between items-start">
                        <div>
                          <h3 className="font-bold text-lg flex items-center">
                            {budget.name}
                            {isOverdue && (
                              <span className="ml-2 text-xs text-red-600 bg-red-200 px-2 py-0.5 rounded-full">
                                ⚠️ Overdue
                              </span>
                            )}
                          </h3>
                          <p className="text-sm text-gray-600">{budget.categoryName}</p>
                        </div>
                        <span className="font-semibold text-green-600">
                          ${budget.allocatedAmount}
                        </span>
                      </div>
                      {budget.description && (
                        <p className="mt-2 text-sm text-gray-500">{budget.description}</p>
                      )}
                    </li>
                  );
                })}
              </ul>
            )}
          </div>

          {/* Budget Detail Panel */}
          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-2">
            {selectedBudget ? (
              <>
                <div className="flex justify-between items-center mb-6">
                  <h2 className="text-2xl font-bold text-green-600">{selectedBudget.name}</h2>
                  <span className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                    {selectedBudget.categoryName}
                  </span>
                </div>

                <div className="mb-6">
                  <h3 className="font-medium text-gray-700 mb-3">Budget Progress</h3>
                  {progressLoading ? (
                    <div className="flex items-center">
                      <svg className="animate-spin h-5 w-5 mr-2 text-blue-500" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                      <span>Loading progress...</span>
                    </div>
                  ) : progressError ? (
                    <p className="text-red-500 text-sm">{progressError}</p>
                  ) :overdueMap[selectedBudget.budgetId] ? (
                    <div className="p-3 bg-red-100 text-red-700 rounded-lg">
                      ⚠️ This budget is overdue.
                    </div>
                  ): (
                    <>
                      <div className="w-full bg-gray-200 rounded-full h-4">
                        <div className="bg-green-500 h-4 rounded-full" style={{ width: `${progress}%` }}></div>
                      </div>
                      <div className="flex justify-between text-sm text-gray-600 mt-2">
                        <span>{progress}% of budget used</span>
                        <span>{100 - progress}% remaining</span>
                      </div>
                    </>
                  )}
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <h3 className="font-medium text-gray-700 mb-2">Allocated Amount</h3>
                    <p className="text-2xl font-bold text-green-600">
                      ${selectedBudget.allocatedAmount}
                    </p>
                  </div>
                </div>

                {selectedBudget.description && (
                  <div className="mb-6">
                    <h3 className="font-medium text-gray-700 mb-2">Description</h3>
                    <p className="text-gray-600">{selectedBudget.description}</p>
                  </div>
                )}

                <div className="flex space-x-4">
                  <button
                    onClick={() => navigate(`/update-budgets/${selectedBudget.budgetId}`)}
                    className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                  >
                    Edit Budget
                  </button>
                  <button
                    onClick={() => navigate(`/view-transaction-by-budgetId/${selectedBudget.budgetId}`)}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    View Transactions
                  </button>
                </div>
              </>
            ) : (
              <div className="flex flex-col items-center justify-center h-full text-gray-500">
                <svg className="w-16 h-16 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5v14m0 0l4-4m-4 4l-4-4" />
                </svg>
                <p>Select a budget to view details</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ViewBudgets;
