package cn.balalals.liveboost;

import cn.balalals.liveboost.event.EntitySpawnEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class LiveBoost extends JavaPlugin {
    public static JavaPlugin PLUGIN;
    public static Logger LOGGER = null;
    @Override
    public void onEnable() {
        PLUGIN = this;
        Bukkit.getPluginCommand("liveboost").setExecutor(new LiveBoostCommandExecutor(this));
        Bukkit.getPluginManager().registerEvents(new EntitySpawnEventListener(), this);
        LOGGER = getLogger();
        LOGGER.info("LiveBoost 启动中");
        // 加载默认配置
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
