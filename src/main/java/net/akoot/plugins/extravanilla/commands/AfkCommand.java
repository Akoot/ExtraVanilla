package net.akoot.plugins.extravanilla.commands;

import net.akoot.plugins.extravanilla.reference.ExtraPaths;
import net.akoot.plugins.ultravanilla.Strings;
import net.akoot.plugins.ultravanilla.Users;
import net.akoot.plugins.ultravanilla.commands.UltraCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AfkCommand extends UltraCommand implements CommandExecutor, TabExecutor, Listener {

    public AfkCommand(JavaPlugin instance, Strings strings) {
        super(instance, strings, ChatColor.GRAY);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {

                Player player = (Player) sender;
                toggleAFK(player);
            } else {
                sender.sendMessage(uvStrings.getString("error.player-only"));
            }
        } else if (args.length == 1) {
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player != null) {
                sender.sendMessage(query(player.getName(), isAFK(player)));
            } else {
                sender.sendMessage(uvStrings.getString("error.player-offline"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isAFK(player)) {
            setAFK(player, false);
        }
    }

    private boolean isAFK(Player player) {
        return Users.getUser(player).getBoolean(ExtraPaths.User.AFK, false);
    }

    private void toggleAFK(Player player) {
        setAFK(player, !isAFK(player));
    }

    private String query(String name, boolean afk) {
        String status = strings.getString("command.afk.query." + afk);
        return strings.getString("command.afk.query.message", "%p", name, "%s", status);
    }

    private void broadcast(String name, boolean afk) {
        String status = strings.getString("command.afk.broadcast." + afk);
        plugin.getServer().broadcastMessage(strings.getString("command.afk.broadcast.message", "%p", name, "%s", status));
    }

    private void setAFK(Player player, boolean afk) {
        Users.set(player, ExtraPaths.User.AFK, afk);
        broadcast(player.getName(), afk);
    }
}