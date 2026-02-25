package engine.process;

import engine.map.Map;
import engine.habitant.Habitant;
import engine.map.Horloge;
import engine.map.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobileElementManager implements MobileInterface {

    private Map map;
    private List<Habitant> habitants= new ArrayList<Habitant>();
    private Horloge horloge= new Horloge();
    private Random random = new Random();

    public MobileElementManager(Map map) {
        this.map = map;
    }

    @Override
    public void nextRound() {

        // 1. Déterminer s'il fait nuit (entre 23h et 06h59)
        int heureActuelle = horloge.getHeureObject().getHeures();
        boolean estLaNuit = (heureActuelle >= 23 || heureActuelle < 7);
        horloge.incrementer();


        for (Habitant h: habitants){
            if (h.getBesoins().getSante() <= 0) {
                continue; // Il est mort, on ignore ses actions et on passe au suivant
            }
            h.vivre(estLaNuit);

            if (estLaNuit || h.getBesoins().getFatigue() < 20) {
                // S'il fait nuit OU qu'il tombe de fatigue en journée : IL DORT (Gris)
                // Il ne bouge pas. On ne fait pas appel à moveRandomly().
            }
            else if (h.getMoral() < 30) {
                // Déprime (Rouge) : Mouvement très ralenti
                if (random.nextInt(3) == 0) { // 1 chance sur 3 d'avancer
                    moveRandomly(h);
                }
            }
            else {
                // Forme normale : Bouge normalement
                moveRandomly(h);
            }
        }

        // 3. On vérifie les rencontres (Seulement le jour ! On ne rencontre pas de gens en dormant)
        if (!estLaNuit) {
            verifierRencontres();
        }
    }

    private void verifierRencontres() {
        for (int i = 0; i < habitants.size(); i++) {
            for (int j = i + 1; j < habitants.size(); j++) {

                Habitant h1 = habitants.get(i);
                Habitant h2 = habitants.get(j);

                // Si h1 et h2 sont sur la même case
                if (h1.getPosition().equals(h2.getPosition())) {
                    if (h1.getBesoins().getSante() > 0 && h2.getBesoins().getSante() > 0) {

                        // S'ils ont tous les deux une Agréabilité > 50, ils sympathisent et créent un lien
                        if (h1.getAgreabilite() > 50 && h2.getAgreabilite() > 50) {
                            h1.ajouterLienAmical(h2);
                            h2.ajouterLienAmical(h1);
                        } else {
                            // S'ils ne sont pas assez agréables, ils ne deviennent pas amis.
                            // Mais le simple fait de voir quelqu'un remonte un TOUT PETIT PEU le besoin social.
                            h1.getBesoins().setSocial(h1.getBesoins().getSocial() + 2);
                            h2.getBesoins().setSocial(h2.getBesoins().getSocial() + 2);
                        }
                    }
                }
            }
        }
    }
    private void moveRandomly(Habitant h) {
        int direction = random.nextInt(4);
        Block pos = h.getPosition();
        int l = pos.getLine();
        int col = pos.getColumn();

        // RÉUTILISATION DU CODE DU PROF : On utilise les méthodes de la Map !
        if (direction == 0 && !map.isOnTop(pos)) {
            l--;
        } else if (direction == 1 && !map.isOnBottom(pos)) {
            l++;
        } else if (direction == 2 && !map.isOnLeftBorder(pos)) {
            col--;
        } else if (direction == 3 && !map.isOnRightBorder(pos)) {
            col++;
        }

        h.setPosition(map.getBlock(l, col));
    }
    @Override
    public Horloge getHorloge() {
        return horloge;
    }

    @Override
    public List<Habitant> getHabitants(){
        return habitants;
    }

    @Override
    public Habitant getHabitant(int line, int column) {
        for (Habitant h : habitants) {
            if (h.getPosition().getLine() == line && h.getPosition().getColumn() == column) {
                return h;
            }
        }
        return null;
    }

    public void addHabitant(Habitant h){
        habitants.add(h);
    }

}