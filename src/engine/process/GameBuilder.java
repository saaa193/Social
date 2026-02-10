package engine.process;

import config.GameConfiguration;
import engine.habitant.Habitants;
import engine.map.Block;
import engine.map.Map;

public class GameBuilder {
    public static Map buildMap() {
        return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
    }

    public static MobileInterface buildInitMobile(Map map) {
        MobileInterface manager = new MobileElementManager(map);

        for(int i=0;i<100;i++) {
            int line=(int)(Math.random()*GameConfiguration.LINE_COUNT);
            int column=(int)(Math.random()*GameConfiguration.COLUMN_COUNT);

            Block position=map.getBlock(line,column);

            String prenom= "Habitants"+i;
            int age= 5+i;
            String sexe;

            if(i%2==0) {
                sexe="Male";
            }
            else {
                sexe="Female";
            }

            Habitants h=new Habitants(position,prenom,sexe, age);
        }

        return manager;
    }

}