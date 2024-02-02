package me.kuwg.crate;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crate {
    private final String name;
    private final List<ItemStack> possibleRewards;
    private final Location location;

    public Crate(String name, List<ItemStack> reward, World world, double x, double y, double z){
        this.name = name;
        this.possibleRewards = reward;
        this.location=new Location(world, x, y, z);
        final String block = location.getBlock().getType().toString().toLowerCase();
        if(block.contains("chest")||block.contains("shulker_box"))
            throw new IllegalArgumentException(
                    "Invalid parameter, block at coordinates (x, y, z) " + x + " " + y + " " + z +
                            " in world " + world.getName() +
                            " is type " + location.getBlock().getType().toString().toLowerCase().replaceAll("_", " ") +
                            " while a chest of any type (Chest, Ender Chest, Trapped Chest, Shulker Boxes) was expected."
            );
    }
    public Crate(String name, World world, double x, double y, double z){
        this(name, new ArrayList<>(), world, x, y, z);
    }
    public Crate(String name, Location location){
        this(name, location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
    public ItemStack getNextReward(){
        return possibleRewards.get(new Random().nextInt(0, possibleRewards.size()-1));
        /*
        Create a new random,
        get next int in bound 0 to max list size,
        and then get the index at that int
        */
    }

    public void addReward(ItemStack reward){
        possibleRewards.add(reward);
    }
    public void removeReward(ItemStack reward){
        possibleRewards.remove(reward);
    }

    public List<ItemStack> getPossibleRewards() {
        return possibleRewards;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
