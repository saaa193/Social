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

    // L'information active en cours de propagation (null = aucune)
    private InformationTransmission informationEnCours = null;

    public MobileElementManager(Map map) {
        this.map = map;
    }

    /**
     * Cœur du jeu : la boucle de simulation principale.
     * Appelée à chaque tour pour mettre à jour l'état de chaque habitant.
     */
    @Override
    public void nextRound() {
        int heureActuelle = horloge.getHeureObject().getHeures();
        boolean estLaNuit = (heureActuelle >= 23 || heureActuelle < 7);
        horloge.incrementer(10);

        for (Habitant h : habitants) {
            if (h.getBesoins().getSante() > 0) {
                h.executerTour(estLaNuit); //
            }
        }

        if (!estLaNuit) verifierRencontres();

        if (informationEnCours != null) {
            informationEnCours.propagerDansReseau(habitants);
        }
    }


    /**
     * Détection des rencontres entre habitants.
     * Les interactions dépendent maintenant du profil psychologique OCEAN.
     */
    private void verifierRencontres() {
        for (int i = 0; i < habitants.size(); i++) {
            for (int j = i + 1; j < habitants.size(); j++) {

                Habitant h1 = habitants.get(i);
                Habitant h2 = habitants.get(j);

                if (h1.getPosition().equals(h2.getPosition())) {
                    if (h1.getBesoins().getSante() > 0 && h2.getBesoins().getSante() > 0) {

                        // CAS 1 : Les deux sont agréables → lien amical
                        if (h1.getAgreabilite() > 50 && h2.getAgreabilite() > 50) {
                            h1.ajouterLienAmical(h2);
                            h2.ajouterLienAmical(h1);

                            // CAS 2 : Les deux sont consciencieux → lien professionnel
                        } else if (h1.getConscience() > 60 && h2.getConscience() > 60) {
                            h1.ajouterLienProfessionnel(h2);
                            h2.ajouterLienProfessionnel(h1);

                            // CAS 3 : NOUVEAU — impact psychologique selon la vulnérabilité
                        } else {
                            // On vérifie si h1 est vulnérable face à h2 anxieux
                            if (h1.getPsychologie().estVulnerable() && h2.getNevrosisme() > 60) {
                                // h1 absorbe l'anxiété de h2 → perd du moral
                                h1.getBesoins().setMoral(h1.getBesoins().getMoral() - 3);
                            }

                            // On vérifie si h2 est vulnérable face à h1 anxieux
                            if (h2.getPsychologie().estVulnerable() && h1.getNevrosisme() > 60) {
                                // h2 absorbe l'anxiété de h1 → perd du moral
                                h2.getBesoins().setMoral(h2.getBesoins().getMoral() - 3);
                            }

                            // Un résilient n'est pas affecté → gain de moral au contraire
                            if (h1.getPsychologie().estResiliant()) {
                                h1.getBesoins().setMoral(h1.getBesoins().getMoral() + 2);
                            }

                            if (h2.getPsychologie().estResiliant()) {
                                h2.getBesoins().setMoral(h2.getBesoins().getMoral() + 2);
                            }

                            // Interaction basique quand même
                            h1.getBesoins().setSocial(h1.getBesoins().getSocial() + 2);
                            h2.getBesoins().setSocial(h2.getBesoins().getSocial() + 2);
                        }
                    }
                }
            }
        }
    }


    /**
     * Déclenche la propagation d'une nouvelle information.
     * Appelée depuis MacroDashboard quand l'utilisateur clique
     * sur "Propager une Information".
     */
    public void lancerInformation(String theme, float virulence, float veracite) {
        this.informationEnCours = new InformationTransmission(theme, virulence, veracite);
    }

    /**
     * Arrête la propagation en cours.
     */
    public void arreterInformation() {
        this.informationEnCours = null;
    }

    //ACCESSEURS
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