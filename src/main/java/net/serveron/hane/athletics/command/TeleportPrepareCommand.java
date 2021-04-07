package net.serveron.hane.athletics.command;

import net.kyori.adventure.text.Component;
import net.serveron.hane.haneserverlobby.HaneServerLobby;
import net.serveron.hane.haneserverlobby.Listener.PrepareListener;
import net.serveron.hane.haneserverlobby.util.ColorSearch;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleportPrepareCommand implements CommandExecutor, TabCompleter {
    private final HaneServerLobby plugin;
    private PrepareListener listener;

    public TeleportPrepareCommand(HaneServerLobby plugin) {
        this.plugin = plugin;
        plugin.getCommand("protect").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use commands with the console.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length > 0) {
            if (player.hasPermission("protect")) {
                switch (args[0].toLowerCase()) {
                    case "selector":
                        if(listener==null){
                            listener = new PrepareListener(plugin, player);
                            player.sendMessage(Component.text("選択したら、/selected <名前> で登録されます。").color(ColorSearch.Gold));
                        } else {
                            player.sendMessage(Component.text("現在選択モードです。").color(ColorSearch.Gold));
                        }
                        break;
                    case "cancel":
                        if(listener==null){
                            player.sendMessage(Component.text("誰も選択モードではありません。").color(ColorSearch.Gold));
                        } else {
                            listener.deInit();
                            listener = null;
                            player.sendMessage(Component.text("現在選択モードをキャンセルしました。").color(ColorSearch.Gold));
                        }
                        break;
                    case "register":
                        if(args.length == 2){
                            LocationStructure ls = getLS(args[1]);
                            if(ls==null){
                                //土地を登録してsave
                                plugin.getHaneAsistConfig().getLocationStructureList().add(listener.getLocationStructure(args[1]));
                                plugin.getHaneAsistConfig().saveProtectRegion();

                                player.sendMessage(Component.text(args[1]+"を登録しました。").color(ColorSearch.Gold));
                                listener.deInit();
                                listener = null;

                                plugin.getProtectListener().reloadProtectEvent();
                                player.sendMessage(Component.text("土地保護リスナーをreloadしました。").color(ColorSearch.Gold));
                            } else {
                                player.sendMessage(Component.text("同じ名前の土地が存在します。").color(ColorSearch.Gold));
                            }
                        } else {
                            player.sendMessage(Component.text("引数が違います。/selected <名前> で登録。").color(ColorSearch.Gold));
                        }
                        break;
                    case "remove":
                        if(args.length == 2){
                            LocationStructure ls = getLS(args[1]);
                            if(ls!=null){
                                plugin.getHaneAsistConfig().getLocationStructureList().remove(ls);
                                plugin.getHaneAsistConfig().saveProtectRegion();
                                player.sendMessage(Component.text(args[1]+"を削除しました。").color(ColorSearch.Gold));

                                plugin.getProtectListener().reloadProtectEvent();
                                player.sendMessage(Component.text("土地保護リスナーをreloadしました。").color(ColorSearch.Gold));
                            } else {
                                player.sendMessage(Component.text(args[1]+"は存在しませんでした。").color(ColorSearch.Gold));
                            }
                            break;
                        } else {
                            player.sendMessage(Component.text("引数が違います。/selected <名前> で登録。").color(ColorSearch.Gold));
                        }
                        break;
                    case "info":
                        plugin.getHaneAsistConfig().getLocationStructureList()
                                .forEach(l -> player.sendMessage(Component.text(l.locationString()).color(ColorSearch.Gold)));
                        break;
                }
            } else {
                player.sendMessage("権限がありません。");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String command, String[] args) {
        List<String> autoComplete = new ArrayList<>();
        if (sender.hasPermission("protect")) {
            if (args.length == 1) {//一段目
                autoComplete.addAll(Arrays.asList("selector", "register", "info","remove","cancel"));
            } else if(args.length == 2){
                if(args[0].equals("remove")){
                    plugin.getHaneAsistConfig().getLocationStructureList()
                            .forEach(l -> autoComplete.add(l.getName()));
                }
            }
        }
        //文字比較と削除-----------------------------------------------------
        //Collections.sort(autoComplete);
        autoComplete.removeIf(str -> !str.startsWith(args[args.length - 1]));
        //------------------------------------------------------
        return autoComplete;
    }

    private LocationStructure getLS(String name){
        LocationStructure locationStructure = null;
        for(LocationStructure ls : plugin.getHaneAsistConfig().getLocationStructureList()){
            if(ls.getName().equals(name)){
                locationStructure = ls;
                break;
            }
        }
        return locationStructure;
    }
}
