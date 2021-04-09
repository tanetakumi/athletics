package net.serveron.hane.athletics;

import net.serveron.hane.athletics.util.PlayerData;
import net.serveron.hane.athletics.util.SQLite;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Athletics extends JavaPlugin {
    private SQLite sqLite;
    private ExecutorService threadPool;

    @Override
    public void onEnable() {
        // Plugin startup logic
        sqLite = new SQLite(getDataFolder(),"PlayerData","players");
        sqLite.openConnection();
        threadPool = Executors.newFixedThreadPool(3);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(sqLite!=null){
            sqLite.closeDisconnect();
        }
        if(threadPool!=null){
            threadPool.shutdown();
        }
    }

    public SQLite getSqLite() {
        return sqLite;
    }

    public void runAsyncSetPlayerData(PlayerData playerData){
        Runnable task = () -> sqLite.setData(playerData.getUuid(),playerData.getStringData());
        threadPool.submit(task);
    }
    public void runAsyncDeletePlayerData(String key){
        Runnable task = () -> sqLite.deleteData(key);
        threadPool.submit(task);
    }

}
