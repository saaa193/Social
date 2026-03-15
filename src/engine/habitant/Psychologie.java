package engine.habitant;

import engine.habitant.besoin.Besoins;
import engine.habitant.deplacement.*;
import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.etat.EtatIsole;
import engine.habitant.etat.EtatStable;
import engine.habitant.nutrition.NutritionConsciente;
import engine.habitant.nutrition.NutritionNevrosee;
import engine.habitant.nutrition.NutritionSociale;
import engine.habitant.nutrition.StrategieNutrition;
import engine.habitant.lien.Liens;
import java.util.List;

/**
 * Psychologie : encapsule le profil Big Five (OCEAN) d'un Habitant.
 * Responsabilité : connaître les traits de personnalité et
 * déterminer l'état psychologique courant.
 */

public class Psychologie {

     private int ouverture;
     private int conscience;
     private int extraversion;
     private int agreabilite;
     private int nevrosisme;

     public Psychologie() {
         this.ouverture    = (int)(Math.random() * 101);
         this.conscience   = (int)(Math.random() * 101);
         this.extraversion = (int)(Math.random() * 101);
         this.agreabilite  = (int)(Math.random() * 101);
         this.nevrosisme   = (int)(Math.random() * 101);
     }

     /**
      * Détermine l'état psychologique dominant de l'habitant.
      * Retourne un objet EtatHabitant polymorphique.
      * L'appelant fait juste etat.appliquer(habitant) sans savoir quel état c'est.
      */

     public EtatHabitant determinerEtat(Besoins besoins) {
         if (nevrosisme > 70 && besoins.getMoral() < 50) {
             return new EtatAnxieux();
         }
         if (extraversion > 70 && besoins.getSocial() < 30) {
             return new EtatIsole();
         }
         if (besoins.getMoral() > 70 && besoins.getSocial() > 60) {
             return new EtatEpanoui();
         }
         return new EtatStable();
        }

     /**
      * Calcule la compatibilité OCEAN entre deux profils psychologiques.
      */
     public int calculerCompatibiliteAvec(Psychologie autre) {
         double baseAgreabilite = (this.agreabilite + autre.agreabilite) / 2.0;
         double diffOuverture   = Math.abs(this.ouverture - autre.ouverture);
         double bonusOuverture  = (100 - diffOuverture) / 2.0;
         int force = (int)((baseAgreabilite * 0.6) + (bonusOuverture * 0.4));
         return Math.max(10, Math.min(80, force));
        }

     //Getters
    public int getOuverture() { return ouverture; }
    public int getConscience() { return conscience; }
    public int getExtraversion() { return extraversion; }
    public int getAgreabilite() { return agreabilite; }
    public int getNevrosisme() { return nevrosisme; }

    /**
     * Retourne la stratégie de nutrition selon le profil OCEAN dominant.
     * on compare des valeurs métier.
     */
    public StrategieNutrition determinerStrategieNutrition() {
        if (conscience > 65) {
            return new NutritionConsciente();
        }
        if (nevrosisme > 65) {
            return new NutritionNevrosee();
        }
        return new NutritionSociale();
    }

    /**
     * Fait évoluer les traits OCEAN selon l'état psychologique actuel.
     * L'évolution n'est plus fixe à 1 point — elle dépend de l'intensité
     * des traits et des besoins de l'habitant.
     */
    public void evoluer(EtatHabitant etat, Besoins besoins) {

        if (etat instanceof EtatEpanoui) {
            // Quand on est épanoui, on s'améliore
            // L'agréabilité aide à progresser plus vite
            int bonus = 1;
            if (agreabilite > 60) {
                bonus = 2;
            }
            agreabilite = Math.min(100, agreabilite + bonus);
            nevrosisme  = Math.max(0,   nevrosisme  - bonus);
            conscience  = Math.min(100, conscience  + 1);
            ouverture   = Math.min(100, ouverture   + 1);

        } else if (etat instanceof EtatAnxieux) {
            // Plus le moral est bas, plus on se dégrade vite
            int malus = 1;
            if (besoins.getMoral() < 25) {
                malus = 2;
            }
            // La conscience protège contre la dégradation
            if (conscience > 60) {
                malus = Math.max(1, malus - 1);
            }

            nevrosisme = Math.min(100,nevrosisme + malus);
            extraversion = Math.max(0, extraversion - malus);
            conscience = Math.max(0, conscience - 1);
            ouverture = Math.max(0, ouverture - 1);

        } else if (etat instanceof EtatIsole) {
            // Plus le social est bas, plus l'isolement empire
            int malus = 1;
            if (besoins.getSocial() < 20) {
                malus = 2;
            }
            extraversion = Math.max(0, extraversion - malus);
            agreabilite = Math.max(0, agreabilite - 1);
            ouverture = Math.max(0, ouverture - 1);
            conscience = Math.max(0, conscience - 1);

        }
        // EtatStable → rien ne change
    }

    /**
     * Retourne la stratégie de déplacement selon le profil OCEAN.
     * Même principe que determinerStrategieNutrition().
     */
    public StrategieDeplacement determinerStrategieDeplacement() {
        // Névrosisme très élevé → anxieux, fuit les autres
        if (nevrosisme > 65) {
            return new DeplacementAnxieux();
        }
        // Extraversion élevée → cherche les autres
        if (extraversion > 65) {
            return new DeplacementExtraverti();
        }
        // Extraversion faible → introverti, bouge peu
        if (extraversion < 35) {
            return new DeplacementIntroverti();
        }
        // Par défaut → déplacement stable aléatoire
        return new DeplacementStable();
    }

    /**
     * Un habitant est vulnérable si son névrosisme est élevé
     * et son agréabilité est faible.
     * Une personne vulnérable absorbe facilement les émotions négatives des autres.
     */
    public boolean estVulnerable() {
        return nevrosisme > 60 && agreabilite < 40;
    }

    /**
     * Un habitant est résilient si sa conscience et son agréabilité sont élevées.
     * Une personne résiliente résiste bien aux chocs émotionnels des autres.
     */
    public boolean estResiliant() {
        return conscience > 60 && agreabilite > 60;
    }

    /**
     * Fait évoluer les traits OCEAN selon les liens sociaux de l'habitant.
     * Les personnes autour de nous influencent notre profil psychologique.
     * Appelée depuis Habitant.agir() à chaque tour.
     */
    public void evoluerSelonReseau(List<Liens> relations) {

        // Si l'habitant n'a pas de liens, rien ne se passe
        if (relations.isEmpty()) {
            return;
        }

        // On parcourt tous les liens de l'habitant
        for (Liens lien : relations) {
            Habitant proche = lien.getPartenaire();

            // Plus le lien est fort, plus l'influence est grande
            // Un lien de force 100 donne une influence de 1.0
            // Un lien de force 50 donne une influence de 0.5
            double influence = lien.getForce() / 100.0;

            // Si le proche est très névrosé → notre névrosisme monte légèrement
            if (proche.getNevrosisme() > 70) {
                nevrosisme = Math.min(100, nevrosisme + (int)(influence * 2));
            }

            // Si le proche est très agréable → notre agréabilité monte légèrement
            if (proche.getAgreabilite() > 70) {
                agreabilite = Math.min(100, agreabilite + (int)(influence * 1));
            }

            // Si le proche est très extraverti → notre extraversion monte légèrement
            if (proche.getExtraversion() > 70) {
                extraversion = Math.min(100, extraversion + (int)(influence * 1));
            }
        }
    }

}