import React, { useState, useEffect } from "react";
import { getAllNotifications, markAsRead } from "../api/notificationService";
import DashboardLayout from "../components/DashBoard";

const ViewNotifications = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await getAllNotifications();
        console.log(response['notifications']);
        setNotifications(response['notifications']); // Assuming response contains notifications array
      } catch (err) {
        setError("Failed to load notifications. Please try again.");
        console.error("Notification fetch error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchNotifications();
  }, []);

  const handleMarkAsRead = async (notificationId) => {
    try {
      await markAsRead(notificationId);
      setNotifications(notifications.map(notification =>
        notification.notificationId === notificationId
          ? { ...notification, read: true } // Assuming there's a 'read' field
          : notification
      ));
    } catch (err) {
      console.error("Error marking notification as read:", err);
    }
  };

  return (
    <DashboardLayout>
      <div className="flex-1 p-6 overflow-auto bg-gradient-to-br from-purple-50 to-pink-100">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-full">
          <div className="bg-white rounded-2xl shadow-lg p-6 lg:col-span-3 overflow-y-auto">
            <h2 className="text-3xl font-bold text-purple-600 mb-6">Your Notifications</h2>

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
            ) : notifications.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                <p>No notifications found</p>
              </div>
            ) : (
              <ul className="space-y-8">
                {notifications.map((notification, index) => (
                  <li
                    key={index}
                    className="p-6 rounded-2xl bg-white shadow-lg border border-purple-100"
                  >
                    <div className="mb-4 flex justify-between items-center">
                      <h3 className="text-xl font-bold text-purple-700">{notification.type}</h3>
                      <span className={`text-${notification.level.toLowerCase()}-600 font-semibold`}>
                        {notification.level}
                      </span>
                    </div>

                    <div className="text-sm text-gray-600">{notification.message}</div>

                    <div className="mt-4">
                      <button
                        className="text-purple-600 hover:text-purple-800 font-semibold"
                        onClick={() => handleMarkAsRead(notification.notificationId)}
                        disabled={notification.read} // Disable if already marked as read
                      >
                        {notification.read ? "Read" : "Mark as Read"}
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

export default ViewNotifications;
