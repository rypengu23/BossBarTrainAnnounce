package com.github.rypengu23.bossbartrainannounce.config;

public class CommandMessage {

    public static String BossBarTrainAnnounce_ConfigReload;
    public static String BossBarTrainAnnounce_CommandFailure;
    public static String BossBarTrainAnnounce_DoNotHavePermission;

    public static String CommandFailure;
    public static String CommandDoNotHavePermission;

    public static String Command_Flag_RegistBound;
    public static String Command_Flag_RemoveBound;
    public static String Command_Flag_RegistTerminal;
    public static String Command_Flag_RemoveTerminal;
    public static String Command_Flag_RegistFast;
    public static String Command_Flag_RemoveFast;

    public static String Command_playerData_SetSpeed;
    public static String Command_playerData_LiftSpeed;
    public static String Command_playerData_ShowBossBar;
    public static String Command_playerData_HideBossBar;
    public static String Command_playerData_ShowAnnounce;
    public static String Command_playerData_HideAnnounce;
    public static String Command_playerData_AlreadySetting;

    public static String Command_Info_SHOWSTATUS1;
    public static String Command_Info_SHOWSTATUS2;
    public static String Command_Info_SHOWSTATUS3;
    public static String Command_Info_SHOWSTATUS4;
    public static String Command_Info_SHOWSTATUS5;
    public static String Command_Info_PlayerNotFound;

    public static String Command_Help_Line1;
    public static String Command_Help_Line2;
    public static String Command_Help_Line3;
    public static String Command_Help_Line4;

    private MainConfig mainConfig;

    public CommandMessage(MainConfig mainConfig){
        this.mainConfig = mainConfig;
    }

    public void changeLanguageCommandMessages(){
        if(mainConfig.getLanguage().equals("ja")){
            BossBarTrainAnnounce_ConfigReload = "Configをリロードしました。";
            BossBarTrainAnnounce_CommandFailure = "不正なコマンドです。";
            BossBarTrainAnnounce_DoNotHavePermission = "権限を所有していません。";

            CommandFailure = "コマンドの形式が不正です。";
            CommandDoNotHavePermission = "権限を所有していません。";

            Command_Flag_RegistBound = "行き先を登録しました。";
            Command_Flag_RemoveBound = "行き先を削除しました。";
            Command_Flag_RegistTerminal = "終着駅を設定しました。";
            Command_Flag_RemoveTerminal = "終着駅設定を解除しました。";
            Command_Flag_RegistFast = "アナウンス速度高速化フラグを設定しました。";
            Command_Flag_RemoveFast = "アナウンス速度高速化フラグを解除しました。";

            Command_playerData_SetSpeed = "トロッコを高速モードに設定しました。";
            Command_playerData_LiftSpeed = "トロッコを高速モードを解除しました。";
            Command_playerData_ShowBossBar = "BossBar案内を表示に設定しました。";
            Command_playerData_HideBossBar = "BossBar案内を非表示に設定しました。";
            Command_playerData_ShowAnnounce = "チャットアナウンスを表示に設定しました。";
            Command_playerData_HideAnnounce = "チャットアナウンスを非表示に設定しました。";
            Command_playerData_AlreadySetting = "既に設定済みです。";

            Command_Info_SHOWSTATUS1 = "§b――――― §f{player}の情報 §b―――――";
            Command_Info_SHOWSTATUS2 = "高速化: {speed}";
            Command_Info_SHOWSTATUS3 = "BossBar表示: {bossbar}";
            Command_Info_SHOWSTATUS4 = "アナウンス表示: {announce}";
            Command_Info_SHOWSTATUS5 = "§b――――――――――――――――――――――――――――――――――――――――";
            Command_Info_PlayerNotFound = "指定したプレイヤーは見つかりませんでした。";

            Command_Help_Line1 = "§b――――― §fBossBarTrainAnnounce コマンドガイド §b―――――";
            Command_Help_Line2 = "§fコマンドガイドは以下のページを御覧ください。";
            Command_Help_Line3 = "§fhttps://mc.my-cra.page/bossbartrainannounce/";
            Command_Help_Line4 = "§b――――――――――――――――――――――――――――――――――――――――";

        } else if(mainConfig.getLanguage().equals("en")){
            BossBarTrainAnnounce_ConfigReload = "Config reloaded.";
            BossBarTrainAnnounce_CommandFailure = "Command failure.";
            BossBarTrainAnnounce_DoNotHavePermission = "You do not have permission.";

            CommandFailure = "The command format is invalid.";
            CommandDoNotHavePermission = "You do not have permission.";

            Command_Flag_RegistBound = "The destination has been registered.";
            Command_Flag_RemoveBound = "Deleted destination.";
            Command_Flag_RegistTerminal = "The terminal station has been set.";
            Command_Flag_RemoveTerminal = "Terminating station setting has been removed.";
            Command_Flag_RegistFast = "Set the announcement speed-up flag.";
            Command_Flag_RemoveFast = "Announcement speed acceleration flag has been removed.";

            Command_playerData_SetSpeed = "Set the trolley to high-speed mode.";
            Command_playerData_LiftSpeed = "The trolley has been deactivated in high-speed mode.";
            Command_playerData_ShowBossBar = "BossBar guidance is set to display.";
            Command_playerData_HideBossBar = "BossBar guidance is now hidden.";
            Command_playerData_ShowAnnounce = "Chat announcements are now set to display.";
            Command_playerData_HideAnnounce = "Chat announcements are now set to be hidden.";
            Command_playerData_AlreadySetting = "It is already set.";

            Command_Info_SHOWSTATUS1 = "§b――――― §f{player}'s information §b―――――";
            Command_Info_SHOWSTATUS2 = "Increase speed: {speed}";
            Command_Info_SHOWSTATUS3 = "Show BossBar: {bossbar}";
            Command_Info_SHOWSTATUS4 = "Show Announce: {announce}";
            Command_Info_SHOWSTATUS5 = "§b――――――――――――――――――――――――――――――――――――――――";
            Command_Info_PlayerNotFound = "Player not found.";

            Command_Help_Line1 = "§b――――― §fBossBarTrainAnnounce Command guide §b―――――";
            Command_Help_Line2 = "§fFor a command guide, see the following page.";
            Command_Help_Line3 = "§fhttps://mc.my-cra.page/bossbartrainannounce/";
            Command_Help_Line4 = "§b――――――――――――――――――――――――――――――――――――――――";
        }
    }
}

