package net.grexcraft.cloud_signs.command;

import net.grexcraft.cloud_signs.model.SignData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class CreateSignCommand implements CommandExecutor {

    Plugin plugin;

    public CreateSignCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length != 2) {
            throw new IllegalArgumentException("missing arguments");
        }

        String title = strings[0];
        String name = strings[1];

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            Set<Material> materials = new HashSet<>();
            materials.add(Material.AIR);

            Block block = player.getTargetBlock(materials, 100);
            BlockState state = block.getState();

            if (state instanceof Sign) {
                Location location = state.getLocation();

                List<SignData> signs = new ArrayList<>();

                plugin.getConfig().getList("signs", Collections.emptyList()).forEach(obj -> {
                    if (obj instanceof SignData) signs.add((SignData) obj);
                });

                for (SignData sign : signs) {
                    if (sign.getLocation().equals(location)) {
                        return false;
                    }
                }
                signs.add(
                    new SignData(location, name, title, false)
                );

                plugin.getConfig().set("signs", signs);
                plugin.saveConfig();

                player.spigot().sendMessage(
                        new ComponentBuilder("Sign has successfully been saved for slot: ").color(ChatColor.GRAY)
                                .append(name).color(ChatColor.DARK_AQUA)
                                .create()
                );

            } else {
                player.spigot().sendMessage(
                        new ComponentBuilder("Invalid target block, please look at a sign!").color(ChatColor.RED).create()
                );
            }

            return true;
        }
        return false;
    }
}
