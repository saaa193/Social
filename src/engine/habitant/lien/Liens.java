package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Classe abstraite Liens.
 * Elle sert de modèle (ou de "contrat") pour tous les types de relations sociales.
 * Le fait qu'elle soit abstraite empêche d'instancier un "lien" générique :
 * on doit obligatoirement définir s'il s'agit d'un lien Amical, Familial ou Professionnel.
 */
public abstract class Liens {

    // protected permet aux classes filles (Familial, Amical, etc.) d'accéder directement aux données,
    // tout en restant caché pour le reste du programme (encapsulation).
    protected Habitant partenaire;
    protected int force;

    public Liens(Habitant partenaire, int force) {
        this.partenaire = partenaire;
        this.force = force;
    }

    // Accesseurs
    public Habitant getPartenaire() {
        return partenaire;
    }

    public int getForce() {
        return force;
    }

    /**
     * Méthode abstraite : C'est le point clé de notre polymorphisme.
     * Chaque type de lien devra définir sa propre façon d'impacter le moral/social.
     * Le manager n'a pas besoin de savoir quel est le type de lien, il appelle juste cette méthode.
     */
    public abstract void appliquerBonusMental(Habitant proprietaire);
}