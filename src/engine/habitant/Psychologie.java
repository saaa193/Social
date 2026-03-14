package engine.habitant;

import engine.habitant.besoin.Besoins;
import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.etat.EtatIsole;
import engine.habitant.etat.EtatStable;
import engine.habitant.nutrition.NutritionConsciente;
import engine.habitant.nutrition.NutritionNevrosee;
import engine.habitant.nutrition.NutritionSociale;
import engine.habitant.nutrition.StrategieNutrition;

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
    public int getOuverture()    { return ouverture; }
    public int getConscience()   { return conscience; }
    public int getExtraversion() { return extraversion; }
    public int getAgreabilite()  { return agreabilite; }
    public int getNevrosisme()   { return nevrosisme; }

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
     * Fait évoluer légèrement les traits OCEAN selon l'état psychologique actuel.
     * Appelée à chaque tour depuis Habitant.agir().
     */
    public void evoluer(EtatHabitant etat) {
        if (etat instanceof EtatEpanoui) {
            agreabilite = Math.min(100, agreabilite + 1);
            nevrosisme  = Math.max(0,   nevrosisme  - 1);
            conscience  = Math.min(100, conscience  + 1);
            ouverture   = Math.min(100, ouverture   + 1);
        } else if (etat instanceof EtatAnxieux) {
            nevrosisme   = Math.min(100, nevrosisme   + 1);
            extraversion = Math.max(0,   extraversion - 1);
            conscience   = Math.max(0,   conscience   - 1);
            ouverture    = Math.max(0,   ouverture    - 1);
        } else if (etat instanceof EtatIsole) {
            extraversion = Math.max(0, extraversion - 1);
            agreabilite  = Math.max(0, agreabilite  - 1);
            ouverture    = Math.max(0, ouverture    - 1);
            conscience   = Math.max(0, conscience   - 1);
        }
        // EtatStable → rien ne change
    }


}