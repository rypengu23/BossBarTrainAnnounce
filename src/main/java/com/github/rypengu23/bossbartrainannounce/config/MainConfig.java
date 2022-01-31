package com.github.rypengu23.bossbartrainannounce.config;

import org.bukkit.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class MainConfig {

    //バージョン
    private Double version;
    //言語
    private String language;

    //DB情報
    private String hostname;
    private String db;
    private String user;
    private String password;

    //選択用アイテム
    private String selectItem;

    //トロッコ高速化機能の利用可否
    private boolean useMinecartSpeedup;

    //走行モードの既定値
    private boolean defaultMode;

    //ユーザーによる高速モードの変更を許可するかどうか
    private boolean allowChangeModeForPlayer;

    //トロッコ高速時の速度
    private Double maxMinecartSpeed = (double) 0.4;

    //高速走行時、アナウンス1通ごとの送信速度を何倍に早めるか
    private Double announceMagnification = (double) 1.0;

    //アナウンスロケーションの検知精度を高めるか
    private boolean useSpeedUpPlugin;

    //接頭辞
    private String prefix;

    //乗り換え判定距離
    private int transferRange;

    //アナウンス送信間隔
    private long announceInterval;
    private long announceIntervalOfFastFlag;

    MainConfig(Configuration config){
        version = config.getDouble("version");
        language = config.getString("language");

        //DB接続先情報
        hostname = config.getString("database.hostname");
        db = config.getString("database.db");
        user = config.getString("database.user");
        password = config.getString("database.password");

        //ポジションセット用アイテム
        selectItem = config.getString("select.item");

        //高速化設定
        useMinecartSpeedup = config.getBoolean("speedUp.useMinecartSpeedUp");
        defaultMode = config.getBoolean("speedUp.defaultMode");
        allowChangeModeForPlayer = config.getBoolean("speedUp.allowChangeModeForPlayer");
        maxMinecartSpeed = config.getDouble("speedUp.maxMinecartSpeed");
        announceMagnification = config.getDouble("speedUp.announceMagnification");
        useSpeedUpPlugin = config.getBoolean("speedUp.useSpeedUpPlugin");

        //その他設定
        prefix = config.getString("setting.prefix");
        transferRange = config.getInt("setting.transferRange");
        announceInterval = config.getLong("setting.announceInterval");
        announceIntervalOfFastFlag = config.getLong("setting.announceIntervalOfFastFlag");


    }

    public Map getConfigTypeList() {
        Map<String, String> map = new HashMap<>();
        map.put("version", "int");
        map.put("language", "String");

        //DB接続先情報
        map.put("database.hostname", "String");
        map.put("database.db", "String");
        map.put("database.user", "String");
        map.put("database.password", "String");

        //ポジションセット用アイテム
        map.put("select.item", "String");

        //高速化設定
        map.put("speedUp.useMinecartSpeedUp", "int");
        map.put("speedUp.defaultMode", "int");
        map.put("speedUp.allowChangeModeForPlayer", "int");
        map.put("speedUp.maxMinecartSpeed", "int");
        map.put("speedUp.announceMagnification", "int");
        map.put("speedUp.useSpeedUpPlugin", "int");

        //その他設定
        map.put("setting.prefix", "String");
        map.put("setting.transferRange", "int");
        map.put("setting.announceInterval", "int");
        map.put("setting.announceIntervalOfFastFlag", "int");

        return map;
    }

    public Double getVersion() {
        return version;
    }

    public String getLanguage() {
        return language;
    }

    public String getHostname() {
        return hostname;
    }

    public String getDb() {
        return db;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSelectItem() {
        return selectItem;
    }

    public boolean isUseMinecartSpeedup() {
        return useMinecartSpeedup;
    }

    public boolean isDefaultMode() {
        return defaultMode;
    }

    public boolean isAllowChangeModeForPlayer() {
        return allowChangeModeForPlayer;
    }

    public Double getMaxMinecartSpeed() {
        return maxMinecartSpeed;
    }

    public Double getAnnounceMagnification() {
        return announceMagnification;
    }

    public boolean isUseSpeedUpPlugin() {
        return useSpeedUpPlugin;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getTransferRange() {
        return transferRange;
    }

    public long getAnnounceInterval() {
        return announceInterval;
    }

    public long getAnnounceIntervalOfFastFlag() {
        return announceIntervalOfFastFlag;
    }
}
