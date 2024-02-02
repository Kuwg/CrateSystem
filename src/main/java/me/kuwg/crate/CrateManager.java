package me.kuwg.crate;

import me.kuwg.CrateSystem;
import me.kuwg.config.CrateConfiguration;
import me.kuwg.db.DatabaseManager;
import me.kuwg.listener.CrateEventListener;
import me.kuwg.util.Couple;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CrateManager {
    private static final DatabaseManager databaseManager = CrateSystem.getInstance().getDatabaseManager();
    private static final Set<Crate> crates = ConcurrentHashMap.newKeySet();
    private static final Set<Block> cratesBlock = ConcurrentHashMap.newKeySet();

    public static void loadCrates() {
        crates.addAll(databaseManager.loadAllCrates());
        cratesBlock.addAll(crates.stream().map(crate -> crate.getLocation().getBlock()).toList());
    }

    public static void addCrate(final Crate crate) {
        crates.add(crate);
        cratesBlock.add(crate.getLocation().getBlock());
    }

    public static void removeCrate(final Crate crate) {
        crates.remove(crate);
        cratesBlock.remove(crate.getLocation().getBlock());
    }

    public static void saveCrates() {
        crates.forEach(databaseManager::saveCrate);
    }

    public static String nextCrateName() {
        return "crate_" + (crates.size());
    }

    public static Couple<Boolean, Crate> getCrateAtIf(final Block block) {
        for (Block b : cratesBlock) {
            if (!b.getLocation().equals(block.getLocation()))
                continue;
            for (Crate c : crates) {
                if (c.getLocation().getBlock().equals(b))
                    return new Couple<>(true, c);
            }
        }
        return new Couple<>(false, null);
    }

    public static ItemStack getCratesKey() {
        return CrateEventListener.KEY;
    }

    public static void openCrate(Player player, Crate crate, boolean mainHand) {
        final List<ItemStack> rewards = crate.getPossibleRewards();
        if(rewards.isEmpty()){
            player.sendMessage("§4Sadly the rewards are empty for now.");
            return;
        }
        if(mainHand)
            player.getInventory().setItemInMainHand(null);
        else player.getInventory().setItemInOffHand(null);
        Inventory inventory = Bukkit.createInventory(player, 27,
                Component.text(CrateSystem.getConfiguration().getString("crate-inventory-name")));
        final ItemStack none = new ItemStack(Material.GRAY_STAINED_GLASS);
        ItemMeta noneMeta = none.getItemMeta();
        noneMeta.setDisplayName("");
        none.setItemMeta(noneMeta);
        for (int i = 0; i <= 8; i++) {
            if (i == 4) {
                inventory.setItem(i, new ItemStack(Material.HOPPER));
            } else {
                inventory.setItem(i, none);
            }
        }
        for (int i = 20; i <= 27; i++) {
            inventory.setItem(i, none);
        }
        inventory.setItem(9, none);
        inventory.setItem(19, none);


        int maxTicks = new Random().nextInt(40, 120);
        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                tick++;
                int index = (tick / 5) % rewards.size();
                for (int i = 9; i <= 17; i++) {
                    inventory.setItem(i, rewards.get(index));
                }
                if (tick >= maxTicks) {
                    cancel();
                    ItemStack finalReward = crate.getNextReward();
                    for (int i = 9; i <= 17; i++) {
                        inventory.setItem(i, finalReward);
                    }
                    player.getInventory().addItem(finalReward);
                }
            }
        }.runTaskTimer(CrateSystem.getInstance(), 0L, 1L);

    }

    private static final Map<Player, Crate> editingCrates = new HashMap<>();
    public static void openCrateEditor(Player player, Crate crate) {
        Inventory inventory = Bukkit.createInventory(player, 27,
                Component.text(CrateSystem.getConfiguration().getString("crate-edit-inventory-name")));
        for(ItemStack item : crate.getPossibleRewards()){
            inventory.addItem(item);
        }
        player.openInventory(inventory);
        editingCrates.put(player, crate);
    }

    public static void saveCrateEditor(Player player, Crate crate, Inventory inventory){
        crate.setRewards(inventory);
        player.closeInventory();
        player.sendMessage("§aSuccessfully saved the crate!");
    }

    public static @Nullable Crate getEditingCrate(Player player){
        return editingCrates.getOrDefault(player, null);
    }
}