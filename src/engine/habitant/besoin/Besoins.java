package engine.habitant.besoin;

public class Besoins {
    private int faim = 100;
    private int moral = 100;
    private int fatigue = 100;
    private int sante = 100;
    private int social = 100;

    public void vivre(boolean estLaNuit){

        if (estLaNuit) {
            // NUIT : L'habitant dort.
            // Il récupère de l'énergie doucement (environ 8h pour faire 0 -> 100%)
            if (Math.random() < 0.25) this.fatigue += 1;

        } else {
            // JOUR : L'habitant vit sa vie.

            // 1. La fatigue baisse très lentement (Il faut tenir 16h !)
            if (Math.random() < 0.10) this.fatigue -= 1;

            // 2. Le besoin social baisse petit à petit
            if (Math.random() < 0.15) this.social -= 1;

            // 3. La faim baisse (On aura faim vers midi)
            if (Math.random() < 0.20) this.faim -= 1;
        }

        // --- LOGIQUE DU MORAL ET DE LA SANTÉ ---

        // Si l'habitant a très faim ou se sent très seul, il déprime petit à petit
        if (this.faim < 30 || this.social < 30 || this.fatigue < 20) {
            if (Math.random() < 0.10) this.moral -= 1;
        }
        // À l'inverse, si tout va bien, le moral remonte naturellement !
        else if (this.faim > 70 && this.social > 70 && this.fatigue > 50) {
            if (Math.random() < 0.05) this.moral += 1;
        }

        // La santé ne baisse QUE s'il meurt de faim ou s'il est en dépression totale
        if (this.faim <= 0 || this.moral <= 0) {
            if (Math.random() < 0.15) this.sante -= 1;
        }

        // --- SÉCURITÉ : Garder les valeurs entre 0 et 100 ---
        if (this.faim < 0) this.faim = 0;
        if (this.fatigue < 0) this.fatigue = 0;
        if (this.fatigue > 100) this.fatigue = 100;
        if (this.social < 0) this.social = 0;
        if (this.social > 100) this.social = 100;
        if (this.sante < 0) this.sante = 0;
        if (this.moral < 0) this.moral = 0;
        if (this.moral > 100) this.moral = 100;
    }

    // --- GETTERS & SETTERS (Inchangement par rapport à ton code) ---
    public int getFaim(){ return faim; }
    public int getMoral(){ return moral; }
    public int getFatigue(){ return fatigue; }
    public int getSante(){ return sante; }
    public int getSocial(){ return social; }

    public void setFaim(int faim){ this.faim = Math.max(0, Math.min(100, faim)); }
    public void setMoral(int moral){ this.moral=Math.max(0, Math.min(100, moral)); }
    public void setFatigue(int fatigue){ this.fatigue=Math.max(0, Math.min(100, fatigue)); }
    public void setSante(int sante){ this.sante=Math.max(0, Math.min(100, sante)); }
    public void setSocial(int social){ this.social=Math.max(0, Math.min(100, social)); }
}