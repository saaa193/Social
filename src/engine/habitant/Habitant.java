package engine.habitant;

import config.GameConfiguration;
import engine.evenement.EventVisitor;
import engine.habitant.deplacement.StrategieDeplacement;
import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatBurnout;
import engine.habitant.etat.EtatDepressif;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Liens;
import engine.MobileElement;
import engine.habitant.lien.Professionnel;
import engine.habitant.visitor.ContagionVisitor;
import engine.map.Block;
import engine.habitant.besoin.Besoins;
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
 * Hérite de MobileElement — pattern Template Method du prof :
 * executerTour() est fixé, seDeplacer() et agir() sont délégués.
 */
public class Habitant extends MobileElement {

	// Identité
	private String prenom;
	private String sexe;
	private int age;

	// Besoins vitaux
	private Besoins besoins;

	// Profil psychologique OCEAN
	private Psychologie psychologie;

	// Taux de dégradation personnels (calculés depuis OCEAN)
	private double tauxFaim;
	private double tauxFatigue;
	private double tauxSocial;
	private double tauxRecuperation;

	// Réseau social
	private List<Liens> relations = new ArrayList<Liens>();

	// Pattern Strategy : comportement de déplacement injecté selon OCEAN
	private StrategieDeplacement strategieDeplacement;

	// Compteur de tours dans un état négatif → déclenche un traumatisme
	private int toursEtatNegatif = 0;
	private Traumatisme traumatisme = new Traumatisme();

	// Position de domicile — fixe toute la simulation
	// Chaque habitant a son propre point d'ancrage spatial
	private Block domicile;

	// Destination courante — change périodiquement selon le profil OCEAN
	// L'extraverti explore, l'anxieux reste près de chez lui
	private Block destination;

	public Habitant(Block position, Map map, String prenom, String sexe, int age) {
		super(position, map);
		this.prenom = prenom;
		this.sexe = sexe;
		this.age = age;

		// Le domicile est fixé une fois pour toutes à la position initiale
		this.domicile = position;
		// La destination de départ est le domicile
		this.destination = position;

		this.psychologie = new Psychologie();
		this.besoins = new Besoins(this.psychologie.determinerStrategieNutrition());

		// Légère variance initiale pour diversifier la population
		this.besoins.setFaim(60 + (int) (Math.random() * 40));
		this.besoins.setFatigue(50 + (int) (Math.random() * 50));
		this.besoins.setSocial(40 + (int) (Math.random() * 60));
		this.besoins.setMoral(40 + (int) (Math.random() * 40));

		// Calcul des taux personnels depuis OCEAN
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

		// Stratégie de déplacement initiale selon OCEAN
		this.strategieDeplacement = psychologie.determinerStrategieDeplacement();
	}

	/**
	 * Pattern Visitor : L'habitant "accepte" de subir un événement.
	 * L'événement applique ses propres règles — double dispatch.
	 */
	public void acceptEvent(EventVisitor visiteurEvenement) {
		visiteurEvenement.visit(this);
	}

	/**
	 * Supprime tous les liens dont la force est tombée à 0.
	 */
	private void nettoyerLiensMorts() {
		List<Liens> aSupprimer = new ArrayList<Liens>();
		for (Liens l : relations) {
			if (l.estMort()) aSupprimer.add(l);
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
			// La limite compte TOUS les liens (pas juste amicaux)
			// Plafonnée à 8 maximum, même pour les super-extravertis
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

	// --- ACCESSEURS ---
	public List<Liens> getRelation()     { return relations; }
	public String getPrenom()            { return prenom; }
	public String getSexe()              { return sexe; }
	public int getAge()                  { return age; }
	public int getMoral()                { return besoins.getMoral(); }
	public Besoins getBesoins()          { return besoins; }
	public Psychologie getPsychologie()  { return psychologie; }
	public int getExtraversion()         { return psychologie.getExtraversion(); }
	public int getOuverture()            { return psychologie.getOuverture(); }
	public int getConscience()           { return psychologie.getConscience(); }
	public int getAgreabilite()          { return psychologie.getAgreabilite(); }
	public int getNevrosisme()           { return psychologie.getNevrosisme(); }
	public Map getMap()                  { return map; }

	// Accesseurs domicile et destination
	public Block getDomicile()           { return domicile; }
	public Block getDestination()        { return destination; }
	public void setDestination(Block destination) { this.destination = destination; }

	@Override
	public String toString() {
		return prenom + " (" + sexe + ", " + age + " ans) - Moral: " + getMoral();
	}

	// --- TEMPLATE METHOD (héritage de MobileElement) ---

	@Override
	protected void seDeplacer() {
		// Mort → ne bouge pas
		if (besoins.getSante() <= 0) return;

		// Nuit → la majorité dort, mais les noctambules bougent
		if (estLaNuit()) {
			boolean noctambule = psychologie.getExtraversion() > 70
					|| psychologie.getConscience() < 30;
			if (!noctambule) return;
			if (Math.random() > 0.50) return;
		}

		// Épuisement total → seul un effondrement complet bloque
		if (besoins.getFatigue() < 10) return;

		// Fatigue modérée → ralentissement progressif (pas un mur)
		if (besoins.getFatigue() < 40) {
			double chanceDeBouger = besoins.getFatigue() / 40.0;
			if (Math.random() > chanceDeBouger) return;
		}

		// Déprimé → ralenti mais pas immobile
		if (getMoral() < 20 && Math.random() > 0.50) return;

		// Délègue au Strategy Pattern OCEAN
		strategieDeplacement.deplacer(this, map);
	}

	@Override
	protected void agir(boolean estLaNuit) {
		besoins.vivre(estLaNuit, tauxFaim, tauxFatigue, tauxSocial, tauxRecuperation);

		EtatHabitant etat = psychologie.determinerEtat(besoins);
		etat.appliquer(this);
		psychologie.evoluer(etat, besoins);
		psychologie.evoluerSelonReseau(relations);
		besoins.setStrategieNutrition(psychologie.determinerStrategieNutrition());

		// La stratégie de déplacement évolue avec le profil OCEAN
		this.strategieDeplacement = psychologie.determinerStrategieDeplacement();

		// Contagion émotionnelle via les liens sociaux
		int impact = etat.accept(new ContagionVisitor());
		if (impact != 0) {
			for (Liens l : relations) {
				Habitant proche = l.getPartenaire();
				int impactModule = (int) (impact * (l.getForce() / 100.0));

				if (impactModule < 0) {
					if (proche.getPsychologie().estVulnerable())
						impactModule = (int) (impactModule * 1.5);
					if (proche.getPsychologie().estResiliant())
						impactModule = (int) (impactModule * 0.5);
				}
				if (impactModule > 0 && proche.getPsychologie().estResiliant())
					impactModule = (int) (impactModule * 1.2);

				proche.getBesoins().setMoral(proche.getBesoins().getMoral() + impactModule);
			}
		}

		// Traumatisme si état négatif prolongé
		if (etat instanceof EtatDepressif || etat instanceof EtatAnxieux || etat instanceof EtatBurnout) {
			toursEtatNegatif++;
			if (toursEtatNegatif >= 5) {
				traumatisme.appliquer(this);
				toursEtatNegatif = 0;
			}
		} else {
			toursEtatNegatif = 0;
		}

		nettoyerLiensMorts();
	}
}