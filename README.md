# 🚀 Quirk

A modern, production-ready QR Code Generator built with **Spring Boot**, **Thymeleaf**, and **ZXing**. Generate customizable QR codes in PNG or SVG format with support for logo embedding, custom colors, dark mode, and responsive UI.

---

## ✨ Features

- 🔗 Generate QR codes from URLs
- 🖼️ Export as **PNG** or **SVG**
- 🎨 Customize foreground and background colors
- 🏷️ Custom download filename
- 🖼️ Logo upload (PNG only)
- 🌙 Dark / Light mode
- 📱 Fully responsive UI
- ⚡ Async QR generation using Fetch API
- 📋 Copy URL to clipboard
- 🔄 Reset form
- ⏳ Loading spinner while generating
- ✅ Inline validation messages
- 🛡️ Bean Validation
- 🚨 Global Exception Handling
- 📦 Clean layered architecture

---

## 🛠️ Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Bean Validation
- ZXing
- SLF4J Logging
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript (ES6+)
- Fetch API
- Async/Await

### Testing
- JUnit 5
- Mockito (Work in Progress)

---

## 📂 Project Structure

```
src
├── main
│   ├── java
│   │   └── com.harsh.qrgenerator
│   │       ├── controller
│   │       ├── dto
│   │       │   ├── request
│   │       │   └── response
│   │       ├── exception
│   │       ├── service
│   │       │   └── impl
│   │       └── QrGeneratorApplication.java
│   │
│   └── resources
│       ├── static
│       │   ├── css
│       │   └── js
│       └── templates
│
└── test
    └── java
```

---

## 🏗️ Architecture

```
Browser
    │
    ▼
Spring MVC Controller
    │
    ▼
Bean Validation
    │
    ▼
Service Layer
    │
    ▼
ZXing Library
    │
    ▼
PNG / SVG Response
```

---

## 📸 Screenshots

### Light Theme

> Add screenshot here

### Dark Theme

> Add screenshot here

### Generated QR

> Add screenshot here

---

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/QuickQR-Pro.git
```

### Navigate to the Project

```bash
cd QuickQR-Pro
```

### Run the Application

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

The application will start at:

```
http://localhost:8080
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 📦 Built With

- Spring Boot
- Thymeleaf
- ZXing
- Maven
- Java 21

---

## 🎯 Highlights

- Layered Architecture
- Constructor Injection
- DTO-based Request/Response
- Bean Validation
- Global Exception Handling
- Custom Exception Classes
- Service Interface + Implementation
- Responsive UI
- Dark Mode
- SVG Generation
- Logo Embedding
- Download Support
- Clean Code Practices

---

## 🔮 Future Improvements

- User Authentication (Spring Security + JWT)
- QR History
- Database Integration
- Docker Support
- CI/CD Pipeline
- Cloud Deployment
- Analytics Dashboard

---

## 👨‍💻 Author

**Harsh Saini**

- GitHub: https://github.com/yourusername
- LinkedIn: https://linkedin.com/in/yourprofile

---

## ⭐ If you like this project

Please consider giving it a ⭐ on GitHub.
