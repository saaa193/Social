package engine.map;

public class Horloge {
    private Date date;
    private Heure heure;

    public Horloge(){
        this.date=new Date(19,3, 2026);
        this.heure=new Heure(9,1,1);
    }

    public void incrementer(){
        if(heure.incrementer()){
            date.incrementerJour();
        }
    }

    @Override
    public String toString(){
        return date.toString()+":"+heure.toString();
    }
}
