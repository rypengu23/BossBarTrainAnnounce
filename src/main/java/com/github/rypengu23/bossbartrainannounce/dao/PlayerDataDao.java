package com.github.rypengu23.bossbartrainannounce.dao;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.model.PlayerDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDataDao {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public PlayerDataDao(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * プレイヤー情報を取得
     * @param uuid
     * @return
     */
    public PlayerDataModel getPlayerData(String uuid){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        PlayerDataModel model = new PlayerDataModel();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_PlayerData ");
            selectSql.append("WHERE ");
            selectSql.append("UUID = ? ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, uuid);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                if(result.getInt("SPEED_UP_FLAG") == 1){
                    model.setSpeedUpFlag(true);
                }else{
                    model.setSpeedUpFlag(false);
                }

                if(result.getInt("SHOW_BOSSBAR_FLAG") == 1){
                    model.setShowBossBar(true);
                }else{
                    model.setShowBossBar(false);
                }

                if(result.getInt("SHOW_CHATANNOUNCE_FLAG") == 1){
                    model.setShowChatAnnounce(true);
                }else{
                    model.setShowChatAnnounce(false);
                }
            } else {
                model = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    /**
     * プレイヤー情報を登録
     * @param playerDataModel
     * @return
     */
    public int insertPlayerData(String uuid, PlayerDataModel playerDataModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO ");
            insertSql.append("BBTA_PlayerData ");
            insertSql.append("(UUID, SPEED_UP_FLAG, SHOW_BOSSBAR_FLAG, SHOW_CHATANNOUNCE_FLAG) ");
            insertSql.append("VALUES (?,?,?,?)");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, uuid);
            if(!playerDataModel.isSpeedUpFlag()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            if(!playerDataModel.isShowBossBar()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            if(!playerDataModel.isShowChatAnnounce()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * プレイヤー情報を更新
     * @param playerDataModel
     * @return
     */
    public int updatePlayerData(String uuid, PlayerDataModel playerDataModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_PlayerData ");
            insertSql.append("SET ");
            insertSql.append("SPEED_UP_FLAG = ?,");
            insertSql.append("SHOW_BOSSBAR_FLAG = ?,");
            insertSql.append("SHOW_CHATANNOUNCE_FLAG = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            if(!playerDataModel.isSpeedUpFlag()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            if(!playerDataModel.isShowBossBar()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            if(!playerDataModel.isShowChatAnnounce()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }
            ps.setString(p++, uuid);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
