package engine.process;

import engine.map.Map;
import engine.habitant.Habitant;
import engine.map.Horloge;
import engine.map.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MobileElementManager : C'est le moteur de la simulation (le "Controller").
 * Il coordonne tout : le temps qui passe, les déplacements et les interactions sociales.
 */
public class MobileElementManager implements MobileInterface {

    private Map map;
    private List<Habitant> habitants = new ArrayList<Habitant>();
    private Horloge horloge = new Horloge();
    private Random random = new Random();

    public MobileElementManager(Map map) {
        this.map = map;
    }

    /**
     * Cœur du jeu : la boucle de simulation principale.
     * Appelée à chaque tour pour mettre à jour l'état de chaque habitant.
     */
    @Override
    public void nextRound() {

        // 1. Gestion du cycle Jour/Nuit (déterminant pour le comportement des agents)
        int heureActuelle = horloge.getHeureObject().getHeures();
        boolean estLaNuit = (heureActuelle >= 23 || heureActuelle < 7);
        int vitesse = 10; // Exemple : 10 minutes par tick
        horloge.incrementer(vitesse);

        // 2. Mise à jour de chaque habitant
        for (Habitant h : habitants) {
            // On vérifie s'il est en vie avant de traiter son tour
            if (h.getBesoins().getSante() <= 0) {
                continue;
            }

            // Mise à jour des besoins biologiques
            h.vivre(estLaNuit);

            // LOGIQUE D'ÉTAT : On décide du comportement de l'habitant selon son état
            if (estLaNuit || h.getBesoins().getFatigue() < 20) {
                // État Sommeil : L'habitant ne bouge pas (économie de ressources)
            }
            else if (h.getMoral() < 30) {
                // État Déprime : Mouvement ralenti (1 chance sur 3 de bouger)
                if (random.nextInt(3) == 0) {
                    moveRandomly(h);
                }
            }
            else {
                // État Normal : Mouvement libre
                moveRandomly(h);
            }
        }

        // 3. Gestion des interactions sociales (seulement le jour, quand ils sont actifs)
        if (!estLaNuit) {
            verifierRencontres();
        }
    }

    /**
     * Détection des collisions pour les interactions sociales.
     * Compare les positions de tous les habitants entre eux.
     */
    private void verifierRencontres() {
        for (int i = 0; i < habitants.size(); i++) {
            for (int j = i + 1; j < habitants.size(); j++) {

                Habitant h1 = habitants.get(i);
                Habitant h2 = habitants.get(j);

                // Si les deux habitants sont sur la même case (collision)
                if (h1.getPosition().equals(h2.getPosition())) {
                    if (h1.getBesoins().getSante() > 0 && h2.getBesoins().getSante() > 0) {

                        // Logique de sociabilisation basée sur l'agréabilité
                        if (h1.getAgreabilite() > 50 && h2.getAgreabilite() > 50) {
                            h1.ajouterLienAmical(h2);
                            h2.ajouterLienAmical(h1);
                        } else {
                            // Interaction basique même sans devenir amis
                            h1.getBesoins().setSocial(h1.getBesoins().getSocial() + 2);
                            h2.getBesoins().setSocial(h2.getBesoins().getSocial() + 2);
                        }
                    }
                }
            }
        }
    }

    /**
     * Algorithme de mouvement aléatoire.
     * Utilise les méthodes de Map pour s'assurer que l'habitant ne sort pas du cadre.
     */
    private void moveRandomly(Habitant h) {
        int direction = random.nextInt(4);
        Block pos = h.getPosition();
        int l = pos.getLine();
        int col = pos.getColumn();

        // Réutilisation des méthodes de la Map
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

    // --- ACCESSEURS ---
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