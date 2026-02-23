package engine.habitant;

import engine.MobileElement;
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


    private List<Habitant> amis = new ArrayList<Habitant>();


    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;

        this.besoins=new Besoins();

        this.ouverture=(int)(Math.random() * 101);
        this.conscience=(int)(Math.random() * 101);
        this.extraversion=(int)(Math.random() * 101);
        this.agreabilite=(int)(Math.random() * 101);
        this.nevrosisme=(int)(Math.random() * 101);

    }

    public void vivre(){
        besoins.vivre();
        if (extraversion > 70 && besoins.getSocial() < 30){
            besoins.setMoral(besoins.getMoral() - 1);
        }
        if(nevrosisme > 70 && besoins.getSante() < 50){
            besoins.setMoral(besoins.getMoral() - 2);
        }
    }

    public void ajouterAmi(Habitant nouvelAmi) {
        if (!amis.contains(nouvelAmi)) {
            amis.add(nouvelAmi);
            besoins.setMoral(besoins.getMoral() + 10);
        }
        int gainSocial=15;
        if(this.extraversion > 70){
            gainSocial+=10;
        }
        besoins.setSocial(besoins.getSocial() + gainSocial);
    }


    public List<Habitant> getAmis() {
        return amis;
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