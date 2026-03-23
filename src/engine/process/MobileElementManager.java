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

    private boolean mauvaisTemps = false;
    private int heureAuChangementMeteo = 0;

    private GestionnaireGroupes gestionnaireGroupes = new GestionnaireGroupes();

    // L'information active en cours de propagation (null = aucune)
    private InformationTransmission informationEnCours = null;

    // Historique des stats pour le graphique récapitulatif
    private List<String> historiqueJours = new ArrayList<String>();
    private List<Double> historiqueNevrosisme = new ArrayList<Double>();
    private List<Double> historiqueAgreabilite = new ArrayList<Double>();
    private List<Double> historiqueMoral = new ArrayList<Double>();

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
        boolean estLeWeekend = horloge.estWeekend();
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

        // Effets psychologiques du weekend
        if (estLeWeekend && !estLaNuit) {
            for (Habitant h : habitants) {
                if (h.getBesoins().getSante() > 0) {

                    // Extravertis profitent du weekend socialement
                    if (h.getExtraversion() > 60) {
                        h.getBesoins().setSocial(h.getBesoins().getSocial() + 2);
                        h.getBesoins().setMoral(h.getBesoins().getMoral() + 1);
                    }

                    // Introvertis récupèrent mieux seuls
                    if (h.getExtraversion() < 35) {
                        h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 2);
                        h.getBesoins().setMoral(h.getBesoins().getMoral() + 1);
                    }

                    // Névrosés stressent quand même le weekend
                    if (h.getNevrosisme() > 65) {
                        h.getBesoins().setMoral(h.getBesoins().getMoral() - 1);
                    }
                }
            }
        }

        // Changement de météo automatique 1 fois par jour à 8h
        if (horloge.getHeureObject().getHeures() == 8 && heureAuChangementMeteo != 8) {
            mauvaisTemps = Math.random() < 0.35; // 35% de chance de mauvais temps
            heureAuChangementMeteo = 8;

            // Effets psychologiques immédiats selon la météo
            for (Habitant h : habitants) {
                if (h.getBesoins().getSante() > 0) {

                    if (mauvaisTemps) {
                        // Névrosés très affectés par le mauvais temps
                        if (h.getNevrosisme() > 60) {
                            h.getBesoins().setMoral(h.getBesoins().getMoral() - 3);
                            h.getBesoins().setFatigue(h.getBesoins().getFatigue() - 2);
                        }
                    } else {
                        // Ouverts profitent du beau temps
                        if (h.getOuverture() > 60) {
                            h.getBesoins().setMoral(h.getBesoins().getMoral() + 3);
                            h.getBesoins().setSocial(h.getBesoins().getSocial() + 2);
                        }
                    }
                }
            }
        } else if (horloge.getHeureObject().getHeures() != 8) {
            heureAuChangementMeteo = 0; // reset pour le lendemain
        }

        // Mise à jour des groupes sociaux et influence du leader
        gestionnaireGroupes.actualiserGroupes(habitants);
        gestionnaireGroupes.appliquerInfluences();

        // Enregistrement des stats à minuit pour le graphique récapitulatif
        if (horloge.getHeureObject().getHeures() == 0) {
            enregistrerStatsJour();
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

    public boolean isMauvaisTemps() {
        return mauvaisTemps;
    }

    /**
     * Retourne le gestionnaire de groupes pour l'affichage dans les dashboards.
     */
    public GestionnaireGroupes getGestionnaireGroupes() {
        return gestionnaireGroupes;
    }

    /**
     * Enregistre les moyennes psychologiques du jour écoulé.
     * Appelée à minuit à chaque changement de jour.
     * Utilisée pour le graphique récapitulatif hebdomadaire.
     */
    private void enregistrerStatsJour() {
        if (habitants.isEmpty()) return;

        // Calcul des moyennes OCEAN de la population
        double totalNevrosisme = 0;
        double totalAgreabilite = 0;
        double totalMoral = 0;

        for (Habitant h : habitants) {
            if (h.getBesoins().getSante() > 0) {
                totalNevrosisme += h.getNevrosisme();
                totalAgreabilite += h.getAgreabilite();
                totalMoral += h.getMoral();
            }
        }

        int taille = habitants.size();

        // Stockage dans les listes historiques
        historiqueJours.add(horloge.getDateCourte());
        historiqueNevrosisme.add(totalNevrosisme / taille);
        historiqueAgreabilite.add(totalAgreabilite / taille);
        historiqueMoral.add(totalMoral / taille);
    }

    /**
     * Retourne l'historique pour le graphique récapitulatif.
     */
    public List<String> getHistoriqueJours() { return historiqueJours; }
    public List<Double> getHistoriqueNevrosisme() { return historiqueNevrosisme; }
    public List<Double> getHistoriqueAgreabilite() { return historiqueAgreabilite; }
    public List<Double> getHistoriqueMoral() { return historiqueMoral; }
}