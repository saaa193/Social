package engine.habitant;

import engine.MobileElement;
import engine.map.Block;

public class Habitant extends MobileElement {
    private String prenom;
    private String sexe;
    private int age;

    public Habitant(Block position, String prenom, String sexe, int age) {
        super(position);
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
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

    @Override
    public String toString() {
        return prenom+ "(" + sexe + "," + age + "ans)";
    }
}
