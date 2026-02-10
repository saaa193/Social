package engine.map;

public class Heure {
    private int heures;
    private int minutes;
    private int seconds;

    public Heure(int heures, int minutes, int seconds) {
        this.heures = heures;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public boolean incrementer(){
        seconds++;
        if(seconds >= 59){
            seconds = 0;
            minutes++;
            if(minutes > (59)){
                minutes = 0;
                heures++;
                if(heures > (23)){
                    heures = 0;
                    return true;
                }
            }
        }
        return false;

    }

    public static String transform(int value) {
        String result = "";
        if (value < 10) {
            result = "0" + value;
        } else {
            result = String.valueOf(value);
        }
        return result;
    }

    public String toString(){
        return transform(heures) + ":" + transform(minutes)+ ":" + transform(seconds);
    }
}
