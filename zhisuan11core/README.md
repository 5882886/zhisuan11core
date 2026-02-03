这是我的第一个Minecraft Bukkit插件。

> 开发团队：我、Kimi、DeepSeek……
> 
> 做得不好，还请各位大佬批评指正！

目前用于天津大学2024级智算11班MC服务器

## 兼容性

已在 Mohist 1.20.1、Paper 1.20.1 & 1.21.11测试。

理论上在所有基于`Bukkit api`的Minecraft服务器核心上均可运行！

## 文件说明

| 文件名                     | 用途                 |
|-------------------------|--------------------|
| `Zhisuan11core.java`    | 主文件，插件启动标识，发送游戏内公告 |
| `Zhisuan11Command`      | 定义游戏内命令            |
| `Zhisuan11TabCompleter` | 游戏内命令输入补全          |
| `JoinInfo.java`         | 在玩家加入时发送标题和聊天信息    |
| `JoinItem.java`         | 向玩家发送物品            |
| `Respawn.java`          | 重设出生点              |

## 更新日志

### 2026-2-3

实现物品给予功能

### 2025-3

新建文件夹