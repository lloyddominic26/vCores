package net.vectromc.vscoreboard.commands;

import net.vectromc.vscoreboard.utils.Utils;
import net.vectromc.vscoreboard.vScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ToggleScoreboardCommand implements CommandExecutor {

    private vScoreboard plugin;

    public ToggleScoreboardCommand() {
        plugin = vScoreboard.getPlugin(vScoreboard.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, plugin.getConfig().getString("MustBePlayer").replaceAll("%server_prefix%", plugin.getConfig().getString("ServerPrefix")).replaceAll("%plugin_prefix%", plugin.getConfig().getString("PluginPrefix")));
        } else {
            Player player = (Player) sender;
            if (plugin.tsb.contains(player.getUniqueId())) {
                plugin.tsb.remove(player.getUniqueId());
                Utils.sendMessage(player, plugin.getConfig().getString("Scoreboard.ToggleOnMessage"));
            } else {
                plugin.tsb.add(player.getUniqueId());
                Utils.sendMessage(player, plugin.getConfig().getString("Scoreboard.ToggleOffMessage"));
                new BukkitRunnable() {
                    public void run() {
                        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    }
                }.runTaskLater(plugin, 5);
            }
        }
        return true;
    }
}
