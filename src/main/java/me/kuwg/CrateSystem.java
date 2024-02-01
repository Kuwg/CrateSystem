package me.kuwg;

import me.kuwg.config.CrateConfiguration;
import me.kuwg.db.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrateSystem extends JavaPlugin {
    private static CrateSystem instance;
    private static CrateConfiguration configuration;
    private DatabaseManager databaseManager;
    @Override
    public void onEnable() {
        instance=this;
        configuration=new CrateConfiguration(instance);
        databaseManager=DatabaseManager.fromConfig(configuration);

    }

    @Override
    public void onDisable() {
        databaseManager.disconnect();

    }

    public static CrateSystem getInstance() {
        return instance;
    }

    public static CrateConfiguration getConfiguration(){
        return configuration;
    }
}
