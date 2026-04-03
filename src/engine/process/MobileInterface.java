package engine.process;

import java.util.List;

import engine.map.Horloge;
import engine.habitant.Habitant; // Attention au rename (sans S)

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 */
public interface MobileInterface {

	void nextRound();

	Horloge getHorloge();

	List<Habitant> getHabitants();

	Habitant getHabitant(int line, int column);
}