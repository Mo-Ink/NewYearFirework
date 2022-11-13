package io.github.sugarmgp.newyearfirework;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewYearFirework extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getCommand("newyearfirework").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        saveConfig();
        Bukkit.getScheduler().cancelTasks(this);
    }
}
