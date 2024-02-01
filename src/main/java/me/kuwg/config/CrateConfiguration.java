package me.kuwg.config;

import me.kuwg.CrateSystem;
import org.bukkit.configuration.file.FileConfiguration;

public class CrateConfiguration {
    private final CrateSystem main;

    private final FileConfiguration config;

    public CrateConfiguration(CrateSystem main) {
        this.main = main;
        this.config=main.getConfig();
    }

    public FileConfiguration getConfig(){
        return config;
    }
    public void saveConfig(){
        main.saveConfig();
    }

    public boolean getBoolean(String path){
        return config.getBoolean(path);
    }
}
