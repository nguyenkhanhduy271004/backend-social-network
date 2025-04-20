# Social Network - Spring Boot RESTful API

## Introduction
This is a Social Network project built with **Spring Boot**, providing RESTful APIs for social networking functionalities such as user management, posts, comments, likes, and friend system.

## Key Features
- **User Management:**
    - Registration, login, and JWT authentication.
    - Forgot password and profile update.
    - Follow and friend list.

- **Post Management:**
    - Create, update, delete, and view posts.
    - Support images for posts.

- **Comment Management:**
    - Add, edit, and delete comments.

- **Likes:**
    - Like and unlike posts.

- **Notifications:**
    - Notifications for new likes and comments.

## Technologies Used
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** (JWT Authentication)
- **Hibernate & JPA**
- **MySQL** 
- **Maven**
- **Swagger UI** (API Documentation)

## Project Structure
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.socialnetwork
│   │   │       ├── controller
│   │   │       ├── service
│   │   │       ├── repository
│   │   │       ├── model
│   │   │       ├── config
│   │   │       └── dto
│   │   └── resources
│       ├── application.yml
│       └── static
│           └── images
```

## Installation
1. **Clone the project:**
```bash
git clone https://github.com/nguyenkhanhduy271004/backend-social-network
```

2. **Configure the database:**
   Edit the `application.yml` file:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/socialnetwork
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
```

3. **Run the project:**
```bash
./mvnw spring-boot:run
```

## Usage
- **Access Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoints
### Users
- `POST /api/auth/register` - Register a new account
- `POST /api/auth/login` - Login
- `GET /api/users/{id}` - Get user information

### Posts
- `POST /api/posts` - Create a post
- `GET /api/posts` - Get all posts
- `PUT /api/posts/{id}` - Update a post
- `DELETE /api/posts/{id}` - Delete a post

### Comments
- `POST /api/posts/{id}/comments` - Add a comment
- `DELETE /api/comments/{id}` - Delete a comment

## Contribution
- Fork the project and create a pull request.
- Report bugs via the **Issues** section.

## Contact
- **Email:** your.email@example.com
- **GitHub:** https://github.com/nguyenkhanhduy271004

## License
This project is released under the **MIT** license.
