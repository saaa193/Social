package engine.map;

public class Horloge {
    private Date date;
    private Heure heure;

    public Horloge() {
        this.date = new Date(12, 2, 2026); // Date du rendu ;)
        this.heure = new Heure(8, 0);      // Commence à 08h00
    }

    public void incrementer() {
        // Si l'heure passe minuit (return true), on change de jour
        if (heure.incrementer()) {
            date.incrementerJour();
        }
    }

    // --- C'EST CETTE METHODE QU'IL MANQUAIT POUR LE GUI ---
    public String getHeureActuelle() {
        return toString();
    }

    @Override
    public String toString() {
        return date.toString() + " - " + heure.toString();
    }
}