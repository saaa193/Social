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

    // L'unique instance — pattern Singleton comme le prof
    private static RandomProvider instance = new RandomProvider();

    // Le générateur partagé par tout le projet
    private Random random = new Random();

    // Constructeur privé — personne ne peut faire new RandomProvider()
    private RandomProvider() {}

    // Accès à l'instance unique — comme VariableRepository.getInstance()
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
     * Remplace Math.random() partout dans le projet.
     */
    public double nextDouble() {
        return random.nextDouble();
    }

    /**
     * Retourne un entier entre 0 (inclus) et bound (exclus).
     * Remplace (int)(Math.random() * N) partout dans le projet.
     */
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}