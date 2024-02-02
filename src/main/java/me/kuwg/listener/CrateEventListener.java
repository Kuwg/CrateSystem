package me.kuwg.listener;

import me.kuwg.CrateSystem;
import me.kuwg.crate.Crate;
import me.kuwg.crate.CrateManager;
import me.kuwg.util.Couple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;


@SuppressWarnings("deprecation") // for some reason 1.20.4 / paper have all default methods deprecated
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
        if(clickedBlock==null)return;
        // if(event.getAction().isLeftClick()||event.getAction().isRightClick()) return;
        // remove because only exists on paper and not spigot.
        final Couple<Boolean, Crate> crateCouple = CrateManager.getCrateAtIf(clickedBlock);
        if(!crateCouple.getX())return;
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            if(isKey(player.getInventory().getItemInMainHand())){
                CrateManager.openCrate(player, crateCouple.getY(), true);
            }
            else if(isKey(player.getInventory().getItemInOffHand())){
                CrateManager.openCrate(player, crateCouple.getY(),  false);
            }
            event.setCancelled(true);
        }
        else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(!player.hasPermission("cratesystem.edit")){
                player.sendMessage(CrateSystem.getConfiguration().getPrefix() + CrateSystem.getConfiguration().noPermission());
                return;
            }
            event.setCancelled(true);
            CrateManager.openCrateEditor(player, crateCouple.getY());
        }
    }

    /*
    STACK OVERFLOW ERROR:
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        final Player player = (Player) event.getPlayer();
        if(event.getView().getTitle().equals(CrateSystem.getConfiguration().getString("crate-inventory-name"))){
            player.openInventory(event.getInventory());
        }
        else if(event.getView().getTitle().equals(CrateSystem.getConfiguration().getString("crate-edit-inventory-name"))){
            Crate crate = CrateManager.getEditingCrate(player);
            if(crate==null)return;
            CrateManager.saveCrateEditor(player, crate, event.getInventory());
        }
    }
    */

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(CrateSystem.getConfiguration().getString("crate-inventory-name"))) {
            event.setCancelled(true);
        } else if (event.getView().getTitle().equals(CrateSystem.getConfiguration().getString("crate-edit-inventory-name"))) {
            if (event.getClickedInventory() != event.getView().getTopInventory()) {
                event.setCancelled(false);
                return;
            }
            Player player = (Player) event.getWhoClicked();

            if (event.getClickedInventory() != null && CrateManager.isEditing(player)) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null &&
                        clickedItem.getItemMeta().getDisplayName().equals(
                                "§aClick to confirm!"
                        )) {
                    event.getClickedInventory().setItem(26, null);
                    Crate crate = CrateManager.getEditingCrate(player);
                    assert crate != null:"how";
                    CrateManager.saveCrateEditor(player, crate, event.getClickedInventory());
                    player.closeInventory();
                    event.setCancelled(true);
                }
            }
        }
    }



    private static boolean isKey(final ItemStack itemStack){
        return itemStack.getItemMeta()!=null &&
                itemStack.getItemMeta().getDisplayName().equals(KEY.getItemMeta().getDisplayName())&&
                itemStack.getType().equals(KEY.getType())&&
                Objects.equals(itemStack.getItemMeta().getLore(), KEY.getItemMeta().getLore()) &&
                itemStack.getEnchantments()==KEY.getEnchantments();
    }
}
