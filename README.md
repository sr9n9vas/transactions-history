**Transactions History**
------------------------------------------------------------------------------------------

This small rest service spring boot application has two services one for save the transaction and another one for 
retrieve the transactions with in specified time.

**Technology Stack**
* Java
* Spring Boot
* Spring JDBC
* H2(DataBase)


**Run the application**

required Gradle to run the application

* checkout the project and navigate to root directory of project.
* to build the run the command `./gradlew clean build`
* run the application using `java -jar build/libs/demo.jar` or `./gradlew bootRun` command


**Rest Services**

POST   `http://localhost:8080/transactions`

Request Body:

`{
"amount" : 100,
"timestamp": 1529860741000
}`

Response Body:

`{
     "status": "success",
     "errors": []
 }`

* amount - is transaction amount
*timestamp - transaction time (milliseconds) UTC time

GET `http://localhost:8080/statistics`

Response Body:

`{
     "sum": 600,
     "avg": 300,
     "max": 500,
     "min": 100,
     "count": 2
 }`
 
 Note: GET returns statistics of transactions happened with in the 60 seconds from current time(UTC), this time you can be changed using `transactions.within.time` in application.properties file) .
 
  










