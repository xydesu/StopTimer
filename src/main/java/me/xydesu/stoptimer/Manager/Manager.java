package me.xydesu.stoptimer.Manager;

import github.scarsz.discordsrv.DiscordSRV;
import me.xydesu.stoptimer.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Manager {

    /** True when the server is running Folia (thread-per-region scheduler). */
    private static final boolean FOLIA;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        FOLIA = folia;
    }

    private final Plugin plugin;
    private Runnable taskCancelAction;
    private boolean tickFirstRun;
    private long tickLastTimeLeft;
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
        tickFirstRun = true;
        tickLastTimeLeft = -1;

        if (config.getBossbarEnabled()) {
            bossbarManager.createBossbar();
        }

        final boolean titleFirstRun = config.getTitleFirstRun();
        final boolean messageFirstRun = config.getMessageFirstRun();
        final boolean discordFirstRun = config.getDiscordFirstRun();
        final List<Integer> titleSeconds = config.getTitleSeconds();
        final List<Integer> messageSeconds = config.getMessageSeconds();
        final List<Integer> discordSeconds = config.getDiscordSeconds();

        if (FOLIA) {
            io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask =
                plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, t ->
                    tick(titleFirstRun, messageFirstRun, discordFirstRun,
                         titleSeconds, messageSeconds, discordSeconds),
                    1L, 1L);
            taskCancelAction = foliaTask::cancel;
        } else {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    tick(titleFirstRun, messageFirstRun, discordFirstRun,
                         titleSeconds, messageSeconds, discordSeconds);
                }
            };
            taskCancelAction = runnable.runTaskTimer(plugin, 0L, 1L)::cancel;
        }
    }

    private void tick(boolean titleFirstRun, boolean messageFirstRun, boolean discordFirstRun,
                      List<Integer> titleSeconds, List<Integer> messageSeconds,
                      List<Integer> discordSeconds) {
        long timeLeft = getTimeLeft();

        if (config.getBossbarEnabled()) {
            bossbarManager.updateBossbar();
            bossbarManager.showBossbar();
        }

        if (timeLeft != tickLastTimeLeft || tickFirstRun) {
            // Title notification
            if ((tickFirstRun && titleFirstRun) || titleSeconds.contains((int) timeLeft)) {
                final long t = timeLeft;
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    runForPlayer(player, new Runnable() {
                        @Override
                        public void run() {
                            player.sendTitle(message.getTitle(), message.getSubtitle(t), 10, 70, 20);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        }
                    });
                }
            }
            // Chat message notification
            if ((tickFirstRun && messageFirstRun) || messageSeconds.contains((int) timeLeft)) {
                final List<String> notifyMsg = message.getMessage(timeLeft);
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    runForPlayer(player, new Runnable() {
                        @Override
                        public void run() {
                            for (String line : notifyMsg) {
                                player.sendMessage(line);
                            }
                        }
                    });
                }
                for (String line : notifyMsg) {
                    plugin.getLogger().info("[StopTimer] " + colorToAnsi(line));
                }
            }
            // Discord notification
            if ((tickFirstRun && discordFirstRun) || discordSeconds.contains((int) timeLeft)) {
                try {
                    DiscordSRV.getPlugin().getMainTextChannel().sendMessage(message.getDiscordMessage(timeLeft)).queue();
                } catch (Exception ex) {
                    plugin.getLogger().warning("Failed to send Discord message: " + ex.getMessage());
                }
            }
            tickFirstRun = false;
            tickLastTimeLeft = timeLeft;
        }

        if (timeLeft <= 0) {
            final String kickMsg = message.getKickMessage();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                runForPlayer(player, new Runnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(kickMsg);
                    }
                });
            }
            stopTask();
            durationSeconds = -1;
            endTimeMillis = -1;
            if (config.getBossbarEnabled()) {
                bossbarManager.hideBossbar();
                bossbarManager.removeBossbar();
            }
            Bukkit.shutdown();
        }
    }

    private void stopTask() {
        if (taskCancelAction != null) {
            taskCancelAction.run();
            taskCancelAction = null;
        }
    }

    /**
     * Runs an action for a player on the correct thread.
     * On Folia the action is dispatched to the player's entity scheduler;
     * on standard Bukkit/Spigot/Paper it runs directly.
     */
    private void runForPlayer(final Player player, final Runnable action) {
        if (FOLIA) {
            player.getScheduler().run(plugin, t -> action.run(), null);
        } else {
            action.run();
        }
    }

    public boolean cancelCountdown() {
        if (getTimeLeft() <= 0 || taskCancelAction == null) return false;
        stopTask();
        if (config.getBossbarEnabled()) {
            bossbarManager.hideBossbar();
            bossbarManager.removeBossbar();
        }
        durationSeconds = -1;
        endTimeMillis = -1;
        for (final Player player : Bukkit.getOnlinePlayers()) {
            runForPlayer(player, new Runnable() {
                @Override
                public void run() {
                    for (String line : message.getNotifyCancel()) {
                        player.sendMessage(line);
                    }
                }
            });
        }
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