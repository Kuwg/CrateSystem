package me.kuwg;

import me.kuwg.config.CrateConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrateSystem extends JavaPlugin {
    private static CrateSystem instance;
    private static CrateConfiguration configuration;
    @Override
    public void onEnable() {
        instance=this;
        configuration=new CrateConfiguration(instance);


    }

    @Override
    public void onDisable() {

    }

    public static CrateSystem getInstance() {
        return instance;
    }

    public static CrateConfiguration getConfiguration(){
        return configuration;
    }
}
