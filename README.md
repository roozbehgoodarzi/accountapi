# The Task:  
Create a small application and design a REST API with the following prerequisites and use
cases. Some tasks are optional, feel free to decide if you want to work on them.  

### Use cases:
#### Deposit money into a specified bank account  
‣ Enable adding some amount to a specified bank account  
#### Transfer some money across two bank accounts  
‣ Imagine this like a regular bank transfer. You should be able to withdraw money
from one account and deposit it to another account  
#### Show current balance of the specific bank account  
‣ Show the balance of a specified bank account  
#### Filter accounts by account type  
‣ Request account information by account type (could be multiple)  
#### Show a transaction history  
‣ For an account, specified by IBAN, show the transaction history
#### *Bonus - account creation  
‣ To open an account and assign it with an IBAN an endpoint should be provided.
#### *Bonus - account locking
‣ For security reasons, it might be a good idea to be able to lock a specific account.
For example, if an internal fraud management system spots something suspicious,
it should be able to lock that account. Naturally, if the account can be locked, there
should be an unlock functionality in place

### How to run:  
#### build:  
`./gradlew build`  

#### run:  
`./gradlew bootRun`

#### OpenApi swagger-UI for api documentations 
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