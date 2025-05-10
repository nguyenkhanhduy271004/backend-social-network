# Social Network Backend

A robust social network backend built with Spring Boot, featuring modern social media functionalities and best practices in software development.

## 🚀 Features

- **User Management**
  - User registration and authentication
  - Profile management
  - Admin privileges and user roles
  - Email verification

- **Content Management**
  - Posts with text, images, and videos
  - Stories (24-hour content)
  - Reels (short-form videos)
  - Comments and likes
  - Group posts and discussions

- **Social Features**
  - Follow/Unfollow users
  - Like and comment on posts
  - Share and repost content
  - Group creation and management
  - Real-time notifications

- **Security**
  - JWT-based authentication
  - OAuth2 integration
  - Role-based access control
  - Rate limiting
  - Input validation

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.4.5
- **Database**: MySQL 8.0
- **Caching**: Redis
- **Authentication**: Spring Security + JWT
- **API Documentation**: Swagger/OpenAPI
- **File Storage**: Cloudinary
- **Email Service**: Spring Mail
- **Real-time Communication**: WebSocket
- **Rate Limiting**: Bucket4j
- **Object Mapping**: ModelMapper
- **Containerization**: Docker

## 📋 Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose
- MySQL 8.0
- Redis

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone [repository-url]
   cd social-network
   ```

2. **Environment Setup**
   - Configure your database settings in `application.properties`
   - Set up your Cloudinary credentials
   - Configure email service settings

3. **Run with Docker**
   ```bash
   docker-compose up -d
   ```

4. **Run Locally**
   ```bash
   mvn spring-boot:run
   ```

## 🔧 Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/social_network
spring.datasource.username=user
spring.datasource.password=password
```

### Redis Configuration
```properties
spring.redis.host=localhost
spring.redis.port=6379
```

### JWT Configuration
```properties
jwt.secret=your-secret-key
jwt.expiration=86400000
```

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

## 🏗️ Project Structure

```
src/main/java/com/project/social_network/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── converter/       # DTO converters
├── dto/            # Data Transfer Objects
├── exceptions/     # Custom exceptions
├── model/          # Entity classes
├── repository/     # Data access layer
├── security/       # Security configurations
├── service/        # Business logic
└── util/           # Utility classes
```

## 🔐 Security Features

- JWT-based authentication
- Role-based access control
- Password encryption
- Rate limiting
- Input validation
- CORS configuration
- XSS protection

## 🧪 Testing

Run tests using Maven:
```bash
mvn test
```

## 📦 Deployment

1. Build the application:
   ```bash
   mvn clean package
   ```

2. Run with Docker:
   ```bash
   docker-compose up -d
   ```

## 🔄 CI/CD

The project includes GitHub Actions workflows for:
- Automated testing
- Code quality checks
- Docker image building
- Deployment

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞: 0903525012 for Support

For support, email kuy271004@gmail.com or create an issue in the repository.