package net.grexcraft.cloud_signs.command;

import net.grexcraft.cloud_signs.CloudSignsPlugin;
import net.grexcraft.cloud_signs.model.SignData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class FixSignsCommand implements CommandExecutor {
    CloudSignsPlugin plugin;

    public FixSignsCommand(CloudSignsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (SignData sign :
                plugin.getWorker().getSignDataCopy()) {
            sign.setClicked(false);
        }
        return true;
    }
}
