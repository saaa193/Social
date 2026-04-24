package engine.process;

import config.GameConfiguration;
import config.RandomProvider;
import engine.analyse.CohesionSociale;
import engine.map.Map;
import engine.habitant.Habitant;
import engine.map.Horloge;
import log.LoggerUtility;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * MobileElementManager : le moteur de la simulation.
 * Coordonne le temps, les déplacements et les interactions sociales.
 * Délègue les effets environnementaux à GestionnaireEnvironnement.
 */
public class MobileElementManager implements MobileInterface {

	private static final Logger logger = LoggerUtility.getLogger(MobileElementManager.class, "text");

	private Map map;
	private List<Habitant> habitants = new ArrayList<Habitant>();
	private Horloge horloge = new Horloge();

	private GestionnaireGroupes gestionnaireGroupes = new GestionnaireGroupes();
	private GestionnaireEnvironnement gestionnaireEnvironnement = new GestionnaireEnvironnement();
	private CalculateurInteraction calculateur = new CalculateurInteraction();

	private InformationTransmission informationEnCours = null;

	private List<String> historiqueJours = new ArrayList<String>();
	private List<Double> historiqueNevrosisme = new ArrayList<Double>();
	private List<Double> historiqueAgreabilite = new ArrayList<Double>();
	private List<Double> historiqueMoral = new ArrayList<Double>();

	private int forceInfluence = 5;
	private int resistanceCollective = 50;

	private int toursDepuisPropagation = 0;

	private boolean enAttentePatientZero = false;

	private CohesionSociale cohesion = new CohesionSociale();

	private List<Double> historiqueExtraversion = new ArrayList<Double>();

	public MobileElementManager(Map map) {
		this.map = map;
	}

	/**
	 * Cœur du jeu : la boucle de simulation principale.
	 * Chaque responsabilité est déléguée à la bonne classe.
	 */
	@Override
	public void nextRound() {
		logger.debug("Tour " + horloge.getHeureActuelle() + " — " + habitants.size() + " habitants actifs");
		int heureActuelle = horloge.getHeureObject().getHeures();
		boolean estLaNuit = (heureActuelle >= 23 || heureActuelle < 7);
		boolean estLeWeekend = horloge.estWeekend();
		horloge.incrementer(10);

		// 1. Chaque habitant vit son tour (Template Method)
		executerToursHabitants(estLaNuit);
		// Vieillissement naturel de l'information
		if (informationEnCours != null) {
			for (Habitant h : habitants) {
				h.vieillirInformation();
			}
		}

		// 2. Rencontres entre habitants au même endroit
		if (!estLaNuit) verifierRencontres();

		if (!estLaNuit) disperserEntassements();

		// 3. Propagation d'information si active
		if (informationEnCours != null) {
			toursDepuisPropagation++;
			informationEnCours.propagerDansReseau(habitants);
		}

		// 4. Effets environnementaux — délégués à GestionnaireEnvironnement
		// Inclut maintenant les phases de journée (matin, midi, soir...)
		gestionnaireEnvironnement.appliquerEffets(
				habitants, estLeWeekend, estLaNuit, horloge, heureActuelle
		);

		// 5. Groupes sociaux et influence collective
		gestionnaireGroupes.actualiserGroupes(habitants);
		gestionnaireGroupes.appliquerInfluences(forceInfluence);
		appliquerResistance();

		// 6. Stats journalières à minuit
		if (horloge.getHeureObject().getHeures() == 0) {
			enregistrerStatsJour();
		}
	}

	private void executerToursHabitants(boolean estLaNuit) {
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				h.executerTour(estLaNuit);
			}
		}
	}

	private void appliquerResistance() {
		int moralCible = Math.max(60, resistanceCollective);

		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() <= 0) continue;

			int moralActuel = h.getBesoins().getMoral();
			int ecart = moralCible - moralActuel;

			double coefficient = GameConfiguration.COEFF_RESISTANCE_COLLECTIVE;
			if (h.getPsychologie().estResiliant())  coefficient = 0.20;
			if (h.getPsychologie().estVulnerable()) coefficient = 0.08;

			int force = (int)(ecart * coefficient);
			if (Math.abs(ecart) > 5 && force == 0) force = ecart > 0 ? 1 : -1;

			h.getBesoins().setMoral(moralActuel + force);
		}
	}

	private void verifierRencontres() {
		HashMap<Habitant, Integer> rencontresCeTour = new HashMap<Habitant, Integer>();

		for (int i = 0; i < habitants.size(); i++) {
			for (int j = i + 1; j < habitants.size(); j++) {

				Habitant h1 = habitants.get(i);
				Habitant h2 = habitants.get(j);

				if (h1.getPosition().equals(h2.getPosition())) {
					if (h1.getBesoins().getSante() > 0 && h2.getBesoins().getSante() > 0) {
						logger.warn("Rencontre negative : " + h1.getPrenom() + " fragilise par " + h2.getPrenom());

						int r1 = rencontresCeTour.getOrDefault(h1, 0);
						int r2 = rencontresCeTour.getOrDefault(h2, 0);
						if (r1 >= 2 || r2 >= 2) continue;

						// Algorithme psychologique — probabilite modulee par les profils OCEAN
						double probabilite = calculateur.calculerProbabilite(h1, h2);
						if (RandomProvider.getInstance().nextDouble() > probabilite) continue;

						rencontresCeTour.put(h1, r1 + 1);
						rencontresCeTour.put(h2, r2 + 1);

						// Historique de confiance accumule
						h1.incrementerRencontres();
						h2.incrementerRencontres();

						if (h1.getAgreabilite() > 50 && h2.getAgreabilite() > 50) {
							h1.ajouterLienAmical(h2);
							h2.ajouterLienAmical(h1);
							logger.info("Rencontre amicale : " + h1.getPrenom() + " + " + h2.getPrenom());
						} else if (h1.getConscience() > 60 && h2.getConscience() > 60) {
							h1.ajouterLienProfessionnel(h2);
							h2.ajouterLienProfessionnel(h1);
						} else {
							if (h1.getPsychologie().estVulnerable() && h2.getNevrosisme() > 60)
								h1.getBesoins().setMoral(h1.getBesoins().getMoral() - 3);
							if (h2.getPsychologie().estVulnerable() && h1.getNevrosisme() > 60)
								h2.getBesoins().setMoral(h2.getBesoins().getMoral() - 3);
							if (h1.getPsychologie().estResiliant())
								h1.getBesoins().setMoral(h1.getBesoins().getMoral() + 2);
							if (h2.getPsychologie().estResiliant())
								h2.getBesoins().setMoral(h2.getBesoins().getMoral() + 2);
							h1.getBesoins().setSocial(h1.getBesoins().getSocial() + 2);
							h2.getBesoins().setSocial(h2.getBesoins().getSocial() + 2);
						}
					}
				}
			}
		}

	}

	public void lancerInformationDepuis(Habitant patientZero) {
		patientZero.devenirPatientZero();
		this.enAttentePatientZero = false;
		logger.info("Patient zero designe : " + patientZero.getPrenom());
	}

	public boolean isEnAttentePatientZero() {
		return enAttentePatientZero;
	}

	public void preparerPropagation(String theme, float virulence, float veracite) {
		this.enAttentePatientZero = true;
		this.informationEnCours = new InformationTransmission(theme, virulence, veracite);
		this.toursDepuisPropagation = 0;
		for (Habitant h : habitants) {
			h.reinitialiserInformation();
		}
	}

	/**
	 * Disperse les habitants trop entassés au même endroit.
	 * Si plus de 3 habitants sont sur le même bloc, les surplus
	 * sont déplacés aléatoirement d'un pas.
	 */
	private void disperserEntassements() {
		// Compte les habitants par position
		java.util.HashMap<String, List<Habitant>> parPosition =
				new java.util.HashMap<String, List<Habitant>>();

		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() <= 0) continue;
			String cle = h.getPosition().getLine() + "," + h.getPosition().getColumn();
			if (!parPosition.containsKey(cle)) {
				parPosition.put(cle, new ArrayList<Habitant>());
			}
			parPosition.get(cle).add(h);
		}

		// Disperse les blocs avec plus de 3 habitants
		for (List<Habitant> groupe : parPosition.values()) {
			if (groupe.size() <= 3) continue;

			// Les 3 premiers restent, les autres sont dispersés
			for (int i = 3; i < groupe.size(); i++) {
				Habitant h = groupe.get(i);
				// Nouvelle destination aléatoire pour forcer la dispersion
				int nouvelleLigne   = RandomProvider.getInstance().nextInt(map.getLineCount());
				int nouvelleColonne = RandomProvider.getInstance().nextInt(map.getColumnCount());
				h.setDestination(map.getBlock(nouvelleLigne, nouvelleColonne));
			}
		}
	}


	public void arreterInformation() {
		// Réinitialise les états informés quand on arrête la propagation
		for (Habitant h : habitants) {
			h.reinitialiserInformation();
		}
		this.informationEnCours = null;
		logger.info("Propagation terminee — information effacee");

	}

	@Override
	public Horloge getHorloge() { return horloge; }

	@Override
	public List<Habitant> getHabitants() { return habitants; }

	@Override
	public Habitant getHabitant(int line, int column) {
		for (Habitant h : habitants) {
			if (h.getPosition().getLine() == line
					&& h.getPosition().getColumn() == column) {
				return h;
			}
		}
		return null;
	}

	public void addHabitant(Habitant h) { habitants.add(h); }

	public boolean isMauvaisTemps() {
		return gestionnaireEnvironnement.isMauvaisTemps();
	}

	public int getForceInfluence() { return forceInfluence; }

	public GestionnaireGroupes getGestionnaireGroupes() {
		return gestionnaireGroupes;
	}

	private void enregistrerStatsJour() {
		if (habitants.isEmpty()) return;

		double totalNevrosisme  = 0;
		double totalAgreabilite = 0;
		double totalMoral       = 0;
		double totalExtraversion = 0;
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				totalNevrosisme  += h.getNevrosisme();
				totalAgreabilite += h.getAgreabilite();
				totalMoral       += h.getMoral();
			}
		}

		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				totalExtraversion += h.getExtraversion();
			}
		}

		int taille = habitants.size();
		historiqueJours.add(horloge.getDateCourte());
		historiqueNevrosisme.add(totalNevrosisme / taille);
		historiqueAgreabilite.add(totalAgreabilite / taille);
		historiqueMoral.add(totalMoral / taille);
		historiqueExtraversion.add(totalExtraversion / taille);
		logger.info("Stats jour " + horloge.getDateCourte() + " - Moral=" + (int)(totalMoral/taille) + " Nevrosisme=" + (int)(totalNevrosisme/taille));
	}

	public List<String> getHistoriqueJours()      { return historiqueJours; }
	public List<Double> getHistoriqueNevrosisme()  { return historiqueNevrosisme; }
	public List<Double> getHistoriqueAgreabilite() { return historiqueAgreabilite; }
	public List<Double> getHistoriqueMoral()       { return historiqueMoral; }
	public int getToursDepuisPropagation() {
		return toursDepuisPropagation;
	}
	public List<Double> getHistoriqueExtraversion() { return historiqueExtraversion; }


	public void setParametres(int resistance, int forceInfluence) {
		this.resistanceCollective = resistance;
		this.forceInfluence = forceInfluence;
		for (Habitant h : habitants) {
			h.setResistanceCollective(resistance);
		}
	}
}