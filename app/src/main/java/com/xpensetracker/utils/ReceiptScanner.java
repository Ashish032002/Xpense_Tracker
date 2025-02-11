package com.xpensetracker.utils;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptScanner {
    private TextRecognizer recognizer;

    public ReceiptScanner() {
        // Initialize with Latin script recognition
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    public interface ReceiptScanCallback {
        void onScanComplete(ReceiptData receiptData);
        void onScanError(Exception e);
    }

    public void scanReceipt(Bitmap bitmap, ReceiptScanCallback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    ReceiptData data = extractReceiptData(text);
                    callback.onScanComplete(data);
                })
                .addOnFailureListener(callback::onScanError);
    }

    private ReceiptData extractReceiptData(Text text) {
        ReceiptData data = new ReceiptData();
        String fullText = text.getText().toLowerCase();

        // Extract total amount
        Pattern amountPattern = Pattern.compile("(?i)(total|amount|sum)[:\\s]*[₹rRs\\s]*([\\d,.]+)");
        Matcher amountMatcher = amountPattern.matcher(fullText);
        if (amountMatcher.find()) {
            String amount = amountMatcher.group(2).replaceAll("[,\\s]", "");
            try {
                data.setTotalAmount(Double.parseDouble(amount));
            } catch (NumberFormatException e) {
                data.setTotalAmount(0.0);
            }
        }

        // Extract date
        Pattern datePattern = Pattern.compile("(?i)(date)[:\\s]*(\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4})");
        Matcher dateMatcher = datePattern.matcher(fullText);
        if (dateMatcher.find()) {
            data.setDate(dateMatcher.group(2));
        }

        // Extract merchant name (usually at the top of receipt)
        String[] lines = fullText.split("\\n");
        if (lines.length > 0) {
            data.setMerchantName(lines[0].trim());
        }

        return data;
    }

    public static class ReceiptData {
        private double totalAmount;
        private String date;
        private String merchantName;

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getMerchantName() { return merchantName; }
        public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    }
}