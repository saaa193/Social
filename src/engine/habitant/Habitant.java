package engine.habitant;

import engine.MobileElement;
import engine.map.Block;
import java.util.ArrayList;
import java.util.List;

public class Habitant extends MobileElement {
    private String prenom;
    private String sexe;
    private int age;

    private int moral;

    private List<Habitant> amis = new ArrayList<Habitant>();


    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        this.moral = (int)(Math.random() * 100);
    }

    public void ajouterAmi(Habitant nouvelAmi) {
        if (!amis.contains(nouvelAmi)) {
            amis.add(nouvelAmi);
        }
    }

    public void augmenterMoral() {
        this.moral += 10; // Une rencontre fait plaisir
        if (this.moral > 100) this.moral = 100;
    }

    public void baisserMoral() {
        this.moral -= 1;
        if (this.moral < 0) this.moral = 0;
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
        return moral;
    }

    @Override
    public String toString() {
        return prenom + " (" + sexe + "," + age + "ans) - Moral:" + moral;    }
}
