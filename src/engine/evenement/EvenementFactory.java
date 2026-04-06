package engine.evenement;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Simple Factory : crée le bon événement à partir de son nom.
 * Le reste du code ne connaît que l'interface EvenementSimulation.
 */
public class EvenementFactory {

    public static EvenementSimulation creer(String nomEvenement) {
        switch (nomEvenement) {
            case "Tempête Urbaine":    return new EventTempete();
            case "Festival de Quartier": return new EventFestival();
            case "Crise Économique":   return new EventCrise();
            case "Semaine Culturelle": return new EventFestival();
            case "Épidémie":           return new EventEpidemie();
            default:
                throw new IllegalArgumentException("Événement inconnu : " + nomEvenement);
        }
    }
}