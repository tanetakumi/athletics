package net.serveron.hane.athletics.command;

import net.kyori.adventure.text.Component;
import net.serveron.hane.athletics.Athletics;
import net.serveron.hane.athletics.config.TeleportStructure;
import net.serveron.hane.athletics.listener.PrepareListener;
import net.serveron.hane.athletics.util.ColorSearch;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleportPrepareCommand implements CommandExecutor, TabCompleter {
    private final Athletics plugin;
    private PrepareListener listener;

    public TeleportPrepareCommand(Athletics plugin) {
        this.plugin = plugin;
        plugin.getCommand("pt").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use commands with the console.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length > 0) {
            if (player.hasPermission("pt")) {
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
                            if(listener==null){
                                player.sendMessage(Component.text("現在誰も選択モードではありません。").color(ColorSearch.Gold));
                            } else {
                                if(plugin.getAthleticsConfig().containedInTeleportStructure(args[1])){
                                    player.sendMessage(Component.text("同じ名前の土地が存在します。").color(ColorSearch.Gold));
                                } else {
                                    TeleportStructure ts = listener.getTeleportStructure(args[1]);
                                    if(ts!=null){
                                        plugin.getAthleticsConfig().addTeleportStructure(ts);
                                        plugin.getJumpTeleportListener().reloadListener();
                                        player.sendMessage(Component.text("登録が完了しました。リスナーを終了します。").color(ColorSearch.Gold));
                                    } else {
                                        player.sendMessage(Component.text("必要な情報が登録できていません。").color(ColorSearch.Gold));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(Component.text("引数が違います。/selected <名前> で登録。").color(ColorSearch.Gold));
                        }
                        break;
                    case "remove":
                        if(args.length == 2){
                            if(plugin.getAthleticsConfig().removeTeleportStructure(args[1])){
                                player.sendMessage(Component.text(args[1]+"を削除しました。").color(ColorSearch.Gold));
                                plugin.getJumpTeleportListener().reloadListener();
                                player.sendMessage(Component.text("リロードしました。").color(ColorSearch.Gold));
                            } else {
                                player.sendMessage(Component.text(args[1]+"は存在しませんでした。").color(ColorSearch.Gold));
                            }
                        } else {
                            player.sendMessage(Component.text("引数が違います。/selected <名前> で登録。").color(ColorSearch.Gold));
                        }
                        break;
                    case "info":
                        plugin.getAthleticsConfig().getTeleportStructureList()
                                .forEach(t -> player.sendMessage(Component.text(t.getText()).color(ColorSearch.Gold)));
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
        if (sender.hasPermission("pt")) {
            if (args.length == 1) {//一段目
                autoComplete.addAll(Arrays.asList("selector", "register", "info","remove","cancel"));
            } else if(args.length == 2){
                if(args[0].equals("remove")){
                    plugin.getAthleticsConfig().getTeleportStructureList().forEach(t -> autoComplete.add(t.getName()));
                }
            }
        }
        //文字比較と削除-------------------------------------------
        autoComplete.removeIf(str -> !str.startsWith(args[args.length - 1]));
        //------------------------------------------------------
        return autoComplete;
    }
}
