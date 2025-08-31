# library-bookmanagement
SWE6002 Enterprise Systems Development 

This is a project for the class Enterprise Systems Development. It's a functioning library management system built with MySQL and Spring Boot. The tools needed are as follows:


[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8.1-red.svg)](https://maven.apache.org/)


- Configure MySQL Database, create a database or let the application create it automatically:
CREATE DATABASE IF NOT EXISTS library_db;


- Update the application properties by editing src/main/resources/application.properties:
propertiesspring.datasource.url=jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password


- Build the project:
mvn clean install


- Run application:
mvn spring-boot:run

Then, you can access the app via

-API Base URL: http://localhost:8080/api
-Swagger UI: http://localhost:8080/swagger-ui.html
