package me.kuwg.util;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.*;
import java.util.stream.Collectors;

public class ItemDataSerializer {
    public static String serializeEnchants(Map<Enchantment, Integer> enchantmentList) {
        return enchantmentList.entrySet().stream()
                .map(entry -> entry.getKey().getKey().getKey() + "-" + entry.getValue())
                .collect(Collectors.joining(","));
    }

    public static String serializeLore(List<String> lore) {
        return lore==null?"":String.join(" ", lore);
    }

    public static Map<Enchantment, Integer> deserializeEnchants(String enchantList) {
        Map<Enchantment, Integer> enchantmentList = new HashMap<>();

        if (enchantList != null && !enchantList.isEmpty()) {
            String[] enchants = enchantList.split(",");
            for (String enchant : enchants) {
                String[] data = enchant.split("-");
                if (data.length == 2) {
                    String enchantName = data[0];
                    int enchantLevel;
                    try {
                        enchantLevel = Integer.parseInt(data[1]);
                        enchantmentList.put(Enchantment.getByKey(NamespacedKey.minecraft(enchantName)), enchantLevel);
                        // need to fix deprecated methods...
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return enchantmentList;
    }

    public static List<String> deserializeLore(String lore) {
        return lore==null?new ArrayList<>():Arrays.asList(lore.split(" "));
    }
}
