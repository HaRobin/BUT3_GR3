# ASBank

HALM Robin - ZELL Renaud

## Rapport de fin de sprint 3

### Problèmes rencontrés

#### Selenium
Nous n'avons pas réussi à faire fonctionner Selenium sur aucun de nos deux postes. En effet, concernant le poste de Renaud, nous soupçonnons que l'utilisation de Java 8 puisse poser problème ou alors le poste lui-même aurait un problème de configuration autre, étant donné qu'il est impossible pour Renaud de faire fonctionner le projet sous Java 11.
Nous vous avions sollicité lors de notre dernier cours pour que vous nous aidiez à résoudre ce souci, mais après y avoir apporté les modifications que vous avez suggérées, je ne réussissais quand même pas à lancer de tests avec Selenium.

#### Workflow
D'autre part, une tâche qui était à réaliser pour le précédent livrable n'a toujours pas pu être réalisée. Il s'agissait d'implémenter un workflow GitHub pour réaliser les tests et envoyer un rapport de qualité Sonar sur la page SonarCloud de notre projet. Cependant, nous avons rencontré différents problèmes lors de la configuration relative à la base de données dans un premier temps, puis à la soumission du rapport Sonar. Des traces du travail fourni peuvent être retrouvées sur la branche `dev_workflow`. Pour malgré tout continuer l'évaluation de la qualité du projet `ASBank`, nous avons continué d'utiliser un SonarQube dockerisé. C'est pourquoi je vous invite à vous référer aux différentes documentations disponibles dans le répertoire `./doc` de notre projet, où vous trouverez des captures évolutives des rapports Sonar.

### Conclusion

Cette expérience nous a permis de gagner en compétences, mais plus particulièrement en résolution de problèmes, même si nous n'avons pas pu trouver des solutions à tous nos problèmes.
Nous nous rendons d'autant plus compte de l'importance d'intégrer les outils vus au cours de ce module dès le début d'un projet.
