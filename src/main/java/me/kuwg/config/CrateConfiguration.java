package me.kuwg.config;

import me.kuwg.CrateSystem;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

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

    public boolean getBoolean(String path, boolean def){
        return config.getBoolean(path, def);
    }

    public String getString(String path){
        return config.getString(path, "");
    }

    public String getSQLSetting(String setting){
        return this.getString("mysql."+setting);
    }

    public String noPermission(){
        return config.getString("no-permission", "§4You cannot do this!");
    }
    public String getPrefix(){
        return config.getString("prefix", "§b[§6CrateSystem§b]");
    }
    public String getKeyString(String path){
        return config.getString("crate-key."+path, "");
    }
    public List<String> getKeyList(String path){
        return config.getStringList("crate-key."+path);
    }
    public boolean getKeyBool(String path){
        return config.getBoolean("crate-key."+path, false);
    }
}
