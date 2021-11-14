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

    //接頭辞
    private String prefix;

    //乗り換え判定距離
    private int transferRange;

    //アナウンス送信間隔
    private long announceInterval;

    MainConfig(Configuration config){
        version = config.getDouble("version");
        language = config.getString("language");

        hostname = config.getString("database.hostname");
        db = config.getString("database.db");
        user = config.getString("database.user");
        password = config.getString("database.password");

        selectItem = config.getString("select.item");

        prefix = config.getString("setting.prefix");

        transferRange = config.getInt("setting.transferRange");

        announceInterval = config.getLong("setting.announceInterval");
    }

    public Map getConfigTypeList() {
        Map<String, String> map = new HashMap<>();
        map.put("version", "double");
        map.put("language", "String");

        map.put("database.hostname", "String");
        map.put("database.db", "String");
        map.put("database.user", "String");
        map.put("database.password", "String");

        map.put("select.item", "String");

        map.put("setting.prefix", "String");

        map.put("setting.transferRange", "int");

        map.put("setting.announceInterval", "int");


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

    public String getPrefix() {
        return prefix;
    }

    public int getTransferRange() {
        return transferRange;
    }
}
