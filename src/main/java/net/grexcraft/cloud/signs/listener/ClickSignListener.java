package net.grexcraft.cloud.signs.listener;

import net.grexcraft.cloud.signs.CloudSignsPlugin;
import net.grexcraft.cloud.signs.client.CloudWebClient;
import net.grexcraft.cloud.signs.dto.ImageDto;
import net.grexcraft.cloud.signs.dto.PoolSlotDto;
import net.grexcraft.cloud.signs.enums.ServerState;
import net.grexcraft.cloud.signs.model.SignData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ClickSignListener implements Listener {

    CloudSignsPlugin plugin;

    public ClickSignListener(CloudSignsPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event == null) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getState() == null) return;
        if (event.getClickedBlock().getState() instanceof Sign) {

            List<SignData> signs = plugin.getWorker().getSignDataCopy();

            PoolSlotDto slot = null;
            SignData signData = null;
            boolean wasClicked = false;
            for (SignData sign : signs) {
                if (sign.getLocation().equals(event.getClickedBlock().getLocation())) {
                    wasClicked = sign.isClicked();
                    sign.setClicked(true);
                    slot = CloudWebClient.getPoolSlotByName(sign.getSlot());
                    signData = sign;
                    break;
                }
            }

            if (signData == null) return;
            if (slot == null) return;

            if (slot.getServer() == null) {
                if (wasClicked) {
                    event.getPlayer().spigot().sendMessage(
                            new ComponentBuilder("Server is already starting...").color(ChatColor.GRAY).create()
                    );
                    return;
                }
                // block clicking for 4 seconds
                SignData finalSignData = signData;
                Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> finalSignData.setClicked(false), 4*20L);

                event.getPlayer().spigot().sendMessage(
                        new ComponentBuilder("No server available, a server will be started\nThis may take a few moments.").color(ChatColor.GRAY).create()
                );

                ImageDto image = CloudWebClient.getImageForPool(slot.getPool().getName());

                if (image == null) return;

                plugin.getLogger().info("requesting server start for " + image.getName() + ":" + image.getTag());

                CloudWebClient.createServerRequest(image);

            } else {
                if (slot.getServer().getState().equals(ServerState.RUNNING))
                    sendPlayer(event.getPlayer(), slot.getServer().getName());
            }
        }
    }

    private void sendPlayer(Player player, String server) {
        player.spigot().sendMessage(
                new ComponentBuilder("Connection player to server...").color(ChatColor.GRAY).create()
        );

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException eee) {
            Bukkit.getLogger().info("You'll never see me!");
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }
}
