package engine.map;

/**
 * Classe Date : Gère le calendrier de la simulation.
 * Elle permet de suivre l'écoulement du temps (jours, mois, années)
 * et de déterminer si l'on est en semaine ou en weekend.
 */
public class Date {
    private int jour;
    private int mois;
    private int annee;

    // Jour de la semaine stocké sous forme d'index (0 = Lundi, 6 = Dimanche)
    private int jourSemaine = 0;
    // Utilisation d'un tableau pour faciliter le formatage de l'affichage
    private String[] nomsJours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

    public Date(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    /**
     * Logique métier permettant de définir si l'habitant est en weekend.
     * Utile pour changer les comportements des habitants (repos, loisirs).
     */
    public boolean estWeekend(){
        // Les indices 5 (Samedi) et 6 (Dimanche) correspondent au weekend.
        return (jourSemaine == 5 || jourSemaine == 6);
    }

    /**
     * Méthode de cycle temporel : met à jour la date à chaque incrémentation.
     * Utilise une logique de calendrier simplifiée (30 jours/mois).
     */
    public void incrementerJour() {
        // Avance le compteur de jour dans la semaine (modulo 7)
        jourSemaine++;
        if (jourSemaine > 6) {
            jourSemaine = 0;
        }

        // Gestion du changement de mois
        jour++;
        if (jour > 30) {
            jour = 1;
            mois++;
            // Gestion du changement d'année
            if (mois > 12) {
                mois = 1;
                annee++;
            }
        }
    }

    /**
     * Permet d'afficher la date proprement dans l'interface (UI).
     */
    @Override
    public String toString() {
        return nomsJours[jourSemaine] + " " + jour + "/" + mois + "/" + annee;
    }
}