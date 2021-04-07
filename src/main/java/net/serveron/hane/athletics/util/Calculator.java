package net.serveron.hane.athletics.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    static class StopWatch {
        private final String playerName;
        private final double startTime;
        private final String athleticName;

        public StopWatch(String playerName, String athleticName){
            this.playerName = playerName;
            this.startTime = (double)System.currentTimeMillis()/1000;
            this.athleticName = athleticName;
        }

        public String getPlayerName(){
            return playerName;
        }

        public double getStartTime() {
            return startTime;
        }

        public String getAthleticName() {
            return athleticName;
        }
    }

    private static final List<StopWatch> calcList = new ArrayList<>();

    public static void playerStart(Player player,String athleticName){
        for(StopWatch stopWatch: calcList){
            if(stopWatch.getPlayerName().equals(player.getName())){
                calcList.remove(stopWatch);
                break;
            }
        }
        calcList.add(new StopWatch(player.getName(),athleticName));
    }

    public static double getPlayerTime(Player player,String athleticName){
        double finishTime = (double)System.currentTimeMillis()/1000;

        for(StopWatch stopWatch: calcList){
            if(stopWatch.getPlayerName().equals(player.getName()) && stopWatch.getAthleticName().equals(athleticName)){
                double startTime = stopWatch.getStartTime();
                calcList.remove(stopWatch);
                return finishTime - startTime;
            }
        }
        return -1;
    }

}
