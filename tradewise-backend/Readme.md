# TradeWise Backend

The backend of **TradeWise**, a stock trading analytics platform that automates trade data processing and visualization to help traders optimize their strategies.

## 🚀 Project Overview

TradeWise provides a powerful backend API to:
- Authenticate users (Sign-up, Sign-in, Recovery)
- Upload and analyze trade files (.csv, .xlsx, .pdf)
- Display analytics dashboards
- Organize multi-asset portfolios
- Generate and download trade reports
- Maintain trade log history
- Manage user and general app settings

---

## 🛠️ Tech Stack

- **Language**: Python 3.11+ / Node.js
- **Framework**: Django REST / Flask / Express
- **Database**: PostgreSQL / MongoDB / MySQL
- **Authentication**: JWT-based auth
- **Storage**: AWS S3 / Local file system
- **Others**: Pandas / Celery / Firebase (optional)

---

## 🧩 Features

- ✅ Secure user authentication and recovery
- 📂 Upload trades from `.csv`, `.xlsx`, `.pdf` files
- 📊 Dashboard showing trade analytics with filtering
- 📁 Portfolio organizer for multi-asset tracking
- 📑 Report generation in multiple formats
- 📜 View and filter trade logs
- ⚙️ User and global app settings
- 🔐 Secure logout and session management

---

## 📦 Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/VRP-github/Tradewise.git
cd Tradewise/tradewise-backend
```

### 2. Environment setup

```bash
cp .env.example .env  # Add your DB_URL, SECRET_KEY, etc.
```

### 3. Install dependencies

#### For Python (Django/Flask)

```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

#### For Node.js (Express)

```bash
npm install
```

### 4. Run the server

```bash
# Python
python manage.py runserver  # or flask run

# Node
npm run dev
```

---

## 📁 Folder Structure

```
tradewise-backend/
├── controllers/      # Business logic
├── models/           # Database schema
├── routes/           # API route handlers
├── utils/            # Helper functions
├── config/           # Env & DB setup
├── middleware/       # Auth, logging, etc.
├── tests/            # Unit & integration tests
├── uploads/          # Uploaded trade files
├── .env.example      # Sample environment variables
└── server.js / app.py
```

---

## 🧪 API Endpoints (Sample)

| Method | Endpoint                | Description                  |
|--------|-------------------------|------------------------------|
| POST   | `/api/auth/signup`      | Register a new user          |
| POST   | `/api/auth/login`       | User login                   |
| POST   | `/api/auth/recover`     | Password recovery            |
| POST   | `/api/trade/upload`     | Upload a trading file        |
| GET    | `/api/dashboard`        | Get dashboard data           |
| GET    | `/api/trades/logs`      | View uploaded trade logs     |
| GET    | `/api/portfolio`        | Fetch portfolio details      |
| GET    | `/api/analytics/report` | Download trade analytics     |
| PUT    | `/api/settings/user`    | Update user settings         |
| PUT    | `/api/settings/general` | Update general settings      |

---


## 📹 Demo

[![TradeWise YouTube Demo](https://img.shields.io/badge/YouTube-Demo-red)](https://your-youtube-link-here)

---

## 👥 Team Byte-Code

- Yash Dinesh Daswani (Captain)
- Aishwarya Subhash Kadam
- Devarsh Jayeshkumar Patel
- Viraj Rajesh Patel

