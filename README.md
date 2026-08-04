# Quirk

A modern, production-ready QR code generator built with Spring Boot, Thymeleaf, and ZXing. Generate customizable QR codes in PNG or SVG format, with support for logo embedding, custom colors, dark mode, and a fully responsive UI.
---

## Features

- Generate QR codes from any URL
- Export as PNG or SVG
- Customize foreground and background colors
- Optional logo embedding (PNG only)
- Custom download filename
- Dark / light mode with saved preference
- Fully responsive UI
- Async QR generation via the Fetch API
- Copy-to-clipboard for the source URL
- Inline field validation with server-side error mapping
- Loading state while generating
- Bean Validation on all inputs
- Global exception handling
- Clean, layered architecture

---

## Tech Stack

**Backend**
- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Bean Validation
- ZXing
- SLF4J logging
- Maven

**Frontend**
- HTML5 / CSS3
- JavaScript (ES6+)
- Fetch API, async/await

**Testing**
- JUnit 5
- Mockito *(in progress)*

---

## Project Structure

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

## Architecture

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

## Getting Started

Clone the repository:

```bash
git clone https://github.com/yourusername/quirk.git
cd quirk
```

Run the application:

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

The app will be available at `http://localhost:8080`.

---

## Running Tests

```bash
mvn test
```

---

## Highlights

- Layered architecture with constructor injection
- DTO-based request/response models
- Bean Validation with custom exception classes
- Global exception handling
- Service interface + implementation split
- Responsive UI with dark mode
- SVG generation and logo embedding
- Clean code practices throughout

---

## Roadmap

- User authentication (Spring Security + JWT)
- QR generation history
- Database integration
- Docker support
- CI/CD pipeline
- Cloud deployment
- Analytics dashboard

---

## Author

**Harsh Saini**

- GitHub: [github.com/yourusername](https://github.com/yourusername)
- LinkedIn: [linkedin.com/in/yourprofile](https://linkedin.com/in/yourprofile)

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

If you find this project useful, consider giving it a star on GitHub.