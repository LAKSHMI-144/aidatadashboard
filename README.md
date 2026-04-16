# AI Data Dashboard - React + Spring Boot

A full-stack application that allows users to upload CSV files, visualize data in charts, and get AI-powered insights using OpenAI API.

## Fixed Issues

### Backend Fixes
- **OpenAI API Integration**: Replaced manual HTTP calls with proper OpenAI Java client
- **Environment Variables**: Secure API key management using `OPENAI_API_KEY`
- **Error Handling**: Comprehensive exception handling and logging
- **Data Validation**: Better CSV parsing with column detection
- **Chart Data**: Improved numeric/text column detection for reliable chart generation

### Frontend Fixes
- **JavaScript Syntax**: Fixed critical syntax error (invalid backticks)
- **Error Handling**: Added try-catch blocks for all API calls
- **Chart Rendering**: Added null checks and loading states
- **User Experience**: Better error messages and loading indicators

## Quick Setup

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+
- OpenAI API Key

### Backend Setup

1. **Navigate to backend directory:**
```bash
cd demo
```

2. **Replace the DataController:**
```bash
# Backup original
mv src/main/java/com/example/demo/DataController.java src/main/java/com/example/demo/DataController.java.backup

# Use fixed version
mv src/main/java/com/example/demo/DataController_fixed.java src/main/java/com/example/demo/DataController.java
```

3. **Set OpenAI API Key:**
```bash
# Option 1: Environment Variable (Recommended)
export OPENAI_API_KEY=your_openai_api_key_here

# Option 2: Add to application.properties
echo "openai.api.key=your_openai_api_key_here" >> src/main/resources/application.properties
```

4. **Build and run:**
```bash
mvn clean install
mvn spring-boot:run
```

Backend will run on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory:**
```bash
cd ai-dashboard
```

2. **Replace App.js with fixed version:**
```bash
# Backup original
mv src/App.js src/App.js.backup

# Use fixed version
mv src/App_fixed.js src/App.js
```

3. **Install dependencies and run:**
```bash
npm install
npm start
```

Frontend will run on `http://localhost:3000`

## Testing the Application

### Test Data Flow
1. **Upload a CSV file** with mixed text and numeric columns
2. **Check chart display** - should auto-detect columns
3. **Click "Analyze"** to get AI insights
4. **Verify error handling** - try uploading invalid files

### Sample CSV Format
```csv
Product,Sales,Region
Laptop,1200,North
Phone,800,South
Tablet,600,East
Laptop,1500,West
```

## API Endpoints

### Backend Endpoints
- `POST /upload` - Upload CSV file
- `GET /chart` - Get chart data (labels & values)
- `GET /query` - Get AI analysis of uploaded data

### Response Formats

#### Chart Data Response
```json
{
  "labels": ["Laptop", "Phone", "Tablet"],
  "values": [1200, 800, 600]
}
```

#### AI Analysis Response
```json
"Based on the data, Laptop has the highest sales at $1200..."
```

## Architecture Improvements

### Current Architecture
```
Frontend (React) <---> Backend (Spring Boot) <---> OpenAI API
       |                        |
    Chart.js                CSV Processing
```

### Recommended Improvements

#### 1. Database Integration
```java
// Add JPA dependency
@Entity
public class DataSet {
    @Id @GeneratedValue
    private Long id;
    private String fileName;
    private String jsonData;
    private LocalDateTime uploadTime;
}
```

#### 2. Caching Layer
```java
@Service
public class DataAnalysisService {
    @Cacheable("ai-analysis")
    public String analyzeData(List<Map<String, String>> data) {
        // AI analysis logic
    }
}
```

#### 3. Async Processing
```java
@Async
public CompletableFuture<String> analyzeWithAIAsync() {
    // Long-running AI analysis
}
```

#### 4. Frontend State Management
```javascript
// Use Redux or Context API for better state management
const DataContext = createContext();

function DataProvider({ children }) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  // ... context logic
}
```

#### 5. Input Validation
```java
public class CsvValidator {
    public static void validateFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new FileSizeExceededException("File too large");
        }
        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new InvalidFileTypeException("CSV files only");
        }
    }
}
```

## Troubleshooting

### Common Issues

#### 1. "No AI response received"
- **Cause**: Missing or invalid OpenAI API key
- **Fix**: Set `OPENAI_API_KEY` environment variable
- **Check**: Backend logs for API key errors

#### 2. "Chart not rendering"
- **Cause**: No numeric columns detected
- **Fix**: Ensure CSV has at least one numeric column
- **Debug**: Check browser console for chart data

#### 3. CORS Errors
- **Cause**: Frontend/backend port mismatch
- **Fix**: Ensure backend runs on 8080, frontend on 3000
- **Check**: CORS configuration in application.properties

#### 4. File Upload Issues
- **Cause**: Invalid CSV format or empty file
- **Fix**: Validate CSV format before upload
- **Debug**: Check backend logs for parsing errors

### Debug Commands

#### Backend
```bash
# Check logs
tail -f logs/application.log

# Test endpoints
curl -X GET http://localhost:8080/chart
curl -X GET http://localhost:8080/query
```

#### Frontend
```bash
# Check network requests
# Open browser dev tools -> Network tab

# Test API calls
fetch('http://localhost:8080/chart')
  .then(res => res.json())
  .then(data => console.log(data))
```

## Security Considerations

### Current Vulnerabilities
- API key exposed in source code (fixed)
- No input validation on file uploads
- No rate limiting on API calls

### Recommended Security Fixes

#### 1. Environment Variables
```properties
# application.properties
openai.api.key=${OPENAI_API_KEY}
spring.datasource.password=${DB_PASSWORD}
```

#### 2. Input Validation
```java
@RestController
@Validated
public class DataController {
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCSV(
        @RequestParam("file") 
        @Valid @NotNull MultipartFile file) {
        // Validation logic
    }
}
```

#### 3. Rate Limiting
```java
@RateLimiter(name = "api-limit", fallbackMethod = "rateLimitFallback")
@GetMapping("/query")
public ResponseEntity<?> query() {
    // AI analysis logic
}
```

## Production Deployment

### Docker Setup
```dockerfile
# Dockerfile (Backend)
FROM openjdk:17-jdk-slim
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```dockerfile
# Dockerfile (Frontend)
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

### Environment Variables
```bash
# Production
OPENAI_API_KEY=prod_api_key
DB_URL=jdbc:postgresql://db:5432/aidashboard
DB_USERNAME=appuser
DB_PASSWORD=secure_password
```

## Performance Optimization

### Backend Optimizations
1. **Connection Pooling**: Configure database connection pool
2. **Async Processing**: Use @Async for long-running operations
3. **Caching**: Cache AI responses for repeated queries
4. **Pagination**: Handle large CSV files in chunks

### Frontend Optimizations
1. **Code Splitting**: Lazy load chart components
2. **Debouncing**: Prevent rapid API calls
3. **Virtual Scrolling**: For large datasets
4. **Service Workers**: Cache API responses

## Monitoring & Logging

### Application Monitoring
```java
// Add Actuator dependency
// application.properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=always
```

### Structured Logging
```java
Logger logger = LoggerFactory.getLogger(DataController.class);

logger.info("Processing file: {}", file.getOriginalFilename());
logger.error("Error in AI analysis: {}", e.getMessage(), e);
```

## Next Steps

1. **Add database persistence** for uploaded datasets
2. **Implement user authentication** and authorization
3. **Add more chart types** (line, pie, scatter plots)
4. **Create data export functionality**
5. **Add real-time updates** with WebSocket
6. **Implement comprehensive testing** (unit, integration, e2e)

## Support

For issues:
1. Check the troubleshooting section
2. Review application logs
3. Verify environment variables
4. Test API endpoints independently
