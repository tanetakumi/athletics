package net.serveron.hane.athletics.listener;

import net.serveron.hane.athletics.Athletics;
import net.serveron.hane.athletics.config.TeleportStructure;
import org.bukkit.event.Listener;

import java.util.List;

public class JumpTeleportListener implements Listener {
    private final Athletics plugin;

    private final List<TeleportStructure> teleportStructureList;

    public JumpTeleportListener(Athletics plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
}
