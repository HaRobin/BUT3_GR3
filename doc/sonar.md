# Utilisation de sonar

Dans cette documentation, nous allons utiliser sonar pour évaluer la qualité de code du projet BankIUT. Ce document
sera divisé en deux sections, la première étant dédié à l'utilisation de SonarLint dans votre IDE, la seconde étant
dédié
à l'installation de SonarQube.

## Installation et utilisation de SonarLint

### Installation de SonarLint

Dans votre IDE installer le plugin SonarLint.

![img.png](images/sonar/img.png)

### Utilisation de SonarLint

Faite un clic droit sur votre projet, puis cliquez sur `SonarLint` -> `Analyse with SonarLint`.

![img.png](images/sonar/img-2.png)

Voici le résultat de notre exécution de SonarLint sur le projet BankIUT le 08/11/2024 (Sprint 3).

![img.png](images/sonar/img-3.png)

## Installation et configuration de SonarQube

Malheureusement à l'état actuel de la documentation, nous n'avons pas pu réaliser d'analyse de code avec SonarQube. Nous
allons donc documenter l'état actuel de note avancement dans l'utilisation de SonarQube.

### Installation de Docker

Nous avons utilisé Docker pour installer SonarQube. Pour cela, il vous faudra installer Docker sur votre machine. Pour
cela suivé la documentation officielle de docker pour l'installer sur votre
machine : [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)

### Installation de SonarQube

Pour installer SonarQube, il vous suffit de lancer la commande suivante :

```shell
docker compose up -d --build 
```

Pour toutes les marriage suivant, il vous suffira de lancer la commande suivante :

```shell
docker compose up -d
```

### Configuration de SonarQube

Pour accéder à l'interface de SonarQube, il vous suffit d'ouvrir votre navigateur et de vous rendre à l'adresse
suivante : [http://localhost:9001/](http://localhost:9001/), connectez-vous une première fois avec les identifiants
suivants : `admin` / `admin` puis reconfigurer les identifiants.

Configurer le projet SonarQube en suivant les étapes suivantes :

A VENIR
