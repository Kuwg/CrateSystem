package me.kuwg.listener;

import me.kuwg.CrateSystem;
import me.kuwg.crate.Crate;
import me.kuwg.crate.CrateManager;
import me.kuwg.util.Couple;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CrateEventListener implements Listener {
    public static ItemStack KEY;
    static {
        Material mat = Material.getMaterial(CrateSystem.getConfiguration().getKeyString("material"));
        if(mat==null)mat=Material.DIAMOND;
        KEY = new ItemStack(mat);
        ItemMeta meta = KEY.getItemMeta();
        meta.setUnbreakable(true);
        meta.setLore(CrateSystem.getConfiguration().getKeyList("lore"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if(CrateSystem.getConfiguration().getKeyBool("enchanted"))
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setDisplayName(CrateSystem.getConfiguration().getKeyString("name"));
    }
    public static void reload(){
        Material mat = Material.getMaterial(CrateSystem.getConfiguration().getKeyString("material"));
        if(mat==null)mat=Material.DIAMOND;
        KEY = new ItemStack(mat);
        ItemMeta meta = KEY.getItemMeta();
        meta.setUnbreakable(true);
        meta.setLore(CrateSystem.getConfiguration().getKeyList("lore"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if(CrateSystem.getConfiguration().getKeyBool("enchanted"))
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setDisplayName(CrateSystem.getConfiguration().getKeyString("name"));
    }













    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Block clickedBlock = event.getClickedBlock();
        final Couple<Boolean, Crate> crateCouple = CrateManager.getCrateAtIf(clickedBlock);
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) && crateCouple.getX()){
            if(isKey(event.getPlayer().getInventory().getItemInMainHand())){
                CrateManager.openCrate(event.getPlayer(), crateCouple.getY(), true);
            }
            else if(isKey(event.getPlayer().getInventory().getItemInOffHand())){
                CrateManager.openCrate(event.getPlayer(), crateCouple.getY(),  false);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if(event.getView().getTitle().equals(CrateSystem.getConfiguration().getString("crate-inventory-name"))){
            event.getPlayer().openInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getView().getTitle().equals(CrateSystem.getConfiguration().getString("crate-inventory-name")))
            event.setCancelled(true);

    }


    private static boolean isKey(final ItemStack itemStack){
        return itemStack.getItemMeta().getDisplayName().equals(KEY.getItemMeta().getDisplayName())&&
                itemStack.getType().equals(KEY.getType())&&
                itemStack.getLore().equals(KEY.getLore())&&
                itemStack.getEnchantments()==KEY.getEnchantments();
    }
}
