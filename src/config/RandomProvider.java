package config;

import java.util.Random;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Fournisseur de nombres aléatoires — pattern Singleton.
 * Calqué sur VariableRepository du prof (Tree V1) :
 * constructeur privé, instance unique, accès via getInstance().
 *
 * Avantage : avec setSeed(), la simulation est reproductible.
 * Sans seed : comportement aléatoire normal.
 */
public class RandomProvider {

	private static RandomProvider instance = new RandomProvider();
	private Random random = new Random();

	private RandomProvider() {}

	public static RandomProvider getInstance() {
		return instance;
	}

	/**
	 * Fixe le seed pour rendre la simulation reproductible.
	 * Avec le même seed, on obtient exactement la même simulation.
	 */
	public void setSeed(long seed) {
		this.random = new Random(seed);
	}

	/**
	 * Retourne un double entre 0.0 et 1.0.
	 */
	public double nextDouble() {
		return random.nextDouble();
	}

	/**
	 * Retourne un entier entre 0 (inclus) et bound (exclus).
	 */
	public int nextInt(int bound) {
		return random.nextInt(bound);
	}
}