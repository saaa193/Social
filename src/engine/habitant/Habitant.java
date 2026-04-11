package engine.habitant;

import config.GameConfiguration;
import config.RandomProvider;
import engine.MobileElement;
import engine.evenement.EventVisitor;
import engine.habitant.besoin.Besoins;
import engine.habitant.deplacement.StrategieDeplacement;
import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatBurnout;
import engine.habitant.etat.EtatDepressif;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Liens;
import engine.habitant.lien.Professionnel;
import engine.habitant.visitor.ContagionVisitor;
import engine.map.Block;
import engine.map.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Représente un habitant de la simulation avec ses besoins et son profil OCEAN.
 * Hérite de MobileElement — pattern Template Method.
 */
public class Habitant extends MobileElement {

	private String prenom;
	private String sexe;
	private int age;

	private Besoins besoins;
	private Psychologie psychologie;

	private double tauxFaim;
	private double tauxFatigue;
	private double tauxSocial;
	private double tauxRecuperation;

	private int toursDepuisInformation = 0;
	private int nbRencontres = 0;

	private List<Liens> relations = new ArrayList<Liens>();
	private StrategieDeplacement strategieDeplacement;

	private int toursEtatNegatif = 0;
	private Traumatisme traumatisme = new Traumatisme();

	private Block domicile;
	private Block destination;

	private int resistanceCollective = 50;

	private static final ContagionVisitor contagionVisitor = new ContagionVisitor();

	private int toursAvantChangementDestination = RandomProvider.getInstance().nextInt(GameConfiguration.TOURS_AVANT_CHANGEMENT);

	private boolean estInforme = false;

	/**
	 * Construit un habitant avec son identite et le place sur la carte.
	 *
	 * @param position la case initiale de l'habitant sur la carte
	 * @param map      la carte de la simulation
	 * @param prenom   le prenom de l'habitant
	 * @param sexe     le sexe de l'habitant
	 * @param age      l'age de l'habitant
	 */
	public Habitant(Block position, Map map, String prenom, String sexe, int age) {
		super(position, map);
		this.prenom = prenom;
		this.sexe = sexe;
		this.age = age;

		this.domicile = position;
		this.destination = position;

		this.psychologie = new Psychologie();
		this.besoins = new Besoins(this.psychologie.determinerStrategieNutrition());

		this.besoins.setFaim(60 + (int) (RandomProvider.getInstance().nextInt(40)));
		this.besoins.setFatigue(50 + (int) (RandomProvider.getInstance().nextInt(50)));
		this.besoins.setSocial(40 + (int) (RandomProvider.getInstance().nextInt(60)));
		this.besoins.setMoral(40 + (int) (RandomProvider.getInstance().nextInt(40)));

		this.tauxFaim = GameConfiguration.BASE_FAIM
				- (psychologie.getConscience() / 100.0) * GameConfiguration.OCEAN_IMPACT;

		this.tauxFatigue = GameConfiguration.BASE_FATIGUE
				+ (psychologie.getNevrosisme() / 100.0) * GameConfiguration.OCEAN_IMPACT
				+ (psychologie.getExtraversion() / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;

		this.tauxSocial = GameConfiguration.BASE_SOCIAL
				+ (psychologie.getExtraversion() / 100.0) * GameConfiguration.OCEAN_IMPACT
				- (psychologie.getAgreabilite() / 100.0) * GameConfiguration.OCEAN_IMPACT / 2;

		this.tauxRecuperation = GameConfiguration.BASE_RECUPERATION
				- (psychologie.getNevrosisme() / 100.0) * GameConfiguration.OCEAN_IMPACT * 2;

		this.strategieDeplacement = psychologie.determinerStrategieDeplacement();
	}

	/**
	 * Pattern Visitor : l'habitant accepte de subir un événement.
	 */
	public void acceptEvent(EventVisitor visiteurEvenement) {
		visiteurEvenement.visit(this);
	}

	private void nettoyerLiensMorts() {
		List<Liens> aSupprimer = new ArrayList<Liens>();
		for (Liens l : relations) {
			if (l.estMort()) aSupprimer.add(l);
			if (l.getPartenaire().getBesoins().getSante() <= 0) aSupprimer.add(l);
		}
		for (Liens l : aSupprimer) {
			relations.remove(l);
		}
	}

	/**
	 * Gestion d'une rencontre amicale.
	 * Si déjà connu → renforce le lien.
	 * Si inconnu → crée un lien selon compatibilité OCEAN.
	 */
	public void ajouterLienAmical(Habitant autre) {
		boolean dejaConnu = false;

		for (Liens l : relations) {
			if (l.getPartenaire() == autre) {
				l.evoluerForce(this);
				l.appliquerBonusMental(this);
				dejaConnu = true;
				break;
			}
		}

		if (!dejaConnu) {
			int limiteAmis = Math.min(8, (psychologie.getExtraversion() / 15) + 2);
			if (this.relations.size() < limiteAmis) {
				int forceInitiale = calculerCompatibilite(autre);
				Liens nouveauLien = new Amical(autre, forceInitiale);
				relations.add(nouveauLien);
				nouveauLien.appliquerBonusMental(this);
			} else {
				this.besoins.setSocial(this.besoins.getSocial() + 3);
			}
		}
	}

	/**
	 * Gestion d'une rencontre professionnelle.
	 * Si déjà connu → renforce le lien.
	 * Si inconnu → crée un lien selon compatibilité OCEAN.
	 *
	 * @param autre l'habitant rencontré professionnellement
	 */
	public void ajouterLienProfessionnel(Habitant autre) {
		boolean dejaConnu = false;

		for (Liens l : relations) {
			if (l.getPartenaire() == autre) {
				l.evoluerForce(this);
				l.appliquerBonusMental(this);
				dejaConnu = true;
				break;
			}
		}

		if (!dejaConnu) {
			int limiteCollegues = Math.min(5, (psychologie.getConscience() / 20) + 1);
			if (this.relations.size() < limiteCollegues) {
				int forceInitiale = calculerCompatibilite(autre);
				Liens nouveauLien = new Professionnel(autre, forceInitiale);
				relations.add(nouveauLien);
				nouveauLien.appliquerBonusMental(this);
			} else {
				this.besoins.setSocial(this.besoins.getSocial() + 3);
			}
		}
	}

	private int calculerCompatibilite(Habitant autre) {
		return psychologie.calculerCompatibiliteAvec(autre.getPsychologie());
	}

	public List<Liens> getRelation() { return relations; }
	public String getPrenom() { return prenom; }
	public String getSexe() { return sexe; }
	public int getAge() { return age; }
	public int getMoral() { return besoins.getMoral(); }
	public Besoins getBesoins() { return besoins; }
	public Psychologie getPsychologie() { return psychologie; }
	public int getExtraversion() { return psychologie.getExtraversion(); }
	public int getOuverture() { return psychologie.getOuverture(); }
	public int getConscience() { return psychologie.getConscience(); }
	public int getAgreabilite() { return psychologie.getAgreabilite(); }
	public int getNevrosisme() { return psychologie.getNevrosisme(); }
	public int getNbRencontres() { return nbRencontres; }
	public void incrementerRencontres() { this.nbRencontres++; }
	public int getToursAvantChangement() { return toursAvantChangementDestination; }
	public void setToursAvantChangement(int tours) { this.toursAvantChangementDestination = tours; }
	public Map getMap() { return map; }
	public Block getDomicile() { return domicile; }
	public Block getDestination() { return destination; }
	public void setDestination(Block destination) { this.destination = destination; }
	public boolean estInforme() { return estInforme; }

	/**
	 * Met a jour la resistance collective de la population.
	 *
	 * @param resistance la valeur du slider (0 a 100)
	 */
	public void setResistanceCollective(int resistance) {
		this.resistanceCollective = resistance;
	}

	/**
	 * Retourne vrai si l'habitant est porteur d'une information.
	 */
	public void vieillirInformation() {
		if (!estInforme) return;
		toursDepuisInformation++;
		int seuilOubli = 3 + (psychologie.getConscience() / 10);
		if (toursDepuisInformation > seuilOubli) {
			estInforme = false;
			toursDepuisInformation = 0;
		}
	}

	public void recevoirInformation() {
		this.estInforme = true;
		this.toursDepuisInformation = 0;
	}

	public void devenirPatientZero() {
		this.estInforme = true;
		this.toursDepuisInformation = 0;
	}

	public void reinitialiserInformation() {
		this.estInforme = false;
	}

	@Override
	public String toString() {
		return prenom + " (" + sexe + ", " + age + " ans) - Moral: " + getMoral();
	}

	@Override
	protected void seDeplacer() {
		if (besoins.getSante() <= 0) return;

		if (estLaNuit()) {
			boolean noctambule = psychologie.getExtraversion() > GameConfiguration.SEUIL_NOCTAMBULE_EXTRAVERSION
					|| psychologie.getConscience() < GameConfiguration.SEUIL_NOCTAMBULE_CONSCIENCE;
			if (!noctambule) return;
			if (RandomProvider.getInstance().nextDouble() > GameConfiguration.PROBA_NOCTAMBULE_BOUGE) return;
		}

		if (besoins.getFatigue() < GameConfiguration.SEUIL_EPUISEMENT_TOTAL) return;

		if (besoins.getFatigue() < GameConfiguration.SEUIL_FATIGUE_LENTE) {
			double chanceDeBouger = besoins.getFatigue() / (double) GameConfiguration.SEUIL_FATIGUE_LENTE;
			if (RandomProvider.getInstance().nextDouble() > chanceDeBouger) return;
		}

		if (RandomProvider.getInstance().nextDouble() > GameConfiguration.PROBA_DEPRIME_BOUGE) return;

		strategieDeplacement.deplacer(this, map);
	}

	@Override
	protected void agir(boolean estLaNuit) {
		if (besoins.getSante() <= 0) {
			relations.clear();
			return;
		}

		besoins.vivre(estLaNuit, tauxFaim, tauxFatigue, tauxSocial, tauxRecuperation);

		EtatHabitant etat = psychologie.determinerEtat(besoins);
		etat.appliquer(this);
		psychologie.evoluer(etat, besoins);
		psychologie.evoluerSelonReseau(relations);
		besoins.setStrategieNutrition(psychologie.determinerStrategieNutrition());
		this.strategieDeplacement = psychologie.determinerStrategieDeplacement();

		int impact = etat.accept(contagionVisitor);
		if (impact != 0) {
			for (Liens l : relations) {
				Habitant proche = l.getPartenaire();
				int impactModule = (int)(impact * (l.getForce() / 100.0));

				if (impactModule < 0) {
					if (proche.getPsychologie().estVulnerable())
						impactModule = (int)(impactModule * GameConfiguration.FACTEUR_VULNERABLE);
					if (proche.getPsychologie().estResiliant())
						impactModule = (int)(impactModule * GameConfiguration.FACTEUR_RESILIENT);
				}
				if (impactModule > 0 && proche.getPsychologie().estResiliant())
					impactModule = (int)(impactModule * GameConfiguration.FACTEUR_RESILIENT_POSITIF);

				proche.getBesoins().setMoral(proche.getBesoins().getMoral() + impactModule);
			}
		}

		if (etat instanceof EtatDepressif || etat instanceof EtatAnxieux || etat instanceof EtatBurnout) {
			toursEtatNegatif++;
			if (toursEtatNegatif >= GameConfiguration.TOURS_AVANT_TRAUMATISME) {
				traumatisme.appliquer(this, resistanceCollective);
				toursEtatNegatif = 0;
			}
		} else {
			toursEtatNegatif = 0;
		}

		nettoyerLiensMorts();
	}
}