# Order Processing Application
Simulation of an order processing application.
Developed by Gabor Tavali.

The application is able to parse an order CSV file with a given header. (See below.)\
It goes through the lines, validate it, persist orders to the database then upload a result file to an FTP server.

## Technology Stack
* Spring, Spring Boot
* MariaDB database
* FlyWay database migration
* openCSV
* Maven

## Requirements
* JDK 1.8 or later
* Maven 3.2+

## DB settings

The application uses MariaDB to persist the orders.\
You can use a built-in docker-compose with a MariaDB 10.2.\
In the folder /order-processing-app/src/main/docker:
```
docker-compose up
```
or
```
docker-compose up -d
```
without logs.\
In this case you need to install Docker to your machine.\
https://www.docker.com \
The credentials comes from the application.yml file (/order-processing-app/src/main/resources/).

```
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/orderdb
    username: admin
    password: admin
```

If you don't want to use Docker, feel free to configure your own MariaDB.\
Visit the application.yml file (/order-processing-app/src/main/resources/) and modify the following lines:
```
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/orderdb
    username: admin
    password: admin
    driver-class-name: org.mariadb.jdbc.Driver
```
The application uses FlyWay to DB migrations. The initial SQL script is in the /order-processing-app/src/main/resources/db/migration folder.

## FTP settings
To upload the result file to an FTP server you need to configure one.
Visit the application.yml file (/order-processing-app/src/main/resources/) and modify the following lines:
```
ftp:
  server: ftp.yourftp.com
  port: 21
  user: user
  passw: passw
```
If you don't have an FTP server or you don't want to use this function, do not remove the dummy values and you can also find the response file in the project folder.\
The name of the file on the FTP is
```
response-{actual date}.csv
```
and in the project folder
```
response.csv
```
In the project folder it is overwritten in every run.

## Run
1.) In the project folder:
```
mvn clean spring-boot:run -Dspring-boot.run.arguments="yourCsvFile.csv"
```
2.) Or create a runnable .jar file:
```
mvn clean install
```
then
```
java -jar target/order-processing-app-1.0.jar yourCsvFile.csv
```

## Input CSV header

* LineNumber
* OrderItemId
* OrderId
* BuyerName
* BuyerEmail
* Address
* Postcode
* SalePrice
* ShippingPrice
* SKU
* Status
* OrderDate (yyyy-mm-dd)

The delimitter is ";".