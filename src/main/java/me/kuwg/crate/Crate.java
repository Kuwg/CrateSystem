package me.kuwg.crate;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crate {
    private final List<ItemStack> possibleRewards;
    private final double x, y, z;
    public Crate(List<ItemStack> reward, double x, double y, double z){
        this.possibleRewards = reward;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Crate(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.possibleRewards =new ArrayList<>();
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

    public double getLocationX() {
        return x;
    }

    public double getLocationY() {
        return y;
    }

    public double getLocationZ() {
        return z;
    }
}
