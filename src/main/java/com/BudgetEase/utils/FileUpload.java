package com.BudgetEase.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileUpload {

    private final Cloudinary cloudinary;

    public FileUpload() {
        // Load Cloudinary credentials from .env file
        Dotenv dotenv = Dotenv.load();
        String cloudName = dotenv.get("CLOUDINARY_CLOUD_NAME");
        String apiKey = dotenv.get("CLOUDINARY_API_KEY");
        String apiSecret = dotenv.get("CLOUDINARY_API_SECRET");

        // Initialize Cloudinary configuration
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadFile(File file) throws IOException {
        // Validate file type
        if (!isAllowedFileType(file)) {
            throw new IllegalArgumentException("Invalid file type. Only images and PDFs are allowed.");
        }

        // Determine resource type based on file type
        String resourceType = isImageFile(file) ? "image" : "raw";

        // Upload the file to Cloudinary
        @SuppressWarnings("rawtypes")
        Map result = cloudinary.uploader().upload(file, ObjectUtils.asMap("resource_type", resourceType));

        // Return the secure URL of the uploaded file
        return (String) result.get("secure_url");
    }

    private boolean isAllowedFileType(File file) {
        // Check file extension
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") ||
               fileName.endsWith(".gif") || fileName.endsWith(".bmp") || fileName.endsWith(".tiff") ||
               fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx") ||
               fileName.endsWith(".txt");
    }

    private boolean isImageFile(File file) {
        // Check if the file is an image based on its extension
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") ||
               fileName.endsWith(".gif") || fileName.endsWith(".bmp") || fileName.endsWith(".tiff");
    }
}