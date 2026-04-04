package engine.process;

import engine.map.Map;
import engine.habitant.Habitant;
import engine.map.Horloge;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * MobileElementManager : C'est le moteur de la simulation (le "Controller").
 * Il coordonne tout : le temps qui passe, les déplacements et les interactions sociales.
 */
public class MobileElementManager implements MobileInterface {

	private Map map;
	private List<Habitant> habitants = new ArrayList<Habitant>();
	private Horloge horloge = new Horloge();

	private GestionnaireGroupes gestionnaireGroupes = new GestionnaireGroupes();
	private GestionnaireEnvironnement gestionnaireEnvironnement = new GestionnaireEnvironnement();

	// L'information active en cours de propagation (null = aucune)
	private InformationTransmission informationEnCours = null;

	// Historique des stats pour le graphique récapitulatif
	private List<String> historiqueJours = new ArrayList<String>();
	private List<Double> historiqueNevrosisme = new ArrayList<Double>();
	private List<Double> historiqueAgreabilite = new ArrayList<Double>();
	private List<Double> historiqueMoral = new ArrayList<Double>();

	private int forceInfluence = 5;
	private int resistanceCollective = 50;

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

		executerToursHabitants(estLaNuit);

		if (!estLaNuit) verifierRencontres();

		if (informationEnCours != null) {
			informationEnCours.propagerDansReseau(habitants);
		}

		gestionnaireEnvironnement.appliquerEffets(habitants, estLeWeekend, estLaNuit, horloge);

		gestionnaireGroupes.actualiserGroupes(habitants);
		gestionnaireGroupes.appliquerInfluences(forceInfluence);
		appliquerResistance();


		if (horloge.getHeureObject().getHeures() == 0) {
			enregistrerStatsJour();
		}
	}

	/**
	 * Exécute le tour de chaque habitant vivant.
	 */
	private void executerToursHabitants(boolean estLaNuit) {
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				h.executerTour(estLaNuit);
			}
		}
	}

	/**
	 * Applique l'effet de la resistance collective sur le moral de la population.
	 * Resistance < 50 : moral baisse — population fragile.
	 * Resistance > 50 : moral monte — population resiliente.
	 * Effet visible immediatement sur le camembert.
	 */
	private void appliquerResistance() {
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				if (resistanceCollective < 50) {
					h.getBesoins().setMoral(h.getBesoins().getMoral() - 2);
				} else if (resistanceCollective > 50) {
					h.getBesoins().setMoral(h.getBesoins().getMoral() + 2);
				}
			}
		}
	}

	/**
	 * Détection des rencontres entre habitants.
	 * Les interactions dépendent maintenant du profil psychologique OCEAN.
	 */
	private void verifierRencontres() {
		// Limite de rencontres par habitant par tour
		// Empêche l'explosion de liens quand beaucoup sont au même endroit
		HashMap<Habitant, Integer> rencontresCeTour = new HashMap<Habitant, Integer>();

		for (int i = 0; i < habitants.size(); i++) {
			for (int j = i + 1; j < habitants.size(); j++) {

				Habitant h1 = habitants.get(i);
				Habitant h2 = habitants.get(j);

				if (h1.getPosition().equals(h2.getPosition())) {
					if (h1.getBesoins().getSante() > 0 && h2.getBesoins().getSante() > 0) {

						// Vérifier si l'un des deux a déjà eu trop de rencontres ce tour
						int r1 = rencontresCeTour.getOrDefault(h1, 0);
						int r2 = rencontresCeTour.getOrDefault(h2, 0);

						if (r1 >= 2 || r2 >= 2) continue; // Max 2 rencontres par tour

						rencontresCeTour.put(h1, r1 + 1);
						rencontresCeTour.put(h2, r2 + 1);

						// Le reste de ta logique de rencontre (CAS 1, 2, 3) reste identique
						if (h1.getAgreabilite() > 50 && h2.getAgreabilite() > 50) {
							h1.ajouterLienAmical(h2);
							h2.ajouterLienAmical(h1);
						} else if (h1.getConscience() > 60 && h2.getConscience() > 60) {
							h1.ajouterLienProfessionnel(h2);
							h2.ajouterLienProfessionnel(h1);
						} else {
							// CAS 3 inchangé — vulnérabilité, résilience, etc.
							if (h1.getPsychologie().estVulnerable() && h2.getNevrosisme() > 60) {
								h1.getBesoins().setMoral(h1.getBesoins().getMoral() - 3);
							}
							if (h2.getPsychologie().estVulnerable() && h1.getNevrosisme() > 60) {
								h2.getBesoins().setMoral(h2.getBesoins().getMoral() - 3);
							}
							if (h1.getPsychologie().estResiliant()) {
								h1.getBesoins().setMoral(h1.getBesoins().getMoral() + 2);
							}
							if (h2.getPsychologie().estResiliant()) {
								h2.getBesoins().setMoral(h2.getBesoins().getMoral() + 2);
							}
							h1.getBesoins().setSocial(h1.getBesoins().getSocial() + 2);
							h2.getBesoins().setSocial(h2.getBesoins().getSocial() + 2);
						}
					}
				}
			}
		}
	}

	/**
	 * Declenche la propagation d'une information et applique
	 * un impact immediat visible sur toute la population.
	 * Veracite elevee → moral monte. Rumeur → moral baisse.
	 *
	 * @param theme     le theme de l'information
	 * @param virulence la vitesse de propagation (0.0 a 1.0)
	 * @param veracite  la veracite de l'information (0.0 a 1.0)
	 */
	public void lancerInformation(String theme, float virulence, float veracite) {
		this.informationEnCours = new InformationTransmission(theme, virulence, veracite);

		int impact = (int) ((veracite - 0.5f) * 40);
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				h.getBesoins().setMoral(h.getBesoins().getMoral() + impact);
				if (veracite < 0.4f) {
					h.getPsychologie().augmenterNevrosisme(5);
				} else if (veracite > 0.6f) {
					h.getPsychologie().augmenterOuverture(3);
				}
			}
		}
	}

	/**
	 * Arrête la propagation en cours.
	 */
	public void arreterInformation() {
		this.informationEnCours = null;
	}

	@Override
	public Horloge getHorloge() {
		return horloge;
	}

	@Override
	public List<Habitant> getHabitants() {
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

	public void addHabitant(Habitant h) {
		habitants.add(h);
	}

	/**
	 * Retourne vrai si la meteo actuelle est mauvaise.
	 * Delegue a GestionnaireEnvironnement.
	 */
	public boolean isMauvaisTemps() {
		return gestionnaireEnvironnement.isMauvaisTemps();
	}

	/**
	 * Retourne la force d'influence courante du slider.
	 * Utilisee par MainGUI pour declencher les evenements.
	 */
	public int getForceInfluence() {
		return forceInfluence;
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
	public List<String> getHistoriqueJours() {
		return historiqueJours;
	}

	public List<Double> getHistoriqueNevrosisme() {
		return historiqueNevrosisme;
	}

	public List<Double> getHistoriqueAgreabilite() {
		return historiqueAgreabilite;
	}

	public List<Double> getHistoriqueMoral() {
		return historiqueMoral;
	}

	/**
	 * Met a jour les parametres de simulation depuis les sliders.
	 * Propage la resistance a chaque habitant.
	 * Appelee depuis MainGUI a chaque tour.
	 *
	 * @param resistance     valeur du slider Resistance (0 a 100)
	 * @param forceInfluence valeur du slider Force d'influence (0 a 10)
	 */
	public void setParametres(int resistance, int forceInfluence) {
		this.resistanceCollective = resistance;
		this.forceInfluence = forceInfluence;
		for (Habitant h : habitants) {
			h.setResistanceCollective(resistance);
		}
	}
}