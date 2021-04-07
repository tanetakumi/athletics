package net.serveron.hane.athletics.listener;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.serveron.hane.haneserverlobby.HaneServerLobby;
import net.serveron.hane.haneserverlobby.util.ColorSearch;
import net.serveron.hane.haneserverlobby.util.ItemManager;
import net.serveron.hane.haneserverlobby.util.TeleportStructure;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareListener implements Listener {

    //前もってストラクチャ―を作成しておき、sortを使わず処理する。
    //X -> Z -> Y　の順で処理することで高速化
    //Mall以外の処理ができるように一応Listにしておくが速度を上げるにはリストはないほうが良い。

    //Kyori adventure を使用していきたい。
    private final HaneServerLobby plugin;
    private final Player targetPlayer;

    private Location loc1;
    private Location loc2;

    public PrepareListener(HaneServerLobby plugin, Player player){
        this.plugin = plugin;
        this.targetPlayer = player;
        ItemManager.setPrepareStick(player,0,"Location1");
        ItemManager.setPrepareStick(player,1,"Location2");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(player.getName().equals(targetPlayer.getName())){
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if(itemMeta!=null && itemMeta.hasDisplayName()){
                if(PlainComponentSerializer.plain().serialize(itemMeta.displayName()).equals("Location1")){
                    e.setCancelled(true);
                    loc1 = e.getBlock().getLocation();
                    player.sendMessage(Component.text("Location1をセットしました").color(ColorSearch.Gold));
                } else if(PlainComponentSerializer.plain().serialize(itemMeta.displayName()).equals("Location2")){
                    e.setCancelled(true);
                    loc2 = e.getBlock().getLocation();
                    player.sendMessage(Component.text("Location2をセットしました").color(ColorSearch.Gold));
                }
            }
        }
    }

    public TeleportStructure getLocationStructure(String name){
        if(loc1!=null && loc2!=null){
            return new TeleportStructure(targetPlayer.getLocation(),loc1,loc2);
        } else {
            return null;
        }
    }

    public void deInit(){
        targetPlayer.getInventory().setItem(0,null);
        targetPlayer.getInventory().setItem(1,null);
        HandlerList.unregisterAll(this);
    }
}
