# DirectDeal
DirectDeal is een online direct handelssysteem voor iedereen. Daarin kan iedereen vrij niet alleen zijn spullen verhandelen maar ook waren kopen die anderen verkopen.  

# Het Doel
DirectDeal is mijn vrijetijdsproject die onder gedecentraliseerde omgeving, vooral MSA (Micro Service Architecture), naar ontwerp en implementatie van enterprise java omgeving streeft. In die project gebruikte ik populaire of veelbelovende technologieën, ontwerp patronen, en gereedschappen. Zoals onder meer Spring Boot, Spring Webflux, Event Sourcing(Axon Framework), CQRS patroon, Kafka, Redis, No Sql(Mongo DB), REST-API, Docker, Kubernetes, Java 11, JPA(Hibernate), en Gradle.  
De reden waarom ik die project begon is om het gebrek aan ervaring onder gedecentraliseerde omgeving te compenseren. Eindelijk door die project heb ik een enterprise java omgeving implementeren onder MSA omgeving. Daarom geloof ik in dat ik bereid ben om in een project te werken onder gedecentraliseerde omgeving.  

# Ontwikkel-Omgeving
* Operating System: Linux (Debian/Ubuntu), Windows (WSL)
* JAVA: 12 
* Mysql: 5.7 
* Mongo DB : 4.0.25 
* Kafka: wurstmeister/kafka:2.12-2.4.0 
* Redis: 6.2.4 
* Spring boot: 2.5.0 
* Spring webflux: 5.3.7 
* Spring cloud gateway: 3.0.4 
* Spring cloud kubernetes: 2.0.4 
* Gradle: 7.4 
* Axon framework: 4.5 
* Kubernetes/minikube : v1.30.1 
* Hibernate : 5.4.31 
* Vue.js:2.6.14 
* Vuetify:2.5.4

# Het Verloop van Uitvoering
## 1. De Github-Opslagplaats van DirectDeal Project Klonen
```
$> git clone https://github.com/nieuwmijnleven/directdeal.git
$> cd ./directdeal
```
## 2. Minikube Installeren
[How to Install Minikube on Ubuntu](https://phoenixnap.com/kb/install-minikube-on-ubuntu#:~:text=Step%201%3A%20Update%20System%20and%20Install%20Required%20Packages%0ABefore,apt-get%20install%20curl%0Asudo%20apt-get%20install%20apt-transport-https%0AIn%20the%20)

## 3. Alle MSA Services Opbouwen en Starten
```
$> ./start-direct-deal-service.sh
```
* Letop: Het kost behoorlijke tijd meer dan 5 minuten. U kan status van elke container controleren als u die opdracht "kubectl get pods" in de shell uitvoert.  

## 4. DirectDeal Website Koppelen
[http://localhost:8084](http://localhost:8084)

# SysteemArchitectuur
## 1. De structuur van het geheel systeem
Het gehele systeem bestaat uit 6 Micro-Services: direct-deal-account-service, direct-deal-chatting-service, direct-deal-gateway-service, direct-deal-sale-service, en direct-deal-sale-catalog-service. Elke Micro-Service speelt een rol in dit systeem. direct-deal-account-service is bijvoorbeeld verantwoordelijk voor het management van gebruikersaccount en in/outloggen en direct-deal-sale-service geeft functionaliteiten van het registreren, wijzigen, en verwijderen van producten aan gebruikers. opvallend is dat voor direct-deal-sale-service en direct-deal-sale-catalog-service Event-Sourcing en CQRS(Command Query Responsibility Segregation) Patroon gelden. Daardoor, aan kan de kant van het lezen (direct-deal-sale-catalog-service) wordt lock contentie verminderd en hoeft transactie niet gebruikt te worden. Dus, die systeem kan gebruikers voorzien van sneller reactiesnelheid van lezen. 

![image](https://github.com/nieuwmijnleven/directdeal/assets/56591823/8a180293-c45e-4bf8-aab4-447fa0b3a8ad)

## 2. MicroServices
### 1. direct-deal-account-service
* Die microservice is verantwoordelijk voor het management van gebruikersaccount en in/outloggen
* Die microservice creërt een JWT(JSON Web Token) en stuurt het een gebruiker wanneer de gebruiker in de directdeal systeem logt.
* door JWT in de authorization veld van httpaanvraagheader te injecteren, kunnen gebruikers alle microservices gebruiken. 
  
### 2. direct-deal-chatting-service
* Die microservice verstrekt functionaliteit van chatting tussen verkoper en koper 

### 3. direct-deal-gateway-service
* Die microservice speelt een rol in API gateway onder MSA-Omgeving

### 4. direct-deal-sale-service
* Die microservice geeft functionaliteiten van het registreren, wijzigen, en verwijderen van producten aan gebruikers
* Event-sourcing en CQRS(Command Query Responsibility Segregation) patroon gelden voor die microservice
* Die microservice implementeert de `Command` deel van CQRS Patroon
* Die microservice organiseert de `EventStore` in Event-Sourcing

### 5. direct-deal-sale-catalog-service
* Die microservice geeft lijst van verkrijgbare producten aan gebruikers
* Event-sourcing en CQRS(Command Query Responsibility Segregation) patroon gelden voor die microservice
* Die microservice implementeert de `Query` deel van CQRS Patroon
* Die microservice organiseert de `` in Event-Sourcing 

### 6. direct-deal-transaction-history-service
* Die microservice geeft list van voltooide transacties aan gebruikers

## 3. Technologieën, Ontwerp Patronen, en Gereedschappen
### 1. EventSourcing
Event sourcing is een gunstige manier om atomisch status te bijwerken en evenement te publiceren. De traditionele wijze om een entiteit aan te houden is om zijn huidige status te bewaren. Event sourcing gebruikt wezenlijk andere, event-gecentrialiseerd aanpak aan persistentie. Een zakelijke object wordt aangehouden door een serie status wijziging evenementen op te slaan. Wanneer de status van een object verandert, een nieuwe evenement wordt toegevoegd aan de serie evenementen. doordat een evenement een operatie is, is het wezenlijk atomisch. De huidige status van een entiteit wordt hergebouwd door zijn evenements te overspelen. 

Evenementen worden aangehouden in een evenementopslag. Niet allen fungeert de evenementopslag als een database van evenementen, maar ook gedraagt als een berichtenbroker. Het verstrekt een API die het mogelijk maken dat services abonneren op evenementen. Elke evenement die wordt aangehouden in de evenementopslag wordt geleverd door de evenementopslag naar alle interessante abonnee. De evenementopslag is de ruggengraat van event-driven microservice architectuur.

In deze architectuur, verzoeken om een entiteit te bijwerken worden behandeld door evenementen van de entiteit te zoeken van de evenementopslag, de huidige status te herbouwen, de entiteit te bijwerken, en de nieuwe evenement te bewaren.  

### 2. CQRS(Command Query Responsibility Segregation) Patroon
CQRS staat voor Scheiding van opdracht- en queryverantwoordelijkheid, een patroon dat lees- en updatebewerkingen voor een gegevensarchief scheidt. In plaats ervan om een ingewikkeld join query te gebruiken, in CQRS wordt er een allen-lezen view database voor toegevoegd. Het gevolg daarvan is dat lock contentie wordt verminderd door joinbewerkingen en de transactie hoeft niet gebruikt te worden doordat een zakelijke object tussen alleen-lezen database en allen-schrijven database wordt gesychroniseerd volgens de chronologische volgorde van evenementen door middel van een berichtenbroker zoals Kafka en RabbitMQ. In alleen-schrijven database wordt het voorkomen dat updateopdrachten samenvoegingsconflicten op domeinniveau veroorzaken.
Het implementeren van CQRS kan de prestaties, schaalbaarheid en beveiliging maximaliseren. 

<p align="center">
  <img src = "" width=670/>
</p>

# Schermafbeeldingen
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

# Auteur

* **Jeon, Cheol** 

