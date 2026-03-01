package engine.habitant.besoin;

/**
 * Cette classe gère le modèle biologique et psychologique d'un habitant.
 * Elle contient l'état actuel des besoins et la logique de mise à jour à chaque tour de jeu.
 */
public class Besoins {
    // Les 5 piliers de la vie de l'habitant.
    // Initialisés à 100 pour représenter un état de santé/bien-être maximal.
    private int faim = 100;
    private int moral = 100;
    private int fatigue = 100;
    private int sante = 100;
    private int social = 100;

    /**
     * Méthode appelée à chaque tour de simulation ("nextRound").
     * @param estLaNuit : booléen qui change le comportement biologique selon le moment de la journée.
     */
    public void vivre(boolean estLaNuit){

        if (estLaNuit) {
            // --- LOGIQUE DE NUIT ---
            // Le sommeil est prioritaire. On utilise Math.random() pour que la récupération soit progressive,
            // ce qui rend la simulation moins robotique qu'une simple incrémentation fixe.
            if (Math.random() < 0.25) this.fatigue += 1;

        } else {
            // --- LOGIQUE DE JOUR ---
            // Les besoins diminuent au fil de la journée.
            // Les probabilités permettent de simuler une dégradation lente et réaliste.

            // 1. La fatigue baisse (l'habitant dépense de l'énergie).
            if (Math.random() < 0.10) this.fatigue -= 1;

            // 2. Le besoin social baisse (l'habitant se sent seul s'il n'interagit pas).
            if (Math.random() < 0.15) this.social -= 1;

            // 3. La faim baisse (besoin vital).
            if (Math.random() < 0.20) this.faim -= 1;
        }

        // --- LOGIQUE DU MORAL ET DE LA SANTÉ ---

        // Si l'habitant est dans un état critique (besoins faibles), son moral en pâtit.
        if (this.faim < 30 || this.social < 30 || this.fatigue < 20) {
            if (Math.random() < 0.10) this.moral -= 1;
        }
        // À l'inverse, si tous les besoins sont satisfaits, le moral remonte (cercle vertueux).
        else if (this.faim > 70 && this.social > 70 && this.fatigue > 50) {
            if (Math.random() < 0.05) this.moral += 1;
        }

        // Si les besoins vitaux (faim ou moral) sont à zéro, la santé commence à se dégrader.
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

    // --- GETTERS & SETTERS ---
    public int getFaim(){ return faim; }
    public int getMoral(){ return moral; }
    public int getFatigue(){ return fatigue; }
    public int getSante(){ return sante; }
    public int getSocial(){ return social; }

    // Utilisation de Math.max/min pour assurer une sécurité supplémentaire lors de la modification.
    public void setFaim(int faim){ this.faim = Math.max(0, Math.min(100, faim)); }
    public void setMoral(int moral){ this.moral=Math.max(0, Math.min(100, moral)); }
    public void setFatigue(int fatigue){ this.fatigue=Math.max(0, Math.min(100, fatigue)); }
    public void setSante(int sante){ this.sante=Math.max(0, Math.min(100, sante)); }
    public void setSocial(int social){ this.social=Math.max(0, Math.min(100, social)); }
}