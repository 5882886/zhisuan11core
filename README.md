## 简介

这是我的第一个Minecraft Bukkit插件。

> 开发团队：我、Kimi、DeepSeek……
> 
> 做得不好，还请各位大佬批评指正。

目前用于天津大学2024级智算11班MC服务器。

## 功能

- [x] 初始物品
- [x] 简单公告
- [x] 传送指令
- [x] 主菜单
- [x] 侧边栏
- [x] 问答系统
- [x] 数据库支持
- [ ] 每日登录

> 更多的以后再说……

## 兼容性

api 版本是 1.20.1。

已在 Mohist 1.20.1、Paper 1.20.1 & 1.21.11测试。

理论上在所有基于`Bukkit api`的Minecraft服务器核心上均可运行！

## 文件说明

> 基本上所有文件均采用驼峰命名法

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


## 更新日志

### 版本记录

2026/02/19：正式版 v1.2

2026/02/17：正式版 v1.1

2026/02/04：正式版 v1.0

2025/03/17：测试版 v1.0-snapshot

### 详细信息

|     日期     | 更新内容        |
|:----------:|-------------|
| 2026-02-19 | 正式实现数据库支持   |
| 2026-02-17 | 问答系统基本完成    |
| 2026-02-13 | 实现基础的问答系统   |
| 2026-02-12 | 更新文件结构      |
| 2026-02-09 | 插件菜单界面更新    |
| 2026-02-05 | 新增游戏主菜单和侧边栏 |
| 2026-02-04 | 更新传送模块和重生逻辑 |
| 2026-02-03 | 实现物品给予功能    | 
| 2025-04-28 | 增加传送功能      |
| 2025-03-17 | 新建文件夹       |