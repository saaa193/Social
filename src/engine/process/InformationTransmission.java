package engine.process;

import engine.habitant.Habitant;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Liens;
import engine.habitant.visitor.ContagionVisitor;
import engine.habitant.visitor.ReceptiviteVisitor;

import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * InformationTransmission : propage une information/rumeur
 * dans le réseau social via les liens entre habitants.
 *
 * [REFACTORING] Modèle SIR adapté au social.
 * Ancienne formule : probabilité plate (virulence + force_lien).
 * Nouvelle formule : produit de 4 facteurs indépendants inspiré du
 * modèle épidémiologique SIR (Kermack & McKendrick, 1927) :
 *
 *   P = virulence × force_lien × réceptivité_OCEAN × facteur_état
 *
 * - La réceptivité OCEAN varie selon la véracité de l'information
 *   (inspiré de Pennycook & Rand 2019 sur les fake news et la conscience)
 * - Le facteur état est calculé via ReceptiviteVisitor (Pattern Visitor)
 *
 * Usage des Patterns :
 * - Visitor (ReceptiviteVisitor) : comportement selon l'état sans instanceof
 * - Visitor (ContagionVisitor)   : impact émotionnel de la transmission
 * - SRP                          : cette classe a un seul rôle — propager
 */
public class InformationTransmission {

	private String theme;
	private float virulence;  // 0.0 = lente, 1.0 = épidémique
	private float veracite;   // 0.0 = rumeur, 1.0 = fait avéré

	// Visitors partagés — sans état, une seule instance suffit (économie mémoire)
	private final ContagionVisitor  contagionVisitor  = new ContagionVisitor();
	private final ReceptiviteVisitor receptiviteVisitor = new ReceptiviteVisitor();

	public InformationTransmission(String theme, float virulence, float veracite) {
		this.theme     = theme;
		this.virulence = virulence;
		this.veracite  = veracite;
	}

	/**
	 * Propage l'information sur toute la population.
	 * Appelée à chaque tour par MobileElementManager.
	 *
	 * On réinitialise d'abord tous les états "informé" pour ne pas
	 * cumuler les propagations de tours précédents.
	 * Puis on itère sur chaque porteur et ses liens.
	 */
	public void propagerDansReseau(List<Habitant> habitants) {
		// Seuls les habitants DÉJÀ informés peuvent propager
		for (Habitant porteur : habitants) {
			if (!porteur.estInforme()) continue; // ← seuls les informés propagent
			if (porteur.getBesoins().getSante() <= 0) continue;

			for (Liens lien : porteur.getRelation()) {
				Habitant cible = lien.getPartenaire();
				if (cible.getBesoins().getSante() <= 0) continue;
				if (cible.estInforme()) continue; // déjà informé → skip
				tenterTransmission(porteur, cible, lien.getForce());
			}
		}
	}

	/**
	 * Tente de transmettre l'information du porteur vers la cible.
	 *
	 * Formule SIR adaptée — 4 facteurs multipliés :
	 *   P = virulence × facteurLien × réceptivitéOCEAN × facteurÉtat
	 *
	 * Si la probabilité passe :
	 * - La cible est marquée "informée" (auréole dorée)
	 * - Son moral est impacté selon l'état du porteur (ContagionVisitor)
	 * - Ses traits OCEAN évoluent selon la véracité
	 *
	 * @param porteur   habitant qui diffuse l'information
	 * @param cible     habitant qui reçoit (ou non) l'information
	 * @param forceLien force du lien social entre eux (0–100)
	 */
	private void tenterTransmission(Habitant porteur, Habitant cible, int forceLien) {
		// Si déjà informé → pas besoin de retransmettre
		if (cible.estInforme()) return;

		double probabilite = calculerProbabiliteTransmission(porteur, cible, forceLien);

		if (Math.random() < probabilite) {

			// 1. Marquage SIR : la cible devient informée
			cible.recevoirInformation();

			// 2. Impact émotionnel via ContagionVisitor
			EtatHabitant etatPorteur = porteur.getPsychologie()
					.determinerEtat(porteur.getBesoins());
			int impact = etatPorteur.accept(contagionVisitor);

			if (impact < 0) {
				double amplification = 1.0 + (cible.getNevrosisme() / 100.0);
				impact = (int) (impact * amplification);
			}

			BiaisCognitif biais = cible.getPsychologie().determinerBiais();
			int impactFiltre = biais.filtrerImpact(impact, veracite);
			cible.getBesoins().setMoral(cible.getBesoins().getMoral() + impactFiltre);

			// 3. Effet de la véracité sur les traits OCEAN
			if (veracite > 0.6f) {
				cible.getBesoins().setMoral(cible.getBesoins().getMoral() + 15);
				cible.getPsychologie().augmenterOuverture(1);
			} else if (veracite < 0.4f) {
				cible.getBesoins().setMoral(cible.getBesoins().getMoral() - 15);
				cible.getPsychologie().augmenterNevrosisme(2);
				cible.getPsychologie().diminuerAgreabilite(1);
			}
		}
	}

	/**
	 * Calcule la probabilité de transmission selon le modèle SIR adapté.
	 *
	 * P = virulence × facteurLien × réceptivitéOCEAN × facteurÉtat
	 *
	 * Facteur OCEAN selon véracité (inspiré de la littérature en psych. sociale) :
	 *
	 * → Info vraie (véracité > 0.6) :
	 *      réceptivité = 0.3 + 0.4 × (ouverture/100) + 0.3 × (conscience/100)
	 *      Les ouverts d'esprit et les consciencieux acceptent les faits vérifiés.
	 *
	 * → Rumeur (véracité < 0.4) :
	 *      réceptivité = 0.3 + 0.5 × (névrosisme/100) - 0.3 × (conscience/100)
	 *      Les névrosés y croient (anxiété), les consciencieux résistent (esprit critique).
	 *
	 * → Info neutre (entre 0.4 et 0.6) :
	 *      réceptivité = 0.3 + 0.3 × (extraversion/100) + 0.2 × (agréabilité/100)
	 *      Les extravertis aiment partager, les agréables font confiance par défaut.
	 *
	 * @param porteur   l'habitant émetteur
	 * @param cible     l'habitant récepteur
	 * @param forceLien la force du lien social (0–100)
	 * @return probabilité entre 0.0 et 1.0
	 */
	private double calculerProbabiliteTransmission(Habitant porteur,
	                                               Habitant cible,
	                                               int forceLien) {
		// Facteur 1 — virulence globale (paramètre utilisateur, 0 à 1)
		double facteurVirulence = virulence;

		// Facteur 2 — force du lien normalisée (0 à 1)
		double facteurLien = forceLien / 100.0;

		// Facteur 3 — réceptivité OCEAN de la cible selon la véracité
		double receptivite;
		if (veracite > 0.6f) {
			// Info vraie : ouverts et consciencieux réceptifs
			receptivite = 0.3
					+ 0.4 * (cible.getOuverture()  / 100.0)
					+ 0.3 * (cible.getConscience()  / 100.0);

		} else if (veracite < 0.4f) {
			// Rumeur : névrosés croient, consciencieux résistent
			receptivite = 0.3
					+ 0.5 * (cible.getNevrosisme()  / 100.0)
					- 0.3 * (cible.getConscience()  / 100.0);

		} else {
			// Info neutre : extravertis et agréables transmettent
			receptivite = 0.3
					+ 0.3 * (cible.getExtraversion() / 100.0)
					+ 0.2 * (cible.getAgreabilite()  / 100.0);
		}
		// Clamp — la probabilité reste dans [0.05, 0.95]
		receptivite = Math.max(0.05, Math.min(0.95, receptivite));

		// Facteur 4 — modulation par l'état psychologique de la cible (Visitor)
		EtatHabitant etatCible = cible.getPsychologie()
				.determinerEtat(cible.getBesoins());
		double facteurEtat = etatCible.accept(receptiviteVisitor);

		// Formule finale
		return facteurVirulence * facteurLien * receptivite * facteurEtat;
	}

	// ── Getters ─────────────────────────────────────────────────────────────────

	public String getTheme()     { return theme; }
	public float  getVirulence() { return virulence; }
	public float  getVeracite()  { return veracite; }
}
