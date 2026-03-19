# StopTimer

A Spigot / Paper plugin for scheduled server shutdown with a countdown timer. Supports custom messages, Discord notifications, PlaceholderAPI integration, and full multi-language support.

## Features

- Start an auto-shutdown countdown with `/stopserver <time>` (supports s/m/h units, e.g. `30s`, `5m`, `1h`)
- Broadcast custom messages, titles, subtitles, and a BossBar progress bar during the countdown
- Cancel the countdown with `/stopserver cancel`
- Reload configuration live with `/stopserver reload`
- DiscordSRV integration (optional) — syncs countdown notifications to a Discord channel
- PlaceholderAPI support — display the remaining countdown time in other plugins
- Fully customisable messages via language files
- BossBar countdown display with configurable text
- Configurable per-stage notifications (title, chat, Discord) at any remaining-time threshold
- Multi-language support — ships with English (`en`) and Traditional Chinese (`zh_tw`)

## Commands

| Command                 | Permission                    | Description                  |
|-------------------------|-------------------------------|------------------------------|
| `/stopserver <time>`    | `stoptimer.stopserver` or OP  | Start the shutdown countdown |
| `/stopserver cancel`    | `stoptimer.stopserver` or OP  | Cancel the countdown         |
| `/stopserver reload`    | `stoptimer.stopserver` or OP  | Reload configuration         |

**Examples:**
- `/stopserver 5m` — server shuts down in five minutes
- `/stopserver 30s` — server shuts down in thirty seconds
- `/stopserver cancel` — cancel the countdown

## Permissions

- `stoptimer.stopserver` — allows use of all `/stopserver` subcommands
- OP players have access automatically

## Language / Localisation

Set the language in `config.yml`:

```yaml
language: en   # en (English) or zh_tw (Traditional Chinese)
```

Language files are stored under `plugins/StopTimer/lang/`. To create a custom language:

1. Copy `plugins/StopTimer/lang/en.yml` to `plugins/StopTimer/lang/<code>.yml`
2. Translate all message values
3. Set `language: <code>` in `config.yml`
4. Run `/stopserver reload`

## Message Customisation

Edit `plugins/StopTimer/lang/<language>.yml` to customise all displayed text.
Use `%time%` as a placeholder for the remaining time.

## BossBar Support

- Enable or disable the BossBar in `config.yml` with `BossBar: true/false`
- The bar text is set in the language file under `messages.bossbar.message`
- Progress decreases automatically as the countdown runs

## PlaceholderAPI Support

- `%stoptimer_time%` — formatted time remaining (e.g. `4 minute 20 second`)
- `%stoptimer_time_raw%` — seconds remaining as a plain number
- `%stoptimer_message%` — fully formatted message with remaining time

## DiscordSRV Support

- When DiscordSRV is installed, countdown and cancellation messages are sent automatically to the configured Discord channel
- DiscordSRV is an **optional** dependency; the plugin works without it

## Installation

1. Place `StopTimer.jar` in the server's `/plugins` folder
2. Restart the server
3. Edit `/plugins/StopTimer/config.yml` to select a language and configure notifications
4. Edit `/plugins/StopTimer/lang/en.yml` (or your chosen language file) to customise messages
5. (Optional) Install [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) and/or [DiscordSRV](https://www.spigotmc.org/resources/18494/)

## FAQ

- **The countdown command does nothing?**
    - Verify you have the correct permission or are an OP
    - Check that required dependencies (PlaceholderAPI) are installed

- **How do I customise messages?**
    - Edit the language file at `plugins/StopTimer/lang/<language>.yml` and run `/stopserver reload`

- **BossBar not showing?**
    - Ensure `BossBar: true` is set in `config.yml`

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.

## Source Code

GitHub: [https://github.com/xydesu/StopTimer](https://github.com/xydesu/StopTimer)

Issues and pull requests are welcome!

---
Author: xydesu