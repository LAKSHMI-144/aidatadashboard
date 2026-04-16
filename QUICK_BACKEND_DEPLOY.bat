@echo off
echo ========================================
echo Backend Deployment - Railway Guide
echo ========================================
echo.

echo Your backend is ready for Railway deployment!
echo.

echo Step 1: Create Railway Account
echo 1. Go to https://railway.app
echo 2. Click "Sign Up" 
echo 3. Choose "Continue with GitHub"
echo 4. Authorize Railway access
echo.

echo Step 2: Deploy Backend
echo 1. Click "New Project"
echo 2. Select "Deploy from GitHub repo"
echo 3. Choose your "aidatadashboard" repository
echo 4. Set Root Directory to: demo
echo 5. Railway will auto-detect Spring Boot
echo 6. Click "Deploy"
echo.

echo Step 3: Configure Environment Variables
echo In Railway dashboard, add these variables:
echo   OPENAI_API_KEY=sk-your-api-key-here
echo   SPRING_PROFILES_ACTIVE=production
echo   PORT=8080
echo.

echo Step 4: Get Your Backend URL
echo Railway will give you a URL like:
echo   https://your-app-name.railway.app
echo.

echo Step 5: Update Frontend
echo 1. Go to Vercel dashboard
echo 2. Add environment variable:
echo    REACT_APP_API_URL=https://your-backend-name.railway.app
echo 3. Redeploy frontend
echo.

echo Your Complete App URLs:
echo   Frontend: https://aidatadashboard.vercel.app
echo   Backend: https://your-app-name.railway.app
echo   GitHub:  https://github.com/LAKSHMI-144/aidatadashboard
echo.

echo Cost: $5/month for Railway backend
echo Time: 10 minutes total
echo.

echo Ready to deploy? Open https://railway.app now!
echo.
pause
