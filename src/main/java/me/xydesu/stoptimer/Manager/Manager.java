package me.xydesu.stoptimer.Manager;

import github.scarsz.discordsrv.DiscordSRV;
import me.xydesu.stoptimer.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Manager {

    private final Plugin plugin;
    private BukkitRunnable task;
    private long endTimeMillis = -1;
    private long durationSeconds = -1;
    private final MessageManager message;
    private final BossbarManager bossbarManager;
    private final ConfigManager config;

    public Manager(Main plugin, MessageManager messageManager, ConfigManager config) {
        this.plugin = plugin;
        this.message = messageManager;
        this.bossbarManager = new BossbarManager(messageManager, this);
        this.config = config;
    }

    public long getTimeLeft() {
        if (endTimeMillis < 0) return -1;
        long left = (long) Math.ceil((endTimeMillis - System.currentTimeMillis()) / 1000.0);
        return Math.max(0, left);
    }

    public long getTimeMax() {
        return durationSeconds;
    }

    public long parseTime(String input) {
        try {
            char unit = input.charAt(input.length() - 1);
            long value = Long.parseLong(input.substring(0, input.length() - 1));
            switch (unit) {
                case 's': return value;
                case 'm': return value * 60;
                case 'h': return value * 3600;
                default: return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static String colorToAnsi(String msg) {
        if (msg == null) return "";
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg
                .replace("§0", "\u001B[30m")
                .replace("§1", "\u001B[34m")
                .replace("§2", "\u001B[32m")
                .replace("§3", "\u001B[36m")
                .replace("§4", "\u001B[31m")
                .replace("§5", "\u001B[35m")
                .replace("§6", "\u001B[33m")
                .replace("§7", "\u001B[37m")
                .replace("§8", "\u001B[90m")
                .replace("§9", "\u001B[94m")
                .replace("§a", "\u001B[92m")
                .replace("§b", "\u001B[96m")
                .replace("§c", "\u001B[91m")
                .replace("§d", "\u001B[95m")
                .replace("§e", "\u001B[93m")
                .replace("§f", "\u001B[97m")
                .replace("§l", "\u001B[1m")
                .replace("§n", "\u001B[4m")
                .replace("§o", "\u001B[3m")
                .replace("§r", "\u001B[0m")
                + "\u001B[0m";
    }

    public void startCountdown(long seconds) {
        if (getTimeLeft() > 0) {
            return;
        }
        durationSeconds = seconds;
        endTimeMillis = System.currentTimeMillis() + seconds * 1000;

        if (config.getBossbarEnabled()) {
            bossbarManager.createBossbar();
        }

        boolean titleFirstRun = config.getTitleFirstRun();
        boolean messageFirstRun = config.getMessageFirstRun();
        boolean discordFirstRun = config.getDiscordFirstRun();
        java.util.List<Integer> titleSeconds = config.getTitleSeconds();
        java.util.List<Integer> messageSeconds = config.getMessageSeconds();
        java.util.List<Integer> discordSeconds = config.getDiscordSeconds();

        task = new BukkitRunnable() {
            boolean firstRun = true;
            long lastTimeLeft = -1;

            @Override
            public void run() {
                long timeLeft = getTimeLeft();

                if (config.getBossbarEnabled()) {
                    bossbarManager.updateBossbar();
                    bossbarManager.showBossbar();
                }

                if (timeLeft != lastTimeLeft || firstRun) {
                    // Title notification
                    if ((firstRun && titleFirstRun) || titleSeconds.contains((int) timeLeft)) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendTitle(message.getTitle(), message.getSubtitle(timeLeft), 10, 70, 20);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        });
                    }
                    // Chat message notification
                    if ((firstRun && messageFirstRun) || messageSeconds.contains((int) timeLeft)) {
                        List<String> notifyMsg = message.getMessage(timeLeft);
                        Bukkit.getOnlinePlayers().forEach(player -> notifyMsg.forEach(player::sendMessage));
                        notifyMsg.forEach(line -> plugin.getLogger().info("[StopTimer] " + colorToAnsi(line)));
                    }
                    // Discord notification
                    if ((firstRun && discordFirstRun) || discordSeconds.contains((int) timeLeft)) {
                        try {
                            DiscordSRV.getPlugin().getMainTextChannel().sendMessage(message.getDiscordMessage(timeLeft)).queue();
                        } catch (Exception ex) {
                            plugin.getLogger().warning("Failed to send Discord message: " + ex.getMessage());
                        }
                    }
                    firstRun = false;
                    lastTimeLeft = timeLeft;
                }

                if (timeLeft <= 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(message.getKickMessage()));
                    cancel();
                    durationSeconds = -1;
                    endTimeMillis = -1;
                    if (config.getBossbarEnabled()) {
                        bossbarManager.hideBossbar();
                        bossbarManager.removeBossbar();
                    }
                    Bukkit.shutdown();
                }
            }
        };

        task.runTaskTimer(plugin, 0, 1);
    }

    public boolean cancelCountdown() {
        if (getTimeLeft() <= 0 || task == null) return false;
        task.cancel();
        if (config.getBossbarEnabled()) {
            bossbarManager.hideBossbar();
            bossbarManager.removeBossbar();
        }
        durationSeconds = -1;
        endTimeMillis = -1;
        Bukkit.getOnlinePlayers().forEach(player -> {
            message.getNotifyCancel().forEach(player::sendMessage);
        });
        try {
            DiscordSRV.getPlugin().getMainTextChannel().sendMessage(message.getDiscordCancel()).queue();
        } catch (Exception ignored) {}
        return true;
    }

    public void shutdown() {
        cancelCountdown();
        bossbarManager.removeBossbar();
    }

}