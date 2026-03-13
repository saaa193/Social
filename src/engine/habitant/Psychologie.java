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
     * Pas de if enchaînés sur le type — on compare des valeurs métier.
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
}