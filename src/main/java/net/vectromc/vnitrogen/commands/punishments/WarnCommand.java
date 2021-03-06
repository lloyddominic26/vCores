package net.vectromc.vnitrogen.commands.punishments;

import net.vectromc.vnitrogen.management.PunishmentManagement;
import net.vectromc.vnitrogen.utils.Utils;
import net.vectromc.vnitrogen.vNitrogen;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

    private vNitrogen plugin;
    private Boolean silent;

    public WarnCommand() {
        plugin = vNitrogen.getPlugin(vNitrogen.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getConfig().getString("Warn.Permission"))) {
            Utils.sendMessage(sender, plugin.getConfig().getString("NoPermission").replaceAll("%server_prefix%", plugin.getConfig().getString("ServerPrefix")).replaceAll("%plugin_prefix%", plugin.getConfig().getString("PluginPrefix")));
        } else {
            String consoleName = plugin.getConfig().getString("Console.name");
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0 || args.length == 1) {
                    Utils.sendMessage(player, plugin.getConfig().getString("Warn.IncorrectUsage").replaceAll("%server_prefix%", plugin.getConfig().getString("ServerPrefix")).replaceAll("%plugin_prefix%", plugin.getConfig().getString("PluginPrefix")));
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        Utils.sendMessage(player, plugin.getConfig().getString("Warn.InvalidPlayer").replaceAll("%server_prefix%", plugin.getConfig().getString("ServerPrefix")).replaceAll("%plugin_prefix%", plugin.getConfig().getString("PluginPrefix")));
                    } else {
                        plugin.setPlayerColor(player);
                        plugin.setPlayerColor(target);
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + " " + args[i];
                        }
                        if (reason.contains("-s")) {
                            reason = reason.replaceFirst(" -s", "");
                            this.silent = true;
                        } else {
                            this.silent = false;
                        }
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            if (!silent) {
                                onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Warn.GlobalMessage").replaceAll("%executor%", player.getDisplayName()).replaceAll("%target%", target.getDisplayName()).replaceAll("%reason%", reason)));
                            } else {
                                if (onlinePlayers.hasPermission(plugin.getConfig().getString("Silent.Notify"))) {
                                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Silent.Prefix") + " " + plugin.getConfig().getString("Warn.GlobalMessage").replaceAll("%executor%", player.getDisplayName()).replaceAll("%target%", target.getDisplayName()).replaceAll("%reason%", reason)));
                                }
                            }
                        }
                        if (!silent) {
                            Utils.sendMessage(player, plugin.getConfig().getString("Warn.ExecutorResponse").replaceAll("%player%", target.getDisplayName()).replaceAll("%reason%", reason));
                        } else {
                            Utils.sendMessage(player, plugin.getConfig().getString("Silent.Prefix") + " " + plugin.getConfig().getString("Warn.ExecutorResponse").replaceAll("%player%", target.getDisplayName()).replaceAll("%reason%", reason));
                        }
                        Utils.sendMessage(target, plugin.getConfig().getString("Warn.TargetResponse").replaceAll("%reason%", reason).replaceAll("%executor%", player.getDisplayName()));

                        PunishmentManagement punishmentManagement = new PunishmentManagement(target);
                        int id = plugin.data.config.getInt(target.getUniqueId().toString() + ".WarnsAmount") + 1;
                        punishmentManagement.addWarn();
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Executor", player.getUniqueId().toString());
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Reason", reason);
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Silent", silent.toString());
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Server", player.getWorld().getName());
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Date", System.currentTimeMillis());
                        plugin.data.saveData();
                    }
                }
            } else {
                if (args.length == 0 || args.length == 1) {
                    Utils.sendMessage(sender, plugin.getConfig().getString("Warn.IncorrectUsage").replaceAll("%server_prefix%", plugin.getConfig().getString("ServerPrefix")).replaceAll("%plugin_prefix%", plugin.getConfig().getString("PluginPrefix")));
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        Utils.sendMessage(sender, plugin.getConfig().getString("Warn.InvalidPlayer").replaceAll("%server_prefix%", plugin.getConfig().getString("ServerPrefix")).replaceAll("%plugin_prefix%", plugin.getConfig().getString("PluginPrefix")));
                    } else {
                        plugin.setPlayerColor(target);
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + " " + args[i];
                        }
                        if (reason.contains("-s")) {
                            reason = reason.replaceFirst(" -s", "");
                            this.silent = true;
                        } else {
                            this.silent = false;
                        }
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            if (!silent) {
                                onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Warn.GlobalMessage").replaceAll("%executor%", consoleName).replaceAll("%target%", target.getDisplayName()).replaceAll("%reason%", reason)));
                            } else {
                                if (onlinePlayers.hasPermission(plugin.getConfig().getString("Silent.Notify"))) {
                                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Silent.Prefix") + " " + plugin.getConfig().getString("Warn.GlobalMessage").replaceAll("%executor%", consoleName).replaceAll("%target%", target.getDisplayName()).replaceAll("%reason%", reason)));
                                }
                            }
                        }
                        if (!silent) {
                            Utils.sendMessage(sender, plugin.getConfig().getString("Warn.ExecutorResponse").replaceAll("%player%", target.getDisplayName()).replaceAll("%reason%", reason));
                        } else {
                            Utils.sendMessage(sender, plugin.getConfig().getString("Silent.Prefix") + " " + plugin.getConfig().getString("Warn.ExecutorResponse").replaceAll("%player%", target.getDisplayName()).replaceAll("%reason%", reason));
                        }
                        Utils.sendMessage(target, plugin.getConfig().getString("Warn.TargetResponse").replaceAll("%reason%", reason).replaceAll("%executor%", consoleName));

                        PunishmentManagement punishmentManagement = new PunishmentManagement(target);
                        int id = plugin.data.config.getInt(target.getUniqueId().toString() + ".WarnsAmount") + 1;
                        punishmentManagement.addWarn();
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Executor", "Console");
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Reason", reason);
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Silent", silent.toString());
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Server", target.getWorld().getName());
                        plugin.data.config.set(target.getUniqueId() + ".Warns." + id + ".Date", System.currentTimeMillis());
                        plugin.data.saveData();
                    }
                }
            }
        }
        return true;
    }
}
