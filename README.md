# ZappHRMS – Java Spring Boot Microservices-based HR Management System

🎯 **Real-World HRMS Application** built using Java Spring Boot Microservices, showcasing modern backend architecture with API gateway, service discovery, event-driven messaging, containerization, observability, and role-based access control.

---

## 🧩 System Overview

**ZappHRMS** is designed for internal teams involved in recruitment and candidate lifecycle management. It provides role-based access control for Admins, Business Development (BD), and Talent Acquisition Teams (TAT) to collaborate efficiently in managing clients, job openings, candidates, and communication.

---

## 📌 User Roles & Responsibilities

| Role      | Responsibilities |
|-----------|------------------|
| **Admin** | Creates clients, users, assigns roles; has full system access |
| **BD** (Business Development) | Manages job creation for clients |
| **TAT** (Talent Acquisition Team) | Manages candidates and their status |
| **Candidate** | Only used as data (no login access) |

Role-based access control is implemented using **Keycloak** with **OAuth2** and **RBAC**.

---

## 🧱 Microservices in the System

- `client-service`: Manages client data
- `job-service`: Handles job postings per client
- `candidate-service`: Tracks candidate profiles and statuses
- `message-service`: Sends email/SMS via event-driven architecture using Kafka
- `user-service`: Manages users and role assignments
- `eureka-service`: Service registry using Spring Cloud Netflix Eureka
- `gateway-service`: API Gateway with OAuth2, Circuit Breaker, Retry, Rate Limiter using **Resilience4j**
- `keycloak`: Auth server for secure login and access control

---

## 🔄 Event-Driven Messaging

- `message-service` is built using **Spring Cloud Functions** and **Spring Cloud Stream**
- Uses **Kafka** for asynchronous communication to handle notifications
- Triggers messages when job or candidate events occur

---

## ⚙️ Tech Stack

- **Java 17**, **Spring Boot**, **Spring Cloud**
- **Kafka** – Event broker
- **MySQL** – Relational database
- **Keycloak** – Authentication & Authorization (OAuth2, RBAC)
- **Eureka** – Service discovery
- **API Gateway** – Entry point for all services
- **Docker** & **Google Jib** – For container image creation
- **Docker Compose** – Local orchestration of all services
- **Kubernetes** – Deployment using manifests and Helm charts
- **Resilience4j** – Retry, Circuit Breaker, Rate Limiting
- **Grafana**, **Loki**, **Tempo**, **Prometheus** – Observability & Monitoring

---

## 🧪 Observability & Monitoring

- Integrated **Loki**, **Tempo**, **Prometheus**, and **Grafana** for logs, traces, and metrics
- Preconfigured dashboards to monitor microservices health and latency

---

## 🚀 Deployment

### 💻 Local Setup
- Use Docker Compose to spin up all microservices
- Prebuilt Jib images are available for each service

☁️ Kubernetes Setup
Helm charts used for 3rd-party components (Keycloak, Kafka, Prometheus, etc.)

K8s manifests created for all custom services

📎 License
This project is for educational and demonstration purposes.

🙋‍♂️ Author
Ramjee Ambedkar
🔗 LinkedIn: https://www.linkedin.com/in/m-ramjee-ambedkar-b33127304/ 
💻 GitHub: 

⭐️ Give it a Star!
If you found this helpful, feel free to ⭐ the repo!


