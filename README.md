# Banque Service - Application GraphQL

Application Spring Boot avec GraphQL pour la gestion de comptes bancaires et de transactions.

## � Captures d'écran

L'application inclut une interface GraphiQL complète permettant de tester toutes les fonctionnalités :

- Création de comptes (COURANT et EPARGNE)
- Gestion des transactions (DEPOT et RETRAIT)
- Consultation des statistiques
- Requêtes personnalisées

Voir les exemples d'utilisation avec screenshots dans la section [Tests avec GraphiQL](#-tests-avec-graphiql).

## �📋 Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, ou VS Code)

## 🚀 Démarrage de l'application

### 1. Compiler et lancer l'application

```bash
./mvnw spring-boot:run
```

Ou sur Windows :

```bash
mvnw.cmd spring-boot:run
```

### 2. Accès aux interfaces

- **GraphiQL** : http://localhost:8082/graphiql
- **Console H2** : http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:banque`
  - Username: `sa`
  - Password: _(laisser vide)_

## 📊 Structure du projet

```
src/main/java/com/example/banque_service/
├── controllers/
│   └── CompteControllerGraphQL.java
├── dto/
│   ├── CompteRequest.java
│   └── TransactionRequest.java
├── entities/
│   ├── Compte.java
│   └── Transaction.java
├── enums/
│   ├── TypeCompte.java
│   └── TypeTransaction.java
├── exceptions/
│   └── GraphQLExceptionHandler.java
├── repositories/
│   ├── CompteRepository.java
│   └── TransactionRepository.java
└── BanqueServiceApplication.java

src/main/resources/
├── graphql/
│   └── schema.graphqls
└── application.properties
```

## 🔧 Configuration

Les configurations sont définies dans `application.properties` :

```properties
# Base de données H2
spring.datasource.url=jdbc:h2:mem:banque
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Serveur
server.port=8082

# GraphQL
spring.graphql.graphiql.enabled=true
```

## 📝 Tests avec GraphiQL

### Requêtes (Queries)

#### 1. Récupérer tous les comptes

```graphql
query {
    allComptes {
        id
        solde
        dateCreation
        type
    }
}
```

![Tous les comptes](screen/Screenshot%202025-11-15%20110220.png)

#### 2. Récupérer un compte par ID

```graphql
query {
    compteById(id: 1) {
        id
        solde
        dateCreation
        type
    }
}
```

#### 3. Requête avec paramètre

```graphql
query($id: ID) {
    compteById(id: $id) {
        id
        type
    }
}
```

Variables :
```json
{
    "id": 1
}
```

#### 4. Statistiques sur les soldes

```graphql
query {
    totalSolde {
        count
        sum
        average
    }
}
```

![Statistiques soldes](screen/Screenshot%202025-11-15%20110302.png)

#### 5. Récupérer toutes les transactions d'un compte

```graphql
query {
    compteTransactions(id: 1) {
        id
        montant
        date
        type
    }
}
```

#### 6. Récupérer toutes les transactions

```graphql
query {
    allTransactions {
        id
        montant
        date
        type
        compte {
            id
            type
        }
    }
}
```

#### 7. Statistiques sur les transactions

```graphql
query {
    transactionStats {
        count
        sumDepots
        sumRetraits
    }
}
```

![Statistiques transactions](screen/Screenshot%202025-11-15%20110318.png)

### Mutations

#### 1. Créer un nouveau compte

```graphql
mutation {
    saveCompte(compte: {
        solde: 1500.0,
        dateCreation: "2024/11/18",
        type: COURANT
    }) {
        id
        solde
        type
    }
}
```

![Créer compte COURANT](screen/Screenshot%202025-11-15%20110121.png)

#### 2. Créer un compte d'épargne

```graphql
mutation {
    saveCompte(compte: {
        solde: 3000.0,
        dateCreation: "2024/11/18",
        type: EPARGNE
    }) {
        id
        solde
        type
    }
}
```

![Créer compte EPARGNE](screen/Screenshot%202025-11-15%20110150.png)

#### 3. Ajouter une transaction (dépôt)

```graphql
mutation {
    addTransaction(transaction: {
        compteId: 1,
        montant: 500.0,
        date: "2024/11/18",
        type: DEPOT
    }) {
        id
        montant
        type
        compte {
            id
        }
    }
}
```

![Ajouter dépôt](screen/Screenshot%202025-11-15%20110246.png)

#### 4. Ajouter une transaction (retrait)

```graphql
mutation {
    addTransaction(transaction: {
        compteId: 1,
        montant: 200.0,
        date: "2024/11/18",
        type: RETRAIT
    }) {
        id
        montant
        type
        compte {
            id
        }
    }
}
```

![Ajouter retrait](screen/Screenshot%202025-11-15%20110336.png)

## 🎯 Fonctionnalités

### Gestion des Comptes
- ✅ Créer un compte (COURANT ou EPARGNE)
- ✅ Récupérer tous les comptes
- ✅ Récupérer un compte par ID
- ✅ Calculer les statistiques des soldes (nombre, somme, moyenne)

### Gestion des Transactions
- ✅ Ajouter une transaction (DEPOT ou RETRAIT)
- ✅ Récupérer les transactions d'un compte
- ✅ Récupérer toutes les transactions
- ✅ Calculer les statistiques des transactions (nombre, somme des dépôts, somme des retraits)

### Gestion des erreurs
- ✅ Messages d'erreur personnalisés
- ✅ Validation des données
- ✅ Gestion des comptes inexistants

## 🔍 Types disponibles

### TypeCompte
- `COURANT` : Compte courant
- `EPARGNE` : Compte d'épargne

### TypeTransaction
- `DEPOT` : Dépôt d'argent
- `RETRAIT` : Retrait d'argent

## 📌 Notes importantes

- La base de données H2 est **en mémoire** : les données sont perdues à chaque redémarrage
- Le format de date attendu est : `yyyy/MM/dd` (ex: `2024/11/18`)
- Le port du serveur est configuré sur **8082**
- GraphiQL est activé pour faciliter les tests

## 🐛 Dépannage

### Erreur "Compte not found"
Vérifiez que le compte existe avec la requête `allComptes` avant de faire référence à son ID.

### Erreur "Invalid date format"
Assurez-vous d'utiliser le format `yyyy/MM/dd` pour les dates.

### Port déjà utilisé
Si le port 8082 est déjà utilisé, modifiez `server.port` dans `application.properties`.

## 📚 Technologies utilisées

- **Spring Boot 3.x**
- **Spring for GraphQL**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Maven**

## 👨‍💻 Développement

Pour ajouter de nouvelles fonctionnalités :

1. Mettre à jour le schéma GraphQL dans `schema.graphqls`
2. Créer ou modifier les entités dans `entities/`
3. Ajouter les méthodes dans les repositories
4. Implémenter les méthodes dans `CompteControllerGraphQL`
5. Tester avec GraphiQL

## 📄 Licence

Ce projet est à des fins éducatives.
