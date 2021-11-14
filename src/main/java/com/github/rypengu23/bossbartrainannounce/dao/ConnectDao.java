package com.github.rypengu23.bossbartrainannounce.dao;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDao {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public ConnectDao(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * DB初期接続処理
     * テーブルが無い場合、作成する
     */
    public boolean connectionCheck() {

        int result = 0;

        //アナウンス表示地点テーブル
        String announceTable = ("CREATE TABLE IF NOT EXISTS BBTA_AnnounceInfo(UUID VARCHAR(36) NOT NULL, Next_Or_Soon INT NOT NULL, WORLD VARCHAR(200) NOT NULL,X INT NOT NULL,Y INT NOT NULL,Z INT NOT NULL, NEXT_STATION VARCHAR(200) NOT NULL, LINE_NAME VARCHAR(200) NOT NULL, BOUND_JP VARCHAR(200), BOUND_EN VARCHAR(200), TYPE_JP VARCHAR(200) NOT NULL, DOOR_SIDE VARCHAR(5), TERMINAL INT NOT NULL DEFAULT 0, DIRECTION INT NOT NULL DEFAULT 0, VIA_LINE_NAME VARCHAR(1004), VIA_LINE_OWNER_UUID VARCHAR(184), REDSTONE_ID INT UNIQUE, PRIMARY KEY(WORLD,X,Y,Z))");
        //路線情報テーブル
        String lineTable = ("CREATE TABLE IF NOT EXISTS BBTA_LineInfo(UUID VARCHAR(36) NOT NULL, NAME_JP VARCHAR(200) NOT NULL, NAME_EN VARCHAR(200) NOT NULL, LINE_COLOR VARCHAR(10) NOT NULL, TYPE_JP VARCHAR(1004) NOT NULL, TYPE_EN VARCHAR(1004) NOT NULL, RING INT DEFAULT 0 NOT NULL, PRIMARY KEY(UUID, NAME_JP))");
        //駅情報テーブル
        String stationTable = ("CREATE TABLE IF NOT EXISTS BBTA_StationInfo(UUID VARCHAR(36), LINE_NAME VARCHAR(200), NAME_KANJI VARCHAR(200), NAME_EN VARCHAR(200), NAME_KATAKANA VARCHAR(200), WORLD VARCHAR(200), pos1X INT, pos1Y INT, pos1Z INT, pos2X INT, pos2Y INT, pos2Z INT, NUMBER VARCHAR(200), PRIMARY KEY(UUID, LINE_NAME, NAME_KANJI))");
        //レッドストーンテーブル
        String redstoneTable = ("CREATE TABLE IF NOT EXISTS BBTA_AnnounceRedstone(REDSTONE_ID INT AUTO_INCREMENT, STATUS INT, WORLD VARCHAR(50), X INT, Y INT, Z INT, PRIMARY KEY(REDSTONE_ID)) ");
        try {
            //初回のみシーケンスに0をセット
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            //アナウンス表示地点テーブル
            statement.executeUpdate(announceTable);
            //路線情報テーブル
            statement.executeUpdate(lineTable);
            //駅情報テーブル
            statement.executeUpdate(stationTable);
            //レッドストーンテーブル
            statement.executeUpdate(redstoneTable);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection(){

        String url = "jdbc:mysql://" + mainConfig.getHostname() + "/" + mainConfig.getDb() + "?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false";
        String user = mainConfig.getUser();
        String password = mainConfig.getPassword();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return connection;
        }
    }

}
