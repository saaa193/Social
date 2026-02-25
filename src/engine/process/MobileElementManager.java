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
        horloge.incrementer();
        for (Habitant h: habitants){
            if (h.getBesoins().getSante() <= 0) {
                continue; // Il est mort, on ignore ses actions et on passe au suivant
            }
            h.vivre();

            if (h.getBesoins().getFatigue() < 20) {
                // Il est trop fatigué (Gris), il s'arrête pour dormir
                // Optionnel : on peut faire remonter un peu sa fatigue ici
                h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 5);
            }
            //Si le point est rouge il se déplace au ralenti
            else if (h.getMoral()< 30) {
                if (random.nextInt(30)== 0) {
                    moveRandomly(h);
                }
                
            } else {
                moveRandomly(h);
            }
        }

        verifierRencontres();

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