package engine.map;

/**
 * Classe Horloge : C'est le moteur temporel de la simulation.
 * Elle synchronise la Date et l'Heure et fournit une interface unifiée
 * pour les autres composants du jeu.
 */
public class Horloge {
    private Date date;
    private Heure heure;

    public Horloge() {
        // Initialisation du temps de départ
        this.date = new Date(12, 2, 2026);
        this.heure = new Heure(8, 0);
    }

    /**
     * Méthode principale appelée à chaque tour de la simulation.
     * Elle gère la transition entre les heures et, si nécessaire, le passage au jour suivant.
     */
    public void incrementer(int nbMinutes) {
        if (heure.incrementer(nbMinutes)) {
            date.incrementerJour();
        }
    }

    /**
     * Délégation : L'horloge interroge son composant Date pour savoir si c'est le weekend.
     */
    public boolean estWeekend() {
        return date.estWeekend();
    }

    public String getHeureActuelle() {
        return toString();
    }

    // Permet d'accéder à l'objet Heure pour des besoins spécifiques (ex: déclencher des events).
    public Heure getHeureObject() {
        return heure;
    }

    /**
     * Affiche la date et l'heure courante (ex: "Lundi 12/2/2026 - 08:00").
     */
    @Override
    public String toString() {
        return date.toString() + " - " + heure.toString();
    }
}