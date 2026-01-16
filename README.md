# StopTimer

一個支援 Spigot / Paper 的伺服器自動關機倒數插件，支援自訂訊息、Discord 通知、PlaceholderAPI 擴充。

## 功能特色

- 透過 `/stopserver <時間>` 指令啟動自動關機倒數（支援 s/m/h 單位，如 `30s`, `5m`, `1h`）
- 倒數期間廣播自訂訊息、標題、副標題與 BossBar 進度條
- 支援倒數取消 `/stopserver cancel`
- 支援即時重新載入設定 `/stopserver reload`
- 支援 DiscordSRV 插件，將倒數通知同步發送至 Discord 頻道
- 支援 PlaceholderAPI，可在其他插件顯示剩餘倒數時間
- 允許自訂所有訊息內容
- BossBar 倒數顯示，可自訂進度條內容
- 可自定義每個倒數階段的提示（標題、聊天、Discord）

## 指令說明

| 指令                    | 權限                          | 說明                 |
|-------------------------|-------------------------------|----------------------|
| `/stopserver <時間>`    | `stoptimer.stopserver` 或 OP  | 啟動自動關機倒數      |
| `/stopserver cancel`    | `stoptimer.stopserver` 或 OP  | 取消倒數             |
| `/stopserver reload`    | `stoptimer.stopserver` 或 OP  | 重新載入設定檔       |

**範例：**
- `/stopserver 5m` —— 伺服器將於五分鐘後關閉
- `/stopserver 30s` —— 伺服器將於三十秒後關閉
- `/stopserver cancel` —— 取消倒數

## 權限節點

- `stoptimer.stopserver`：允許使用所有 /stopserver 指令
- OP 也自動擁有權限

## 訊息自訂

請於 `config.yml` 內的 `messages` 區塊自訂所有顯示文字  
支援變數 `%time%` 顯示剩餘時間。

## BossBar 支援

- 啟用 BossBar 功能後，倒數期間將顯示進度條
- 可於 `config.yml` 中調整 BossBar 顯示內容
- 進度條會根據剩餘時間自動減少

## PlaceholderAPI 支援

- `%stoptimer_time%`：剩餘倒數時間（如 4分鐘 20秒）
- `%stoptimer_time_raw%`：剩餘倒數時間（純秒數，數字格式）
- `%stoptimer_message%`：自訂訊息格式剩餘時間

## DiscordSRV 支援

- 啟用 DiscordSRV 後，倒數訊息自動同步到 Discord 頻道
- 支援倒數通知與取消通知

## 安裝方式

1. 將 StopTimer.jar 放入伺服器 `/plugins` 資料夾
2. 重新啟動伺服器
3. 編輯 `/plugins/StopTimer/config.yml` 以自訂訊息
4. （可選）安裝 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 及 [DiscordSRV](https://www.spigotmc.org/resources/18494/)

## 常見問題

- 倒數指令無反應？
    - 請確認權限與指令拼寫正確
    - 檢查有無安裝相關依賴（如 DiscordSRV, PlaceholderAPI）

- 如何自訂倒數訊息？
    - 編輯 `config.yml` 內 messages 區塊，並重新載入 `/stopserver reload`

- BossBar 沒有顯示？
    - 請確認 `config.yml` 中 `BossBar` 設為 `true`

## 授權

本專案採用 MIT 授權，詳情請見 [LICENSE](LICENSE)。

## 原始碼

GitHub: [https://github.com/xydesu/StopTimer](https://github.com/xydesu/StopTimer)

如有建議或問題歡迎開 Issue 或 PR！

---
作者：xydesu