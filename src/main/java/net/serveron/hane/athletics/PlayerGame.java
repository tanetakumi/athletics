package net.serveron.hane.athletics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
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
            if(!players.contains(playerData)) {
                players.add(new PlayerData(player.getUniqueId().toString(), result));
            }
        }
    }

    public void onJoinAthletic(Player player,String athleticName){
        athleticCalc.playerStart(player,athleticName);
    }
    public int onFish(Player player){
        Optional<PlayerData> optionalPlayerData = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();
        optionalPlayerData.ifPresent(PlayerData::addFishingCount);
        return optionalPlayerData.get().getFishing();
    }

    public void onCompleteAthletic(Player player,String athleticName){
        int time = athleticCalc.getPlayerTime(player,athleticName);
        player.sendMessage(Component.text("クリアおめでとうございます！\nクリアタイムは"+time+"秒です。").color(TextColor.color(0x00ffff)));
        Optional<PlayerData> optionalPlayerData = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();
        boolean update = false;
        if(time>0){
            if(optionalPlayerData.isPresent()){
                PlayerData playerData = optionalPlayerData.get();
                if(athleticName.equals("at1")){
                    playerData.addAt1Count();
                    update = true;
                } else if(athleticName.equals("at2")){
                    if(time<playerData.getAt2()){
                        playerData.updataAt2(time);
                        update = true;
                    }
                } else if(athleticName.equals("at3")){
                    //System.out.println("fdsfsdf");
                    if(time<playerData.getAt3()){
                        playerData.updataAt3(time);
                        update = true;
                    }
                } else if(athleticName.equals("at4")){
                    if(time<playerData.getAt4()){
                        playerData.updataAt4(time);
                        update = true;
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
            TextComponent component = Component.text("名前: "+optionalPlayerData.get().getName() +"\n"+
                    "アスレ1: " +optionalPlayerData.get().getAt1()+"回"+
                    " アスレ2: " +optionalPlayerData.get().getAt2()+"秒\n"+
                    "アスレ3: " +optionalPlayerData.get().getAt3()+"秒"+
                    " アスレ4: " +optionalPlayerData.get().getAt4()+"秒\n"+
                    "PVP: "+optionalPlayerData.get().getPvp_win()+"/"+optionalPlayerData.get().getPvp_lose()+"\n"+
                    "釣り: "+optionalPlayerData.get().getFishing()+"回"
            ).color(TextColor.color(0x00ffff));
            player.sendMessage(component);
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
        Optional<PlayerData> optionalPlayerData = players.stream()
                .filter(p -> p.getUuid().equals(player.getUniqueId().toString())).findFirst();
        optionalPlayerData.ifPresent(plugin::runAsyncSetPlayerData);
        players.removeIf(playerData -> playerData.getUuid().equals(player.getUniqueId().toString()));
    }
}
