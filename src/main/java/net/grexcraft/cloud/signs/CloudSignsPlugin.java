package net.grexcraft.cloud.signs;

import org.bukkit.plugin.java.JavaPlugin;

public final class CloudSignsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        initCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initCommands() {

    }
}
