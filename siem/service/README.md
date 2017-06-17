# Soteria SIEP Service

### Proposed technology

- Spring Boot
- Spring Security
- OAuth
- MongoDB
- MySQL

### Implementation
We should keep well known REST architecture.

##### Why MongoDB?
Our logs are unstructured. I think the best option is to save them in _document oriented_ database. 
We should take a look on **ECS logging** and make schema like that. It will help us during querying.

##### Why MySQL?
The main benefit of using **relational databas** is **Hibernate ORM**. This could be right place for
all entities we need in our application such as:

- users with roles
- projects
- agents
- etc...