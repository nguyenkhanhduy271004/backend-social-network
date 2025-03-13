# Sử dụng OpenJDK 17
FROM openjdk:17

# Định nghĩa biến ARG cho file JAR
ARG FILE_JAR=target/*.jar

# Copy file JAR vào container
COPY ${FILE_JAR} app.jar

# Cấu hình lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]

# Mở cổng 8080
EXPOSE 8080
