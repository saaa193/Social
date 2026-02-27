package engine.map;

public class Horloge {
    private Date date;
    private Heure heure;

    public Horloge() {
        this.date = new Date(12, 2, 2026); // Date du rendu ;)
        this.heure = new Heure(8, 0);      // Commence à 08h00
    }

    public void incrementer() {
        if (heure.incrementer()) {
            date.incrementerJour();
        }
    }

    public boolean estWeekend() {
        return date.estWeekend(); // L'horloge demande à son objet date
    }

    public String getHeureActuelle() {
        return toString();
    }

    // --- AJOUT : Permet au moteur de savoir quelle heure il est ---
    public Heure getHeureObject() {
        return heure;
    }

    @Override
    public String toString() {
        return date.toString() + " - " + heure.toString();
    }
}