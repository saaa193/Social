package engine.habitant.besoin;

public class Besoins {
    private int faim=100;
    private int moral=100;
    private int fatigue=100;
    private int sante=100;
    private int social=100;

    // --- MODIFICATION : La biologie s'adapte au Jour/Nuit ---
    public void vivre(boolean estLaNuit){
        if (estLaNuit) {
            // PENDANT LA NUIT (SOMMEIL)
            this.fatigue += 15; // On se repose très vite !
            // La faim et le social ne baissent pas quand on dort
        } else {
            // PENDANT LE JOUR
            this.fatigue -= 2;
            this.social -= 2;

            // ASTUCE DE SURVIE : En attendant d'avoir des restaurants, on ralentit la faim
            // (1 chance sur 3 de baisser, sinon ils meurent tous en 2 minutes)
            if (Math.random() < 0.3) {
                this.faim -= 1;
            }
        }

        // Si l'habitant a trop faim ou est trop seul
        if (faim < 20 || social < 20) {
            this.moral -= 2;
        }

        // Si l'habitant a trop faim ou est trop triste, sa santé baisse
        if (this.faim <= 0 || this.moral <= 0) {
            this.sante -= 3;
        }

        // Sécurité pour rester entre 0 et 100
        if (faim < 0) faim = 0;
        if (fatigue < 0) fatigue = 0;
        if (fatigue > 100) fatigue = 100; // Ne pas dépasser 100% de repos
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