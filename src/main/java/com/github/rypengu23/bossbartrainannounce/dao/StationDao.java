package com.github.rypengu23.bossbartrainannounce.dao;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class StationDao {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public StationDao() {
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 全ての駅情報を取得
     *
     * @return
     */
    public HashSet<StationModel> getStationListAll() {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        HashSet<StationModel> resultList = new HashSet<>();

        try {
            int p = 1;
            StationModel model = new StationModel();

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT WORLD,POS1X,POS1Y,POS1Z,POS2X,POS2Y,POS2Z FROM ");
            selectSql.append("BBTA_StationInfo ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());

            ResultSet result = ps.executeQuery();

            while (result.next()) {
                model = new StationModel();

                model.setSelectPositionModel(new SelectPositionModel(result.getString("WORLD"), result.getInt("pos1X"), result.getInt("pos1Y"), result.getInt("pos1Z"), result.getInt("pos2X"), result.getInt("pos2Y"), result.getInt("pos2Z")));

                resultList.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 引数の地点が駅内か判定し、駅内であれば駅情報を返す
     *
     * @param position
     * @return
     */
    public StationModel getStationForCoordinate(Location position) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        StationModel model = new StationModel();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_StationInfo ");
            selectSql.append("WHERE ");
            selectSql.append("WORLD = ? AND ");
            selectSql.append("pos1X <= ? AND ");
            selectSql.append("? <= pos2X AND ");
            selectSql.append("pos1Y <= ? AND ");
            selectSql.append("? <= pos2Y AND ");
            selectSql.append("pos1Z <= ? AND ");
            selectSql.append("? <= pos2Z ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, position.getWorld().getName());
            ps.setInt(p++, position.getBlockX());
            ps.setInt(p++, position.getBlockX());
            ps.setInt(p++, position.getBlockY());
            ps.setInt(p++, position.getBlockY());
            ps.setInt(p++, position.getBlockZ());
            ps.setInt(p++, position.getBlockZ());

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                model.setUUID(result.getString("UUID"));
                model.setStationNameKanji(result.getString("NAME_KANJI"));
                model.setStationNameEnglish(result.getString("NAME_EN"));
                model.setStationNameKatakana(result.getString("NAME_KATAKANA"));
                model.setLineNameJp(result.getString("LINE_NAME"));
                model.setSelectPositionModel(new SelectPositionModel(result.getString("WORLD"), result.getInt("pos1X"), result.getInt("pos1Y"), result.getInt("pos1Z"), result.getInt("pos2X"), result.getInt("pos2Y"), result.getInt("pos2Z")));
                model.setNumber(result.getString("NUMBER"));
            } else {
                model = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    /**
     * 駅名から、駅情報を取得
     *
     * @param stationName
     * @return
     */
    public StationModel getStationForStationName(String UUID, String stationName, String lineNameJP) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        StationModel model = new StationModel();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_StationInfo ");
            selectSql.append("WHERE ");
            selectSql.append("UUID = ? AND ");
            selectSql.append("(NAME_KANJI = ? OR ");
            selectSql.append("NAME_EN = ? OR ");
            selectSql.append("NAME_KATAKANA = ?) AND ");
            selectSql.append("LINE_NAME = ?");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);
            ps.setString(p++, lineNameJP);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                model.setUUID(result.getString("UUID"));
                model.setStationNameKanji(result.getString("NAME_KANJI"));
                model.setStationNameEnglish(result.getString("NAME_EN"));
                model.setStationNameKatakana(result.getString("NAME_KATAKANA"));
                model.setLineNameJp(result.getString("LINE_NAME"));
                model.setSelectPositionModel(new SelectPositionModel(result.getString("WORLD"), result.getInt("pos1X"), result.getInt("pos1Y"), result.getInt("pos1Z"), result.getInt("pos2X"), result.getInt("pos2Y"), result.getInt("pos2Z")));
                model.setNumber(result.getString("NUMBER"));
            } else {
                model = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    /**
     * 駅に所属している全ての路線情報を取得
     * 戻り値は引数の路線名を除外する
     *
     * @param stationNameJp
     * @param lineNameJp
     * @return
     */
    public ArrayList<ArrayList<String>> getLineBelongsStation(String stationNameJp, int posX, int posY, int posZ, String lineNameJp) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();


        ArrayList<ArrayList<String>> resultList = new ArrayList<>();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT UUID,LINE_NAME FROM ");
            selectSql.append("BBTA_StationInfo ");
            selectSql.append("WHERE ");
            selectSql.append("NAME_KANJI = ? AND ");
            selectSql.append("? <= POS1X AND ");
            selectSql.append("POS1X <= ? AND ");
            selectSql.append("? <= POS2X AND ");
            selectSql.append("POS2X <= ? AND ");
            selectSql.append("? <= POS1Y AND ");
            selectSql.append("POS1Y <= ? AND ");
            selectSql.append("? <= POS2Y AND ");
            selectSql.append("POS2Y <= ? AND ");
            selectSql.append("? <= POS1Z AND ");
            selectSql.append("POS1Z <= ? AND ");
            selectSql.append("? <= POS2Z AND ");
            selectSql.append("POS2Z <= ? ");
            if (lineNameJp != null) {
                selectSql.append("AND LINE_NAME != ? ");
            }

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, stationNameJp);
            ps.setInt(p++, posX - mainConfig.getTransferRange());
            ps.setInt(p++, posX + mainConfig.getTransferRange());
            ps.setInt(p++, posX - mainConfig.getTransferRange());
            ps.setInt(p++, posX + mainConfig.getTransferRange());
            ps.setInt(p++, posY - mainConfig.getTransferRange());
            ps.setInt(p++, posY + mainConfig.getTransferRange());
            ps.setInt(p++, posY - mainConfig.getTransferRange());
            ps.setInt(p++, posY + mainConfig.getTransferRange());
            ps.setInt(p++, posZ - mainConfig.getTransferRange());
            ps.setInt(p++, posZ + mainConfig.getTransferRange());
            ps.setInt(p++, posZ - mainConfig.getTransferRange());
            ps.setInt(p++, posZ + mainConfig.getTransferRange());
            if (lineNameJp != null) {
                ps.setString(p++, lineNameJp);
            }

            ResultSet result = ps.executeQuery();

            while (result.next()) {
                ArrayList<String> work = new ArrayList<>();
                work.add(result.getString("UUID"));
                work.add(result.getString("LINE_NAME"));
                resultList.add(work);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 引数の駅名から、漢字の駅名を取得
     * @param UUID
     * @param lineName
     * @param stationName
     * @return
     */
    public String getStationNameKanji(String UUID, String lineName, String stationName) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        LineDao lineDao = new LineDao();
        lineName = lineDao.getLineNameJP(UUID, lineName);

        String resultStr = "";

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT NAME_KANJI FROM ");
            selectSql.append("BBTA_StationInfo ");
            selectSql.append("WHERE ");
            selectSql.append("UUID = ? AND ");
            selectSql.append("LINE_NAME = ? AND ");
            selectSql.append("(NAME_KANJI = ? OR ");
            selectSql.append("NAME_EN = ? OR ");
            selectSql.append("NAME_KATAKANA = ?) ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                resultStr = result.getString("NAME_KANJI");
            } else {
                resultStr = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultStr;
    }

    /**
     * 駅情報を登録
     *
     * @param stationModel
     * @return
     */
    public int registStation(StationModel stationModel) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;
            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO ");
            insertSql.append("BBTA_StationInfo ");
            insertSql.append("(UUID, LINE_NAME, NAME_KANJI, NAME_EN, NAME_KATAKANA, WORLD, POS1X, POS1Y, POS1Z, POS2X, POS2Y, POS2Z, NUMBER) ");
            insertSql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, stationModel.getUUID());
            ps.setString(p++, stationModel.getLineNameJp());
            ps.setString(p++, stationModel.getStationNameKanji());
            ps.setString(p++, stationModel.getStationNameEnglish());
            ps.setString(p++, stationModel.getStationNameKatakana());
            ps.setString(p++, stationModel.getSelectPositionModel().getWorldName());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos1X());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos1Y());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos1Z());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos2X());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos2Y());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos2Z());
            ps.setString(p++, stationModel.getNumber());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 駅情報を更新
     * @param UUID
     * @param stationModel
     * @return
     */
    public int changeStationInfo(String UUID, StationModel stationModel) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;
            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_StationInfo ");
            insertSql.append("SET ");
            insertSql.append("WORLD = ?, ");
            insertSql.append("POS1X = ?, ");
            insertSql.append("POS1Y = ?, ");
            insertSql.append("POS1Z = ?, ");
            insertSql.append("POS2X = ?, ");
            insertSql.append("POS2Y = ?, ");
            insertSql.append("POS2Z = ?, ");
            insertSql.append("NUMBER = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? AND ");
            insertSql.append("(NAME_KANJI = ? OR ");
            insertSql.append("NAME_EN = ? OR ");
            insertSql.append("NAME_KATAKANA = ?) ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            System.out.println(insertSql.toString());
            ps.setString(p++, stationModel.getSelectPositionModel().getWorldName());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos1X());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos1Y());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos1Z());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos2X());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos2Y());
            ps.setInt(p++, stationModel.getSelectPositionModel().getPos2Z());
            ps.setString(p++, stationModel.getNumber());
            ps.setString(p++, stationModel.getUUID());
            ps.setString(p++, stationModel.getLineNameJp());
            ps.setString(p++, stationModel.getStationNameKanji());
            ps.setString(p++, stationModel.getStationNameEnglish());
            ps.setString(p++, stationModel.getStationNameKatakana());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 駅名を変更
     * @param UUID
     * @param lineNameJp
     * @param oldName
     * @param newStationNameJp
     * @param newStationNameEn
     * @param newStationNameKatakana
     * @return
     */
    public int changeStationName(String UUID, String lineNameJp, String oldName, String newStationNameJp, String newStationNameEn, String newStationNameKatakana) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;
            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_StationInfo ");
            insertSql.append("SET ");
            insertSql.append("NAME_KANJI = ?, ");
            insertSql.append("NAME_EN = ?, ");
            insertSql.append("NAME_KATAKANA = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? AND ");
            insertSql.append("(NAME_KANJI = ? OR ");
            insertSql.append("NAME_EN = ? OR ");
            insertSql.append("NAME_KATAKANA = ?) ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, newStationNameJp);
            ps.setString(p++, newStationNameEn);
            ps.setString(p++, newStationNameKatakana);
            ps.setString(p++, UUID);
            ps.setString(p++, lineNameJp);
            ps.setString(p++, oldName);
            ps.setString(p++, oldName);
            ps.setString(p++, oldName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線名を変更
     * @param UUID
     * @param oldLineNameJp
     * @param newLineNameJp
     * @return
     */
    public int changeLineName(String UUID, String oldLineNameJp, String newLineNameJp) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;
            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_StationInfo ");
            insertSql.append("SET ");
            insertSql.append("LINE_NAME = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("LINE_NAME = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, newLineNameJp);
            ps.setString(p++, UUID);
            ps.setString(p++, oldLineNameJp);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 駅情報を削除
     * @param UUID
     * @param lineName
     * @param stationName
     * @return
     */
    public int removeStationFromStationName(String UUID, String lineName, String stationName) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_StationInfo ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("(NAME_KANJI = ? OR ");
            insertSql.append("NAME_EN = ? OR ");
            insertSql.append("NAME_KATAKANA = ?) AND ");
            insertSql.append("LINE_NAME = ? ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);
            ps.setString(p++, lineName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線名をもとに、駅情報を削除
     * @param UUID
     * @param lineName
     * @return
     */
    public int removeStationFromStationName(String UUID, String lineName) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_StationInfo ");
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
     * 引数の路線に引数の駅名が存在するか確認
     *
     * @param UUID
     * @param lineName
     * @param stationName
     * @return
     */
    public boolean checkStationExit(String UUID, String lineName, String stationName) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        LineDao lineDao = new LineDao();

        //路線名を漢字に変換
        lineName = lineDao.getLineNameJP(UUID, lineName);
        int result = 0;

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT NAME_KANJI FROM ");
            selectSql.append("BBTA_StationInfo ");
            selectSql.append("WHERE ");
            selectSql.append("UUID = ? AND ");
            selectSql.append("LINE_NAME = ? AND ");
            selectSql.append("(NAME_KANJI = ? OR ");
            selectSql.append("NAME_EN = ? OR ");
            selectSql.append("NAME_KATAKANA = ?) ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);
            ps.setString(p++, stationName);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }
}
