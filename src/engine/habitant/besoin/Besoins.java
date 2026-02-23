package engine.habitant.besoin;

public class Besoins {
    private int faim=100;
    private int moral=100;
    private int fatigue=100;
    private int sante=100;
    private int social=100;

    public void vivre(){
        if(faim>0){
            faim--;
        }
        if(fatigue>0){
            fatigue--;
        }
        if(fatigue<20 || faim<20){
            sante=(Math.max(0,sante-1));
        }
        if(social>0){
            social--;
        }
        if(fatigue<20 || faim<20 || sante<20 || faim<20 || social<20){
            moral=(Math.max(0,moral-1));
        }
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
        this.faim=Math.min(0, Math.min(100, faim));
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