# DirectDeal
>**Note:** For extensive documentation and details about the project, please visit the new [directdeal-docs repository](https://github.com/nieuwmijnleven/directdeal-docs). This repository contains all official documentation, architecture descriptions, and development guidelines for the DirectDeal project.

DirectDeal is an online direct-trading platform for everyone. People can not only freely trade their own items, but also purchase products sold by others.

DirectDeal is an online direct-trading platform for everyone. People can not only freely trade their own items, but also purchase products sold by others.

# The Goal
DirectDeal is my hobby project focused on designing and implementing an enterprise Java environment in a decentralized setting, specifically within a microservices architecture (MSA). In this project I made use of popular and promising technologies, design patterns, and tools, including Spring Boot, Spring WebFlux, Event Sourcing (Axon Framework), the CQRS pattern, Kafka, Redis, NoSQL (MongoDB), REST API, Docker, Kubernetes, Java 12, JPA (Hibernate), and Gradle.

In addition, I used Domain-Driven Design (DDD) as a foundation to properly model and structure complex business logic. By dividing the system into clear bounded contexts — such as sales, catalog, chat, and account management — I was able to develop a dedicated domain model for each microservice with consistent terminology and rules. This made it easier to implement business rules accurately and keep services independent, which fits perfectly with the microservices architecture.

The reason I started this project was to compensate for my lack of experience with decentralized environments. Thanks to this project, I successfully implemented an enterprise Java environment within an MSA structure. I therefore believe I am well-prepared to work on projects in a decentralized environment.

# Development Environment
> **Note:** Due to a recent migration, the libraries and tools used have been updated to newer versions. The old versions are shown with strikethrough below, with the new versions listed alongside them.

> **More details:** The current versions of all tools and libraries can be found in section [5. Development Environment](https://github.com/nieuwmijnleven/directdeal-docs/blob/master/Nederlands/5.%20Ontwikkelomgeving.md) of the directdeal-docs.

* Operating System: Linux (Debian/Ubuntu), Windows (WSL)  
* JAVA: ~~12~~ **17**  
* Mysql: ~~5.7~~ **5.7.41**  
* Spring boot: ~~2.5.0~~ **3.2.12**  
* Spring webflux: ~~5.3.7~~ **3.2.12**  
* Spring cloud gateway: ~~3.0.4~~ **4.2.1**
* Spring cloud kubernetes: ~~2.0.4~~ **3.2.0 (Spring Cloud Kubernetes Client Config)**  
* Gradle: ~~7.4~~ **7.6.4**  
* Axon framework: ~~4.5~~ **4.9.4**    
* Hibernate : ~~5.4.31~~ **6.4.10**
* Mongo DB : 4.0.25  
* Kafka: wurstmeister/kafka:2.12-2.4.0  
* Vue.js: 2.6.14  
* Vuetify: 2.5.4  

# Execution Steps
> **Note:** A more detailed explanation of the execution steps — both for the local environment and for the Google Cloud Platform (GCP) environment — can be found in [6. Execution Method](https://github.com/nieuwmijnleven/directdeal-docs/blob/master/Nederlands/6.%20Uitvoeringsmethode.md) of the directdeal-docs.

## 1. Clone the DirectDeal Project GitHub Repository
```
$> git clone https://github.com/nieuwmijnleven/directdeal.git
$> cd ./directdeal
```
## 2. Install Minikube
Follow the instructions to install Minikube on Ubuntu.
[How to Install Minikube on Ubuntu](https://phoenixnap.com/kb/install-minikube-on-ubuntu#:~:text=Step%201%3A%20Update%20System%20and%20Install%20Required%20Packages%0ABefore,apt-get%20install%20curl%0Asudo%20apt-get%20install%20apt-transport-https%0AIn%20the%20)

## 3. Build and Start All MSA Services
```
$> ./start-direct-deal-service.sh
```
Note: this process may take longer than five minutes.
You can check the status of all containers with the command:
```
$> kubectl get pods
```

## 4. Open the DirectDeal Website
[http://localhost:8084](http://localhost:8084)

# System Architecture
> **More information:** Refer to chapters 8, 9, and 10 of the [directdeal-docs](https://github.com/nieuwmijnleven/directdeal-docs) for more details on the system architecture, Domain-Driven Design (DDD), and the structure of the microservices.

## 1. Overall System Structure
The system consists of six microservices: direct-deal-account-service, direct-deal-chatting-service, direct-deal-gateway-service, direct-deal-sale-service, and direct-deal-sale-catalog-service. Each microservice fulfills a specific role within the system. For example, direct-deal-account-service is responsible for managing user accounts and handling login and logout, while direct-deal-sale-service provides functionality for users to register, modify, and delete products.

Notably, the Event Sourcing and CQRS (Command Query Responsibility Segregation) patterns are applied to direct-deal-sale-service and direct-deal-sale-catalog-service. This reduces lock contention on the read side (direct-deal-sale-catalog-service) and eliminates the need to use transactions. This enables the system to offer users faster response times when reading data.
![image](https://github.com/nieuwmijnleven/directdeal/assets/56591823/8a180293-c45e-4bf8-aab4-447fa0b3a8ad)

## 2. Microservices
### 1. direct-deal-account-service
* This microservice is responsible for managing user accounts and handling login and logout
* Upon login, the service generates a JWT (JSON Web Token) and sends it to the user
* By including the JWT in the authorization header of HTTP requests, users gain access to all microservices within the system

### 2. direct-deal-chatting-service
* This microservice provides chat functionality between sellers and buyers

### 3. direct-deal-gateway-service
* This microservice acts as the API Gateway within the MSA environment and serves as the central entry point for external requests directed at the backend microservices

### 4. direct-deal-sale-service
* This microservice facilitates functions for registering, modifying, and deleting products by users
* Both the Event Sourcing and CQRS (Command Query Responsibility Segregation) patterns are applied to this service
* The service is responsible for the Command side of the CQRS pattern
* In addition, this service manages the EventStore for Event Sourcing purposes

### 5. direct-deal-sale-catalog-service
* This microservice provides users with an overview of available products
* Event Sourcing and the CQRS pattern also apply to this service
* The service implements the Query side of CQRS
* In addition, this service reads from the EventStore as part of Event Sourcing

### 6. direct-deal-transaction-history-service
* This microservice shows users an overview of completed transactions

## 3. Technologies, Design Patterns, and Tools
### 1. Event Sourcing
Event Sourcing is an approach in which every change to the state of a domain object is recorded as a separate event. Unlike the traditional method of persistent storage — where only the current state of an entity is saved — Event Sourcing stores a series of state changes (events) in an event store. The current state of an entity is reconstructed by replaying all these events in sequence.

Each state change results in a new event, which is stored as a self-contained operation; this makes the process inherently atomic. The event store serves not only as a database for events, but often also as a message broker. This allows other services to subscribe to relevant events via an exposed API. Each stored event is then forwarded to all interested subscribers, making the event store the backbone of an event-driven microservice architecture.

In this architecture, operations are handled as follows: when a modification request comes in, the relevant events are first retrieved from the event store to reconstruct the current state of the entity. The entity is then updated and a new event is appended.

### 2. CQRS (Command Query Responsibility Segregation) Pattern
CQRS stands for Command Query Responsibility Segregation, a pattern in which read and write operations are strictly separated. Instead of applying complex join queries to a single central database, CQRS sets up a separate view database for read-only purposes.

This approach reduces lock contention when performing join operations and makes it possible to avoid transactions for read operations. Synchronization between the read-only and write-only databases takes place in chronological order of events, typically via a messaging system such as Kafka or RabbitMQ. This prevents merge conflicts at the domain level in the write database.

By implementing CQRS, the performance, scalability, and security of the system can be significantly improved.

<p align="center">
  <img src = "https://github.com/nieuwmijnleven/directdeal/assets/56591823/c2aace41-7e20-4e9d-9285-ed9f6c745c42" width=670/>
</p>

# Screenshots
<p align="center">
  <kbd>
    <img src = "https://github.com/nieuwmijnleven/directdeal/assets/56591823/ac292c21-5ff8-415f-9363-89568ae71de9" width=200/>    
  </kbd>
  <kbd>
    <img src = "https://github.com/nieuwmijnleven/directdeal/assets/56591823/b56f8942-5276-42bc-95db-27b222c18987" width=200/>    
  </kbd>
  <kbd>
    <img src = "https://github.com/nieuwmijnleven/directdeal/assets/56591823/c0f9c429-bd78-4740-9515-fcaa815273d2" width=200/>    
  </kbd>
</p>

# Author

* **Jeon, Cheol**

