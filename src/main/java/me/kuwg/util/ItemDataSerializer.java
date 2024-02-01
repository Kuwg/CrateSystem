package me.kuwg.util;

import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemDataSerializer {
    public static String serializeEnchants(Map<Enchantment, Integer> enchantmentList){
        String s =  enchantmentList.keySet().stream().map(
                enchant -> enchant.getName() + "-" + enchantmentList.get(enchant) + ",").collect(Collectors.joining()
        ); // append EnchantName-EnchantLevel, for every enchant.ù
        return s.substring(0,s.length()-1);
    }
    public static String serializeLore(List<String> lore){
        return String.join(" ", lore.toArray(new String[0]));
    }
}
