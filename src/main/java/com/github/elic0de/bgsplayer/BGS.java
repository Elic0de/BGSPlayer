package com.github.elic0de.bgsplayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class BGS {

    private final String name;
    private final String soundName;

    private final boolean stereoMode;
    private final int radius;
    private final int cooldown;

    private Location location;

    private final MemorySection section;

    private final int id;

    public BGS(String name, String soundName, boolean stereoMode, int radius, int cooldown, Location location, MemorySection section) {
        this.name = name;
        this.soundName = soundName;
        this.stereoMode = stereoMode;
        this.radius = radius;
        this.cooldown = cooldown;
        this.location = location;
        this.section = section;
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BGSPlayer.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                double distance = player.getLocation().distance(location);
                if (distance <= radius) {
                    float volume = 1 - ((float) distance / radius);
                    player.playSound(player.getLocation(), this.soundName, SoundCategory.MASTER, stereoMode ? 1F : volume, 1F);
                }
            }
        }, 20L * cooldown, 20L);
    }

    public void setLocation(Location location) {
        this.location = location;
        section.set("location", location);
    }
    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }
}