# StopTimer

[![Release](https://img.shields.io/github/v/release/xydesu/StopTimer?include_prereleases)](https://github.com/xydesu/StopTimer/releases)
[![bStats](https://img.shields.io/bstats/servers/33227)](https://bstats.org/plugin/bukkit/StopTimer/33227)
[![License](https://img.shields.io/github/license/xydesu/StopTimer)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper%20%7C%20Folia-blue.svg)](https://papermc.io/)

A modern, highly customizable Spigot / Paper / Folia plugin for scheduled server shutdowns with countdown timers. Features custom shutdown reasons, template management, customizable BossBars, Discord integration, PlaceholderAPI support, and multi-language localization.

---

## ✨ Features

- **Flexible Countdown**: Start countdowns with units in seconds (`s`), minutes (`m`), or hours (`h`) (e.g., `30s`, `5m`, `1h`).
- **Dynamic Shutdown Reasons & Templates**:
  - Direct custom reasons using `r:<reason>` (e.g., `/stopserver 5m r:Server maintenance`).
  - Predefined reusable templates using `t:<template>` (e.g., `/stopserver 5m t:update`).
  - `%reason%` placeholder supported across chat, title, actionbar, BossBar, Discord, and kick messages.
- **Customizable BossBar**: Real-time progress bar with configurable colors (`RED`, `BLUE`, `GREEN`, `PINK`, `PURPLE`, `WHITE`, `YELLOW`) and styles (`SOLID`, `NOTCHED_6`, `NOTCHED_10`, `NOTCHED_12`, `NOTCHED_20`).
- **Multi-Stage Notifications**: Configure threshold seconds to broadcast chat messages, titles/subtitles, and Discord messages.
- **DiscordSRV Integration (Optional)**: Automatically send shutdown countdowns and cancel notices to your Discord channels.
- **PlaceholderAPI Integration (Optional)**: Expose remaining countdown times and formatted messages to scoreboards, tablists, and menus.
- **Smart Tab-Completion**: Auto-completes durations, subcommands (`reload`, `cancel`), reasons (`r:`), and templates (`t:<template_name>`).
- **Multi-Language Support**: Bundled with English (`en`) and Traditional Chinese (`zh_tw`).
- **bStats Metrics**: Anonymous statistics collection to help improve the plugin.
- **Folia & Paper Supported**: Built and optimized for high-performance modern server software.

---

## 🎮 Commands & Permissions

Permission node for all commands: `stoptimer.stopserver` (Default: OP)

| Command | Description |
| :--- | :--- |
| `/stopserver [time] [r:<reason> \| t:<template>]` | Start the shutdown countdown with optional reason or template. |
| `/stopserver cancel` | Cancel the ongoing shutdown countdown. |
| `/stopserver reload` | Reload configuration, templates, and language files live. |

### Command Examples:
- `/stopserver` — Starts countdown using the default time and default template from `config.yml`.
- `/stopserver 5m` — Shuts down in 5 minutes with the default reason.
- `/stopserver 10m r:Urgent Maintenance` — Shuts down in 10 minutes with reason `"Urgent Maintenance"`.
- `/stopserver 3m t:update` — Shuts down in 3 minutes with the template `"update"`.
- `/stopserver cancel` — Cancels the running countdown.
- `/stopserver reload` — Reloads `config.yml`, `templates.yml`, and active language file.

---

## 📁 Configuration

### `config.yml`
```yaml
# Language to use for all messages (en, zh_tw, or custom)
language: en

# Default countdown duration if none is specified
DefaultTime: 5m

# BossBar display settings
BossBar:
  Enable: true
  Color: RED      # RED, GREEN, BLUE, YELLOW, PINK, PURPLE, WHITE
  Style: SOLID    # SOLID, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20

# Notification stages
NotifyTime:
  Title:
    Enable: true
    FirstRun: false
    Seconds: [300, 60, 10, 5, 4, 3, 2, 1]
  Message:
    Enable: true
    FirstRun: true
    Seconds: [1800, 600, 300, 60, 10, 5, 4, 3, 2, 1]
  Discord:
    Enable: true
    FirstRun: true
    Seconds: [1800, 600, 300, 60, 10]
```

### `templates.yml`
Define custom, reusable shutdown reason templates:
```yaml
templates:
  default: "Server stopping"
  update: "Server updating"
  maintenance: "Server maintenance"
```

---

## 🌐 Localization (i18n)

StopTimer ships with built-in translations in `plugins/StopTimer/lang/`:
- `en.yml` (English)
- `zh_tw.yml` (繁體中文)

### Custom Languages:
1. Create a new YAML file: `plugins/StopTimer/lang/<code>.yml` (copy from `en.yml`).
2. Translate the messages.
3. Update `language: <code>` in `config.yml`.
4. Execute `/stopserver reload`.

### Available Placeholders in Messages:
- `%time%` — Formatted remaining time (e.g., `5 minutes 0 seconds`).
- `%reason%` — Dynamic or template shutdown reason.

---

## 🧩 PlaceholderAPI Placeholders

When PlaceholderAPI is installed, the following placeholders are available:

| Placeholder | Description | Example Output |
| :--- | :--- | :--- |
| `%stoptimer_time%` | Formatted remaining time | `4 minutes 20 seconds` |
| `%stoptimer_time_raw%` | Raw remaining seconds | `260` |
| `%stoptimer_message%` | Full formatted broadcast message | `Server will restart in 4 minutes 20 seconds!` |

---

## 📊 bStats Metrics

StopTimer collects anonymous statistics via [bStats](https://bstats.org/plugin/bukkit/StopTimer/33227). You can opt out at any time by editing `plugins/bStats/config.yml`.

---

## 📥 Installation

1. Download the latest `StopTimer-x.x.x.jar` from [Releases](https://github.com/xydesu/StopTimer/releases).
2. Place the `.jar` into your server's `plugins/` directory.
3. (Optional) Install [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) and [DiscordSRV](https://www.spigotmc.org/resources/18494/).
4. Start or restart your server.
5. Customize `plugins/StopTimer/config.yml`, `templates.yml`, and language files as desired.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).