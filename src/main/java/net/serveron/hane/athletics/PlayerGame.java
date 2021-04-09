package net.serveron.hane.athletics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.serveron.hane.athletics.util.Calculator;
import net.serveron.hane.athletics.util.ColorSearch;
import net.serveron.hane.athletics.util.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerGame {
    private final Athletics plugin;
    private final List<PlayerData> players = new ArrayList<>();

    public PlayerGame(Athletics plugin){
        this.plugin = plugin;
        for(Player player : Bukkit.getOnlinePlayers()){
            playerJoin(player);
        }
    }

    public void playerJoin(Player player){
        String result = plugin.getSqLite().getData(player.getUniqueId().toString());
        if(result==null){
            players.add(new PlayerData(player.getUniqueId().toString(),""));
        } else {
            if(players.stream().noneMatch(p -> p.getUuid().equals(player.getUniqueId().toString()))) {
                players.add(new PlayerData(player.getUniqueId().toString(), result));
            }
        }
    }

    public void onJoinAthletic(Player player,String athleticName){
        Calculator.playerStart(player,athleticName);
    }

    public int onFish(Player player){
        Optional<PlayerData> opd = players.stream().filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();
        int fishCount = 0;
        if(opd.isPresent()){
            PlayerData playerData = opd.get();
            String strFishCount = playerData.getValue("fish");
            if(strFishCount==null){
                playerData.setValue("fish",String.valueOf(1));
            } else {
                fishCount = stringToInt(strFishCount);
                if(fishCount>0){
                    fishCount++;
                }
                playerData.setValue("fish",String.valueOf(fishCount));
            }
        }
        return fishCount;
    }

    public void onCompleteAthletic(Player player,String athleticName){
        double time = Calculator.getPlayerTime(player,athleticName);
        player.sendMessage(Component.text("クリアおめでとうございます！\nクリアタイムは"+time+"秒です。").color(ColorSearch.Aqua));
        Optional<PlayerData> optionalPlayerData = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();
        boolean update = false;
        if(time>0){
            if(optionalPlayerData.isPresent()){
                PlayerData playerData = optionalPlayerData.get();
                String record = playerData.getValue(athleticName);
                if(record==null){
                    playerData.setValue(athleticName,String.valueOf(time));
                } else {
                    double record_time = stringToDouble(record);
                    if(record_time>time){
                        update = true;
                        playerData.setValue(athleticName,String.valueOf(time));
                    }
                }
            }
        }
        if(update){
            plugin.runAsyncSetPlayerData(optionalPlayerData.get());
        }
    }

    public void showPlayerData(Player player){
        Optional<PlayerData> optionalPlayerData = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();

        if(optionalPlayerData.isPresent()){
            player.sendMessage(Component.text(optionalPlayerData.get().showStringData()).color(ColorSearch.Aqua));
        } else {
            player.sendMessage(Component.text("エラー発生"));
            System.out.println("ERROR データがありませんでした。");
        }
    }

    public boolean savePlayerData(Player player){
        Optional<PlayerData> optionalPlayerData = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();

        if(optionalPlayerData.isPresent()){
            plugin.runAsyncSetPlayerData(optionalPlayerData.get());
            return true;
        } else {
            return false;
        }
    }

    public void playerQuit(Player player){
        Optional<PlayerData> opd = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();
        opd.ifPresent(plugin::runAsyncSetPlayerData);
        players.removeIf(p -> p.getUuid().equals(player.getUniqueId().toString()));
    }

    private double stringToDouble(String str){
        double num;
        try{
            num = Double.parseDouble(str);
            return num;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return -1;
        }
    }
    private int stringToInt(String str){
        int num;
        try{
            num = Integer.parseInt(str);
            return num;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return -1;
        }
    }
}
