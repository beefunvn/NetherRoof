package dev.tranducminh.netherroof;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NetherRoof extends JavaPlugin implements Listener {

    private boolean enabled;
    private int damagePerTick = 1;
    private int damageIntervalTicks = 20;
    private int maxDamageTicks = 60;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        enabled = config.getBoolean("enabled", true);

        if (enabled) {
            getServer().getPluginManager().registerEvents(this, this);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getWorld().getEnvironment() == World.Environment.NETHER && player.getLocation().getY() > 127) {
                            int damageTicks = player.getTicksLived() % damageIntervalTicks;
                            double totalDamage = damagePerTick * Math.min(damageTicks, maxDamageTicks) / 20.0;
                            player.damage(totalDamage);
                        }
                    }
                }
            }.runTaskTimer(this, 0L, 1L);
        } else {
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!enabled) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == World.Environment.NETHER && player.getLocation().getY() > 127) {
            int damageTicks = player.getTicksLived() % damageIntervalTicks;
            if (damageTicks >= maxDamageTicks) {
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!enabled) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == World.Environment.NETHER && player.getLocation().getY() > 127) {
            event.setCancelled(true);
        }
    }
}