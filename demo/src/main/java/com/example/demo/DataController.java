package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DataController {

    private static final Logger logger = Logger.getLogger(DataController.class.getName());
    
    private List<Map<String, String>> data = new ArrayList<>();

    // UPLOAD CSV
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            logger.info("Processing file: " + file.getOriginalFilename());
            
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String headerLine = br.readLine();
            
            if (headerLine == null) {
                return ResponseEntity.badRequest().body("File is empty or invalid");
            }
            
            String[] headers = headerLine.split(",");
            
            // Clean headers
            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].trim().replaceAll("[^a-zA-Z0-9_]", "");
            }

            data.clear();

            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] values = line.split(",");
                
                if (values.length != headers.length) {
                    logger.warning("Skipping malformed line " + lineCount + ": " + line);
                    continue;
                }
                
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    row.put(headers[i], values[i].trim());
                }
                
                data.add(row);
            }

            logger.info("Successfully loaded " + data.size() + " rows of data");
            return ResponseEntity.ok("File uploaded successfully! Loaded " + data.size() + " rows.");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error uploading file", e);
            return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
        }
    }

    // AI ANALYSIS
    private String analyzeWithAI() {
        // Create default prompt for basic analysis
        StringBuilder dataSummary = new StringBuilder();
        dataSummary.append("Dataset Analysis Request:\n");
        dataSummary.append("Total rows: ").append(data.size()).append("\n");
        dataSummary.append("Columns: ").append(String.join(", ", data.get(0).keySet())).append("\n\n");
        
        // Add sample data (first 5 rows)
        dataSummary.append("Sample data:\n");
        for (int i = 0; i < Math.min(5, data.size()); i++) {
            Map<String, String> row = data.get(i);
            dataSummary.append("Row ").append(i + 1).append(": ").append(row).append("\n");
        }

        String prompt = "Analyze this dataset and provide brief insights (max 200 words):\n" + dataSummary.toString();
        return analyzeWithAI(prompt);
    }

    // AI ANALYSIS WITH CUSTOM PROMPT
    private String analyzeWithAI(String prompt) {
        try {
            logger.info("=== AI ANALYSIS START ===");
            
            if (data.isEmpty()) {
                logger.warning("No data available for analysis");
                return "No data available for analysis";
            }
            
            logger.info("Sending prompt to OpenAI: " + prompt.substring(0, Math.min(200, prompt.length())) + "...");

            // Mock AI analysis for now (OpenAI service not configured)
            String analysis = "Dataset contains " + data.size() + " rows with columns: " + String.join(", ", data.get(0).keySet()) + 
                             ". This appears to be structured data suitable for analysis. Consider the patterns and relationships between different columns for insights.";
            
            logger.info("=== AI ANALYSIS SUCCESS (Mock) ===");
            return analysis;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in AI analysis", e);
            logger.info("=== AI ANALYSIS FAILED ===");
            return "Error analyzing data: " + e.getMessage();
        }
    }

    // QUERY (AI)
    @GetMapping("/query")
    public ResponseEntity<?> query() {
        try {
            logger.info("=== QUERY ENDPOINT CALLED ===");
            
            if (data.isEmpty()) {
                logger.warning("No data available for query");
                return ResponseEntity.badRequest().body("Please upload a CSV file first");
            }
            
            String analysis = analyzeWithAI();
            return ResponseEntity.ok(analysis);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in query endpoint", e);
            return ResponseEntity.internalServerError().body("Error processing query: " + e.getMessage());
        }
    }

    // CUSTOM QUERY ENDPOINT
    @PostMapping("/custom-query")
    public ResponseEntity<?> customQuery(@RequestBody Map<String, String> request) {
        try {
            logger.info("=== CUSTOM QUERY START ===");
            
            String userQuery = request.get("query");
            if (userQuery == null || userQuery.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Query cannot be empty");
            }
            
            if (data.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a CSV file first");
            }

            logger.info("User query: " + userQuery);

            // Create comprehensive data context for the AI
            StringBuilder dataContext = new StringBuilder();
            dataContext.append("You are analyzing a CSV dataset. Here is the complete data:\n\n");
            dataContext.append("Dataset Information:\n");
            dataContext.append("Total rows: ").append(data.size()).append("\n");
            dataContext.append("Columns: ").append(String.join(", ", data.get(0).keySet())).append("\n\n");
            
            // Add all data (limit to reasonable size for API)
            dataContext.append("Complete Dataset:\n");
            int maxRows = Math.min(50, data.size()); // Limit to 50 rows for API limits
            for (int i = 0; i < maxRows; i++) {
                Map<String, String> row = data.get(i);
                dataContext.append("Row ").append(i + 1).append(": ");
                for (String col : row.keySet()) {
                    dataContext.append(col).append("=").append(row.get(col)).append(", ");
                }
                dataContext.setLength(dataContext.length() - 2); // Remove last comma
                dataContext.append("\n");
            }
            
            if (data.size() > 50) {
                dataContext.append("... and ").append(data.size() - 50).append(" more rows\n");
            }
            
            dataContext.append("\nUser Question: ").append(userQuery).append("\n\n");
            dataContext.append("Please answer the user's question based on the data provided. ");
            dataContext.append("Be specific, helpful, and provide insights. If the question cannot be answered with the available data, explain what additional information would be needed.");
            dataContext.append("Keep your response concise but thorough (max 300 words).");

            String analysis = analyzeWithAI(dataContext.toString());
            
            logger.info("=== CUSTOM QUERY SUCCESS ===");
            return ResponseEntity.ok(analysis);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in custom query", e);
            return ResponseEntity.internalServerError().body("Error processing query: " + e.getMessage());
        }
    }

    // CHART DATA (ROBUST AUTO DETECT)
    @GetMapping("/chart")
    public ResponseEntity<?> getChartData() {
        try {
            logger.info("=== CHART DATA GENERATION START ===");
            
            List<String> labels = new ArrayList<>();
            List<Double> values = new ArrayList<>();

            if (data.isEmpty()) {
                logger.warning("No data available for chart");
                Map<String, Object> response = new HashMap<>();
                response.put("labels", labels);
                response.put("values", values);
                return ResponseEntity.ok(response);
            }

            logger.info("Total rows available: " + data.size());
            
            // Get all columns
            Set<String> columns = data.get(0).keySet();
            logger.info("Available columns: " + columns);

            // Analyze each column for data type
            Map<String, Boolean> columnNumericMap = new HashMap<>();
            Map<String, Integer> columnNullCount = new HashMap<>();
            
            for (String col : columns) {
                boolean isNumeric = true;
                int nullCount = 0;
                
                for (Map<String, String> row : data) {
                    String value = row.get(col);
                    if (value == null || value.trim().isEmpty()) {
                        nullCount++;
                        continue;
                    }
                    
                    try {
                        Double.parseDouble(value.trim());
                    } catch (NumberFormatException e) {
                        isNumeric = false;
                    }
                }
                
                columnNumericMap.put(col, isNumeric);
                columnNullCount.put(col, nullCount);
                
                logger.info(String.format("Column '%s': numeric=%s, nulls=%d/%d", 
                    col, isNumeric, nullCount, data.size()));
            }

            // Select best columns for chart
            String labelColumn = null;
            String valueColumn = null;

            // Priority 1: Find best numeric column for values (fewest nulls)
            String bestNumericCol = null;
            int minNulls = Integer.MAX_VALUE;
            for (String col : columns) {
                if (columnNumericMap.get(col) && columnNullCount.get(col) < minNulls) {
                    bestNumericCol = col;
                    minNulls = columnNullCount.get(col);
                }
            }
            valueColumn = bestNumericCol;

            // Priority 2: Find best text column for labels (fewest nulls, not numeric)
            String bestTextCol = null;
            minNulls = Integer.MAX_VALUE;
            for (String col : columns) {
                if (!columnNumericMap.get(col) && columnNullCount.get(col) < minNulls) {
                    bestTextCol = col;
                    minNulls = columnNullCount.get(col);
                }
            }
            labelColumn = bestTextCol;

            // Fallback strategies
            if (labelColumn == null) {
                // Use first non-numeric column or first column
                for (String col : columns) {
                    if (!columnNumericMap.get(col)) {
                        labelColumn = col;
                        break;
                    }
                }
                if (labelColumn == null && columns.size() > 0) {
                    labelColumn = columns.iterator().next();
                }
                logger.warning("Using fallback label column: " + labelColumn);
            }

            if (valueColumn == null) {
                // Use first numeric column or second column
                for (String col : columns) {
                    if (columnNumericMap.get(col)) {
                        valueColumn = col;
                        break;
                    }
                }
                if (valueColumn == null && columns.size() > 1) {
                    Iterator<String> it = columns.iterator();
                    it.next(); // skip first
                    valueColumn = it.next();
                }
                logger.warning("Using fallback value column: " + valueColumn);
            }

            logger.info("Final selection - Label column: " + labelColumn + ", Value column: " + valueColumn);

            if (labelColumn == null || valueColumn == null) {
                logger.severe("Could not determine suitable columns for chart");
                Map<String, Object> response = new HashMap<>();
                response.put("labels", labels);
                response.put("values", values);
                return ResponseEntity.ok(response);
            }

            // Generate chart data with comprehensive logging
            int validRows = 0;
            int skippedRows = 0;
            
            for (int i = 0; i < data.size(); i++) {
                Map<String, String> row = data.get(i);
                
                try {
                    String labelValue = row.get(labelColumn);
                    String valueStr = row.get(valueColumn);
                    
                    logger.fine(String.format("Row %d: label='%s', value='%s'", 
                        i, labelValue, valueStr));
                    
                    // Validate label
                    if (labelValue == null || labelValue.trim().isEmpty()) {
                        logger.fine("Skipping row " + i + ": empty label");
                        skippedRows++;
                        continue;
                    }
                    
                    // Validate and parse value
                    if (valueStr == null || valueStr.trim().isEmpty()) {
                        logger.fine("Skipping row " + i + ": empty value");
                        skippedRows++;
                        continue;
                    }
                    
                    double value = Double.parseDouble(valueStr.trim());
                    
                    // Add to chart data
                    labels.add(labelValue.trim());
                    values.add(value);
                    validRows++;
                    
                    logger.fine(String.format("Added: label='%s', value=%.2f", 
                        labelValue.trim(), value));
                    
                } catch (NumberFormatException e) {
                    logger.fine("Skipping row " + i + ": invalid number format");
                    skippedRows++;
                } catch (Exception e) {
                    logger.fine("Skipping row " + i + ": " + e.getMessage());
                    skippedRows++;
                }
            }

            logger.info(String.format("Chart generation complete: %d valid rows, %d skipped rows", 
                validRows, skippedRows));
            
            // Final validation
            if (labels.isEmpty()) {
                logger.warning("No valid chart data generated");
            } else {
                logger.info("Sample chart data:");
                for (int i = 0; i < Math.min(3, labels.size()); i++) {
                    logger.info(String.format("  [%s] = %.2f", labels.get(i), values.get(i)));
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("labels", labels);
            response.put("values", values);
            
            logger.info("=== CHART DATA GENERATION END ===");

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating chart data", e);
            return ResponseEntity.internalServerError().body("Error generating chart data: " + e.getMessage());
        }
    }
}
