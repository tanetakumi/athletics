package net.serveron.hane.athletics;

import net.serveron.hane.athletics.util.SQLite;
import org.bukkit.plugin.java.JavaPlugin;

public final class Athletics extends JavaPlugin {
    private SQLite sqLite;

    @Override
    public void onEnable() {
        // Plugin startup logic
        sqLite = new SQLite(getDataFolder(),"playerdata","players");
        sqLite.openConnection();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(sqLite!=null){
            sqLite.closeDisconnect();
        }
    }

    public SQLite getSqLite() {
        return sqLite;
    }
}
