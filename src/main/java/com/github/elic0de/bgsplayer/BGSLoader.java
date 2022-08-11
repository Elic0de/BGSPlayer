package com.github.elic0de.bgsplayer;


import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;


public final class BGSLoader {
    private BGSLoader() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final HashMap<String, BGS> REGISTRY = Maps.newHashMap();

    public static void reload(File directory) {
        if (REGISTRY != null) {
            for (BGS bgs : BGSLoader.REGISTRY.values()) {
                bgs.cancel();
            }
        }
        REGISTRY.clear();
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null)
            return;
        Arrays.stream(files)
                .map(YamlConfiguration::loadConfiguration)
                .flatMap(config -> config.getKeys(false).stream().map(config::get).filter(MemorySection.class::isInstance).map(MemorySection.class::cast))

                .forEach(section -> {
                    String key = section.getName();
                    if (REGISTRY.containsKey(key)) {
                        BGSPlayer.getInstance().getLogger().warning("名前が重複したBGMが存在します: " + key);
                        return;
                    }
                    String soundName = section.getString("sound");
                    if (soundName == null) {
                        BGSPlayer.getInstance().getLogger().warning("サウンド名が指定されていません: " + key);
                        return;
                    }
                    final String[] locationParts = section.getString("location").split(",");
                    if (Bukkit.getWorld(locationParts[0]) == null) {
                        BGSPlayer.getInstance().getLogger().warning("ワールド名が指定されていません: " + key);
                        return;
                    }
                    int radius = section.getInt("radius");
                    int cooldown = section.getInt("cooldown");
                    boolean stereoMode = section.getBoolean("stereoMode");
                    /*Location location = new Location(Bukkit.getWorld(locationParts[0]), Integer.parseInt(locationParts[1]),
                            Integer.parseInt(locationParts[2]), Integer.parseInt(locationParts[3]));*/
                    BGS bgm = new BGS(key, soundName, stereoMode, Math.max(0, radius), Math.max(0, cooldown), section.getLocation("location"), section);
                    REGISTRY.put(key, bgm);
                });
    }

    @Nullable
    public static BGS get(String name) {
        return REGISTRY.get(name);
    }
}
