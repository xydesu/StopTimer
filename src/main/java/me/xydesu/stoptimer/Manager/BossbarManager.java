package me.xydesu.stoptimer.Manager;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;

public class BossbarManager {

    private BossBar bossbar;
    private final MessageManager message;
    private final Manager manager;

    public BossbarManager(MessageManager messageManager, Manager manager) {
        this.message = messageManager;
        this.manager = manager;
    }

    //create a bossbar when the countdown starts
    public void createBossbar() {
        NamespacedKey key = new NamespacedKey("stoptimer", "countdown");
        bossbar = Bukkit.createBossBar(key, message.getBossbarMessage(manager.getTimeLeft()), BarColor.RED, org.bukkit.boss.BarStyle.SOLID);
        double progress = 1.0;
        if (manager.getTimeMax() > 0) {
            progress = Math.max(0.0, Math.min(1.0, (double) manager.getTimeLeft() / manager.getTimeMax()));
        }
        bossbar.setProgress(progress);
    }

    public void updateBossbar() {
        bossbar.setTitle(message.getBossbarMessage(manager.getTimeLeft()));
        double progress = 1.0;
        if (manager.getTimeMax() > 0) {
            progress = Math.max(0.0, Math.min(1.0, (double) manager.getTimeLeft() / manager.getTimeMax()));
        }
        bossbar.setProgress(progress);
    }

    public void showBossbar() {
        Bukkit.getOnlinePlayers().forEach(bossbar::addPlayer);
    }

    public void hideBossbar() {
        Bukkit.getOnlinePlayers().forEach(bossbar::removePlayer);
    }

    public void removeBossbar() {
        if (bossbar != null) {
            Bukkit.removeBossBar(new NamespacedKey("stoptimer", "countdown"));
            bossbar = null;
        }
    }
}