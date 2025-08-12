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

## 2. Microservices
### 1. direct-deal-account-service
* De microservice is verantwoordelijk voor het beheer van gebruikersaccounts en het in- en uitloggen
* Bij het inloggen genereert de service een JWT (JSON Web Token) en stuurt deze naar de gebruiker
* Door het JWT in de authorization-header van de HTTP-aanvraag mee te sturen, krijgen gebruikers toegang tot alle microservices binnen het systeem 
  
### 2. direct-deal-chatting-service
* Deze microservice biedt chatfunctionaliteit tussen verkopers en kopers

### 3. direct-deal-gateway-service
* Deze microservice fungeert als API Gateway binnen de MSA-omgeving en vormt het centrale toegangspunt voor externe aanvragen richting de achterliggende microservices

### 4. direct-deal-sale-service
* Deze microservice faciliteert functies voor het registreren, aanpassen en verwijderen van producten door gebruikers
* Zowel het Eventsourcing- als CQRS-patroon (Command Query Responsibility Segregation) worden toegepast op deze service
* De service is verantwoordelijk voor het Command-gedeelte van het CQRS-patroon
* Daarnaast beheert deze service de EventStore ten behoeve van Eventsourcing

### 5. direct-deal-sale-catalog-service
* Deze microservice voorziet gebruikers van een overzicht van beschikbare producten
* Voor deze service gelden eveneens Eventsourcing en het CQRS-patroon
* De service implementeert het Query-gedeelte van CQRS
* Daarnaast leest deze service uit de EventStore als onderdeel van Eventsourcing

### 6. direct-deal-transaction-history-service
* Deze microservice toont gebruikers een overzicht van afgeronde transacties

## 3. Technologieën, Ontwerp Patronen en Tools
### 1. EventSourcing
Event Sourcing is een benadering waarbij elke wijziging in de status van een domeinobject wordt vastgelegd als een afzonderlijk evenement. In tegenstelling tot de traditionele manier van persistente opslag — waarbij enkel de actuele status van een entiteit wordt bewaard — bewaart Event Sourcing een serie van statuswijzigingen (evenementen) in een event store. De actuele toestand van een entiteit wordt samengesteld door al deze evenementen achtereenvolgens toe te passen ('replayen').

Elke statuswijziging resulteert in een nieuw evenement, dat als een op zichzelf staande operatie wordt opgeslagen; dit maakt het proces intrinsiek atomair. De event store fungeert niet alleen als database voor evenementen, maar vaak ook als message broker. Hierdoor kunnen andere services zich abonneren op relevante evenementen via een aangeboden API. Elk opgeslagen evenement wordt vervolgens doorgestuurd naar alle geïnteresseerde abonnees, wat van de event store de ruggengraat maakt van een event-driven microservice-architectuur.

In deze architectuur worden bewerkingen als volgt afgehandeld: bij een wijzigingsverzoek worden eerst de relevante evenementen uit de event store opgehaald om de actuele status van de entiteit te reconstrueren. Daarna wordt de entiteit bijgewerkt en een nieuw evenement toegevoegd.  

### 2. CQRS(Command Query Responsibility Segregation) Patroon
CQRS staat voor Command Query Responsibility Segregation, een patroon waarbij lees- en schrijfbewerkingen strikt worden gescheiden. In plaats van complexe join-queries toe te passen op één centrale database, wordt er binnen CQRS een aparte view-database ingericht voor alleen-lezen doeleinden.

Deze aanpak vermindert lock-contentie bij het uitvoeren van join-bewerkingen en maakt het mogelijk om transacties te vermijden voor leesoperaties. Synchronisatie tussen de alleen-lezen en de alleen-schrijven databases vindt plaats in chronologische volgorde van gebeurtenissen, doorgaans via een berichtensysteem zoals Kafka of RabbitMQ. Dit voorkomt samenvoegingsconflicten op domeinniveau in de schrijfdatabank.

Door CQRS te implementeren kunnen prestaties, schaalbaarheid en beveiliging van het systeem significant worden verbeterd.

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

