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
     * La virulence détermine la probabilité de transmission.
     * La véracité détermine l'impact sur les traits OCEAN :
     * - Vraie → impact positif, renforce l'ouverture
     * - Rumeur → impact négatif, fragilise le névrosisme
     */
    private void tenterTransmission(Habitant porteur, Habitant cible, int forceLien) {
        // La virulence + la force du lien déterminent la probabilité
        double probabilite = virulence + (forceLien / 200.0);

        if (Math.random() < probabilite) {

            // Le névrosisme de la cible amplifie l'impact négatif
            EtatHabitant etat = porteur.getPsychologie().determinerEtat(porteur.getBesoins());
            int impact = etat.accept(contagionVisitor);

            if (impact < 0) {
                double amplification = 1.0 + (cible.getNevrosisme() / 100.0);
                impact = (int)(impact * amplification);
            }

            cible.getBesoins().setMoral(cible.getBesoins().getMoral() + impact);

            // Effet de la véracité sur les traits OCEAN
            if (veracite > 0.6f) {
                // Information vraie → rassure, renforce la confiance
                cible.getBesoins().setMoral(cible.getBesoins().getMoral() + 5);
                // L'ouverture augmente — on fait confiance aux infos fiables
                cible.getPsychologie().augmenterOuverture(1);
            } else if (veracite < 0.4f) {
                // Rumeur → anxiété et méfiance
                cible.getBesoins().setMoral(cible.getBesoins().getMoral() - 5);
                // Le névrosisme augmente — la rumeur fragilise
                cible.getPsychologie().augmenterNevrosisme(2);
                // L'agréabilité diminue — on se méfie des autres
                cible.getPsychologie().diminuerAgreabilite(1);
            }
            // Entre 40% et 60% → information neutre, aucun effet OCEAN
        }
    }

}