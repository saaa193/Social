package engine.habitant;

import config.GameConfiguration;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Familial;
import engine.habitant.lien.Liens;
import engine.MobileElement;
import engine.habitant.lien.Professionnel;
import engine.map.Block;
import engine.habitant.besoin.Besoins;
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
    private int ouverture, conscience, extraversion, agreabilite, nevrosisme;

    // Taux de dégradation personnels (calculés depuis OCEAN)
    private double tauxFaim;
    private double tauxFatigue;
    private double tauxSocial;

    // Réseau social
    private List<Liens> relations = new ArrayList<Liens>();

    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        this.besoins = new Besoins();
        this.besoins.setMoral(50);

        // Génération aléatoire du profil OCEAN
        this.ouverture     = (int)(Math.random() * 101);
        this.conscience    = (int)(Math.random() * 101);
        this.extraversion  = (int)(Math.random() * 101);
        this.agreabilite   = (int)(Math.random() * 101);
        this.nevrosisme    = (int)(Math.random() * 101);

        // Calcul des taux personnels depuis OCEAN
        this.tauxFaim    = GameConfiguration.BASE_FAIM
                - (conscience   / 100.0) * GameConfiguration.OCEAN_IMPACT;
        this.tauxFatigue = GameConfiguration.BASE_FATIGUE
                + (nevrosisme   / 100.0) * GameConfiguration.OCEAN_IMPACT
                + (extraversion / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;
        this.tauxSocial  = GameConfiguration.BASE_SOCIAL
                + (extraversion / 100.0) * GameConfiguration.OCEAN_IMPACT
                - (agreabilite  / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;
    }

    // --- VIVRE ---
    public void vivre(boolean estLaNuit) {
        besoins.vivre(estLaNuit, tauxFaim, tauxFatigue, tauxSocial);

        if (extraversion > 70 && besoins.getSocial() < 30) {
            besoins.setMoral(besoins.getMoral() - 2);
        }
        if (nevrosisme > 70 && besoins.getSante() < 50) {
            besoins.setMoral(besoins.getMoral() - 3);
        }

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
            int limiteAmis = (this.extraversion / 10) + 1;

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
     * Calcule la compatibilité entre deux habitants via leurs traits OCEAN.
     *
     * Formule :
     * - Agréabilité moyenne des deux → base du lien
     * - Proximité d'ouverture d'esprit → si similaires, lien plus fort
     *
     * Résultat entre 10 et 80 (jamais parfait, jamais nul)
     */
    private int calculerCompatibilite(Habitant autre) {
        // Base : moyenne des agréabilités (deux personnes sympas s'entendent bien)
        double baseAgreabilite = (this.agreabilite + autre.getAgreabilite()) / 2.0;

        // Bonus : plus leurs ouvertures sont proches, plus ils se comprennent
        // Math.abs → valeur absolue de la différence
        double differenceOuverture = Math.abs(this.ouverture - autre.getOuverture());
        double bonusOuverture = (100 - differenceOuverture) / 2.0;

        // Force finale entre 10 et 80
        int force = (int)((baseAgreabilite * 0.6) + (bonusOuverture * 0.4));
        return Math.max(10, Math.min(80, force));
    }

    // --- ACCESSEURS ---
    public List<Liens> getRelation()   { return relations; }
    public String getPrenom()          { return prenom; }
    public String getSexe()            { return sexe; }
    public int getAge()                { return age; }
    public int getMoral()              { return besoins.getMoral(); }
    public Besoins getBesoins()        { return besoins; }
    public int getExtraversion()       { return extraversion; }
    public int getOuverture()          { return ouverture; }
    public int getConscience()         { return conscience; }
    public int getAgreabilite()        { return agreabilite; }
    public int getNevrosisme()         { return nevrosisme; }

    @Override
    public String toString() {
        return prenom + " (" + sexe + ", " + age + " ans) - Moral: " + getMoral();
    }
}