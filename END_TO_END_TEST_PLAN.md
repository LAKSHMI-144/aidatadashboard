# Full-Stack Project Testing Plan

## Data Flow to Test
```
CSV Upload Backend Storage AI API Chart Generation Frontend Display
```

## Test Components

### 1. Backend Analysis
**Files to validate:**
- `DataController.java` - Main controller
- `OpenAIService_fixed.java` - OpenAI integration
- `DemoApplication.java` - Spring Boot entry point

**Potential Issues:**
- Missing dependencies (Jackson, OpenAI client)
- Configuration issues (API key, CORS)
- Class compilation errors
- Service injection failures

### 2. Frontend Analysis  
**Files to validate:**
- `App_chart_fixed.js` - React component
- Chart.js integration
- API calls to backend

**Potential Issues:**
- Network connectivity to backend
- CORS errors
- Chart data validation
- Error handling

### 3. Integration Testing
**API Endpoints to test:**
- `POST /upload` - CSV file upload
- `GET /chart` - Chart data generation
- `GET /query` - AI analysis

## Step-by-Step Testing

### Step 1: Code Analysis
Let me analyze the current state and identify issues:

#### Backend Issues Found:
1. **OpenAI Service Dependency**: The DataController tries to autowire `OpenAIService_fixed` but it may not be properly configured as a Spring bean
2. **Missing Jackson Dependency**: Added to pom.xml but need to verify it's working
3. **API Key Configuration**: Environment variable setup needed

#### Frontend Issues Found:
1. **File Naming**: Using `App_chart_fixed.js` but React expects `App.js`
2. **API Endpoints**: Hardcoded localhost URLs
3. **Error Handling**: Basic error handling implemented

### Step 2: Create Test Scripts

## Test Results and Fixes

### Issue 1: OpenAI Service Not Properly Configured
**Problem**: `OpenAIService_fixed.java` has `@Service` annotation but may not be scanned
**Fix**: Ensure component scanning is working

### Issue 2: Frontend File Mismatch
**Problem**: React app is looking for `App.js` but we created `App_chart_fixed.js`
**Fix**: Need to rename the file properly

### Issue 3: Missing Dependencies
**Problem**: Jackson dependency may not be properly loaded
**Fix**: Verify pom.xml configuration

### Issue 4: Environment Variables
**Problem**: OpenAI API key not configured
**Fix**: Set environment variable properly

## Detailed Testing Commands

### Backend Testing
```bash
# 1. Check if classes compiled
ls -la target/classes/com/example/demo/

# 2. Run application (if Maven available)
mvn spring-boot:run

# 3. Test endpoints manually
curl -X POST -F "file=@test_data_samples.csv" http://localhost:8080/upload
curl http://localhost:8080/chart
curl http://localhost:8080/query
```

### Frontend Testing
```bash
# 1. Check if React app can start
cd ai-dashboard
npm start

# 2. Test API calls from browser console
fetch('http://localhost:8080/chart').then(r => r.json()).then(console.log)
```

## Debug Logging Strategy

### Backend Debug Points
1. **CSV Upload**: Log file processing, row counts, data validation
2. **Chart Generation**: Log column detection, data type analysis
3. **OpenAI API**: Log request format, response parsing, error handling

### Frontend Debug Points
1. **API Calls**: Log request/response, error handling
2. **Chart Rendering**: Log data validation, ChartJS configuration
3. **File Upload**: Log file selection, upload progress

## Expected Data Flow

### Successful Flow:
1. User selects CSV file
2. Frontend sends POST to `/upload`
3. Backend parses CSV, stores in memory
4. Backend returns success message
5. Frontend calls `/chart` to get chart data
6. Backend analyzes columns, returns labels/values
7. Frontend renders ChartJS with data
8. User clicks "Analyze" button
9. Frontend calls `/query` for AI analysis
10. Backend sends data to OpenAI, returns insights
11. Frontend displays AI results

### Failure Points:
1. **CSV Upload Fails**: Invalid file format, parsing errors
2. **Chart Data Empty**: No numeric columns, all null values
3. **OpenAI API Fails**: Invalid API key, network issues, quota exceeded
4. **Frontend Rendering**: Data validation failures, ChartJS errors

## Next Steps

1. **Fix immediate compilation issues**
2. **Test each endpoint independently**
3. **Validate data flow end-to-end**
4. **Add comprehensive logging**
5. **Test error scenarios**

Let me proceed with implementing these fixes and tests.
