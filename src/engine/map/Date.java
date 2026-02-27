package engine.map;

public class Date {
    private int jour;
    private int mois;
    private int annee;

    private int jourSemaine = 0;
    private String[] nomsJours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

    public Date(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    public boolean estWeekend(){
        return (jourSemaine==5 || jourSemaine==6);
    }

    public void incrementerJour() {
        jourSemaine++;
        if (jourSemaine > 6) {
            jourSemaine = 0;
        }

        jour++;
        if (jour > 30) {
            jour = 1;
            mois++;
            if (mois > 12) {
                mois = 1;
                annee++;
            }
        }
    }


    @Override
    public String toString() {
        return nomsJours[jourSemaine] + " " + jour + "/" + mois + "/" + annee;
    }
}