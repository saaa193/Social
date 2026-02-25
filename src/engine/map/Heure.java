package engine.map;

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

    public boolean incrementer() {
        minutes++;
        if (minutes >= 60) {
            minutes = 0;
            heures++;
            if (heures >= 24) {
                heures = 0;
                return true;
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