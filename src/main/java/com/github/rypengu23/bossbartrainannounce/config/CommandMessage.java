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

    public static String Command_Info_SHOWSTATUS1;
    public static String Command_Info_SHOWSTATUS2;
    public static String Command_Info_SHOWSTATUS3;
    public static String Command_Info_SHOWSTATUS4;
    public static String Command_Info_PlayerNotFound;

    public static String Command_Help_Line1;
    public static String Command_Help_Info;
    public static String Command_Help_AnotherInfo;
    public static String Command_Help_Whitelist;
    public static String Command_Help_reload;
    public static String Command_Help_LineLast;

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

            Command_Info_SHOWSTATUS1 = "§b――――― §f{player}のログイン情報 §b―――――";
            Command_Info_SHOWSTATUS2 = "初回ログイン: {firstlogin}";
            Command_Info_SHOWSTATUS3 = "規制終了日時: {opendate}";
            Command_Info_SHOWSTATUS4 = "§b――――――――――――――――――――――――――――――――――――――――";
            Command_Info_PlayerNotFound = "指定したプレイヤーは見つかりませんでした。";

            Command_Help_Line1 = "§b――――― §fBeginnerManagement コマンドガイド §b―――――";
            Command_Help_Info = "§e/bm info §f: 規制情報を表示。";
            Command_Help_AnotherInfo = "§e/bm info [ﾕｰｻﾞｰ名] §f: 指定したﾕｰｻﾞｰの情報を表示。";
            Command_Help_Whitelist = "§e/bm whitelist [ﾕｰｻﾞｰ名] §f: ホワイトリストに追加。";
            Command_Help_reload = "§e/bm reload §f: Configをリロード。";
            Command_Help_LineLast = "§b――――――――――――――――――――――――――――――――――――――――";

        } else if(mainConfig.getLanguage().equals("en")){
            BossBarTrainAnnounce_ConfigReload = "Config reloaded.";
            BossBarTrainAnnounce_CommandFailure = "Command failure.";
            BossBarTrainAnnounce_DoNotHavePermission = "You do not have permission.";

            CommandFailure = "The command format is invalid.";
            CommandDoNotHavePermission = "You do not have permission.";

            Command_Flag_RegistBound = "行き先を登録しました。";
            Command_Flag_RemoveBound = "行き先を削除しました。";
            Command_Flag_RegistTerminal = "終着駅を設定しました。";
            Command_Flag_RemoveTerminal = "終着駅設定を解除しました。";

            Command_Info_SHOWSTATUS1 = "§b――――― §f{player}'s information §b―――――";
            Command_Info_SHOWSTATUS2 = "First login: {firstlogin}";
            Command_Info_SHOWSTATUS3 = "Regulation end: {opendate}";
            Command_Info_SHOWSTATUS4 = "§b――――――――――――――――――――――――――――――――";
            Command_Info_PlayerNotFound = "Player not found.";

            Command_Help_Line1 = "§b――――― §fBeginnerManagement Help §b―――――";
            Command_Help_Info = "§e/bm info §f: Show regulatory information.";
            Command_Help_AnotherInfo = "§e/bm info [username] §f: Show player information.";
            Command_Help_Whitelist = "§e/bm whitelist [username] §f: Add to white list.";
            Command_Help_reload = "§e/awt reload §f:Reload Config.";
            Command_Help_LineLast = "§b――――――――――――――――――――――――――――――――";
        }
    }
}

