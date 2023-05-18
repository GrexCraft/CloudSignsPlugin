package net.grexcraft.cloud_signs.worker;

import com.google.common.collect.Iterables;
import net.grexcraft.cloud_signs.client.CloudWebClient;
import net.grexcraft.cloud_signs.dto.PoolSlotDto;
import net.grexcraft.cloud_signs.enums.ServerState;
import net.grexcraft.cloud_signs.model.SignData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class GeneralWorker {

    private final Plugin plugin;

    private final List<SignData> signData = new ArrayList<>();
    private final Map<String, PoolSlotDto> slotData = new HashMap<>();
    private final Map<String, Integer> playerCount = new HashMap<>();

    public GeneralWorker(Plugin plugin) {
        this.plugin = plugin;
    }

    public void refresh() {
        refreshSignData();
        refreshSlotData();
        refreshPlayerCounts();
    }

    private void refreshSignData() {
        signData.clear();
        plugin.getConfig().getList("signs", Collections.emptyList()).forEach(obj -> {
            if (obj instanceof SignData) signData.add((SignData) obj);
        });
    }

    private void refreshSlotData() {
        for (SignData sign : signData) {
            slotData.put(sign.getSlot(), CloudWebClient.getPoolSlotByName(sign.getSlot()));
        }
    }

    private void refreshPlayerCounts() {
        for (SignData sign : signData) {
            PoolSlotDto slot = slotData.get(sign.getSlot());
            if (slot == null) continue;
            if (slot.getServer() == null) continue;

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("PlayerCount");
                out.writeUTF(slot.getServer().getName());
            } catch (IOException eee) {
                Bukkit.getLogger().info("You'll never see me!");
            }

            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player != null)
                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        }
    }

    public void setPlayerCount(String slot, int count) {
        playerCount.put(slot, count);
    }

    // run every second
    public void updateSigns() {

        List<SignData> signs = new ArrayList<>(signData);

        for (SignData sign : signs) {
            Block block = Bukkit.getWorld(sign.getLocation().getWorld().getName()).getBlockAt(sign.getLocation());
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign signBlock = (Sign) state;
                PoolSlotDto slot = slotData.get(sign.getSlot());
                if (slot == null) continue;

                signBlock.setLine(0, "-= " + ChatColor.BOLD + sign.getTitle() + ChatColor.RESET + " =-");

                if (slot.getServer() == null) {
                    signBlock.setLine(1, "[" + ChatColor.RED + "no server" + ChatColor.RESET + "]");
                    signBlock.setLine(3, "-");
                } else {
                    String s;
                    ServerState serverState = slot.getServer().getState();
                    switch (serverState) {
                        case RUNNING: s = ChatColor.GREEN + "online"; break;
                        case STOPPED: s = ChatColor.RED + "offline"; break;
                        case STARTING: s = ChatColor.YELLOW + "starting..."; break;
                        case REGISTERED: s = ChatColor.GRAY + "registered"; break;
                        case STOPPING: s = ChatColor.RED + "stopping..."; break;
                        default: s = ChatColor.GRAY + serverState.toString();
                    }
                    signBlock.setLine(1, "[" + s + ChatColor.RESET + "]");
                    if (playerCount.get(slot.getServer().getName()) != null)
                        signBlock.setLine(3, "Online: " + playerCount.get(slot.getServer().getName()));
                }

                signBlock.setLine(2, "");

                signBlock.update();

            } else {
                plugin.getLogger().warning("block at location " + sign.getLocation().getBlock().getLocation() + " is not of type sign");
            }
        }
    }

    public List<SignData> getSignDataCopy() {
        return new ArrayList<>(signData);
    }
}
