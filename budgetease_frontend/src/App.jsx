import React, { useRef,useState,useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import UpdateProfile from "./pages/UpdateProfile";
import Profile from "./pages/Profile";
import ChangePassword from "./pages/ChangePassword";
import CreateBudget from "./pages/CreateBudget";
import ViewBudgets from "./pages/GetBudgets";
import UpdateBudget from "./pages/UpdateBudget";
import ViewTransactionsByBudgetId from "./pages/ViewTransactionByBudgetId";
import CreateGoal from "./pages/CreateGoal";
import ViewGoals from "./pages/ViewGoals";
import UpdateGoal from "./pages/UpdateGoal";
import ViewTransactionsByGoalId from "./pages/ViewTransactionByGoalId";
import CreateTransaction from "./pages/CreateTransaction";
import CreateTransactionsFromImage from "./pages/CreateTransactionFromImage";
import UpdateTransaction from "./pages/UpdateTransaction";
import ViewTransactions from "./pages/ViewTransaction";
import ViewRewards from "./pages/ViewRewards";
import ViewNotifications from "./pages/ViewNotifications";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function App() {
  const token = localStorage.getItem("jwt");

  const [message, setMessage] = useState(null);
  const stompClientRef = useRef(null);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => {
        console.log(str);
      },
      onConnect: () => {
        console.log('Connected to WebSocket');
        stompClient.subscribe('/topic/notifications', (response) => {
          console.log('Received message:', response.body);
          setMessage(JSON.parse(response.body).message);
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    stompClient.activate();
    stompClientRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
  }, []);

  useEffect(() => {
    if (message) {
      const timer = setTimeout(() => setMessage(null), 5000); // 5 seconds
      return () => clearTimeout(timer);
    }
  }, [message]);

  return (
    <Router>
      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      {message && (
  <div className="fixed top-5 right-5 bg-blue-600 text-white px-4 py-3 rounded-lg shadow-lg transition-opacity duration-500 opacity-100">
    {message}
  </div>
)}
        <Routes>
          <Route path="/" element={token ? <Navigate to="/profile" /> : <Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/update-profile" element={token ? <UpdateProfile /> : <Navigate to="/login" />} />
          <Route path="/profile" element={token ? <Profile /> : <Navigate to="/login" />} />
          <Route path="/change-password" element={token ? <ChangePassword /> : <Navigate to="/login" />} />
          <Route path="/create-budget" element={token ? <CreateBudget /> : <Navigate to="/login" />} />
          <Route path="/view-budgets" element={token ? <ViewBudgets /> : <Navigate to="/login" />} />
          <Route path="/update-budgets/:id" element={token ? <UpdateBudget /> : <Navigate to="/login" />} />
          <Route path="/view-transaction-by-budgetId/:id" element={token ? <ViewTransactionsByBudgetId /> : <Navigate to="/login" />} />
          <Route path="/create-goal" element={token ? <CreateGoal /> : <Navigate to="/login" />} />
          <Route path="/view-goals" element={token ? <ViewGoals /> : <Navigate to="/login" />} />
          <Route path="/update-goals/:id" element={token ? <UpdateGoal /> : <Navigate to="/login" />} />
          <Route path="/view-transaction-by-goalId/:id" element={token ? <ViewTransactionsByGoalId /> : <Navigate to="/login" />} />
          <Route path="/create-transaction" element={token ? <CreateTransaction /> : <Navigate to="/login" />} />
          <Route path="/create-transaction-image" element={token ? <CreateTransactionsFromImage /> : <Navigate to="/login" />} />
          <Route path="/update-transaction/:id" element={token ? <UpdateTransaction /> : <Navigate to="/login" />} />
          <Route path="/view-transactions" element={token ? <ViewTransactions /> : <Navigate to="/login" />} />
          <Route path="/view-rewards" element={token ? <ViewRewards /> : <Navigate to="/login" />} />
          <Route path="/view-notifications" element={token ? <ViewNotifications /> : <Navigate to="/login" />} />

        </Routes>
      </div>
    </Router>
  );
}

export default App;
