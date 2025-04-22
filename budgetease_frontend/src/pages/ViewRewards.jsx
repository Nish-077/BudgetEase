import React, { useState, useEffect } from "react";
import { getRewards } from "../api/rewardService";
import DashboardLayout from "../components/DashBoard";
import { useParams } from "react-router-dom";

const ViewRewards = () => {
  const { userId } = useParams();
  const [rewardGroups, setRewardGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchRewards = async () => {
      try {
        const response = await getRewards(userId);
        console.log(response);
        setRewardGroups(response);
      } catch (err) {
        setError("Failed to load rewards. Please try again.");
        console.error("Reward fetch error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchRewards();
  }, [userId]);

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-purple-50 to-pink-100">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-full">
          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-3 overflow-y-auto">
            <h2 className="text-3xl font-bold text-purple-600 mb-6">Your Rewards</h2>

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
            ) : rewardGroups.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                <p>No rewards found</p>
              </div>
            ) : (
              <ul className="space-y-8">
                {rewardGroups.map((group, groupIndex) => (
                  <li
                    key={groupIndex}
                    className="p-6 rounded-2xl bg-white shadow-lg border border-purple-100"
                  >
                    <div className="mb-4 flex justify-between items-center">
                      <h3 className="text-xl font-bold text-purple-700">User Rewards</h3>
                      <span className="text-purple-600 font-semibold">
                        Total: {group.totalPoints} Points
                      </span>
                    </div>

                    <ul className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                      {group.rewards.map((reward, rewardIndex) => (
                        <li
                          key={rewardIndex}
                          className="p-4 bg-gradient-to-r from-purple-50 to-pink-50 rounded-xl shadow-md border border-purple-200 hover:shadow-lg transition-shadow"
                        >
                          <div className="flex justify-between items-center mb-2">
                            <h4 className="font-semibold text-gray-800">{reward.name}</h4>
                            {reward.points !== undefined && (
                              <span className="text-purple-600 font-bold">
                                {reward.points} Points
                              </span>
                            )}
                          </div>

                          <p className="text-sm text-gray-600">Type: {reward.rewardType}</p>

                          {reward.badgeLevel && (
                            <div className="mt-2 inline-block px-2 py-1 text-xs font-medium rounded bg-purple-100 text-purple-700">
                              Badge: {reward.badgeLevel}
                            </div>
                          )}
                        </li>
                      ))}
                    </ul>
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

export default ViewRewards;
