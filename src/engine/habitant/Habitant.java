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

/**
 * La classe Habitant est l'entité centrale du moteur.
 * Elle combine des données d'identité, des besoins vitaux et un profil psychologique unique.
 */
public class Habitant extends MobileElement {
    // Identité
    private String prenom;
    private String sexe;
    private int age;

    // État de santé et bien-être
    private Besoins besoins;

    // Profil psychologique : Inspiré du modèle des "Big Five" (ouverture, conscience, extraversion, agréabilité, névrosisme)
    // Cela permet de rendre chaque habitant unique dans son comportement.
    private int ouverture, conscience, extraversion, agreabilite, nevrosisme;

    // Taux de dégradation personnels (calculés depuis OCEAN)
    private double tauxFaim;
    private double tauxFatigue;
    private double tauxSocial;


    // Liste des relations sociales (le réseau social de l'habitant)
    private List<Liens> relations = new ArrayList<Liens>();

    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        this.besoins = new Besoins();
        this.besoins.setMoral(50); // Départ neutre

        // Génération aléatoire du profil psychologique à la création
        this.ouverture = (int)(Math.random() * 101);
        this.conscience = (int)(Math.random() * 101);
        this.extraversion = (int)(Math.random() * 101);
        this.agreabilite = (int)(Math.random() * 101);
        this.nevrosisme = (int)(Math.random() * 101);

        // Dans le constructeur, APRES la génération OCEAN :
        this.tauxFaim    = GameConfiguration.BASE_FAIM - (conscience   / 100.0) * GameConfiguration.OCEAN_IMPACT;

        this.tauxFatigue = GameConfiguration.BASE_FATIGUE + (nevrosisme   / 100.0) * GameConfiguration.OCEAN_IMPACT + (extraversion / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;

        this.tauxSocial  = GameConfiguration.BASE_SOCIAL + (extraversion / 100.0) * GameConfiguration.OCEAN_IMPACT - (agreabilite  / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;
    }

    /**
     * Méthode appelée à chaque tour pour mettre à jour l'état de l'habitant.
     */
    public void vivre(boolean estLaNuit) {
        besoins.vivre(estLaNuit, tauxFaim, tauxFatigue, tauxSocial);

        // Influence OCEAN sur le moral → inchangé
        if (extraversion > 70 && besoins.getSocial() < 30) {
            besoins.setMoral(besoins.getMoral() - 2);
        }
        if (nevrosisme > 70 && besoins.getSante() < 50) {
            besoins.setMoral(besoins.getMoral() - 3);
        }
    }

    /**
     * Méthode clé pour la gestion du réseau social.
     * Elle intègre une limite de sociabilité basée sur le trait "extraversion".
     */
    public void ajouterLienAmical(Habitant autre) {
        boolean dejaConnu = false;

        // 1. Vérification : Est-ce qu'on se connaît déjà ?
        for (Liens l : relations) {
            if (l.getPartenaire() == autre) {
                l.appliquerBonusMental(this);
                dejaConnu = true;
            }
        }

        // 2. Gestion de la limite d'amis (le "Bottleneck" social)
        if (!dejaConnu) {
            // Plus l'extraversion est élevée, plus le cercle social peut être grand.
            int limiteAmis = (this.extraversion / 10) + 1;

            if (this.relations.size() < limiteAmis) {
                // Il y a de la place : création d'un nouveau lien.
                Liens nouveauLien = new Amical(autre, 50);
                relations.add(nouveauLien);
                nouveauLien.appliquerBonusMental(this);
            } else {
                // Pas de place : interaction sociale légère (juste un gain de social minime).
                this.besoins.setSocial(this.besoins.getSocial() + 5);
            }
        } else {
            // 3. Renforcement d'une relation existante (bonus supplémentaire pour les extravertis).
            int gainSocial = 15;
            if (this.extraversion > 70) {
                gainSocial += 10;
            }
            this.besoins.setSocial(this.besoins.getSocial() + gainSocial);
        }
    }

    // --- ACCESSEURS  ---
    public List<Liens> getRelation() { return relations; }
    public String getPrenom() { return prenom; }
    public String getSexe() { return sexe; }
    public int getAge() { return age; }
    public int getMoral() { return besoins.getMoral(); }
    public Besoins getBesoins() { return besoins; }

    @Override
    public String toString() {
        return prenom + " (" + sexe + "," + age + "ans) - Moral:" + getMoral();
    }

    // Getters pour les traits de personnalité
    public int getExtraversion() { return extraversion; }
    public int getOuverture() { return ouverture; }
    public int getConscience() { return conscience; }
    public int getAgreabilite() { return agreabilite; }
    public int getNevrosisme() { return nevrosisme; }
}