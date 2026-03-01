package engine.map;

/**
 * Classe Heure : Représente le temps journalier dans la simulation.
 * Elle gère l'incrémentation des minutes et des heures, et gère le passage
 * à la journée suivante via un signal booléen.
 */
public class Heure {
    private int heures;
    private int minutes;

    public Heure(int heures, int minutes) {
        this.heures = heures;
        this.minutes = minutes;
    }

    public int getHeures() {
        return heures;
    }
    public int getMinutes() {
        return minutes;
    }

    /**
     * Avance le temps de N minutes.
     * Cette méthode permet d'accélérer la simulation tout en garantissant que les transitions
     * (passage à l'heure suivante ou à minuit) soient toujours déclenchées correctement.
     * * @param nbMinutes Le pas de temps (nombre de minutes à ajouter au compteur).
     * @return true si le passage à minuit a été effectué (nécessitant un changement de jour), false sinon.
     */
    public boolean incrementer(int nbMinutes) {
        boolean jourSuivant = false;

        // On avance minute par minute pour respecter les seuils de basculement (60min -> 1h, 24h -> 00h)
        for (int i = 0; i < nbMinutes; i++) {
            minutes++;

            // Gestion du passage à l'heure suivante
            if (minutes >= 60) {
                minutes = 0;
                heures++;

                // Gestion du passage à minuit
                if (heures >= 24) {
                    heures = 0;
                    jourSuivant = true; // Signal envoyé à l'Horloge pour incrémenter la Date
                }
            }
        }
        return jourSuivant;
    }

    /**
     * Méthode utilitaire pour formater les nombres (ex: 8 devient "08").
     * Utilisée pour avoir un affichage propre dans l'interface graphique.
     */
    public static String transform(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }

    /**
     * Affiche l'heure au format HH:MM pour l'utilisateur.
     */
    @Override
    public String toString() {
        return transform(heures) + ":" + transform(minutes);
    }
}