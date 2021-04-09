package net.serveron.hane.athletics;

import net.serveron.hane.athletics.command.GameCommand;
import net.serveron.hane.athletics.command.TeleportPrepareCommand;
import net.serveron.hane.athletics.config.AthleticsConfig;
import net.serveron.hane.athletics.listener.JumpTeleportListener;
import net.serveron.hane.athletics.util.PlayerData;
import net.serveron.hane.athletics.util.SQLite;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Athletics extends JavaPlugin {
    private SQLite sqLite;
    private PlayerGame playerGame;
    private ExecutorService threadPool;

    private AthleticsConfig athleticsConfig;

    private JumpTeleportListener jumpTeleportListener;

    @Override
    public void onEnable() {
        // Plugin startup logic
        sqLite = new SQLite(getDataFolder(),"PlayerData","players");
        sqLite.openConnection();
        threadPool = Executors.newFixedThreadPool(3);

        playerGame = new PlayerGame(this);

        athleticsConfig = new AthleticsConfig(this);

        jumpTeleportListener = new JumpTeleportListener(this);

        new GameCommand(this);
        new TeleportPrepareCommand(this);
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

    public PlayerGame getPlayerGame(){
        return playerGame;
    }

    public AthleticsConfig getAthleticsConfig() { return athleticsConfig; }

    public JumpTeleportListener getJumpTeleportListener() { return jumpTeleportListener; }

    public void runAsyncSetPlayerData(PlayerData playerData){
        Runnable task = () -> sqLite.setData(playerData.getUuid(),playerData.getStringData());
        threadPool.submit(task);
    }
    public void runAsyncDeletePlayerData(String key){
        Runnable task = () -> sqLite.deleteData(key);
        threadPool.submit(task);
    }

}
