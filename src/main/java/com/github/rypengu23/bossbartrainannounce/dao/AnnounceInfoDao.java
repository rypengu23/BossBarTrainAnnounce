package com.github.rypengu23.bossbartrainannounce.dao;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class AnnounceInfoDao {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public AnnounceInfoDao(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 全てのアナウンス情報を取得
     * @return
     */
    public HashSet<AnnounceInfoModel> getAnnounceInfoListAll(){

        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();

        HashSet<AnnounceInfoModel> resultList = new HashSet<>();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT WORLD,X,Y,Z FROM ");
            selectSql.append("BBTA_AnnounceInfo ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());

            ResultSet result = ps.executeQuery();

            while(result.next()){
                AnnounceInfoModel model = new AnnounceInfoModel();
                model.setWorldName(result.getString("WORLD"));
                model.setPosX(result.getInt("X"));
                model.setPosY(result.getInt("Y"));
                model.setPosZ(result.getInt("Z"));

                resultList.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 引数の地点がアナウンス地点か判定。該当すればアナウンス情報を返す
     * @param worldName
     * @param posX
     * @param posY
     * @param posZ
     * @return
     */
    public AnnounceInfoModel getAnnounceForCoordinate(String worldName, int posX, int posY, int posZ){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        AnnounceInfoModel model = new AnnounceInfoModel();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_AnnounceInfo AS AI ");
            selectSql.append("LEFT OUTER JOIN BBTA_AnnounceRedstone AS AR ");
            selectSql.append("ON AI.REDSTONE_ID = AR.REDSTONE_ID ");
            selectSql.append("WHERE ");
            selectSql.append("AI.WORLD = ? AND ");
            selectSql.append("AI.X = ? AND ");
            selectSql.append("AI.Y = ? AND ");
            selectSql.append("AI.Z = ? ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, worldName);
            ps.setInt(p++, posX);
            ps.setInt(p++, posY);
            ps.setInt(p++, posZ);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                model.setUUID(result.getString("AI.UUID"));
                model.setNextOrSoon(result.getInt("AI.NEXT_OR_SOON"));
                model.setWorldName(result.getString("AI.WORLD"));
                model.setPosX(result.getInt("AI.X"));
                model.setPosY(result.getInt("AI.Y"));
                model.setPosZ(result.getInt("AI.Z"));
                model.setNextStationNameKanji(result.getString("AI.NEXT_STATION"));
                model.setLineNameJP(result.getString("AI.LINE_NAME"));
                model.setBoundJP(result.getString("AI.BOUND_JP"));
                model.setBoundEN(result.getString("AI.BOUND_EN"));
                model.setTypeJP(result.getString("AI.TYPE_JP"));
                model.setExit(result.getString("AI.DOOR_SIDE"));
                if(result.getInt("AI.TERMINAL") == 0){
                    model.setTerminal(false);
                }else{
                    model.setTerminal(true);
                }
                model.setDirection(result.getInt("AI.DIRECTION"));
                model.setViaLineNameJP((result.getString("AI.VIA_LINE_NAME")));
                model.setViaLineOwnerUUID((result.getString("AI.VIA_LINE_OWNER_UUID")));

                result.getInt("AR.REDSTONE_ID");
                if(result.wasNull()){
                    model.setRedstone(false);
                }else{
                    model.setRedstone(true);
                    model.setOnOrOff(result.getInt("AR.STATUS"));
                    model.setRedstonePosX(result.getInt("AR.X"));
                    model.setRedstonePosY(result.getInt("AR.Y"));
                    model.setRedstonePosZ(result.getInt("AR.Z"));
                }
                if(result.getInt("AI.FAST_FLAG") == 0){
                    model.setAnnounceFastFlag(false);
                }else{
                    model.setAnnounceFastFlag(true);
                }
            } else {
                model = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model = null;
        }

        return model;
    }

    /**
     * 路線名・駅名からアナウンス情報を取得
     * @param UUID
     * @param lineName
     * @param stationName
     * @return
     */
    public ArrayList<AnnounceInfoModel> getAnnounceForStationName(String UUID, String lineName, String stationName){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        ArrayList<AnnounceInfoModel> resultList = new ArrayList<>();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_AnnounceInfo AS AI ");
            selectSql.append("LEFT OUTER JOIN BBTA_AnnounceRedstone AS AR ");
            selectSql.append("ON AI.REDSTONE_ID = AR.REDSTONE_ID ");
            selectSql.append("WHERE ");
            selectSql.append("AI.UUID = ? AND ");
            selectSql.append("AI.NEXT_STATION = ? AND ");
            selectSql.append("AI.LINE_NAME = ? ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, stationName);
            ps.setString(p++, lineName);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                AnnounceInfoModel model = new AnnounceInfoModel();
                model.setUUID(result.getString("AI.UUID"));
                model.setNextOrSoon(result.getInt("AI.NEXT_OR_SOON"));
                model.setWorldName(result.getString("AI.WORLD"));
                model.setPosX(result.getInt("AI.X"));
                model.setPosY(result.getInt("AI.Y"));
                model.setPosZ(result.getInt("AI.Z"));
                model.setNextStationNameKanji(result.getString("AI.NEXT_STATION"));
                model.setLineNameJP(result.getString("AI.LINE_NAME"));
                model.setBoundJP(result.getString("AI.BOUND_JP"));
                model.setBoundEN(result.getString("AI.BOUND_EN"));
                model.setTypeJP(result.getString("AI.TYPE_JP"));
                model.setExit(result.getString("AI.DOOR_SIDE"));
                if(result.getInt("AI.TERMINAL") == 0){
                    model.setTerminal(false);
                }else{
                    model.setTerminal(true);
                }
                model.setDirection(result.getInt("AI.DIRECTION"));
                model.setViaLineNameJP((result.getString("AI.VIA_LINE_NAME")));
                model.setViaLineOwnerUUID((result.getString("AI.VIA_LINE_OWNER_UUID")));

                result.getInt("AI.REDSTONE_ID");
                if(result.wasNull()){
                    model.setRedstone(false);
                }else{
                    model.setRedstone(true);
                    model.setOnOrOff(result.getInt("AR.STATUS"));
                    model.setRedstonePosX(result.getInt("AR.X"));
                    model.setRedstonePosY(result.getInt("AR.Y"));
                    model.setRedstonePosZ(result.getInt("AR.Z"));
                }
                if(result.getInt("AI.FAST_FLAG") == 0){
                    model.setAnnounceFastFlag(false);
                }else{
                    model.setAnnounceFastFlag(true);
                }

                resultList.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultList = null;
        }

        return resultList;
    }

    /**
     * アナウンス情報を登録
     * @param announceInfoModel
     * @return
     */
    public int insertAnnounceInfo(AnnounceInfoModel announceInfoModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("(UUID, Next_OR_Soon, WORLD, X, Y, Z, NEXT_STATION, LINE_NAME, TYPE_JP, DOOR_SIDE) ");
            insertSql.append("VALUES (?,?,?,?,?,?,?,?,?,?)");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, announceInfoModel.getUUID());
            ps.setInt(p++, announceInfoModel.getNextOrSoon());
            ps.setString(p++, announceInfoModel.getWorldName());
            ps.setInt(p++, announceInfoModel.getPosX());
            ps.setInt(p++, announceInfoModel.getPosY());
            ps.setInt(p++, announceInfoModel.getPosZ());
            ps.setString(p++, announceInfoModel.getNextStationNameKanji());
            ps.setString(p++, announceInfoModel.getLineNameJP());
            ps.setString(p++, announceInfoModel.getTypeJP());
            ps.setString(p++, announceInfoModel.getExit());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * アナウンス情報をアップデート
     */
    public int updateAnnounceInfo(AnnounceInfoModel announceInfoModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("SET ");
            insertSql.append("NEXT_STATION = ?, ");
            insertSql.append("LINE_NAME = ?, ");
            insertSql.append("BOUND_JP = ?, ");
            insertSql.append("BOUND_EN = ?, ");
            insertSql.append("TYPE_JP = ?, ");
            insertSql.append("DOOR_SIDE = ?, ");
            insertSql.append("TERMINAL = ?, ");
            insertSql.append("DIRECTION = ?, ");
            insertSql.append("VIA_LINE_NAME = ?, ");
            insertSql.append("VIA_LINE_OWNER_UUID = ?, ");
            insertSql.append("FAST_FLAG = ? ");
            insertSql.append("WHERE ");
            insertSql.append("WORLD = ? AND ");
            insertSql.append("X = ? AND ");
            insertSql.append("Y = ? AND ");
            insertSql.append("Z = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, announceInfoModel.getNextStationNameKanji());
            ps.setString(p++, announceInfoModel.getLineNameJP());
            ps.setString(p++, announceInfoModel.getBoundJP());
            ps.setString(p++, announceInfoModel.getBoundEN());
            ps.setString(p++, announceInfoModel.getTypeJP());
            ps.setString(p++, announceInfoModel.getExit());
            if(!announceInfoModel.isTerminal()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }
            ps.setInt(p++, announceInfoModel.getDirection());
            ps.setString(p++, announceInfoModel.getViaLineNameJP());
            ps.setString(p++, announceInfoModel.getViaLineOwnerUUID());
            if(!announceInfoModel.isAnnounceFastFlag()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            ps.setString(p++, announceInfoModel.getWorldName());
            ps.setInt(p++, announceInfoModel.getPosX());
            ps.setInt(p++, announceInfoModel.getPosY());
            ps.setInt(p++, announceInfoModel.getPosZ());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * アナウンスレッドストーン情報を登録
     * @param announceInfoModel
     * @return
     */
    public int insertAnnounceRedstone(AnnounceInfoModel announceInfoModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO ");
            insertSql.append("BBTA_AnnounceRedstone ");
            insertSql.append("(STATUS, WORLD, X, Y, Z) ");
            insertSql.append("VALUES (?,?,?,?,?)");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setInt(p++, announceInfoModel.getOnOrOff());
            ps.setString(p++, announceInfoModel.getWorldName());
            ps.setInt(p++, announceInfoModel.getRedstonePosX());
            ps.setInt(p++, announceInfoModel.getRedstonePosY());
            ps.setInt(p++, announceInfoModel.getRedstonePosZ());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * アナウンスレッドストーンテーブルからレッドストーンIDを削除
     * @param redstoneID
     * @return
     */
    public int removeRedstoneIDOfAnnounceRedstone(int redstoneID){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("WHERE ");
            insertSql.append("REDSTONE_ID = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());

            ps.setInt(p++, redstoneID);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 最後に追加したレッドストーンIDを取得
     * @return
     */
    public int getRedstoneIDOfLatest(){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT REDSTONE_ID FROM ");
            selectSql.append("BBTA_AnnounceRedstone ");
            selectSql.append("ORDER BY ");
            selectSql.append("REDSTONE_ID DESC ");
            selectSql.append("LIMIT 1 ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){

                result = resultSet.getInt("REDSTONE_ID");

            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        }

        return result;
    }

    /**
     * 引数のアナウンス地点からレッドストーンIDを取得
     * @param worldName
     * @param posX
     * @param posY
     * @param posZ
     * @return
     */
    public int getRedstoneIDForAnnouncePosition(String worldName, int posX, int posY, int posZ){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT AR.REDSTONE_ID AS REDSTONE_ID FROM ");
            selectSql.append("BBTA_AnnounceInfo AS AI ");
            selectSql.append("LEFT OUTER JOIN BBTA_AnnounceRedstone AS AR ");
            selectSql.append("ON AI.REDSTONE_ID = AR.REDSTONE_ID ");
            selectSql.append("WHERE ");
            selectSql.append("AI.WORLD = ? AND ");
            selectSql.append("AI.X = ? AND ");
            selectSql.append("AI.Y = ? AND ");
            selectSql.append("AI.Z = ? ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, worldName);
            ps.setInt(p++, posX);
            ps.setInt(p++, posY);
            ps.setInt(p++, posZ);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){

                result = resultSet.getInt("REDSTONE_ID");

            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        }

        return result;
    }

    /**
     * アナウンス情報テーブルにレッドストーンIDを追加
     * @param announceInfoModel
     * @param redstoneID
     * @return
     */
    public int addRedstoneIDOfAnnounceInfo(AnnounceInfoModel announceInfoModel, int redstoneID){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("SET ");
            insertSql.append("REDSTONE_ID = ? ");
            insertSql.append("WHERE ");
            insertSql.append("WORLD = ? AND ");
            insertSql.append("X = ? AND ");
            insertSql.append("Y = ? AND ");
            insertSql.append("Z = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());

            ps.setInt(p++, redstoneID);
            ps.setString(p++, announceInfoModel.getWorldName());
            ps.setInt(p++, announceInfoModel.getPosX());
            ps.setInt(p++, announceInfoModel.getPosY());
            ps.setInt(p++, announceInfoModel.getPosZ());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * アナウンス情報テーブルからレッドストーンIDを削除
     * @param redstoneID
     * @return
     */
    public int removeRedstoneIDOfAnnounceInfo(int redstoneID){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("SET ");
            insertSql.append("REDSTONE_ID = null ");
            insertSql.append("WHERE ");
            insertSql.append("REDSTONE_ID = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());

            ps.setInt(p++, redstoneID);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線名を変更
     * @param UUID
     * @param oldLineName
     * @param newLineName
     * @return
     */
    public int changeLineName(String UUID, String oldLineName, String newLineName){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("SET ");
            insertSql.append("LINE_NAME = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, newLineName);
            ps.setString(p++, UUID);
            ps.setString(p++, oldLineName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 種別名を変更
     * @param UUID
     * @param oldType
     * @param newType
     * @return
     */
    public int changeTypeName(String UUID, String lineName, String oldType, String newType){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("SET ");
            insertSql.append("TYPE_JP = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? AND ");
            insertSql.append("TYPE_JP = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, oldType);
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, oldType);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 駅名を変更
     * @param UUID
     * @param oldStationName
     * @param newStationName
     * @return
     */
    public int changeStationName(String UUID, String lineName, String oldStationName, String newStationName){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("SET ");
            insertSql.append("NEXT_STATION = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? AND ");
            insertSql.append("NEXT_STATION = ? ");


            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, newStationName);
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, oldStationName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * アナウンス情報を削除
     * @param announceInfoModel
     * @return
     */
    public int removeAnnounce(AnnounceInfoModel announceInfoModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("WHERE ");
            insertSql.append("WORLD = ? AND ");
            insertSql.append("X = ? AND ");
            insertSql.append("Y = ? AND ");
            insertSql.append("Z = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, announceInfoModel.getWorldName());
            ps.setInt(p++, announceInfoModel.getPosX());
            ps.setInt(p++, announceInfoModel.getPosY());
            ps.setInt(p++, announceInfoModel.getPosZ());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線名と駅名をもとに、該当する駅のアナウンス情報を削除
     * @param lineName
     * @param stationName
     * @return
     */
    public int removeAnnounceFromStation(String UUID, String lineName, String stationName){

        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? AND ");
            insertSql.append("NEXT_STATION = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, stationName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線名をもとに、アナウンス情報を削除
     * @param lineName
     * @return
     */
    public int removeAnnounceFromLine(String UUID, String lineName){

        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線名・種別をもとに、アナウンス情報を削除
     * @param lineName
     * @return
     */
    public int removeAnnounceFromType(String UUID, String lineName, String typeJP){

        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_AnnounceInfo ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? AND ");
            insertSql.append("TYPE_JP = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, typeJP);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}


