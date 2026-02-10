package engine.process;

import engine.habitant.Habitants;
import engine.map.Horloge;

import java.util.List;

public interface MobileInterface {
    void nextRound();
    Horloge getHorloge();
    List<Habitants> getHabitants();
    Habitants  getHabitant(int line, int column);
}