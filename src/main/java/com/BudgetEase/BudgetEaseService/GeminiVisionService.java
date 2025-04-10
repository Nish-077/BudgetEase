package com.BudgetEase.BudgetEaseService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.BudgetEase.Models.PaymentStatus;
import com.BudgetEase.Models.Transaction;
import com.BudgetEase.Models.TransactionType;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class GeminiVisionService {

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<Transaction> extractTransactionsFromImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("Image file cannot be empty");
            }

            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            Dotenv dotenv = Dotenv.load();
            String apiKey = dotenv.get("GEMINI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalStateException("Gemini API key not found");
            }

            String todaysDate = LocalDate.now().format(DATE_FORMATTER);

            String prompt = String.format("""
                Extract ALL visible transactions from this screenshot of a payment history list.

                Return a JSON array. Each object must include:
                {
                \"merchant\": string,
                \"amount\": number,
                \"date\": string,
                \"type\": \"EXPENSE\" | \"INCOME\",
                \"status\": \"FAILED\" | \"SUCCESS\",
                \"description\": string
                }

                Important:
                - Do not omit the 'status' field
                - Do not omit the 'merchant' field
                - If negative amount encountered, type: "EXPENSE" and amount: absolute value of negative amount 
                - 'Failed' transactions must be detected and status set to 'FAILED'
                - If transaction set to 'FAILED', do not include type field at all
                - Return empty array if no valid transactions are found
                - Only include entries where the amount is visible
                - Output must be valid JSON array
                Assume today's date is %s for relative date conversion.
                """,todaysDate);

            JSONObject requestJson = buildRequest(prompt, base64Image);
            JSONObject response = sendToGeminiAPI(requestJson, apiKey);
            return parseTransactionResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process image: " + e.getMessage(), e);
        }
    }

    private JSONObject buildRequest(String prompt, String base64Image) {
        JSONObject imagePart = new JSONObject()
            .put("mimeType", "image/jpeg")
            .put("data", base64Image);

        JSONObject content = new JSONObject()
            .put("parts", List.of(
                new JSONObject().put("text", prompt),
                new JSONObject().put("inlineData", imagePart)
            ));

        return new JSONObject().put("contents", List.of(content));
    }

    private JSONObject sendToGeminiAPI(JSONObject request, String apiKey) throws IOException {
        URL url = new URL(GEMINI_API_URL + "?key=" + apiKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setConnectTimeout(30000);
        con.setReadTimeout(60000);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            os.write(request.toString().getBytes());
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getResponseCode() < 400 ?
                    con.getInputStream() : con.getErrorStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        if (con.getResponseCode() >= 400) {
            try {
                JSONObject errorJson = new JSONObject(response.toString());
                throw new IOException("Gemini API error: " +
                    errorJson.optString("message", response.toString()));
            } catch (Exception e) {
                throw new IOException("Gemini API error: " + response);
            }
        }

        return new JSONObject(response.toString());
    }

    private List<Transaction> parseTransactionResponse(JSONObject geminiResponse) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            String jsonText = geminiResponse
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .replace("```json", "").replace("```", "").trim();

            JSONArray transactionsData = new JSONArray(jsonText);

            for (int i = 0; i < transactionsData.length(); i++) {
                JSONObject t = transactionsData.getJSONObject(i);
                Transaction transaction = new Transaction();

                transaction.setMerchant(t.optString("merchant", "Unknown"));
                transaction.setAmount(t.optDouble("amount", 0.0));
                transaction.setStatus(parPaymentStatus(t.optString("status", "SUCCESS")));
                transaction.setDescription(t.optString("description", null));
                transaction.setType(parseTransactionType(t.optString("type", "EXPENSE")));

                String dateStr = t.optString("date", LocalDate.now().format(DATE_FORMATTER));
                transaction.setDate(parseDateTime(dateStr, "00:00"));

                transactions.add(transaction);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse transaction data: " + e.getMessage(), e);
        }

        return transactions;
    }

    private LocalDateTime parseDateTime(String dateStr, String timeStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            String[] timeParts = timeStr.split(":");
            int hour = timeParts.length > 0 ? Integer.parseInt(timeParts[0]) : 0;
            int minute = timeParts.length > 1 ? Integer.parseInt(timeParts[1]) : 0;
            return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), hour, minute);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    private TransactionType parseTransactionType(String typeStr) {
        try {
            return TransactionType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TransactionType.EXPENSE;
        }
    }

    private PaymentStatus parPaymentStatus(String typeStr){
        try {
            return PaymentStatus.valueOf(typeStr.toUpperCase());
        } catch (Exception e) {
            return PaymentStatus.SUCCESS;
        }
    }
}
