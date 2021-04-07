package net.serveron.hane.athletics.util;

public class PlayerData {
    private final String uuid;
    private final String name;
    private int at1 ;
    private int at2;
    private int at3;
    private int at4;
    private int pvp_win;
    private int pvp_lose;
    private int fishing;

    public PlayerData(String uuid, String name, int at1, int at2, int at3, int at4, int pvp_win, int pvp_lose, int fishing){
        this.uuid = uuid;
        this.name = name;
        this.at1 = at1;
        this.at2 = at2;
        this.at3 = at3;
        this.at4 = at4;
        this.pvp_win = pvp_win;
        this.pvp_lose = pvp_lose;
        this.fishing = fishing;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getAt1() {
        return at1;
    }

    public int getAt2() {
        return at2;
    }

    public int getAt3() {
        return at3;
    }

    public int getAt4() {
        return at4;
    }

    public int getPvp_win() {
        return pvp_win;
    }

    public int getPvp_lose() {
        return pvp_lose;
    }

    public int getFishing() {
        return fishing;
    }

    public void addAt1Count(){
        at1++;
    }

    public void updataAt2(int time){
        at2 = time;
    }
    public void updataAt3(int time){
        at3 = time;
    }
    public void updataAt4(int time){
        at4 = time;
    }
    public void addFishingCount(){
        fishing++;
    }

}
