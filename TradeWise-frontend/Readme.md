# TradeWise Frontend

This is the frontend Android application for TradeWise, a comprehensive analytics platform for stock traders. The app enables users to upload trade statements, view analytics, manage portfolios, and generate detailed reports—all from their mobile device.

## Features

- **Secure Authentication:** Sign up, sign in, and password recovery.
- **Trade Upload:** Upload Excel (.xlsx), CSV (.csv), or PDF (.pdf) trade statements from device storage.
- **Dashboard:** Visualize trading patterns, filter by date or stock, and review best/worst trading days.
- **Portfolio Organizer:** Manage multiple asset types (stocks, mutual funds, bonds, options) in unified portfolios.
- **Analytics Reporting:** Download analytics reports in Excel, CSV, or PDF formats for any time frame.
- **Settings:** Manage profile, security, subscriptions, commissions, and trade configurations.
- **Trade Log History:** Track all uploaded files and filter logs by date.

## Prerequisites

- Android Studio (latest version recommended)
- Android device or emulator (API level 24+)
- Internet connection for backend API access

## Getting Started

### 1. Clone the Repository

git clone https://github.com/VRP-github/Tradewise.git


### 2. Open the Project

- Launch Android Studio.
- Select **Open an Existing Project** and navigate to the `TradeWise-frontend` directory.

### 3. Configure Backend API

- Update the backend API endpoint in your app's configuration file (e.g., `ApiClient.kt` or `strings.xml`) to match your backend server address.

### 4. Build and Run

- Connect your Android device or start an emulator.
- Click **Run** in Android Studio or use the shortcut **Shift + F10**.
- The app will install and launch on your device/emulator.

## Usage

1. **Register or Sign In:** Create an account or log in with your credentials.
2. **Upload Trades:** Use the "Add Trade" feature to upload your trade statement files.
3. **Analyze Data:** Access the dashboard to view analytics and performance insights.
4. **Manage Portfolio:** Organize your investments and edit sub-portfolios.
5. **Download Reports:** Generate and download analytics reports as needed.
6. **Adjust Settings:** Personalize your experience through the settings menu.

## Folder Structure

- `app/` - Main Android application source code.
- `res/` - Layouts, drawables, and resource files.
- `manifest/` - AndroidManifest and permissions.

## Dependencies

- Kotlin
- Android Jetpack libraries
- Retrofit/OkHttp (for API calls)
- Gson/Moshi (for JSON parsing)
- Material Components

## Team

- Yash Dinesh Daswani
- Aishwarya Subhash Kadam
- Devarsh Jayeshkumar Patel
- Viraj Rajesh Patel

For issues, suggestions, or contributions, please open an issue or submit a pull request.
