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

    public String getHeureActuelle() {
        return toString();
    }

    @Override
    public String toString() {
        return date.toString() + " - " + heure.toString();
    }
}