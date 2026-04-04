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
            case "Alerte Météo":      return new EventMeteo(true);
            case "Beau Temps":        return new EventMeteo(false);
            case "Fête de Quartier":  return new EventSocial();
            case "Offres d'Emploi":   return new EventPerso();
            case "Expo Musée":        return new EventCulturel();
            default:
                throw new IllegalArgumentException("Événement inconnu : " + nomEvenement);
        }
    }
}