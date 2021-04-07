package net.serveron.hane.athletics.listener;

import net.serveron.hane.haneserverlobby.HaneServerLobby;
import net.serveron.hane.haneserverlobby.util.TeleportStructure;
import org.bukkit.event.Listener;

import java.util.List;

public class JumpTeleportListener implements Listener {
    private final HaneServerLobby plugin;

    private final List<TeleportStructure> teleportStructureList;

    public JumpTeleportListener(HaneServerLobby plugin){
        this.plugin = plugin;
        //this.teleportStructureList = plugin.getHsConfig().get
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
}
