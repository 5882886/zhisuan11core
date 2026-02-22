**Language**

English | [简体中文](./README_ZH.md)

## Introduction

This is my fist Minecraft Bukkit Plugin.

> Team members: me, Kimi, DeepSeek ...
> 
> Not done well, welcome to give suggestions.

Currently, it's used on the Minecraft server of my class.

## Functions

- [x] Fist join-in items
- [x] Simple broadcast
- [x] Teleport command
- [x] Game menu
- [x] Sidebar
- [x] Quiz
- [x] Database support
- [ ] Clear drop items
- [ ] Daily login event

> More is coming soon ...

## Compatibility

The api version is 1.20.1.

Has already tested on Mohist 1.20.1、Paper 1.20.1 & 1.21.11.

In theory, it can run on all Minecraft server cores based on the Bukkit API!

## Description

> Basically, all files use camel hump naming convention.

```
├── src
    ├── main
    │   ├── java
    │   │   └── org.zhisuan11
    │   │       ├── core（核心文件）
    │   │       │   ├── JoinInfo.java                   # 在玩家加入时发送标题和聊天信息
    │   │       │   ├── JoinItem.java                   # 向玩家发送物品
    │   │       │   ├── Sidebar.java                    # 游戏内侧边栏
    │   │       │   └── Respawn.java                    # 重设出生点
    │   │       │    
    │   │       ├── command（命令）
    │   │       │   ├── MainCommand.java                # 游戏内核心命令
    │   │       │   ├── TeleportCommand.java            # 游戏内传送命令（接管原版 tp 指令）
    │   │       │   └── CommandTabCompleter.java        # 游戏内命令输入补全
    │   │       │    
    │   │       ├── gui（游戏内菜单）
    │   │       │   ├── GameMenu.java                   # 游戏内菜单核心文件
    │   │       │   └── GameMenuClick.java              # 游戏内菜单点击事件
    │   │       │        
    │   │       ├── quiz（问答系统）
    │   │       │   ├── Quiz.java                       # Quiz主文件
    │   │       │   ├── QuizForSql.java                 # Quiz的MySQL存储
    │   │       │   └── QuizForYaml.java                # Quiz的YAML存储
    │   │       │
    │   │       ├── tasks（定时事件）
    │   │       │   ├── Broadcast.java                  # 服务器公告
    │   │       │   ├── CheckPing.java                  # 检测Ping值    
    │   │       │   ├── ClearDropItems.java             # 清理掉落物              
    │   │       │   ├── InitialTask.java                # 处理初始事件
    │   │       │   └── ScheduleTask.java               # 处理定时事件
    │   │       │    
    │   │       └── Zhisuan11core.java      # 主文件，插件启动标识
    │   │        
    │   └── resources
    │       ├── config.yml      # 核心配置文件
    │       ├── Quiz.yml        # Quiz配置文件
    │       └── plugin.yml      # 插件信息
    │        
    ├── build.gradle    # Gradle配置文件
    ├── README.md
    └── ...
```


## Update Logs

### Versions

2026/02/19：Release v1.2

2026/02/17：Release v1.1

2026/02/04：Release v1.0

2025/03/17：Release v1.0-snapshot

### Details

|    Date    | What's new                         |
|:----------:|------------------------------------|
| 2026-02-19 | complete database support          |
| 2026-02-17 | complete basic quiz system         |
| 2026-02-13 | add quiz system                    |
| 2026-02-12 | update file structure              |
| 2026-02-09 | update game menu                   |
| 2026-02-05 | add game menu and side bar         |
| 2026-02-04 | update teleport and respawn system |
| 2026-02-03 | complete item-give system          | 
| 2025-04-28 | add teleport command               |
| 2025-03-17 | new folder                         |