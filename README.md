Installation and running
---
<h4>Tools</h4>
- JDK 17.0.7 <br/>
- Gradle 7.6.2 <br/>

Don't forget to set Fixer.io API key in application.yml file

---

Task
---
Develop a simple RESTful web service that would satisfy a set of functional requirements, \
as well as a list of non-functional requirements. \
Please note these non-functional requirements are given in order of importance; \
items appearing earlier in the list are more crucial for assignment.

Functional requirements:
```
● Service should expose an HTTP API providing the following functionality:
○ Given a client identifier, return a list of accounts (each client might have 0 or more
accounts with different currencies)
○ Given an account identifier, return transaction history (last transactions come first)
and support result paging using “offset” and “limit” parameters
○ Transfer funds between two accounts indicated by identifiers
● Balance must always be positive (>= 0).
● Currency conversion should take place when transferring funds between accounts with
different currencies
○ For currency exchange rates, you can use any service of your choice, e.g.
https://api.exchangerate.host/latest
○ You may limit the currencies supported by your implementation based on what the currency exchange rate service supports
○ The currency of funds in the transfer operation must match the receiver's account currency 
(e.g. system should return an error when requesting to transfer 30 GBP from a USD account to a EUR account, 
however transferring 30 GBP from USD to GBP is a valid operation - corresponding amount of USD is exchanged to GBP and credited to GBP account).
```
Non-functional requirements:
```
As mentioned previously, the following list is given in order of priority, you may implement only part of the items (more is better, however).
1. Test coverage should be not less than 80%
2. Implemented web service should be resilient to 3rd party service unavailability
3. DB schema versioning should be implemented
```
```
Evaluation criteria:
1. Feature-completeness
2. Code quality, structure
3. Test quality
4. Non-functional requirement implementation
5. Ease of setup
```
---
Request examples
---
Make transaction request:
```
curl --location --request POST 'http://localhost:8080/api/transactions/process' \
--header 'Content-Type: application/json' \
--data-raw '{
  "accountId":"e9e187d7-2ac3-4a71-bb59-5ca84c4a7d66",
  "targetAccountId":"4e6f843f-8e0d-44b2-92d9-152a7efdc1f5",
  "amount":30.0,
  "currency":"EUR"
}'
```

Transaction history request:
```
curl --location --request GET 'http://localhost:8080/api/transactions/4e6f843f-8e0d-44b2-92d9-152a7efdc1f5'
```

Account list request:
```
curl --location --request GET 'http://localhost:8080/api/accounts/a08f92c5-5e91-4b2a-8c84-82410fe5d104'
```