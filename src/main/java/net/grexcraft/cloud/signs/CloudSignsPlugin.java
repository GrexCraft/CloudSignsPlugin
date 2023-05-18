package net.grexcraft.cloud.signs;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.grexcraft.cloud.signs.command.CreateSignCommand;
import net.grexcraft.cloud.signs.command.SlotInfoCommand;
import net.grexcraft.cloud.signs.command.UpdateSignsCommand;
import net.grexcraft.cloud.signs.listener.ClickSignListener;
import net.grexcraft.cloud.signs.model.SignData;
import net.grexcraft.cloud.signs.worker.GeneralWorker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CloudSignsPlugin extends JavaPlugin implements PluginMessageListener {

    public GeneralWorker worker = new GeneralWorker(this);

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(SignData.class);

        saveDefaultConfig();

        List<SignData> signs = new ArrayList<>();

        getConfig().getList("signs", Collections.emptyList()).forEach(obj -> {
            if (obj instanceof SignData) signs.add((SignData) obj);
        });
        getLogger().info("found config with " + signs.size() + " entries.");

        initCommands();
        registerListener();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, () -> worker.refresh(), 20L, 4*20L);
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, () -> worker.updateSigns(), 2*20L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    private void initCommands() {
        getCommand("slotinfo").setExecutor(new SlotInfoCommand());
        getCommand("createcloudsign").setExecutor(new CreateSignCommand(this));
        getCommand("updatesigns").setExecutor(new UpdateSignsCommand(worker));
    }

    private void registerListener() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ClickSignListener(this), this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF(); // Name of server, as given in the arguments
            int count = in.readInt();
            getLogger().info("received for server " + server + ": " + count);
            worker.setPlayerCount(server, count);
        }
    }

    public GeneralWorker getWorker() {
        return worker;
    }
}
