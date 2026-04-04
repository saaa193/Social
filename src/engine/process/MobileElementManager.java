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
 *
 * MobileElementManager : le moteur de la simulation.
 * Coordonne le temps, les déplacements et les interactions sociales.
 * Délègue les effets environnementaux à GestionnaireEnvironnement.
 */
public class MobileElementManager implements MobileInterface {

	private Map map;
	private List<Habitant> habitants = new ArrayList<Habitant>();
	private Horloge horloge = new Horloge();

	private GestionnaireGroupes gestionnaireGroupes = new GestionnaireGroupes();
	private GestionnaireEnvironnement gestionnaireEnvironnement = new GestionnaireEnvironnement();

	private InformationTransmission informationEnCours = null;

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
	 * Chaque responsabilité est déléguée à la bonne classe.
	 */
	@Override
	public void nextRound() {
		int heureActuelle = horloge.getHeureObject().getHeures();
		boolean estLaNuit = (heureActuelle >= 23 || heureActuelle < 7);
		boolean estLeWeekend = horloge.estWeekend();
		horloge.incrementer(10);

		// 1. Chaque habitant vit son tour (Template Method)
		executerToursHabitants(estLaNuit);

		// 2. Rencontres entre habitants au même endroit
		if (!estLaNuit) verifierRencontres();

		// 3. Propagation d'information si active
		if (informationEnCours != null) {
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

	private void verifierRencontres() {
		HashMap<Habitant, Integer> rencontresCeTour = new HashMap<Habitant, Integer>();

		for (int i = 0; i < habitants.size(); i++) {
			for (int j = i + 1; j < habitants.size(); j++) {

				Habitant h1 = habitants.get(i);
				Habitant h2 = habitants.get(j);

				if (h1.getPosition().equals(h2.getPosition())) {
					if (h1.getBesoins().getSante() > 0 && h2.getBesoins().getSante() > 0) {

						int r1 = rencontresCeTour.getOrDefault(h1, 0);
						int r2 = rencontresCeTour.getOrDefault(h2, 0);
						if (r1 >= 2 || r2 >= 2) continue;

						rencontresCeTour.put(h1, r1 + 1);
						rencontresCeTour.put(h2, r2 + 1);

						if (h1.getAgreabilite() > 50 && h2.getAgreabilite() > 50) {
							h1.ajouterLienAmical(h2);
							h2.ajouterLienAmical(h1);
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

	public void arreterInformation() {
		this.informationEnCours = null;
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

		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				totalNevrosisme  += h.getNevrosisme();
				totalAgreabilite += h.getAgreabilite();
				totalMoral       += h.getMoral();
			}
		}

		int taille = habitants.size();
		historiqueJours.add(horloge.getDateCourte());
		historiqueNevrosisme.add(totalNevrosisme / taille);
		historiqueAgreabilite.add(totalAgreabilite / taille);
		historiqueMoral.add(totalMoral / taille);
	}

	public List<String> getHistoriqueJours()      { return historiqueJours; }
	public List<Double> getHistoriqueNevrosisme()  { return historiqueNevrosisme; }
	public List<Double> getHistoriqueAgreabilite() { return historiqueAgreabilite; }
	public List<Double> getHistoriqueMoral()       { return historiqueMoral; }

	public void setParametres(int resistance, int forceInfluence) {
		this.resistanceCollective = resistance;
		this.forceInfluence = forceInfluence;
		for (Habitant h : habitants) {
			h.setResistanceCollective(resistance);
		}
	}
}