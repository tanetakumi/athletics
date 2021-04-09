package net.serveron.hane.athletics.command;

import net.kyori.adventure.text.Component;
import net.serveron.hane.athletics.Athletics;
import net.serveron.hane.athletics.util.ColorSearch;
import net.serveron.hane.athletics.util.PlayerSearch;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameCommand implements CommandExecutor, TabCompleter {
    private final Athletics plugin;

    public GameCommand( Athletics plugin) {
        this.plugin = plugin;
        plugin.getCommand("athle").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof BlockCommandSender)){
            sender.sendMessage("このコマンドはコマンドブロック専用です。");
            return true;
        }
        BlockCommandSender cb = (BlockCommandSender) sender;
        Player player = PlayerSearch.getNearbyPlayer(cb.getBlock().getLocation());
        if(player != null){
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("start")){
                    plugin.getPlayerGame().onJoinAthletic(player,args[1]);
                } else if(args[0].equalsIgnoreCase("stop")){
                    plugin.getPlayerGame().onCompleteAthletic(player,args[1]);
                } else {
                    sender.sendMessage(Component.text("コマンドが違います。"));
                }
            } else if(args.length == 1){
                if(args[0].equalsIgnoreCase("show")){
                    plugin.getPlayerGame().showPlayerData(player);
                } else if(args[0].equalsIgnoreCase("save")){
                    if(plugin.getPlayerGame().savePlayerData(player)){
                        player.sendMessage(Component.text("データがセーブされました。").color(ColorSearch.Aqua));
                    } else {
                        player.sendMessage(Component.text("データがセーブできませんでした。\n運営にお問い合わせください。").color(ColorSearch.Red));
                    }
                }
            } else {
                sender.sendMessage(Component.text("引数が違います。"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String command, String[] args) {
        List<String> autoComplete = new ArrayList<>();
        if(sender.hasPermission("athle")){
            if (args.length == 1){//一段目
                autoComplete.addAll(Arrays.asList("start","stop","show","save"));
            }
        }
        //文字比較と削除-----------------------------------------------------
        //Collections.sort(autoComplete);
        autoComplete.removeIf(str -> !str.startsWith(args[args.length - 1]));
        //------------------------------------------------------
        return autoComplete;
    }
}
