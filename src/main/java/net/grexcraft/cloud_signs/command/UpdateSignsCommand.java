package net.grexcraft.cloud_signs.command;

import net.grexcraft.cloud_signs.worker.GeneralWorker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UpdateSignsCommand implements CommandExecutor {
    GeneralWorker worker;

    public UpdateSignsCommand(GeneralWorker worker) {
        this.worker = worker;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        System.out.println("manually run update sign");
        worker.updateSigns();
        return true;
    }
}
