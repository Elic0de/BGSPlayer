package com.github.elic0de.bgsplayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BGSPlayer extends JavaPlugin {

    public static BGSPlayer instance;
    private static File bgmDir;

    public void onLoad() {
        instance = this;
        bgmDir = new File(getDataFolder(), "bgs");
        if (!bgmDir.exists())
            bgmDir.mkdirs();
    }

    @Override
    public void onEnable() {
        getCommand("bgs").setExecutor((sender, command, label, args) -> {
            if (args.length != 0) {
                switch (args[0]) {
                    case "set":
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "プレイヤーのみ実行ができます");
                            return false;
                        }

                        if (args.length != 2) {
                            sender.sendMessage(ChatColor.RED + "引数が足りません");
                            return false;
                        }

                        if (BGSLoader.get(args[1]) == null) {
                            sender.sendMessage(ChatColor.RED + args[1] + "というbgsは見つかりません");
                            return false;
                        }

                        BGS bgs = BGSLoader.get(args[1]);
                        bgs.setLocation(((Player) sender).getLocation());
                        sender.sendMessage(ChatColor.GREEN + "locationを変更しました");

                        return true;
                    case "reload":
                        BGSLoader.reload(bgmDir);
                        sender.sendMessage(ChatColor.GREEN + "BGSを読み込みました");
                        return true;
                }
            }
            return false;
        });
        BGSLoader.reload(bgmDir);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (BGS bgs : BGSLoader.REGISTRY.values()) {
            bgs.cancel();
        }
    }

    public static BGSPlayer getInstance() {
        return instance;
    }
}
