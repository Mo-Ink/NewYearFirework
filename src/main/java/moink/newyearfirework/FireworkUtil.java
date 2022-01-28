package moink.newyearfirework;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class FireworkUtil {

    private static boolean isWorking = false;

    public static void start(FileConfiguration config) {
        isWorking = true;
        List<Map<?, ?>> locations = config.getMapList("locations");
        int period = config.getInt("period");
        work(locations, period);
    }

    public static void stop() {
        isWorking = false;
    }

    private static void work(List<Map<?, ?>> locations, int period) {
        NewYearFirework instance = NewYearFirework.getInstance();
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, () -> {
            for (Map<?, ?> map : locations) {
                if (!isWorking) return;
                String worldName = (String) map.get("world");
                List<Double> pos = (List<Double>) map.get("pos");
                double x = pos.get(0);
                double y = pos.get(1);
                double z = pos.get(2);

                FireworkEffect.Builder fb = FireworkEffect.builder();
                Random r = new Random();

                //随机颜色
                fb.withColor(
                        Color.fromRGB(r.nextInt(156) + 100, r.nextInt(156) + 100, r.nextInt(156) + 100),
                        Color.fromRGB(r.nextInt(136) + 120, r.nextInt(136) + 120, r.nextInt(136) + 120),
                        Color.fromRGB(r.nextInt(116) + 140, r.nextInt(116) + 140, r.nextInt(116) + 140),
                        Color.fromRGB(r.nextInt(96) + 160, r.nextInt(96) + 160, r.nextInt(96) + 160)
                );
                fb.withFade(
                        Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
                        Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255))
                );

                //随机形状
                FireworkEffect.Type[] type = FireworkEffect.Type.values();
                fb.with(type[r.nextInt(type.length)]);

                //随机效果
                int t = r.nextInt(64);
                if (t % 2 == 0) {
                    fb.withFlicker();
                }
                if (t % 3 == 0 || t % 13 == 0) {
                    fb.withTrail();
                }

                FireworkEffect f = fb.build();

                Bukkit.getScheduler().runTask(instance, () -> {
                    World world = Bukkit.getWorld(worldName);
                    Location location = new Location(world, x, y, z);
                    Firework fw = (Firework) world.spawnEntity(location, EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    fwm.clearEffects();
                    fwm.addEffect(f);
                    fwm.setPower(r.nextInt(3) + 2);
                    fw.setFireworkMeta(fwm);
                });
            }
        }, 1, period);
    }

    public static boolean getStatus() {
        return isWorking;
    }
}
