package engine.map;

public class Heure {
    private int heures;
    private int minutes;

    // On enlève les secondes pour la V1, sinon c'est trop lent !
    // Si tu veux vraiment les garder, remets-les, mais la journée durera 2h réelles.

    public Heure(int heures, int minutes) {
        this.heures = heures;
        this.minutes = minutes;
    }

    public boolean incrementer() {
        minutes++; // On avance de minute en minute
        if (minutes >= 60) {
            minutes = 0;
            heures++;
            if (heures >= 24) {
                heures = 0;
                return true; // Nouvelle journée !
            }
        }
        return false;
    }

    public static String transform(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return transform(heures) + ":" + transform(minutes);
    }
}