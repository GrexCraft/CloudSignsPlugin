package net.grexcraft.cloud_signs.command;

import net.grexcraft.cloud_signs.client.CloudWebClient;
import net.grexcraft.cloud_signs.dto.PoolSlotDto;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlotInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length != 1) {
            throw new IllegalArgumentException("missing arguments");
        }

        String name = strings[0];

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PoolSlotDto poolSlot = CloudWebClient.getPoolSlotByName(name);

            if (poolSlot == null) return false;

            if (poolSlot.getServer() == null) {
                player.spigot().sendMessage(
                        new ComponentBuilder("Pool Slot Info: \n").color(ChatColor.GOLD)
                                .append("no server").color(ChatColor.RED)
                                .create()
                );
            } else {
                player.spigot().sendMessage(
                        new ComponentBuilder("Pool Slot Info: \n").color(ChatColor.GOLD)
                                .append("Name: ").color(ChatColor.GRAY)
                                .append(poolSlot.getServer().getName() + "\n").color(ChatColor.DARK_AQUA)
                                .append("State: ").color(ChatColor.GRAY)
                                .append(poolSlot.getServer().getState() + "\n").color(ChatColor.YELLOW)
                                .append("Image: ").color(ChatColor.GRAY)
                                .append(poolSlot.getServer().getImage().getName() + ":" + poolSlot.getServer().getImage().getTag() + "\n").color(ChatColor.YELLOW)
                                .create()
                );
            }
        }

        return true;
    }
}
