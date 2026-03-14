package engine.habitant;

import config.GameConfiguration;
import engine.evenement.EventVisitor;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Liens;
import engine.MobileElement;
import engine.map.Block;
import engine.habitant.besoin.Besoins;
import engine.map.Map;

import java.util.ArrayList;
import java.util.List;

public class Habitant extends MobileElement {

    // Identité
    private String prenom;
    private String sexe;
    private int age;

    // Besoins vitaux
    private Besoins besoins;

    // Profil psychologique OCEAN
    private Psychologie psychologie;

    // Taux de dégradation personnels (calculés depuis OCEAN)
    private double tauxFaim;
    private double tauxFatigue;
    private double tauxSocial;

    // Réseau social
    private List<Liens> relations = new ArrayList<Liens>();

    public Habitant(Block position, Map map, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        super(position, map);

        this.psychologie = new Psychologie();

        this.besoins = new Besoins(this.psychologie.determinerStrategieNutrition());
        this.besoins.setMoral(50);

        // Calcul des taux personnels depuis OCEAN
        this.tauxFaim    = GameConfiguration.BASE_FAIM
                - (psychologie.getConscience()   / 100.0) * GameConfiguration.OCEAN_IMPACT;
        this.tauxFatigue = GameConfiguration.BASE_FATIGUE
                + (psychologie.getNevrosisme()   / 100.0) * GameConfiguration.OCEAN_IMPACT
                + (psychologie.getExtraversion() / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;
        this.tauxSocial  = GameConfiguration.BASE_SOCIAL
                + (psychologie.getExtraversion() / 100.0) * GameConfiguration.OCEAN_IMPACT
                - (psychologie.getAgreabilite()  / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;
    }
    /**
     * Pattern Visitor : L'habitant "accepte" de subir un événement (météo, social, etc.)
     * L'événement appliquera alors ses propres règles sur l'habitant.
     */
    public void acceptEvent(EventVisitor visiteurEvenement) {
        visiteurEvenement.visit(this);
    }

    // --- VIVRE ---
    public void vivre(boolean estLaNuit) {
        besoins.vivre(estLaNuit, tauxFaim, tauxFatigue, tauxSocial);

        EtatHabitant etat = psychologie.determinerEtat(besoins);
        etat.appliquer(this);

        // NETTOYAGE des liens morts à chaque tour
        nettoyerLiensMorts();
    }

    /**
     * Supprime tous les liens dont la force est tombée à 0.
     */
    private void nettoyerLiensMorts() {
        List<Liens> aSupprimer = new ArrayList<Liens>();

        // 1. On collecte les liens morts
        for (Liens l : relations) {
            if (l.estMort()) {
                aSupprimer.add(l);
            }
        }

        // 2. On les supprime
        for (Liens l : aSupprimer) {
            relations.remove(l);
        }
    }

    /**
     * Gestion d'une rencontre avec un autre habitant.
     * 1. Si déjà connu → evoluerForce + appliquerBonusMental
     * 2. Si inconnu → calcul compatibilité OCEAN → nouveau lien
     */
    public void ajouterLienAmical(Habitant autre) {
        boolean dejaConnu = false;

        for (Liens l : relations) {
            if (l.getPartenaire() == autre) {
                // Lien existant : on le fait évoluer ET on applique le bonus
                l.evoluerForce(this);
                l.appliquerBonusMental(this);
                dejaConnu = true;
                break;
            }
        }

        if (!dejaConnu) {
            int limiteAmis = (psychologie.getExtraversion() / 10) + 1;

            if (this.relations.size() < limiteAmis) {
                // Calcul de la force initiale via compatibilité OCEAN
                int forceInitiale = calculerCompatibilite(autre);
                Liens nouveauLien = new Amical(autre, forceInitiale);
                relations.add(nouveauLien);
                nouveauLien.appliquerBonusMental(this);
            } else {
                // Cercle social plein : petit gain social quand même
                this.besoins.setSocial(this.besoins.getSocial() + 5);
            }
        }
    }

    /**
     * Délègue le calcul de compatibilité à Psychologie.
     */
    private int calculerCompatibilite(Habitant autre) {
        return psychologie.calculerCompatibiliteAvec(autre.getPsychologie());
    }

    // --- ACCESSEURS ---
    public List<Liens> getRelation()   { return relations; }
    public String getPrenom()          { return prenom; }
    public String getSexe()            { return sexe; }
    public int getAge()                { return age; }
    public int getMoral()              { return besoins.getMoral(); }
    public Besoins getBesoins()        { return besoins; }

    public Psychologie getPsychologie()  { return psychologie; }
    public int getExtraversion()         { return psychologie.getExtraversion(); }
    public int getOuverture()            { return psychologie.getOuverture(); }
    public int getConscience()           { return psychologie.getConscience(); }
    public int getAgreabilite()          { return psychologie.getAgreabilite(); }
    public int getNevrosisme()           { return psychologie.getNevrosisme(); }

    @Override
    public String toString() {
        return prenom + " (" + sexe + ", " + age + " ans) - Moral: " + getMoral();
    }

    // =========================================================
    // 2. IMPLEMENTATION DU PATTERN TEMPLATE METHOD (MobileElement)
    // =========================================================

    @Override
    protected void seDeplacer() {
        // Cas où l'habitant ne bouge pas
        if (besoins.getFatigue() < 20) return; // dort
        if (getMoral() < 30 && Math.random() > 0.33) return; // déprimé

        int direction = (int)(Math.random() * 4);
        Block pos = getPosition();
        int l   = pos.getLine();
        int col = pos.getColumn();

        if      (direction == 0 && !map.isOnTop(pos))         l--;
        else if (direction == 1 && !map.isOnBottom(pos))      l++;
        else if (direction == 2 && !map.isOnLeftBorder(pos))  col--;
        else if (direction == 3 && !map.isOnRightBorder(pos)) col++;

        setPosition(map.getBlock(l, col));
    }

    @Override
    protected void agir(boolean estLaNuit) {
        vivre(estLaNuit);
    }
}
