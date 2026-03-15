package engine.process;

import engine.habitant.Habitant;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Liens;
import engine.habitant.visitor.ContagionVisitor;

import java.util.List;

/**
 * InformationTransmission : propage une information/rumeur
 * dans le réseau social via les liens entre habitants.
 * Utilise ContagionVisitor pour calculer l'impact émotionnel.
 */
public class InformationTransmission {

    private String theme;
    private float virulence;  // 0.0 = lente, 1.0 = épidémique
    private float veracite;   // 0.0 = rumeur, 1.0 = fait avéré

    private ContagionVisitor contagionVisitor = new ContagionVisitor();

    public InformationTransmission(String theme, float virulence, float veracite) {
        this.theme = theme;
        this.virulence = virulence;
        this.veracite = veracite;
    }

    /**
     * Propage l'information sur toute la population.
     * Appelée à chaque tour par MobileElementManager.
     */
    public void propagerDansReseau(List<Habitant> habitants) {
        for (Habitant porteur : habitants) {
            for (Liens lien : porteur.getRelation()) {
                Habitant cible = lien.getPartenaire();
                tenterTransmission(porteur, cible, lien.getForce());
            }
        }
    }

    /**
     * Tente de transmettre l'information du porteur vers la cible.
     * La force du lien et la virulence déterminent la probabilité.
     */
    private void tenterTransmission(Habitant porteur, Habitant cible, int forceLien) {
        // Probabilité = virulence + bonus du lien
        double probabilite = virulence + (forceLien / 200.0);

        if (Math.random() < probabilite) {
            // On calcule l'impact via le Visitor
            EtatHabitant etat = porteur.getPsychologie().determinerEtat(porteur.getBesoins());
            int impact = etat.accept(contagionVisitor);

            // Le névrosisme de la cible amplifie l'impact négatif
            if (impact < 0) {
                double amplification = 1.0 + (cible.getNevrosisme() / 100.0);
                impact = (int)(impact * amplification);
            }

            cible.getBesoins().setMoral(cible.getBesoins().getMoral() + impact);
        }
    }


}