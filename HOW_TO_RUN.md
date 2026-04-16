# How to Run Your AI Data Dashboard

## Quick Start Guide

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+
- OpenAI API Key

## Step 1: Set Up Environment

### Set OpenAI API Key
```bash
# Windows (Command Prompt)
set OPENAI_API_KEY=sk-your-actual-api-key-here

# Windows (PowerShell)
$env:OPENAI_API_KEY="sk-your-actual-api-key-here"

# Or add to application.properties file
echo "openai.api.key=sk-your-actual-api-key-here" >> demo/src/main/resources/application.properties
```

## Step 2: Start Backend

```bash
# Navigate to backend directory
cd demo

# Clean and compile
mvn clean install

# Start the backend server
mvn spring-boot:run
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v4.0.5)

... 
Started DemoApplication in 2.5 seconds
```

**Backend will run on:** `http://localhost:8080`

## Step 3: Start Frontend (New Terminal)

```bash
# Navigate to frontend directory
cd ai-dashboard

# Install dependencies
npm install

# Start the frontend server
npm start
```

**Expected Output:**
```
Compiled successfully!

You can now view ai-dashboard in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.1.100:3000

Note that the development build is not optimized.
To create a production build, use npm run build.
```

**Frontend will run on:** `http://localhost:3000`

## Step 4: Test the Application

### 1. Open Browser
Navigate to: `http://localhost:3000`

### 2. Upload CSV File
- Click "Choose File" button
- Select the provided `test_data_samples.csv` file
- Click "Upload" button
- You should see "File uploaded successfully!" message

### 3. View Chart
- The chart should automatically display after upload
- You should see bars for Product vs Sales data

### 4. Get AI Analysis
- Click "Analyze" button
- Wait for AI analysis (may take 10-20 seconds)
- You should see insights about your data

## Troubleshooting

### Backend Issues

**Problem: "mvn command not found"**
```bash
# Install Maven or use Maven wrapper
# Check if Maven is installed
mvn --version

# If not installed, download Maven from https://maven.apache.org/
```

**Problem: "Compilation failed"**
```bash
# Check Java version
java -version

# Should be Java 17 or higher
```

**Problem: "OpenAI API key not configured"**
```bash
# Check environment variable
echo %OPENAI_API_KEY%  # Windows
echo $OPENAI_API_KEY   # Linux/Mac

# Set it again if needed
set OPENAI_API_KEY=sk-your-actual-api-key-here
```

**Problem: "Port 8080 already in use"**
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID)
taskkill /PID <PID> /F

# Or change port in application.properties
echo "server.port=8081" >> demo/src/main/resources/application.properties
```

### Frontend Issues

**Problem: "npm command not found"**
```bash
# Install Node.js from https://nodejs.org/
# Check installation
node --version
npm --version
```

**Problem: "Port 3000 already in use"**
```bash
# Kill process on port 3000
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Or use different port
npm start -- --port=3001
```

**Problem: "Module not found"**
```bash
# Clear npm cache and reinstall
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

### API Issues

**Problem: "Connection refused"**
```bash
# Check if backend is running
curl http://localhost:8080/

# Should return 404 (which means server is running)
```

**Problem: "CORS errors"**
```bash
# Check CORS configuration in application.properties
# Should include:
# spring.web.cors.allowed-origins=http://localhost:3000
```

**Problem: "OpenAI API errors"**
```bash
# Test API key manually
curl -H "Authorization: Bearer sk-your-api-key" \
     -H "Content-Type: application/json" \
     -d '{"model":"gpt-3.5-turbo","messages":[{"role":"user","content":"Hello"}]}' \
     https://api.openai.com/v1/chat/completions
```

## Manual Testing with curl

If the UI doesn't work, test the backend directly:

```bash
# 1. Upload CSV
curl -X POST -F "file=@test_data_samples.csv" http://localhost:8080/upload

# 2. Get chart data
curl http://localhost:8080/chart

# 3. Get AI analysis
curl http://localhost:8080/query
```

## Expected Results

### Successful Upload Response:
```
File uploaded successfully! Loaded 10 rows of data.
```

### Successful Chart Response:
```json
{
  "labels": ["Laptop", "Phone", "Tablet", "Monitor", "Keyboard", "Mouse", "Headphones", "Camera", "Speaker", "Charger"],
  "values": [1200, 800, 600, 400, 150, 75, 200, 900, 300, 50]
}
```

### Successful AI Response:
```
Based on the dataset analysis, I can see that Laptop has the highest sales at $1200, followed by Camera at $900. The Accessories category shows lower price points with Mouse being the most affordable at $75. The data suggests a clear pricing strategy difference between Electronics and Accessories categories.
```

## Quick Test Checklist

- [ ] Backend starts without errors
- [ ] Frontend loads at http://localhost:3000
- [ ] CSV file uploads successfully
- [ ] Chart displays data points
- [ ] AI analysis returns insights
- [ ] No error messages in browser console
- [ ] No error messages in backend logs

## What to Do If It Still Doesn't Work

1. **Check the logs** - Look at both backend and browser console for error messages
2. **Use the test files** - Run `test_backend.py` and open `test_frontend.html`
3. **Verify file structure** - Make sure all files are in the correct directories
4. **Check network** - Ensure both servers are accessible
5. **Review configuration** - Verify API keys and CORS settings

## Need More Help?

If you're still having issues:

1. **Check the detailed bug report** - Look at `BUG_ANALYSIS_REPORT.md`
2. **Run the test scripts** - Use `test_backend.py` and `test_frontend.html`
3. **Review the fixes** - Check `FINAL_FIXES_AND_IMPROVEMENTS.md`
4. **Check logs** - Backend logs and browser console will show specific errors

The application should work correctly after following these steps. The most common issues are missing API keys and port conflicts.
