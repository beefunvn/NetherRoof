package dev.tranducminh.netherroof;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

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
            getServer().getAsyncScheduler().runAtFixedRate(this, task -> {
                for (Player player : getServer().getOnlinePlayers()) {
                    player.getScheduler().execute(this, () -> {
                        if (player.getWorld().getEnvironment() == World.Environment.NETHER && player.getLocation().getY() > 127) {
                            int damageTicks = player.getTicksLived() % damageIntervalTicks;
                            double totalDamage = damagePerTick * Math.min(damageTicks, maxDamageTicks) / 20.0;
                            player.damage(totalDamage);
                        }
                    }, null,1L);
                }
            }, 1L, 50L, TimeUnit.MILLISECONDS);
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
                // Not implemented yet
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