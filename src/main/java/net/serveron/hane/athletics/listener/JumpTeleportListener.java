package net.serveron.hane.athletics.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.serveron.hane.athletics.Athletics;
import net.serveron.hane.athletics.config.TeleportStructure;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class JumpTeleportListener implements Listener {
    private final Athletics plugin;

    private List<TeleportStructure> teleportStructureList;

    public JumpTeleportListener(Athletics plugin){
        this.plugin = plugin;
        this.teleportStructureList = plugin.getAthleticsConfig().getTeleportStructureList();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    public void reloadListener(){
        this.teleportStructureList = plugin.getAthleticsConfig().getTeleportStructureList();
    }

    @EventHandler
    public void onJump(PlayerJumpEvent e){
        teleportStructureList.stream().filter(t -> t.checkLocation(e.getFrom())).findFirst()
                .ifPresent(teleportStructure -> e.getPlayer().teleport(teleportStructure.getTargetLocation()));
    }
}
