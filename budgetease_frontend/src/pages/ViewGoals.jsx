import React, { useState, useEffect } from "react";
import { getGoals, deleteGoal } from "../api/goalService";
import { getProgress } from "../api/financialTargetService";
import DashboardLayout from "../components/DashBoard";
import { useNavigate } from "react-router-dom";

const ViewGoals = () => {
  const [goals, setGoals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedGoal, setSelectedGoal] = useState(null);
  const [progress, setProgress] = useState(0);
  const [progressLoading, setProgressLoading] = useState(false);
  const [progressError, setProgressError] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const fetchGoals = async () => {
      try {
        const response = await getGoals();
        setGoals(response);
        if (response.length > 0) {
          setSelectedGoal(response[0]);
        }
      } catch (err) {
        setError("Failed to load goals. Please try again.");
        console.error("Goal fetch error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchGoals();
  }, []);

  useEffect(() => {
    const fetchProgress = async () => {
      if (!selectedGoal?.goalId) return;

      setProgressLoading(true);
      setProgressError("");
      try {
        const response = await getProgress(selectedGoal.goalId,"goal");
        setProgress(response.progress);
      } catch (err) {
        setProgressError("Failed to load goal progress.");
        console.error("Goal progress fetch error:", err);
      } finally {
        setProgressLoading(false);
      }
    };

    fetchProgress();
  }, [selectedGoal]);

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-purple-50 to-pink-100">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-full">
          
          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-1 overflow-y-auto">
            <h2 className="text-2xl font-bold text-purple-600 mb-6">Your Goals</h2>

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
            ) : goals.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                <p>No goals found</p>
                <button 
                  onClick={() => navigate("/create-goal")} 
                  className="mt-4 text-purple-600 hover:text-purple-800 font-medium"
                >
                  Set your first goal
                </button>
              </div>
            ) : (
              <ul className="space-y-3">
                {goals.map((goal) => (
                  <li
                    key={goal.goalId}
                    className={`p-4 rounded-lg cursor-pointer transition-colors ${
                      selectedGoal?.goalId === goal.goalId
                        ? 'bg-purple-100 border-l-4 border-purple-500'
                        : 'hover:bg-gray-50'
                    }`}
                    onClick={() => setSelectedGoal(goal)}
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-bold text-lg">{goal.name}</h3>
                        <p className="text-sm text-gray-600">{goal.categoryName}</p>
                      </div>
                      <span className="font-semibold text-purple-600">
                        ${goal.targetAmount}
                      </span>
                    </div>
                    {goal.description && (
                      <p className="mt-2 text-sm text-gray-500">{goal.description}</p>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-2">
            {selectedGoal ? (
              <>
                <div className="flex justify-between items-center mb-6">
                  <h2 className="text-2xl font-bold text-purple-600">{selectedGoal.name}</h2>
                  <span className="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-sm font-medium">
                    {selectedGoal.categoryName}
                  </span>
                </div>

                <div className="mb-6">
                  <h3 className="font-medium text-gray-700 mb-3">Goal Progress</h3>
                  {progressLoading ? (
                    <div className="flex items-center">
                      <svg className="animate-spin h-5 w-5 mr-2 text-blue-500" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                      <span>Loading progress...</span>
                    </div>
                  ) : progressError ? (
                    <p className="text-red-500 text-sm">{progressError}</p>
                  ) : (
                    <>
                      <div className="w-full bg-gray-200 rounded-full h-4">
                        <div 
                          className="bg-purple-500 h-4 rounded-full"
                          style={{ width: `${progress}%` }}
                        ></div>
                      </div>
                      <div className="flex justify-between text-sm text-gray-600 mt-2">
                        <span>{progress}% achieved</span>
                        <span>{100 - progress}% remaining</span>
                      </div>
                    </>
                  )}
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <h3 className="font-medium text-gray-700 mb-2">Target Amount</h3>
                    <p className="text-2xl font-bold text-purple-600">${selectedGoal.targetAmount}</p>
                  </div>
                </div>

                {selectedGoal.description && (
                  <div className="mb-6">
                    <h3 className="font-medium text-gray-700 mb-2">Description</h3>
                    <p className="text-gray-600">{selectedGoal.description}</p>
                  </div>
                )}

                <div className="flex space-x-4">
                  <button
                    onClick={() => navigate(`/update-goals/${selectedGoal.goalId}`)}
                    className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
                  >
                    Edit Goal
                  </button>
                  <button
                    onClick={() => navigate(`/view-transaction-by-goalId/${selectedGoal.goalId}`)}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    View Transactions
                  </button>
                </div>
              </>
            ) : (
              <div className="flex flex-col items-center justify-center h-full text-gray-500">
                <svg className="w-16 h-16 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5v14m0 0l4-4m-4 4l-4-4"></path>
                </svg>
                <p>Select a goal to view details</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ViewGoals;
