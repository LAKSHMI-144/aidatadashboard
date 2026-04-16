# AI Data Dashboard - React + Spring Boot

A full-stack application that allows users to upload CSV files, visualize data in charts, and get AI-powered insights using OpenAI API.

## Features

- **Modern UI**: Beautiful glass morphism design with responsive layout
- **CSV Upload**: Drag-and-drop file upload with progress tracking
- **Data Visualization**: Interactive charts with smart column detection
- **Natural Language Queries**: Ask questions about your data in plain English
- **AI Analysis**: Get intelligent insights and data summaries
- **Conversation History**: Maintain chat-like interaction with AI

## Quick Setup

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+
- OpenAI API Key (optional for AI features)

### Backend Setup

1. **Navigate to backend directory:**
```bash
cd demo
```

2. **Set OpenAI API Key:**
```bash
# Environment Variable (Recommended)
export OPENAI_API_KEY=your_openai_api_key_here

# Or add to application.properties
echo "openai.api.key=your_openai_api_key_here" >> src/main/resources/application.properties
```

3. **Build and run:**
```bash
mvn clean install
mvn spring-boot:run
```

### Frontend Setup

1. **Navigate to frontend directory:**
```bash
cd ai-dashboard
```

2. **Install dependencies:**
```bash
npm install
```

3. **Start the development server:**
```bash
npm start
```

### Access the Application

- **Frontend**: http://localhost:3000
- **Backend**: http://localhost:8080

## Usage

1. **Upload a CSV file** using the drag-and-drop interface
2. **View automatic chart generation** with your data
3. **Ask questions** about your data in natural language
4. **Get AI insights** and analysis
5. **Download or share** results

## Sample Datasets

The project includes sample CSV files for testing:
- `sales_data.csv` - Sales performance data
- `employee_data.csv` - Employee information
- `student_grades.csv` - Academic records
- `inventory_data.csv` - Stock management data

## Deployment

### Quick Deploy to Vercel (Frontend Only)

1. **Push to GitHub:**
```bash
git add .
git commit -m "Ready for deployment"
git push origin main
```

2. **Deploy to Vercel:**
- Go to [vercel.com](https://vercel.com)
- Import your GitHub repository
- Deploy automatically

**Live Link**: `https://aidatadashboard.vercel.app`

### Full Stack Deployment

See `README_DEPLOYMENT.md` for complete deployment options including:
- Docker containerization
- Railway (full-stack hosting)
- AWS/Azure/Cloud deployment

## Technology Stack

### Frontend
- **React 18** - Modern UI framework
- **Chart.js** - Data visualization
- **Axios** - HTTP client
- **CSS3** - Modern styling with animations

### Backend
- **Spring Boot 4** - Java framework
- **Maven** - Build tool
- **Jackson** - JSON processing
- **OpenAI API** - AI integration

### Deployment
- **Vercel** - Frontend hosting
- **Railway** - Full-stack hosting
- **Docker** - Containerization
- **GitHub Actions** - CI/CD

## API Endpoints

- `POST /upload` - Upload CSV file
- `GET /chart` - Get chart data
- `GET /query` - Get AI analysis
- `POST /custom-query` - Natural language queries

## Environment Variables

```bash
# Backend
OPENAI_API_KEY=sk-your-api-key-here

# Frontend (optional)
REACT_APP_API_URL=http://localhost:8080
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For issues and questions:
- Create an issue on GitHub
- Check the documentation in `DEPLOYMENT_GUIDE.md`
- Review the troubleshooting section

---

**Your AI Data Dashboard is ready for deployment!**
