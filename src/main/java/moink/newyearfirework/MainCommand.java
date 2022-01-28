package moink.newyearfirework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Plugin plugin = NewYearFirework.getInstance();
        FileConfiguration config = plugin.getConfig();
        String messageFront = ChatColor.YELLOW + config.getString("message-front");
        if (!(commandSender.hasPermission("nyf.commands.use"))) {
            String messageNo = config.getString("message-no");
            commandSender.sendMessage(messageFront + ChatColor.RED + messageNo);
            return false;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "————————————————插件用法————————————");
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "/nyf add <名称> - 添加玩家位置为一个燃放点");
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "/nyf del <名称> - 删除一个燃放点");
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "/nyf start - 开始燃放烟花");
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "/nyf stop - 停止燃放烟花");
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "/nyf reload - 重载插件");
            commandSender.sendMessage(messageFront + ChatColor.AQUA + "—————————————————————————————————————");
        } else if (strings.length == 2) {
            String Message0 = strings[0];
            if (Message0.equals("add")) {
                if (!(commandSender instanceof Player)) {
                    String messageConsole = config.getString("message-console");
                    commandSender.sendMessage(ChatColor.YELLOW + messageFront + ChatColor.AQUA + messageConsole);
                    return false;
                }
                Location location = ((Player) commandSender).getLocation();
                String Message1 = strings[1].toLowerCase();
                List<Map<?, ?>> locations = config.getMapList("locations");
                int flag = checkName(locations, Message1);
                if (flag != -1) {
                    String messageRepeat = config.getString("message-repeat");
                    commandSender.sendMessage(messageFront + ChatColor.RED + messageRepeat);
                    return false;
                }
                locations.add(createPointConfig(Message1, location.getWorld().getName(), location.getX(), location.getY(), location.getZ()));
                config.set("locations", locations);
                plugin.saveConfig();
                String messageSuccess = config.getString("message-success");
                commandSender.sendMessage(messageFront + ChatColor.GREEN + messageSuccess + "添加" + Message1 + "燃放点");
            } else if (Message0.equals("del")) {
                String Message1 = strings[1].toLowerCase();
                List<Map<?, ?>> locations = config.getMapList("locations");
                int flag = checkName(locations, Message1);
                if (flag == -1) {
                    String messageNotfound = config.getString("message-notfound");
                    commandSender.sendMessage(messageFront + ChatColor.RED + messageNotfound);
                } else {
                    locations.remove(flag);
                    config.set("locations", locations);
                    plugin.saveConfig();
                    String messageSuccess = config.getString("message-success");
                    commandSender.sendMessage(messageFront + ChatColor.GREEN + messageSuccess + "删除" + Message1 + "燃放点");
                }
            } else {
                String messageError = config.getString("message-error");
                commandSender.sendMessage(messageFront + ChatColor.RED + messageError);
            }
        } else if (strings.length == 1) {
            String Message0 = strings[0];
            if (Message0.equals("start")) {
                if (FireworkUtil.getStatus()) {
                    String messageWorking = config.getString("message-working");
                    commandSender.sendMessage(messageFront + ChatColor.RED + messageWorking);
                    return false;
                }
                FireworkUtil.start(config);
                String messageSuccess = config.getString("message-success");
                commandSender.sendMessage(messageFront + ChatColor.GREEN + messageSuccess + "开始烟花燃放");
            } else if (Message0.equals("stop")) {
                if (!FireworkUtil.getStatus()) {
                    String messageNonworking = config.getString("message-nonworking");
                    commandSender.sendMessage(messageFront + ChatColor.RED + messageNonworking);
                    return false;
                }
                FireworkUtil.stop();
                String messageSuccess = config.getString("message-success");
                commandSender.sendMessage(messageFront + ChatColor.GREEN + messageSuccess + "停止烟花燃放");
            } else if (Message0.equals("list")) {
                List<Map<?, ?>> locations = config.getMapList("locations");
                if (locations.isEmpty()) {
                    String messageNotfound = config.getString("message-notfound");
                    commandSender.sendMessage(messageFront + ChatColor.RED + messageNotfound);
                    return false;
                }
                commandSender.sendMessage(messageFront + ChatColor.AQUA + "燃放点列表如下：");
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    for (Map<?, ?> map : locations) {
                        String name = (String) map.get("name");
                        String world = (String) map.get("world");
                        List<Double> pos = (List<Double>) map.get("pos");
                        double x = pos.get(0);
                        double y = pos.get(1);
                        double z = pos.get(2);
                        commandSender.sendMessage(messageFront + ChatColor.AQUA + name + " " + world + " " + x + " " + y + " " + z);
                    }
                });
            } else if (Message0.equals("reload")) {
                plugin.reloadConfig();
                String messageReload = config.getString("message-reload");
                commandSender.sendMessage(messageFront + ChatColor.GREEN + messageReload);
            } else {
                String messageError = config.getString("message-error");
                commandSender.sendMessage(messageFront + ChatColor.RED + messageError);
            }
        } else {
            String messageError = config.getString("message-error");
            commandSender.sendMessage(messageFront + ChatColor.RED + messageError);
        }
        return false;
    }

    private Map<String, Object> createPointConfig(String name, String world, double x, double y, double z) {
        HashMap<String, Object> locations = new HashMap<>();
        locations.put("name", name);
        locations.put("world", world);
        locations.put("pos", List.of(x, y, z));
        return locations;
    }

    private int checkName(List<Map<?, ?>> locations, String msg) {
        int num = -1;
        for (int i = 0; i < locations.size(); i++) {
            Map<?, ?> map = locations.get(i);
            String name = (String) map.get("name");
            if (name.equals(msg)) {
                num = i;
                break;
            }
        }
        return num;
    }
}
