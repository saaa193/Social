PROJET SOCIAL
Génie Logiciel et Projet - L2 Informatique
CY Cergy Paris Université 2025/2026
Auteurs : HANANE Sanaa & PIRABAKARAN Parthipan
Encadrant : Tianxiao LIU

Description
-----------
Simulation psychologique et sociale d'une population de 200 habitants
autonomes. Chaque habitant possède un profil de personnalité basé sur
le modèle Big Five (OCEAN : Ouverture, Conscience, Extraversion,
Agréabilité, Névrosisme). Les habitants se déplacent sur une grille,
forment des liens sociaux, évoluent psychologiquement et propagent
des informations selon un modèle SIR adapté.

Exécution sous Eclipse
----------------------
1. File > Import > General > Existing Projects into Workspace
   Sélectionner le dossier racine du projet

2. Clic droit sur le projet > Build Path > Add External JARs
   Ajouter les fichiers .jar du dossier libs/
    - JFreeChart 1.0.19 (graphiques et dashboards)
    - JUnit 4.11 (tests unitaires)
    - Log4j 1.2.17 (système de journalisation)

3. Ouvrir src/test/TestGame.java
   Clic droit > Run As > Java Application

Tests unitaires
---------------
Ouvrir src/test/unit/SocialTestSuite.java
Clic droit > Run As > JUnit Test

6 TestCase sont inclus :
- TestBesoins
- TestEvenement
- TestPsychologie
- TestTraumatisme
- TestLiens
- TestCalculateurInteraction

Logs
----
Les logs sont générés automatiquement dans src/log/social-simulation.log

Niveaux utilisés :
- DEBUG : chaque tour de simulation et rencontres détectées
- INFO  : rencontres amicales et événements déclenchés