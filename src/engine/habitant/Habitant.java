package engine.habitant;

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
    private String prenom;
    private String sexe;
    private int age;

    private Besoins besoins;

    private int ouverture,conscience,extraversion,agreabilite,nevrosisme;


    private List<Liens> relations = new ArrayList<Liens>();


    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;

        this.besoins=new Besoins();

        this.besoins.setMoral(50);

        this.ouverture=(int)(Math.random() * 101);
        this.conscience=(int)(Math.random() * 101);
        this.extraversion=(int)(Math.random() * 101);
        this.agreabilite=(int)(Math.random() * 101);
        this.nevrosisme=(int)(Math.random() * 101);

    }

    public void vivre(boolean estLaNuit){
        besoins.vivre(estLaNuit);
        if (extraversion > 70 && besoins.getSocial() < 30){
            besoins.setMoral(besoins.getMoral() - 2);
        }
        if(nevrosisme > 70 && besoins.getSante() < 50){
            besoins.setMoral(besoins.getMoral() - 3);
        }
    }

    // NOUVELLE MÉTHODE : On cible spécifiquement l'amitié pour les rencontres de rue
    public void ajouterLienAmical(Habitant autre) {
        boolean dejaConnu = false;

        // On vérifie si on le connaît déjà
        for (Liens l : relations) {
            if (l.getPartenaire() == autre) {
                l.appliquerBonusMental(this);
                dejaConnu = true;
            }
        }

        // S'ils ne se connaissent pas, ils deviennent amis
        if (!dejaConnu) {
            Liens nouveauLien = new Amical(autre, 50); // Toujours un lien Amical
            relations.add(nouveauLien);
            nouveauLien.appliquerBonusMental(this);
        }

        // Gain social calculé à chaque interaction
        int gainSocial = 15;
        if (this.extraversion > 70) {
            gainSocial += 10; // Les extravertis gagnent plus
        }
        this.besoins.setSocial(this.besoins.getSocial() + gainSocial);
    }


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

    @Override
    public String toString() {
        return prenom + " (" + sexe + "," + age + "ans) - Moral:" + getMoral();
    }


    public int getExtraversion() {
        return extraversion;
    }
    public int getOuverture() {
        return ouverture;
    }
    public int getConscience() {
        return conscience;
    }
    public int getAgreabilite() {
        return agreabilite;
    }
    public int getNevrosisme() {
        return nevrosisme;
    }

}