# The Task:  
Create a small application and design a REST API with the following prerequisites and use
cases. Some tasks are optional, feel free to decide if you want to work on them.

### How to run:  
#### build:  
`./gradlew build`  

#### run:  
`./gradlew bootRun`

#### OpenApi swagger-UI:  
`http://localhost:8080/swagger-ui/index.html`

#### H2 in memory DB:  
After running project the h2 console is reachable with following address   
`http://localhost:8080/h2-console/`  
Driver class: org.h2.Driver  
JDBC URL: jdbc:h2:mem:testdb  
Username: sa
Password: [blank]

### Tech:  
Spring boot: 2.6  
Project lombok: 1.18.22  
JUnit: 5  
Mapstruct: 1.4.2.final  
Openapi: 1.6.3  
iban4j: 3.2.3-RELEASE  