# Backend Test

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

#### Explicit requirements:
1. Use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. Use frameworks/libraries if you like (**except Spring**), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.
Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

## Implementation

### REST web service

The JAR can be obtained by running `mvn clean install` in the project root.

Executable JAR will be named 

  **${project.artifactId}-executable-${project.version}.jar**


+ **POST** on `/accounts` `JSON object` to add new account.
Newly created account is returned in the response.

  Example :
  
  `{ "name": "John Doe"}`

+ **GET** on `/accounts` to get all accounts.

+ **GET** on `/accounts/:id` to get specified account.

  Example: 
  
  `GET /accounts/1`

+ **POST** on `/transactions` `JSON object` to process new transaction.
Examples: 

  `{ "type": "DEPOSIT", "to": 1, "amount": 25.25 }`
  
  `{ "type": "WITHDRAW", "from": 1, "amount": 25.25 }`

  `{ "type": "TRANSFER", "from": 1 "to": 2, "amount": 25.25 }`

+ **GET** on `/transactions` to get all transactions for all accounts.

+ **GET** on `/accounts/:id/transactions` to get all transactions of the account. 

  Example:
  
  `GET /accounts/1/transactions`

+ **GET** on `/accounts/:id/transactions/incoming` to get all incoming transactions of the account. 
  
    Example:
    
    `GET /accounts/1/transactions/incoming`

+ **GET** on `/accounts/:id/transactions/outgoing` to get all incoming transactions of the account. 
  
    Example:
    
    `GET /accounts/1/transactions/outgoing`
