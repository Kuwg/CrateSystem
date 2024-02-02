package me.kuwg.commands;

import me.kuwg.CrateSystem;
import me.kuwg.crate.Crate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CrateSystemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(!sender.hasPermission("cratesystem.command")){
            sender.sendMessage(CrateSystem.getConfiguration().noPermission());
            return true;
        }
        if(!(sender instanceof Player)){
            sender.sendMessage("§4You cannot do this. Only players can do actions with crates.");
            return true;
        }
        if(args.length==0) {
            sender.sendMessage(CrateSystem.getConfiguration().getPrefix() + "Usage: /CrateSystem <help|create|delete>.");
            return true;
        }
        Player player = (Player) sender;
        final String command = args[0];
        switch (command){
            case "help":
                player.sendMessage(CrateSystem.getConfiguration().getPrefix() + "§eHelp Page: \n");
                break;
            case "create":
                Material block = Material.CHEST;
                if(args.length>1) {
                    switch (args[1].toLowerCase()){
                        case "chest":
                            break;
                        case "trapped_chest":
                            block=Material.TRAPPED_CHEST;break;
                        case "shulker":
                        case "shulker_box":
                            block=Material.SHULKER_BOX;
                            break;
                        case "end_chest":
                        case "end":
                        case "ender_chest":
                            block=Material.ENDER_CHEST;
                            break;
                        default:
                            player.sendMessage("§4Unknown or invalid block type for crates: " + args[1] +
                                    ", please use a chest-type block.");
                            return true;
                    }
                }

                player.getLocation().getBlock().setType(block);
                player.sendMessage("§aSuccessfully created a new crate! Right Click on it to set rewards!");
                Crate crate = new Crate(player.getLocation());

                break;
            default:
                player.sendMessage(CrateSystem.getConfiguration().getPrefix() + "§6Invalid command. Use \"/CrateSystem help\" for help.");

        }
        return true;
    }
}
