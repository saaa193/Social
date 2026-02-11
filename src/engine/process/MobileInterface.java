package engine.process;

import java.util.List;
import engine.map.Horloge;
import engine.habitant.Habitant; // Attention au rename (sans S)

public interface MobileInterface {

    void nextRound();

    // C'est cette ligne qui manquait peut-être !
    Horloge getHorloge();

    List<Habitant> getHabitants();

    Habitant getHabitant(int line, int column);
}