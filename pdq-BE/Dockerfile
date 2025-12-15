# ==========================
# BUILD STAGE
# ==========================
FROM maven:3.9.5-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B


# ==========================
# RUN STAGE
# ==========================
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copy file JAR đã build đúng tên
COPY --from=build /app/target/pdq-restaurant-backend-1.0.0.jar /app/app.jar

# ✅ CÀI ĐẶT TIMEZONE
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime && \
    echo "Asia/Ho_Chi_Minh" > /etc/timezone && \
    apk del tzdata

ENV TZ=Asia/Ho_Chi_Minh

EXPOSE 3000
ENV SPRING_PROFILES_ACTIVE=docker

# ✅ FORCE JAVA SỬ DỤNG TIMEZONE VIỆT NAM
ENTRYPOINT ["java", "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", "/app/app.jar"]