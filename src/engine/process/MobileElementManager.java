package engine.process;

import engine.map.Map;
import engine.habitant.Habitant;
import engine.map.Horloge;
import engine.map.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobileElementManager implements MobileInterface {

    private Map map;
    private List<Habitant> habitants= new ArrayList<Habitant>();
    private Horloge horloge= new Horloge();
    private Random random = new Random();

    public MobileElementManager(Map map) {
        this.map = map;
    }
    @Override
    public void nextRound() {
        horloge.incrementer();
        for (Habitant h: habitants){
            moveRandomly(h);
        }

    }

    private void moveRandomly(Habitant h) {
        int direction = random.nextInt(4);
        Block pos = h.getPosition();
        int l = pos.getLine();
        int col = pos.getColumn();

        if (direction == 0 && l > 0) l--;
        else if (direction == 1 && l < map.getLineCount() - 1) l++;
        else if (direction == 2 && col > 0) col--;
        else if (direction == 3 && col < map.getColumnCount() - 1) col++;

        h.setPosition(map.getBlock(l, col));
    }

    @Override
    public Horloge getHorloge() {
        return horloge;
    }

    @Override
    public List<Habitant> getHabitants(){
        return habitants;
    }

    @Override
    public Habitant getHabitant(int line, int column) {
        for (Habitant h : habitants) {
            if (h.getPosition().getLine() == line && h.getPosition().getColumn() == column) {
                return h;
            }
        }
        return null;
    }

    public void addHabitant(Habitant h){
        habitants.add(h);
    }

}