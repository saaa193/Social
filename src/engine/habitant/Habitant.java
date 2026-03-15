package engine.habitant;

import config.GameConfiguration;
import engine.evenement.EventVisitor;
import engine.habitant.deplacement.StrategieDeplacement;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Liens;
import engine.MobileElement;
import engine.habitant.lien.Professionnel;
import engine.habitant.visitor.ContagionVisitor;
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
    private double tauxRecuperation;

    // Réseau social
    private List<Liens> relations = new ArrayList<Liens>();

    // Ajouter cet attribut avec les autres attributs en haut de Habitant
    private StrategieDeplacement strategieDeplacement;

    public Habitant(Block position, Map map, String prenom, String sexe, int age) {
        super(position, map);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;

        this.psychologie = new Psychologie();

        this.besoins = new Besoins(this.psychologie.determinerStrategieNutrition());

        // Légère variance initiale pour diversifier la population
        this.besoins.setFaim(60 + (int) (Math.random() * 40));  // entre 60 et 100
        this.besoins.setFatigue(50 + (int) (Math.random() * 50));  // entre 50 et 100
        this.besoins.setSocial(40 + (int) (Math.random() * 60));  // entre 40 et 100
        this.besoins.setMoral(40 + (int) (Math.random() * 40));  // entre 40 et 80

        // Calcul des taux personnels depuis OCEAN
        this.tauxFaim = GameConfiguration.BASE_FAIM
                - (psychologie.getConscience() / 100.0) * GameConfiguration.OCEAN_IMPACT;

        this.tauxFatigue = GameConfiguration.BASE_FATIGUE
                + (psychologie.getNevrosisme() / 100.0) * GameConfiguration.OCEAN_IMPACT
                + (psychologie.getExtraversion() / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;

        this.tauxSocial = GameConfiguration.BASE_SOCIAL
                + (psychologie.getExtraversion() / 100.0) * GameConfiguration.OCEAN_IMPACT
                - (psychologie.getAgreabilite() / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;

        this.tauxRecuperation = GameConfiguration.BASE_RECUPERATION
                - (psychologie.getNevrosisme() / 100.0) * GameConfiguration.OCEAN_IMPACT * 2;

        // Calcul de la stratégie de déplacement selon OCEAN
        this.strategieDeplacement = psychologie.determinerStrategieDeplacement();
    }

    /**
     * Pattern Visitor : L'habitant "accepte" de subir un événement (météo, social, etc.)
     * L'événement appliquera alors ses propres règles sur l'habitant.
     */
    public void acceptEvent(EventVisitor visiteurEvenement) {
        visiteurEvenement.visit(this);
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
     * Gestion d'une rencontre professionnelle avec un autre habitant.
     * Deux habitants consciencieux créent un lien professionnel.
     */
    public void ajouterLienProfessionnel(Habitant autre) {
        boolean dejaConnu = false;

        for (Liens l : relations) {
            if (l.getPartenaire() == autre) {
                l.evoluerForce(this);
                l.appliquerBonusMental(this);
                dejaConnu = true;
                break;
            }
        }

        if (!dejaConnu) {
            int limiteCollegues = (psychologie.getConscience() / 10) + 1;

            if (this.relations.size() < limiteCollegues) {
                int forceInitiale = calculerCompatibilite(autre);
                Liens nouveauLien = new Professionnel(autre, forceInitiale);
                relations.add(nouveauLien);
                nouveauLien.appliquerBonusMental(this);
            } else {
                this.besoins.setSocial(this.besoins.getSocial() + 3);
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
    public List<Liens> getRelation() {
        return relations;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public int getAge() {
        return age;
    }

    public int getMoral() {
        return besoins.getMoral();
    }

    public Besoins getBesoins() {
        return besoins;
    }

    public Psychologie getPsychologie() {
        return psychologie;
    }

    public int getExtraversion() {
        return psychologie.getExtraversion();
    }

    public int getOuverture() {
        return psychologie.getOuverture();
    }

    public int getConscience() {
        return psychologie.getConscience();
    }

    public int getAgreabilite() {
        return psychologie.getAgreabilite();
    }

    public int getNevrosisme() {
        return psychologie.getNevrosisme();
    }


    @Override
    public String toString() {
        return prenom + " (" + sexe + ", " + age + " ans) - Moral: " + getMoral();
    }


    // 2. IMPLEMENTATION DU PATTERN TEMPLATE METHOD (MobileElement)
    @Override
    protected void seDeplacer() {
        // NUIT → tout le monde dort, personne ne bouge
        if (estLaNuit()) return;

        // FATIGUE → trop épuisé pour bouger
        if (besoins.getFatigue() < 35) return;

        // DÉPRIMÉ → bouge au ralenti
        if (getMoral() < 30 && Math.random() > 0.33) return;

        // Délègue le déplacement à la stratégie OCEAN
        strategieDeplacement.deplacer(this, map);
    }

    @Override
    protected void agir(boolean estLaNuit) {
        besoins.vivre(estLaNuit, tauxFaim, tauxFatigue, tauxSocial, tauxRecuperation);

        EtatHabitant etat = psychologie.determinerEtat(besoins);
        etat.appliquer(this);
        psychologie.evoluer(etat, besoins);

        // Les liens sociaux influencent aussi l'évolution du profil OCEAN
        psychologie.evoluerSelonReseau(relations);

        besoins.setStrategieNutrition(psychologie.determinerStrategieNutrition());

        // La stratégie de déplacement évolue aussi avec le profil OCEAN
        this.strategieDeplacement = psychologie.determinerStrategieDeplacement();

        int impact = etat.accept(new ContagionVisitor());
        if (impact != 0) {
            for (Liens l : relations) {
                Habitant proche = l.getPartenaire();
                int impactModule = (int)(impact * (l.getForce() / 100.0));
                proche.getBesoins().setMoral(proche.getBesoins().getMoral() + impactModule);
            }
        }

        nettoyerLiensMorts();
    }

}
