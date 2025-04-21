import React from "react";

const DeleteConfirmationModal = ({ isOpen, onClose, onConfirm, budgetName }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
      <div className="bg-white rounded-2xl shadow-2xl p-6 max-w-sm w-full transform transition-all">
        <h3 className="text-xl font-semibold text-red-600 mb-2">Delete Budget</h3>
        <p className="text-gray-700 mb-6">
          Are you sure you want to delete <span className="font-bold">{budgetName}</span>? This action cannot be undone.
        </p>
        <div className="flex justify-end gap-3">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-lg bg-gray-200 text-gray-700 hover:bg-gray-300 transition"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 rounded-lg bg-red-500 text-white hover:bg-red-600 transition"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteConfirmationModal;
