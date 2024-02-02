package me.kuwg;

import me.kuwg.commands.CrateSystemCommand;
import me.kuwg.config.CrateConfiguration;
import me.kuwg.crate.CrateManager;
import me.kuwg.db.DatabaseManager;
import me.kuwg.listener.CrateEventListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CrateSystem extends JavaPlugin {
    private static CrateSystem instance;
    private static CrateConfiguration configuration;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance=this;
        configuration=new CrateConfiguration(instance);
        databaseManager=DatabaseManager.fromConfig(configuration);
        CrateManager.loadCrates();

        // just to suppress the IntelliJ idea NullPointerException warning without SuppressWarnings.
        Objects.requireNonNull(getCommand("cratesystem")).setExecutor(new CrateSystemCommand());
        getServer().getPluginManager().registerEvents(new CrateEventListener(), this);

    }

    @Override
    public void onDisable() {
        CrateManager.saveCrates();
        databaseManager.disconnect();

    }

    public static CrateSystem getInstance() {
        return instance;
    }

    public static CrateConfiguration getConfiguration(){
        return configuration;
    }

    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    public void reload(){
        configuration.saveConfig();
        CrateEventListener.reload();

    }
}
