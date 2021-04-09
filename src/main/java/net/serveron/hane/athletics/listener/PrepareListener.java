package net.serveron.hane.athletics.listener;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.serveron.hane.athletics.Athletics;
import net.serveron.hane.athletics.config.TeleportStructure;
import net.serveron.hane.athletics.util.ColorSearch;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareListener implements Listener {

    //Kyori adventure を使用していきたい。
    private final Athletics plugin;
    private final Player targetPlayer;

    private Location loc1;
    private Location loc2;

    public PrepareListener(Athletics plugin, Player player){
        this.plugin = plugin;
        this.targetPlayer = player;
        player.getInventory().setItem(0,getNamedStick("Location1"));
        player.getInventory().setItem(1,getNamedStick("Location2"));
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

    public TeleportStructure getTeleportStructure(String name){
        if(loc1!=null && loc2!=null){
            return new TeleportStructure(name,targetPlayer.getLocation(),loc1,loc2);
        } else {
            return null;
        }
    }

    private ItemStack getNamedStick(String name){
        ItemStack itemStack = new ItemStack(Material.STICK,1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(name).color(ColorSearch.Gold));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void deInit(){
        targetPlayer.getInventory().setItem(0,null);
        targetPlayer.getInventory().setItem(1,null);
        HandlerList.unregisterAll(this);
    }
}
