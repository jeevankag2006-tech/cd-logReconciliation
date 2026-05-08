# DISTLOG: Distributed Log Reconciliation System 🛡️

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

**DISTLOG** is a professional-grade observability dashboard built for high-scale distributed systems. It solves the "Order & Integrity" problem in microservices by reconciling inconsistent logs from multiple services into a unified, clean, and chronologically accurate global timeline.

## 🚀 Key Features

- **Futuristic Cyber Dashboard**: A high-performance UI featuring glassmorphism cards, neon accents, and smooth Compose animations.
- **Normalization Engine**: Automatically synchronizes disparate timestamp formats (ISO 8601, UNIX, HH:mm:ss) across distributed nodes.
- **Intelligent Reconciliation**: 
    - **Duplicate Detection**: Identifies redundant entries using unique ID and transaction mapping.
    - **Gap Analysis**: Detects sequence interruptions and missing event chains.
- **AI Anomaly Detection**: Simulated deep-scan engine that identifies timestamp drift, duplicate spikes, and suspicious service inactivity.
- **Reconstructed Timeline**: A unified view of the system's global state with advanced filtering (Service, Severity, Type).
- **Data Persistence**: Settings and user preferences persisted via Jetpack DataStore.

## 🛠️ Tech Stack

- **Frontend**: Kotlin, Jetpack Compose, Navigation Compose
- **Design**: Material 3 (Custom Cyber-Dark Theme)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Async**: Kotlin Coroutines & StateFlow
- **Data Handling**: Gson for high-speed JSON processing
- **Local Storage**: Jetpack DataStore Preferences

## 📸 Screenshots

| Dashboard View | Reconstructed Timeline | AI Analysis |
|---|---|---|
| *Operational metrics & stats* | *Unified global event flow* | *Deep scan & risk scoring* |

*(Note: Add your screenshots here after running the app!)*

## 📂 Project Structure

```text
com.distlog.reconciliation
├── data
│   ├── model          # Data entities (LogEntry, Stats, AiInsight)
│   ├── repository     # Reconciliation & Processing Logic
│   └── SettingsManager # Persistence using DataStore
├── ui
│   ├── theme          # Cyber-neon colors, typography & theme
│   ├── components     # Reusable GlassCards, NeonButtons, etc.
│   └── screens        # Main UI Modules (Dashboard, Timeline, etc.)
└── viewmodel          # UI State management via StateFlow
```

## ⚙️ Installation & Usage

1. **Clone the Repo**:
   ```bash
   git clone https://github.com/jeevankag2006-tech/cd-logReconciliation.git
   ```
2. **Open in Android Studio**:
   Import as a Gradle project.
3. **Sync & Run**:
   Sync Gradle and deploy to an Android 10+ device or emulator.
4. **Demo Flow**:
   - Navigate through the **Splash** and **Login**.
   - Use **"SELECT FILES"** to upload a log JSON or use the **"Sync"** button to load the default 500-log dataset.
   - Run **"AI SCAN"** to visualize anomaly detection.
   - Explore the **Timeline** to see reconciled event sequences.

## 🔮 Future Scope

- **Real-time Kafka Integration**: Switch from simulated repository to live Kafka/ELK streaming.
- **ML Root Cause Prediction**: Upgrade the AI engine to suggest fixes for detected anomalies.
- **Cloud Deployment**: Containerized ingestion pipelines for hybrid-cloud environments.

## 📄 License
This project was developed for a Hackathon and is available under the MIT License.

---
**Developed by [Jeevan]**
