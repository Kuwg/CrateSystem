package me.kuwg.crate;

import me.kuwg.CrateSystem;
import me.kuwg.db.DatabaseManager;
import me.kuwg.listener.CrateEventListener;
import me.kuwg.util.Couple;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrateManager {
    private static final DatabaseManager databaseManager = CrateSystem.getInstance().getDatabaseManager();
    private static List<Crate> crates;
    private static List<Block> cratesBlock;
    public static void loadCrates(){
        crates = databaseManager.loadAllCrates();
        cratesBlock=new ArrayList<>();
        crates.forEach(crate -> cratesBlock.add(crate.getLocation().getBlock()));
    }
    public static void addCrate(final Crate crate){
        crates.add(crate);
        cratesBlock.add(crate.getLocation().getBlock());
    }
    public static void removeCrate(final Crate crate){
        crates.remove(crate);
        cratesBlock.remove(crate.getLocation().getBlock());
    }

    public static void saveCrates(){
        crates.forEach(databaseManager::saveCrate);
    }

    public static String nextCrateName(){
        return "crate_"+(crates.size()-1);
    }

    public static Couple<Boolean, Crate> isCrate(final Block block){
        for (Block b : cratesBlock) {
            if (b.getLocation().equals(block.getLocation())) {
                for(Crate c : crates)
                    if(c.getLocation().getBlock().equals(b))
                        return new Couple<>(true, c);
            }
        }
        return new Couple<>(false, null);
    }
    public static ItemStack getCratesKey(){
        return CrateEventListener.KEY;
    }

    public static void openCrate(Player player, Crate crate, boolean mainHand){

    }
}
