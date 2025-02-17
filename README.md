# 📚 Library Management System

The **Library Management System** is a **Spring Boot application** that allows users to manage books, patrons, and borrowing records. It includes:  
1. **Authentication using JWT**  
2. **Caching using Redis**  
3. **Logging with Aspect-Oriented Programming (AOP)**  
4. **Spring Security for secured access**  
5. **Swagger UI for API documentation**  
6. **Unit tests for each controller, respository, and service**  

This README provides a **detailed guide** on setting up, running, and interacting with the application.

---

## **🛠️ Setup Instructions**

### **1️⃣ Prerequisites**
Ensure you have the following installed:  
- **Java 17+**  
- **PostgreSQL** (for production database)  
- **Redis** (for caching)  
- **Redis Insight** (optional, for monitoring Redis cache)  
- **Maven** (for dependency management)  

### **2️⃣ Clone the Repository**
```sh
git clone https://github.com/Mariam22-hub/Maids.cc-Task.git
cd Maids.cc-Task
```

### **3️⃣ Configure Environment Variables**
Replace the following **placeholders** in `env.properties` file:

```properties
# Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/<db_name>
SPRING_DATASOURCE_USERNAME=<your_db_username>
SPRING_DATASOURCE_PASSWORD=<your_db_password>

PORT_NUMBER=8080
spring.data.redis.port=6379

jwt.expired=86400000
secret-key=<your_secret_key_here>
```

---

## **🚀 Running the Application**
Once dependencies are installed and environment variables are set, start the application using:
```sh
mvn clean install
```
Then run the application!
The application will start on `http://localhost:8080/`.

---

## **🔑 Authentication & JWT**

### **Signup (Register a User)**
**Endpoint:** `POST /api/auth/signup`  
**Request Body:**
```json
{
    "username": "john_doe",
    "password": "password123",
    "role": "USER"
}
```
**Response:**
```json
{
    "username": "john_doe"
}
```

### **Login (Authenticate User)**
**Endpoint:** `POST /api/auth/login`  
**Request Body:**
```json
{
    "username": "john_doe",
    "password": "password123"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR...",
    "expiresIn": 86400000
}
```

### **Using JWT for Protected Routes**
For any **protected API**, add the token to the `Authorization` header:
```http
Authorization: Bearer <your_jwt_token>
```
Or simply choode `Bearer Token` in postman

---

## **📖 API Documentation**

View the full **Swagger API documentation** at:  
- [Swagger UI](http://localhost:8080/swagger-ui.html)  
- [OpenAPI JSON](http://localhost:8080/v3/api-docs)  

You will also find the exported postman api collection in the root directory 

### 🔹 Authentication API
| **Endpoint** | **Method** | **Description** |
|-------------|------------|----------------|
| `/api/auth/signup` | `POST` | Registers a new user |
| `/api/auth/login` | `POST` | Authenticates user and returns JWT token |

### 🔹 Book Management API
| **Endpoint** | **Method** | **Description** |
|-------------|------------|----------------|
| `/api/books` | `GET` | Retrieve all books |
| `/api/books/{id}` | `GET` | Retrieve a book by ID |
| `/api/books` | `POST` | Create a new book |
| `/api/books/{id}` | `PUT` | Update book details |
| `/api/books/{id}` | `DELETE` | Remove a book |

### 🔹 Patron Management API
| **Endpoint** | **Method** | **Description** |
|-------------|------------|----------------|
| `/api/patrons` | `GET` | Get all patrons |
| `/api/patrons/{id}` | `GET` | Get a specific patron |
| `/api/patrons` | `POST` | Register a patron |
| `/api/patrons/{id}` | `PUT` | Update patron details |
| `/api/patrons/{id}` | `DELETE` | Remove a patron |

### 🔹 Borrowing System API
| **Endpoint** | **Method** | **Description** |
|-------------|------------|----------------|
| `/api/borrow/{bookId}/patron/{patronId}` | `POST` | Borrow a book |
| `/api/return/{bookId}/patron/{patronId}` | `PUT` | Return a borrowed book |
---

## **⚡ Caching with Redis & Redis Insight**

The application **uses Redis** to cache frequently accessed data, such as book and patron records, improving performance.

### **Setting Up Redis**
1. Install Redis via Docker:
   ```sh
   docker run --name redis -p 6379:6379 -d redis
   ```
2. Install Redis Insight from microsoft store:

3. Choose Redis Insight DB `127.0.0.1:6379`.

### **Example Cached Methods in `BookServiceImp`**:

**🔧 Redis must be running**:
```sh
redis-server
```

---

## **🛠 AOP Logging (Aspect-Oriented Programming)**

The application logs **method calls, execution time, and exceptions** automatically using **AOP (Aspect-Oriented Programming)**.

### **How It Works:**
- Logs method execution **before and after**.
- Logs **exceptions thrown** from service methods.
- **Execution time is recorded** for performance analysis.

### **Example Log Output:**
```
🔵 Method Called: BookServiceImp.getBookById(Long) | Args: [1]
Method Completed: BookServiceImp.getBookById(Long) | Execution Time: 25ms
Exception in Method: BookServiceImp.deleteBook(Long) | Message: Cannot delete book with active borrowing records.
```
---

## 🧪 Unit Testing & Code Coverage
| **Module** | **Class Coverage** | **Method Coverage** | **Line Coverage** | **Branch Coverage** |
|------------|------------------|------------------|----------------|----------------|
| `auth` | **100%** | **100%** | **100%** | **11%** |
| `controller` | **100%** | **100%** | **100%** | **0%** |
| `dto` | **100%** | **100%** | **100%** | **0%** |
| `service` | **100%** | **100%** | **100%** | **100%** |
---

## **📂 Project Modules Explained**

| **Module** | **Description** |
|-----------|----------------|
| `auth` | Handles **user authentication** and **JWT token generation** |
| `config/security` | Configures **Spring Security and JWT filters** |
| `config/redis` | Configures **Redis for caching** |
| `controller` | Handles API **HTTP requests** |
| `dto` | Defines **Data Transfer Objects (DTOs)** for API communication |
| `model` | Contains **Entity Models** for JPA |
| `repository` | Interfaces for **database access (Spring Data JPA)** |
| `service` | Implements **business logic** |
| `aop` | Implements **AOP-based logging** |

---
