package com.github.rypengu23.bossbartrainannounce.config;

public class ConsoleMessage {

    public static String BossBarTrainAnnounce_startupPlugin;
    public static String BossBarTrainAnnounce_startupCompPlugin;
    public static String BossBarTrainAnnounce_LoadDatabase;
    public static String BossBarTrainAnnounce_LoadDatabaseFailure;

    public static String BossBarTrainAnnounce_startupScheduler;

    public static String ConfigUpdater_CheckUpdateConfig;
    public static String ConfigUpdater_UpdateConfig;
    public static String ConfigUpdater_NoConfigUpdates;

    private MainConfig mainConfig;

    public ConsoleMessage(MainConfig mainConfig){
        this.mainConfig = mainConfig;
    }

    public void changeLanguageConsoleMessages(){
        if(mainConfig.getLanguage().equals("ja")){

            BossBarTrainAnnounce_startupPlugin = "プラグインを起動します。";
            BossBarTrainAnnounce_startupCompPlugin = "プラグインが起動しました。";
            BossBarTrainAnnounce_LoadDatabase = "データベースに接続しました。";
            BossBarTrainAnnounce_LoadDatabaseFailure = "データベースの接続に失敗しました。";

            BossBarTrainAnnounce_startupScheduler = "スケジューラを起動。";

            ConfigUpdater_CheckUpdateConfig = "Configの更新確認を行います。";
            ConfigUpdater_UpdateConfig = "古いバージョンのConfigです。アップデートを行います。";
            ConfigUpdater_NoConfigUpdates = "Configは最新バージョンです。";



        } else if(mainConfig.getLanguage().equals("en")){

            BossBarTrainAnnounce_startupPlugin = "Plugin startup.";
            BossBarTrainAnnounce_startupCompPlugin = "Plugin startup complete.";
            BossBarTrainAnnounce_LoadDatabase = "Connected to database.";
            BossBarTrainAnnounce_LoadDatabaseFailure = "Failed to connect to the database.";

            BossBarTrainAnnounce_startupScheduler = "Scheduler startup.";

            ConfigUpdater_CheckUpdateConfig = "Check for Config updates.";
            ConfigUpdater_UpdateConfig = "Update Config.";
            ConfigUpdater_NoConfigUpdates = "No Config updates.";
        }
    }
}
