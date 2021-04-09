package net.serveron.hane.athletics.config;

import net.serveron.hane.athletics.Athletics;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class AthleticsConfig {

    private final Athletics plugin;
    private FileConfiguration config;

    private final List<TeleportStructure> teleportStructureList = new ArrayList<>();

    public AthleticsConfig(Athletics plugin) {
        this.plugin = plugin;
        load();
    }

    @SuppressWarnings("unchecked")
    public void load() {
        // 設定ファイルを保存
        plugin.saveDefaultConfig();
        if (config != null) { // configが非null == リロードで呼び出された
            plugin.reloadConfig();
        }
        config = plugin.getConfig();
        getTeleportLocation();

    }

    private void getTeleportLocation() {
        ConfigurationSection cs = config.getConfigurationSection("region");
        if (cs != null) {
            int num = cs.getKeys(false).size();
            for (int i = 0; i < num; i++) {
                String region_name = config.getString("region.region" + i + ".name");
                Location target = config.getLocation("region.region" + i + ".target");
                Location loc1 = config.getLocation("region.region" + i + ".loc1");
                Location loc2 = config.getLocation("region.region" + i + ".loc2");
                if (target != null && loc1 != null && loc2 != null) {
                    teleportStructureList.add(new TeleportStructure(region_name, target, loc1, loc2));
                }
            }
        }
    }

    public void saveProtectRegion() {
        config.set("region", null);//いったんクリア
        for (int i = 0; i < teleportStructureList.size(); i++) {
            config.set("region.region" + i + ".name", teleportStructureList.get(i).getName());
            config.set("region.region" + i + ".target", teleportStructureList.get(i).getTargetLocation());
            config.set("region.region" + i + ".loc1", teleportStructureList.get(i).getLoc1());
            config.set("region.region" + i + ".loc2", teleportStructureList.get(i).getLoc2());
        }
        plugin.saveConfig();
    }

    public List<TeleportStructure> getTeleportStructureList() {
        return teleportStructureList;
    }

    public void addTeleportStructure(TeleportStructure teleportStructure){
        teleportStructureList.add(teleportStructure);
        saveProtectRegion();
    }

    public boolean removeTeleportStructure(String name){
        boolean res = teleportStructureList.removeIf(t -> t.getName().equals(name));
        saveProtectRegion();
        return res;
    }

    public boolean containedInTeleportStructure(String name){
        return teleportStructureList.stream().anyMatch(t -> t.getName().equals(name));
    }
}
