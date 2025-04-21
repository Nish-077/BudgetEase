import React from "react";
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

function App() {
  const token = localStorage.getItem("jwt");

  return (
    <Router>
      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
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

        </Routes>
      </div>
    </Router>
  );
}

export default App;
