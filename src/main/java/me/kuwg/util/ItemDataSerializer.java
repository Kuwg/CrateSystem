package me.kuwg.util;

import org.bukkit.enchantments.Enchantment;

import java.util.*;
import java.util.stream.Collectors;

public class ItemDataSerializer {
    public static String serializeEnchants(Map<Enchantment, Integer> enchantmentList){
        String s =  enchantmentList.keySet().stream().map(
                enchant -> enchant.getName() + "-" + enchantmentList.get(enchant) + ",").collect(Collectors.joining()
        ); // append EnchantName-EnchantLevel, for every enchant.
        return s.substring(0,s.length()-1);
    }
    public static String serializeLore(List<String> lore){
        return String.join(" ", lore.toArray(new String[0]));
    }
    public static Map<Enchantment, Integer> deserializeEnchants(String enchantList){
        String[] enchants = enchantList.split(" ");
        Map<Enchantment, Integer> enchantmentList = new HashMap<>();
        for(final String enchant : enchants){
            String[] data = enchant.split("-");
            String enchantName = data[0];
            int enchantLevel = Integer.parseInt(data[1]);
            enchantmentList.put(Enchantment.getByName(enchantName), enchantLevel);
        }
        return enchantmentList;
    }
    public static List<String> deserializeLore(String lore){
        return Arrays.asList(lore.split(" "));
    }
}
