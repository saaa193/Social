package engine.habitant.besoin;

public class Besoins {
    private int faim=100;
    private int moral=100;
    private int fatigue=100;
    private int sante=100;
    private int social=100;

    public void vivre(){
        this.faim -= 2;
        this.fatigue -= 2;
        this.social -= 2;


        // Si l'habitant a trop faim ou est trop triste (moral à 0), sa santé baisse
        if (faim < 20 || fatigue < 20 || social < 20) {
            this.moral -= 2;
        }

        // Si l'habitant a trop faim ou est trop triste, sa santé baisse
        if (this.faim <= 0 || this.moral <= 0) {
            this.sante -= 3;
        }

        // Sécurité pour rester entre 0 et 100
        if (faim < 0) faim = 0;
        if (fatigue < 0) fatigue = 0;
        if (social < 0) social = 0;
        if (sante < 0) sante = 0;
        if (moral < 0) moral = 0;
    }

    public int getFaim(){
        return faim;
    }
    public int getMoral(){
        return moral;
    }
    public int getFatigue(){
        return fatigue;
    }
    public int getSante(){
        return sante;
    }
    public int getSocial(){
        return social;
    }

    public void setFaim(int faim){
        this.faim = Math.max(0, Math.min(100, faim));
    }
    public void setMoral(int moral){
        this.moral=Math.max(0, Math.min(100, moral));
    }
    public void setFatigue(int fatigue){
        this.fatigue=Math.max(0, Math.min(100, fatigue));
    }
    public void setSante(int sante){
        this.sante=Math.max(0, Math.min(100, sante));
    }
    public void setSocial(int social){
        this.social=Math.max(0, Math.min(100, social));
    }
}