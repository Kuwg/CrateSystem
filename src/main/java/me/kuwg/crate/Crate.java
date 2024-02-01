package me.kuwg.crate;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crate {
    private final List<ItemStack> possibleRewards;
    public Crate(List<ItemStack> reward){
        this.possibleRewards =reward;
    }
    public Crate(){
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
}
