package moink.newyearfirework;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewYearFirework extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getConfig().options().copyDefaults();
        getCommand("newyearfirework").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        saveConfig();
        Bukkit.getScheduler().cancelTasks(this);
    }
}
