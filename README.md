# Banking App

This project is a bank-related API which provides CRUD functionality to clients.  It is a simplified version of an ATM, as well as the websites provided by commercial banks like Regions, or Wells Fargo.

# Features

* A client can use this API to create a profile and perform other CRUD operations on that account.
* A client can create multiple checking accounts associated with a particular user account.
    * This API offers CRUD functionality for the checking accounts.
* The API also offers endpoints to perform transactions across separate accounts or individual accounts.
    * The user can deposit and withdraw money from the account.
    * The user can send money to another account.
* JSON web tokens are used to achieve user authentication & user authorization.
    * The client is expected to store the token and send it back to the server with each request.
    * The JJWT library was used to handle the lower level aspects of the JSON web token.
    * JJWT Documentation: https://github.com/jwtk/jjwt
    * Thw JJWT library is imported into this project with the help of Maven.

# Tools Used For this project
* Java
* Jax-RS -> Jersey implementation
* MySQL
* Maven
* JSON
* JDBC
* Jax-B
* XML for configuration
* Raw SQL (no ORM)
* Tomcat

# Project Structure
* Every request is initially captured by two filters.
    * The first filter is used for authentication of a user.
    * The second filter then intercepts the request and determines whether authorization is required.  If so, it checks the user's role to allow or block access to the requested resource.
* The request reaches the core application.
    * The resource method sends the client's data to a service method.
    * The service method performs any required operations on the client data and sends the data to the data access object.
    * The dao connects to the database with JDBC and writes SQL to interact with a MySQL database.

# What I learned
* How to use documentation to integrate 3rd party tools, such as JJWT.
* The browser's CORS policy.
    * preflight requests.
    * how to resolve CORS errors.
* Builder design pattern.
* SQL Joins
* HTTP Status Codes

# What I can improve
* Utilize dependency injection
    * I tried to use dependency injection to simplify the authentication/authorization process, similar to what is done in Spring Security with "secured" annotiations over controller methods, but I could not figure out how to get a similar result with a Jax-RS container.
* HATEOAS
    * There is minimal application of HATEOAS in this project. I need to learn more about HATEOUS and further imply the concept.