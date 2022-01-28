package moink.newyearfirework;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewYearFirework extends JavaPlugin {

    private static NewYearFirework INSTANCE;

    public static NewYearFirework getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
    }

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
