package me.kuwg.commands;

import me.kuwg.CrateSystem;
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
                break;
            default:
                player.sendMessage(CrateSystem.getConfiguration().getPrefix() + "§6Invalid command. Use \"/CrateSystem help\" for help.");

        }
        return true;
    }
}
