# DirectDeal
DirectDeal is een online direct-handelsplatform voor iedereen. Daar kunnen mensen niet alleen vrij hun spullen verhandelen, maar ook producten kopen die door anderen worden verkocht.  

# Het Doel
DirectDeal is mijn vrijetijdsproject dat zich in een gedecentraliseerde omgeving, met name binnen een microservices-architectuur (MSA), richt op het ontwerpen en implementeren van een enterprise Java-omgeving. In dit project heb ik gebruikgemaakt van populaire en veelbelovende technologieën, ontwerp­patronen en tools, zoals onder andere Spring Boot, Spring WebFlux, Event Sourcing (Axon Framework), het CQRS-patroon, Kafka, Redis, NoSQL (MongoDB), REST API, Docker, Kubernetes, Java 12, JPA (Hibernate) en Gradle.
De reden waarom ik dit project ben begonnen, is om mijn gebrek aan ervaring met gedecentraliseerde omgevingen te compenseren. Dankzij dit project heb ik met succes een enterprise Java-omgeving geïmplementeerd binnen een MSA-structuur. Daarom geloof ik dat ik goed voorbereid ben om aan projecten in een gedecentraliseerde omgeving te werken.

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

# Uitvoeringsstappen
## 1. De GitHub-opslagplaats van het DirectDeal-project klonen
```
$> git clone https://github.com/nieuwmijnleven/directdeal.git
$> cd ./directdeal
```
## 2. Minikube Installeren
Volg de instructies om Minikube op Ubuntu te installeren.
[How to Install Minikube on Ubuntu](https://phoenixnap.com/kb/install-minikube-on-ubuntu#:~:text=Step%201%3A%20Update%20System%20and%20Install%20Required%20Packages%0ABefore,apt-get%20install%20curl%0Asudo%20apt-get%20install%20apt-transport-https%0AIn%20the%20)

## 3. Alle MSA-services bouwen en starten
```
$> ./start-direct-deal-service.sh
```
Let op: dit proces kan langer dan vijf minuten duren.
U kunt de status van alle containers bekijken met het commando: 
```
$> kubectl get pods
```

## 4. De DirectDeal-website openen
[http://localhost:8084](http://localhost:8084)

# Systeemarchitectuur
## 1. De structuur van het gehele systeem
Het systeem bestaat uit zes microservices: direct-deal-account-service, direct-deal-chatting-service, direct-deal-gateway-service, direct-deal-sale-service en direct-deal-sale-catalog-service. Elke microservice vervult een specifieke rol binnen het systeem. Zo is direct-deal-account-service verantwoordelijk voor het beheer van gebruikersaccounts en het in- en uitloggen, terwijl direct-deal-sale-service functionaliteiten biedt voor het registreren, wijzigen en verwijderen van producten door gebruikers.
Opvallend is dat voor direct-deal-sale-service en direct-deal-sale-catalog-service het Event Sourcing- en CQRS-patroon (Command Query Responsibility Segregation) wordt toegepast. Hierdoor wordt de lock-contentie aan de leeszijde (direct-deal-sale-catalog-service) verminderd en is het niet nodig om transacties te gebruiken. Dit stelt het systeem in staat om gebruikers een snellere reactietijd bij het lezen van gegevens te bieden.
![image](https://github.com/nieuwmijnleven/directdeal/assets/56591823/8a180293-c45e-4bf8-aab4-447fa0b3a8ad)

## 2. MicroServices
### 1. direct-deal-account-service
* De microservice is verantwoordelijk voor het beheer van gebruikersaccounts en het in- en uitloggen.
* Deze microservice creëert een JWT (JSON Web Token) en stuurt dit naar de gebruiker wanneer deze in het DirectDeal-systeem inlogt.
* Door het JWT in de authorization-header van de HTTP-aanvraag te plaatsen, kunnen gebruikers alle microservices gebruiken. 
  
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
  <img src = "https://github.com/nieuwmijnleven/directdeal/assets/56591823/c2aace41-7e20-4e9d-9285-ed9f6c745c42" width=670/>
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

