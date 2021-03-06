# friendbook
This is a simple friendbook application to demonstrate user actions. The application does not contain any UI, but swagger documentation is provided to experiment with APIs. The application requires a PostgreSQL database to run. The url for the PostgreSQL database can be provided in the application.yml configuration file.

Pre-requisites:

1. Java version 1.8 or above
2. A PostgreSQL database
    - The configuration for the database can be provided in /src/main/resources/application.yml
      - spring.datasource.url = Url of the PostgreSQL database
      - spring.datasource.username = Username erquired to connect to database
      - spring.datasource.password = Password required to connect to database
    - The required tables and relationships are created by the application on startup


Following REST APIs are provided in the application:

1. Create a User
    - Endpoint : POST /friendbook/user/create
    - Input Request Body: 
        <code> {
          "emailId": "string",
          "lastName": "string",
          "name": "string",
          "password": "string"
        } </code>
    - Output : 
        <code> {
          "emailId": "string",
          "id": "string",
          "lastName": "string",
          "name": "string"
        } </code>
        
2. Authenticate a User
    - Endpoint : POST /friendbook/user/authenticate
    - Input Request Body: 
        <code> {
          "emailId": "string",
          "password": "string"
        } </code>
    - Output : 
        <code> {
          "emailId": "string",
          "expiresAt": Integer,
          "token": "string"
        } </code>
        
3. Get a User by Id
    - Endpoint : GET /friendbook/user/{id}
    - Input Path Variable: 
        <code> {id} </code>
    - Output : 
        <code> {
          "emailId": "string",
          "id": "string",
          "lastName": "string",
          "name": "string"
        } </code>
        
4. Get a User by email id
    - Endpoint : GET /friendbook/user/email/{emailId}
    - Input Path Variable: 
        <code> {emailId} </code>
    - Output : 
        <code> {
          "emailId": "string",
          "id": "string",
          "lastName": "string",
          "name": "string"
        } </code>
        
5. Add a friend
    - Endpoint : POST /friendbook/user/addFriend
    - Input Request Body: 
        <code> {
          "requestedFriendId": "string"
        } </code>
    - Input Request Header:
        <code> {"Authorization" : "Bearer <jwt_token>"} </code>
    - Output : 
        <code> HTTP Response code as applicable </code>
        
6. Remove a friend
    - Endpoint : POST /friendbook/user/removeFriend
    - Input Request Body: 
        <code> {
          "requestedFriendId": "string"
        } </code>
    - Input Request Header:
        <code> {"Authorization" : "Bearer <jwt_token>"} </code>
    - Output : 
        <code> HTTP Response code as applicable </code>
        
7. Search Users
    - Endpoint : GET /friendbook/users/search
    - Input Request Params: 
        <code> {
          "pageNumber": Integer,
          "pageSize": Integer,
          "searchQuery": "string"
        } </code>
    - Output : 
        <code> {
          "pageNumber": Integer,
          "resultSize": Integer,
          "results": [
            {
              "emailId": "string",
              "id": "string",
              "lastName": "string",
              "name": "string"
            }
          ]
        } </code>
        

**Authentication**

The APIs to add and remove friends require 'Authorization' Http header. The 'Authorization' header should provide a valid JWT in the format 'Bearer <jwt>' generated by using the POST /friendbook/user/authenticate API (Refer API 2). The application uses PBKDF2WithHmacSHA1 with salt-hashing to save password hashes in the database and encodes/decoded the JWT using the HMAC256 algorithm.
    
**How to run the application**

- Setup a PostgreSQL database. Configure the url in the application.yml file.
- Build the application using - mvn clean install
- Run the application using - mvn spring-boot:run
- Default run port : 8888
- If running on localhost:
    - The swagger documentation can be accessed at http://localhost:8888/friendbook/swagger-ui.html
    - The base url is http://localhost:8888/friendbook/
