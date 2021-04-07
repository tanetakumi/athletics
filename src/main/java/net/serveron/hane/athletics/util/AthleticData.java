package net.serveron.hane.athletics.util;

public class AthleticData {
    private final String athleticName;
    private final String playerName;
    private final int rank;
    private final int time;
    private final int num;

    public AthleticData(String athletic, String playerName, int time, int num){
        String[] athle = athletic.split("-", 0);
        this.athleticName = athle[0];
        this.rank = stringToInt(athle[1]);
        this.playerName = playerName;
        this.time = time;
        this.num = num;
    }
    public String getSQLName(){
        return athleticName+"-"+rank;
    }

    public String getAthleticName() {
        return athleticName;
    }

    public int getRank() {
        return rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTime() {
        return time;
    }

    public int getNum() {
        return num;
    }

    private int stringToInt(String str){
        int x = -1;
        try{
            x = Integer.parseInt(str);
        }
        catch(Exception ignored){
        }
        return x;
    }
}
