# Backend Deployment Guide - Complete Your Full-Stack App

## Backend Deployment Options

I'll show you the best options to deploy your Spring Boot backend and connect it to your Vercel frontend.

## Option 1: Railway (Recommended - $5/month)
**Perfect for:** Easy deployment, database included, great for Java apps

### Why Railway?
- **Java/Spring Boot support** - Auto-detects your app
- **Database included** - PostgreSQL ready to use
- **Environment variables** - Easy configuration
- **Custom domains** - Professional URLs
- **Automatic HTTPS** - SSL included
- **Easy scaling** - Grow as needed

### Step-by-Step Railway Deployment:

#### 1. Create Railway Account
1. Go to [railway.app](https://railway.app)
2. Click "Sign Up"
3. Choose "Continue with GitHub"
4. Authorize Railway to access your GitHub

#### 2. Deploy Backend
1. Click "New Project"
2. Select "Deploy from GitHub repo"
3. Choose your `aidatadashboard` repository
4. Railway will auto-detect Spring Boot
5. Click "Deploy"

#### 3. Configure Backend
1. **Set Root Directory**: `demo`
2. **Build Command**: `./mvnw clean package`
3. **Start Command**: `java -jar target/*.jar`
4. **Port**: 8080

#### 4. Environment Variables
Add these in Railway dashboard:
```bash
OPENAI_API_KEY=sk-your-api-key-here
SPRING_PROFILES_ACTIVE=production
PORT=8080
```

#### 5. Get Your Backend URL
Railway will give you a URL like:
`https://your-app-name.railway.app`

---

## Option 2: Render (Alternative - $7/month)
**Perfect for:** Great Java support, generous free tier

### Step-by-Step Render Deployment:

#### 1. Create Render Account
1. Go to [render.com](https://render.com)
2. Sign up with GitHub
3. Authorize Render access

#### 2. Create Web Service
1. Click "New" -> "Web Service"
2. Connect your GitHub repository
3. Set **Root Directory**: `demo`
4. **Runtime**: Java 17
5. **Build Command**: `./mvnw clean package`
6. **Start Command**: `java -jar target/*.jar`

#### 3. Environment Variables
```bash
OPENAI_API_KEY=sk-your-api-key-here
SPRING_PROFILES_ACTIVE=production
PORT=8080
```

---

## Option 3: AWS EC2 (Advanced - $10-50/month)
**Perfect for:** Full control, enterprise features

### Step-by-Step AWS Deployment:

#### 1. Launch EC2 Instance
1. Go to AWS Console
2. Launch EC2 instance (t2.micro or t3.small)
3. Choose Ubuntu 22.04 AMI
4. Configure security groups (ports 22, 80, 8080)
5. Create key pair for SSH access

#### 2. Setup Server
```bash
# SSH into your instance
ssh -i your-key.pem ubuntu@your-ip

# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Install Maven
sudo apt install maven -y

# Clone your repository
git clone https://github.com/LAKSHMI-144/aidatadashboard.git
cd aidatadashboard/demo

# Build and run
./mvnw clean package
java -jar target/*.jar
```

---

## Option 4: Docker + Cloud Platform (Professional)
**Perfect for:** Containerized deployment, portability

### Step-by-Step Docker Deployment:

#### 1. Build Docker Image
```bash
cd demo
docker build -t ai-dashboard-backend .
```

#### 2. Deploy to Platform
- **AWS ECS**: Elastic Container Service
- **Google Cloud Run**: Serverless containers
- **Azure Container Instances**: Easy container hosting

---

## My Recommendation: Railway

**Why Railway is best for you:**
- **Easiest setup** - Auto-detects Spring Boot
- **Database included** - PostgreSQL ready
- **Great pricing** - $5/month starts
- **Professional URL** - `your-app.railway.app`
- **Easy scaling** - Grow as needed
- **Great Java support** - Optimized for Spring Boot

---

## Complete Railway Deployment Steps

### Step 1: Prepare Backend for Production

#### Update application.properties
```properties
# demo/src/main/resources/application-prod.properties
server.port=${PORT:8080}
spring.profiles.active=prod
logging.level.root=WARN
logging.level.com.example.demo=INFO

# CORS for production
cors.allowed-origins=https://aidatadashboard.vercel.app
```

#### Add Railway Configuration
Create `demo/railway.json`:
```json
{
  "build": {
    "builder": "NIXPACKS"
  }
}
```

### Step 2: Deploy to Railway

1. **Go to**: [railway.app](https://railway.app)
2. **Sign up** with GitHub
3. **Click**: "New Project"
4. **Select**: Your `aidatadashboard` repository
5. **Configure**:
   - Root Directory: `demo`
   - Build Command: `./mvnw clean package`
   - Start Command: `java -jar target/*.jar`
6. **Click**: "Deploy"

### Step 3: Configure Environment Variables

In Railway dashboard, add:
```bash
OPENAI_API_KEY=sk-your-api-key-here
SPRING_PROFILES_ACTIVE=production
PORT=8080
```

### Step 4: Get Your Backend URL

After deployment, Railway will give you:
`https://your-backend-name.railway.app`

### Step 5: Update Frontend to Use Production Backend

#### Update React App
In `ai-dashboard/src/App.js`, update API URLs:
```javascript
// Add this at the top of App.js
const API_BASE_URL = process.env.NODE_ENV === 'production' 
  ? 'https://your-backend-name.railway.app' 
  : 'http://localhost:8080';

// Update all API calls to use this base URL
// For example:
axios.post(`${API_BASE_URL}/upload`, formData)
axios.get(`${API_BASE_URL}/chart`)
axios.post(`${API_BASE_URL}/custom-query`, { query })
```

#### Add Environment Variable to Vercel
1. Go to Vercel dashboard
2. Settings -> Environment Variables
3. Add: `REACT_APP_API_URL=https://your-backend-name.railway.app`
4. Redeploy frontend

### Step 6: Test Full-Stack Integration

1. **Upload a CSV file** in your Vercel app
2. **Check if data reaches backend** (should work now)
3. **Test natural language queries** with real backend
4. **Verify chart generation** from backend data

---

## Full-Stack URLs After Deployment

### Your Complete Application:
- **Frontend**: `https://aidatadashboard.vercel.app`
- **Backend**: `https://your-app-name.railway.app`
- **GitHub**: `https://github.com/LAKSHMI-144/aidatadashboard`

### API Endpoints:
- `POST https://your-app-name.railway.app/upload`
- `GET https://your-app-name.railway.app/chart`
- `POST https://your-app-name.railway.app/custom-query`

---

## Cost Summary

### Railway (Recommended):
- **Backend**: $5/month
- **Database**: Included
- **Bandwidth**: Generous free tier
- **Total**: $5/month

### Render:
- **Backend**: $7/month (after free tier)
- **Database**: $7/month
- **Total**: $14/month

### AWS EC2:
- **Server**: $10-20/month
- **Database**: $15-50/month
- **Total**: $25-70/month

---

## Troubleshooting

### Common Issues:

#### 1. "Build Failed" on Railway
- Check if `pom.xml` is in `demo` folder
- Verify Java version compatibility
- Check Maven wrapper permissions

#### 2. "Connection Refused" from Frontend
- Verify CORS settings in backend
- Check if backend URL is correct
- Ensure backend is running

#### 3. "Environment Variables Not Working"
- Check variable names match exactly
- Redeploy after adding variables
- Verify Railway dashboard settings

#### 4. "CORS Errors"
- Update CORS allowed origins
- Include your Vercel domain
- Restart backend after changes

---

## Success Indicators

### When Backend is Successfully Deployed:
- [x] Railway shows "Running" status
- [x] Backend URL responds to health checks
- [x] Frontend can upload files successfully
- [x] Natural language queries work
- [x] Charts display real data
- [x] No CORS errors in browser console

---

## Next Steps After Backend Deployment

### 1. Test Everything
- Upload CSV files
- Test all API endpoints
- Verify natural language queries
- Check chart generation

### 2. Add Real OpenAI API
- Get API key from OpenAI
- Add to Railway environment variables
- Test real AI responses

### 3. Monitor Performance
- Set up Railway monitoring
- Check error logs
- Monitor API usage

### 4. Scale if Needed
- Upgrade Railway plan
- Add load balancing
- Consider database optimization

---

## Ready to Deploy?

**Choose Railway for the easiest and most cost-effective solution:**

1. **Go to**: [railway.app](https://railway.app)
2. **Deploy**: Your backend in 5 minutes
3. **Connect**: Frontend to backend
4. **Enjoy**: Full-stack AI Data Dashboard!

Your complete professional application will be running with both frontend and backend deployed!
