PROJET SOCIAL - README
Génie Logiciel et Projet - L2 Informatique
CY Cergy Paris Université 2025/2026
Auteurs : HANANE Sanaa & PIRABAKARAN Parthipan

===========================================
DESCRIPTION DU PROJET
===========================================

Social est une simulation sociale d'une population
de 200 habitants avec des profils psychologiques
basés sur le modèle Big Five (OCEAN).
Les habitants se déplacent, forment des liens sociaux,
vivent des événements et propagent des informations.

===========================================
LANCER LE PROJET SOUS ECLIPSE
===========================================

1. Importer le projet :
   File > Import > General > Existing Projects into Workspace
   Sélectionner le dossier racine du projet

2. Ajouter les librairies :
   Clic droit sur le projet > Build Path > Add External JARs
   Ajouter tous les fichiers .jar du dossier libs/

3. Lancer la simulation :
   Ouvrir src/test/TestGame.java
   Clic droit > Run As > Java Application

===========================================
LANCER LES TESTS UNITAIRES
===========================================

Ouvrir src/test/unit/SocialTestSuite.java
Clic droit > Run As > JUnit Test

6 TestCase sont inclus :
- TestBesoins
- TestEvenement
- TestPsychologie
- TestTraumatisme
- TestLiens
- TestCalculateurInteraction

===========================================
FICHIERS DE LOG
===========================================

Les logs sont générés automatiquement dans :
src/log/social-simulation.log

Niveaux utilisés :
- DEBUG : chaque tour de simulation
- INFO  : rencontres amicales et événements
- WARN  : rencontres négatives entre habitants

===========================================
LIBRAIRIES UTILISÉES
===========================================

Toutes les librairies sont fournies par l'enseignant
et sont disponibles dans le dossier libs/ :
- JFreeChart 1.0.19 (graphiques et dashboards)
- JUnit 4.11 (tests unitaires)
- Log4j 1.2.17 (système de journalisation)