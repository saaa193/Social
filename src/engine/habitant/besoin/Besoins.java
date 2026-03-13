package engine.habitant.besoin;

import engine.habitant.nutrition.StrategieNutrition;

public class Besoins {

    private int faim    = 100;
    private int moral   = 100;
    private int fatigue = 100;
    private int sante   = 100;
    private int social  = 100;
    private StrategieNutrition strategieNutrition;

    public Besoins(StrategieNutrition strategie) {
        this.strategieNutrition = strategie;
    }

    public void vivre(boolean estLaNuit, double tauxFaim, double tauxFatigue, double tauxSocial) {

        strategieNutrition.appliquer(this);

        if (estLaNuit) {
            if (Math.random() < 0.85) this.fatigue += 3;
            if (Math.random() < 0.50) this.faim    += 2;
        } else {
            if (Math.random() < tauxFaim)    this.faim    -= 1;
            if (Math.random() < tauxFatigue) this.fatigue -= 1;
            if (Math.random() < tauxSocial)  this.social  -= 1;
        }

        if (this.faim < 30 || this.social < 30 || this.fatigue < 20) {
            if (Math.random() < 0.15) this.moral -= 1;
        } else if (this.faim > 70 && this.social > 70 && this.fatigue > 50) {
            if (Math.random() < 0.08) this.moral += 1;
        }

        if (this.faim <= 0 || this.moral <= 0) {
            if (Math.random() < 0.25) this.sante -= 1;
        }
    }

    // --- GETTERS ---
    public int getFaim()    { return faim; }
    public int getMoral()   { return moral; }
    public int getFatigue() { return fatigue; }
    public int getSante()   { return sante; }
    public int getSocial()  { return social; }

    // --- SETTERS avec clamping 0-100 ---
    public void setFaim(int faim)       { this.faim    = Math.max(0, Math.min(100, faim)); }
    public void setMoral(int moral)     { this.moral   = Math.max(0, Math.min(100, moral)); }
    public void setFatigue(int fatigue) { this.fatigue = Math.max(0, Math.min(100, fatigue)); }
    public void setSante(int sante)     { this.sante   = Math.max(0, Math.min(100, sante)); }
    public void setSocial(int social)   { this.social  = Math.max(0, Math.min(100, social)); }
}