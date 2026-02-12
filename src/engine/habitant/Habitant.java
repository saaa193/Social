package engine.habitant;

import engine.MobileElement;
import engine.map.Block;

public class Habitant extends MobileElement {
    private String prenom;
    private String sexe;
    private int age;

    private int moral;

    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        // un moral entre 0 et 100 au debut
        this.moral = (int)(Math.random() * 100);
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
