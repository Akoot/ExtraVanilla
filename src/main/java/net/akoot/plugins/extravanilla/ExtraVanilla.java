package net.akoot.plugins.extravanilla;

import net.akoot.plugins.extravanilla.commands.*;
import net.akoot.plugins.extravanilla.reference.ExtraPaths;
import net.akoot.plugins.extravanilla.serializable.Title;
import net.akoot.plugins.ultravanilla.Config;
import net.akoot.plugins.ultravanilla.UltraPlugin;
import net.akoot.plugins.ultravanilla.reference.Palette;
import net.akoot.plugins.ultravanilla.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public final class ExtraVanilla extends UltraPlugin {

    private static Config titles;
    private static Config config;

    private String motd;

    public String getMotd() {
        return motd;
    }

    public static Config getTitles() {
        return titles;
    }

    public static Config config() {
        return config;
    }

    @Override
    public void start() {

        // Register serializable classes
        serialize(Title.class, "Title");

        // Register config
        config = new Config(this, getClass(), "config.yml");

        // Set the MOTD
        String motd = StringUtil.pickRandom(getConfig().getStringList(ExtraPaths.Config.MOTD_LIST));
        String serverName = getConfig().getString(ExtraPaths.Config.SERVER_NAME);
        String version = ChatColor.valueOf(getConfig().getString(ExtraPaths.Config.VERSION_COLOR)) + getServer().getVersion();
        version = version.substring(version.indexOf("MC: ") + 4, version.length() - 1);
        String motdString = getConfig().getString(ExtraPaths.Config.MOTD_FORMAT).replace("%n", serverName);
        this.motd = Palette.translate(motdString.replaceAll("%n", serverName).replaceAll("%v", version).replaceAll("%m", motd));

        // Register titles
        titles = new Config(this, getClass(), "titles.yml");
        Titles.setTitles((List<Title>) titles.getConfig().getList(ExtraPaths.Titles.ROOT, new ArrayList<>()));

        // Register configs
        registerConfig(titles);

        // Register /extravanilla command
        registerCommand("extravanilla", new ExtravanillaCommand(this));

        // Register /titles command
        registerCommand("title", new TitleCommand(this));

        // Register /afk command
        AfkCommand afkCommand = new AfkCommand(this);
        registerCommand("afk", afkCommand);
        registerEvents(afkCommand);

        // Register /alias command
        registerCommand("alias", new AliasCommand(this));

        // Register /info command
        registerCommand("info", new InfoCommand(this));

        // Register /home, /homes, /delhome and /sethome
        HomeCommand homeCommand = new HomeCommand(this);
        registerCommand("home", homeCommand);
        registerCommand("homes", homeCommand);
        registerCommand("delhome", homeCommand);
        registerCommand("sethome", homeCommand);
        registerCommand("homeof", homeCommand);

        // Register /spawn
        SpawnCommand spawnCommand = new SpawnCommand(this);
        registerCommand("spawn", spawnCommand);
        registerCommand("setspawn", spawnCommand);

        // Register /lag
        registerCommand("lag", new LagCommand(this));

        // Register /nickname
        NicknameCommand nicknameCommand = new NicknameCommand(this);
        registerCommand("nickname", nicknameCommand);
        registerCommand("namecolor", nicknameCommand);
        registerCommand("chatcolor", nicknameCommand);
        registerEvents(nicknameCommand);

        // Register /back
        BackCommand backCommand = new BackCommand(this);
        registerCommand("back", backCommand);
        registerEvents(backCommand);

        // Register events
        registerEvents(new EventListener(this));

    }

    @Override
    public void onDisable() {
    }
}
