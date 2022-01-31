package com.github.rypengu23.bossbartrainannounce.dao;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LineDao {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public LineDao(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 全ての路線情報を取得取得する
     * @return
     */
    public ArrayList<LineModel> getLineListAll(){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        ArrayList<LineModel> resultList = new ArrayList<>();

        try {
            int p = 1;
            LineModel model = new LineModel();

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_LineInfo ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());

            ResultSet result = ps.executeQuery();

            while(result.next()){
                model = new LineModel();
                model.setUUID(result.getString("UUID"));
                model.setLineNameJP(result.getString("NAME_JP"));
                model.setLineNameEN(result.getString("NAME_EN"));
                model.setLineColor(result.getString("LINE_COLOR"));
                model.setType(model.convertTypeHashMap(result.getString("TYPE_JP"), result.getString("TYPE_EN")));
                if(result.getInt("RING") == 1){
                    model.setLoop(true);
                }else{
                    model.setLoop(false);
                }
                resultList.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 路線名から路線情報を取得。引数は何語でも可
     * @param lineName
     * @return
     */
    public LineModel getLine(String UUID, String lineName){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        LineModel model = new LineModel();

        try {
            int p = 1;

            StringBuilder selectSql = new StringBuilder();
            selectSql.append("SELECT * FROM ");
            selectSql.append("BBTA_LineInfo ");
            selectSql.append("WHERE ");
            selectSql.append("UUID = ? AND ");
            selectSql.append("(NAME_JP = ? OR ");
            selectSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(selectSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                model = new LineModel();
                model.setUUID(result.getString("UUID"));
                model.setLineNameJP(result.getString("NAME_JP"));
                model.setLineNameEN(result.getString("NAME_EN"));
                model.setLineColor(result.getString("LINE_COLOR"));
                model.setType(model.convertTypeHashMap(result.getString("TYPE_JP"), result.getString("TYPE_EN")));
                if(result.getInt("RING") == 1){
                    model.setLoop(true);
                }else{
                    model.setLoop(false);
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
     * 路線情報を登録
     * @param lineModel
     * @return
     */
    public int insertLine(LineModel lineModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO ");
            insertSql.append("BBTA_LineInfo ");
            insertSql.append("(UUID, NAME_JP, NAME_EN, LINE_COLOR, TYPE_JP, TYPE_EN, RING) ");
            insertSql.append("VALUES (?,?,?,?,?,?,?)");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, lineModel.getUUID());
            ps.setString(p++, lineModel.getLineNameJP());
            ps.setString(p++, lineModel.getLineNameEN());
            ps.setString(p++, lineModel.getLineColor());
            ps.setString(p++, lineModel.convertTypeCommaStr(0, lineModel.getType()));
            ps.setString(p++, lineModel.convertTypeCommaStr(1, lineModel.getType()));
            if(!lineModel.isLoop()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 路線情報を編集
     * @param UUID
     * @param lineModel
     * @return
     */
    public int changeLineInfo(String UUID, LineModel lineModel){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_LineInfo ");
            insertSql.append("SET ");
            insertSql.append("LINE_COLOR = ?, ");
            insertSql.append("TYPE_JP = ?, ");
            insertSql.append("TYPE_EN = ?, ");
            insertSql.append("RING = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("(NAME_JP = ? OR ");
            insertSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, lineModel.getLineColor());
            ps.setString(p++, lineModel.convertTypeCommaStr(0, lineModel.getType()));
            ps.setString(p++, lineModel.convertTypeCommaStr(1, lineModel.getType()));
            if(!lineModel.isLoop()){
                ps.setInt(p++, 0);
            }else{
                ps.setInt(p++, 1);
            }
            ps.setString(p++, lineModel.getUUID());
            ps.setString(p++, lineModel.getLineNameJP());
            ps.setString(p++, lineModel.getLineNameEN());

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
     * @param newLineNameJP
     * @param newLineNameEN
     * @return
     */
    public int changeLineName(String UUID, String oldLineName, String newLineNameJP, String newLineNameEN){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("UPDATE ");
            insertSql.append("BBTA_LineInfo ");
            insertSql.append("SET ");
            insertSql.append("NAME_JP = ?, ");
            insertSql.append("NAME_EN = ? ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("(NAME_JP = ? OR ");
            insertSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, newLineNameJP);
            ps.setString(p++, newLineNameEN);
            ps.setString(p++, UUID);
            ps.setString(p++, oldLineName);
            ps.setString(p++, oldLineName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 路線情報を削除
     * @param lineName
     * @return
     */
    public int removeLine(String UUID, String lineName){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("DELETE FROM ");
            insertSql.append("BBTA_LineInfo ");
            insertSql.append("WHERE ");
            insertSql.append("UUID = ? AND ");
            insertSql.append("(NAME_JP = ? OR ");
            insertSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(insertSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 引数の路線名から日本語の路線名を取得
     * @param lineName
     * @return
     */
    public String getLineNameJP(String UUID, String lineName) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        String resultStr = "";

        try {
            int p = 1;

            StringBuilder searchSql = new StringBuilder();
            searchSql.append("SELECT NAME_JP FROM ");
            searchSql.append("BBTA_LineInfo ");
            searchSql.append("WHERE ");
            searchSql.append("UUID = ? AND ");
            searchSql.append("(NAME_JP = ? OR ");
            searchSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(searchSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                resultStr = result.getString("NAME_JP");
            } else {
                resultStr = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return resultStr;
        }
    }

    /**
     * 引数の種別名を日本語に変換
     * @param UUID
     * @param lineName
     * @param type
     * @return
     */
    public String getTypeJP(String UUID, String lineName, String type) {

        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        CheckUtil checkUtil = new CheckUtil();


        String typeJP = "";
        String typeEN = "";
        String resultStr = "";

        try {
            int p = 1;

            StringBuilder searchSql = new StringBuilder();
            searchSql.append("SELECT TYPE_JP,TYPE_EN FROM ");
            searchSql.append("BBTA_LineInfo ");
            searchSql.append("WHERE ");
            searchSql.append("UUID = ? AND ");
            searchSql.append("(NAME_JP = ? OR ");
            searchSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(searchSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                typeJP = result.getString("TYPE_JP");
                typeEN = result.getString("TYPE_EN");
            } else {
                resultStr = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] typeJPList = typeJP.split(",");
        String[] typeENList = typeEN.split(",");

        for(int i=0; i<typeJPList.length; i++){
            if(typeJPList[i].equalsIgnoreCase(type) || typeENList[i].equalsIgnoreCase(type)){
                resultStr = typeJPList[i];
                break;
            }
            if(i==typeJPList.length-1){
                resultStr = null;
            }
        }

        return resultStr;

    }

    /**
     * 引数の種別名を英語に変換
     * @param UUID
     * @param lineName
     * @param type
     * @return
     */
    public String getTypeEN(String UUID, String lineName, String type) {

        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();

        String typeJP = "";
        String typeEN = "";
        String resultStr = "";

        try {
            int p = 1;

            StringBuilder searchSql = new StringBuilder();
            searchSql.append("SELECT TYPE_JP,TYPE_EN FROM ");
            searchSql.append("BBTA_LineInfo ");
            searchSql.append("WHERE ");
            searchSql.append("UUID = ? AND ");
            searchSql.append("(NAME_JP = ? OR ");
            searchSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(searchSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                typeJP = result.getString("TYPE_JP");
                typeEN = result.getString("TYPE_EN");
            } else {
                resultStr = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] typeJPList = typeJP.split(",");
        String[] typeENList = typeEN.split(",");

        for(int i=0; i<typeJPList.length; i++){
            if(typeJPList[i].equalsIgnoreCase(type) || typeENList[i].equalsIgnoreCase(type)){
                resultStr = typeENList[i];
                break;
            }
            if(i==typeJPList.length-1){
                resultStr = null;
            }
        }


        return resultStr;
    }

    /**
     * 引数の路線名の種別名が存在しているか確認
     * @param lineName
     * @param type
     * @return
     */
    public boolean checkTypeExit(String UUID, String lineName, String type) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();

        boolean resultFlag = false;
        String typeJP = "";
        String typeEN = "";

        try {
            int p = 1;

            StringBuilder searchSql = new StringBuilder();
            searchSql.append("SELECT TYPE_JP,TYPE_EN FROM ");
            searchSql.append("BBTA_LineInfo ");
            searchSql.append("WHERE ");
            searchSql.append("UUID = ? AND ");
            searchSql.append("(NAME_JP = ? OR ");
            searchSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(searchSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                typeJP = resultSet.getString("TYPE_JP");
                typeEN = resultSet.getString("TYPE_EN");
            }

            String[] typeJPList = typeJP.split(",");
            String[] typeENList = typeEN.split(",");

            for(String work:typeJPList){
                if(work.equalsIgnoreCase(type)){
                    resultFlag = true;
                    break;
                }
            }

            for(String work:typeENList){
                if(work.equalsIgnoreCase(type) || resultFlag){
                    resultFlag = true;
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultFlag;
    }

    /**
     * 引数の路線名が存在しているか確認
     * @param lineName
     * @return
     */
    public boolean checkLineExit(String UUID, String lineName) {
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        boolean resultFlag = false;

        try {
            int p = 1;

            StringBuilder searchSql = new StringBuilder();
            searchSql.append("SELECT NAME_JP ");
            searchSql.append("FROM BBTA_LineInfo ");
            searchSql.append("WHERE ");
            searchSql.append("UUID = ? AND ");
            searchSql.append("(NAME_JP = ? OR ");
            searchSql.append("NAME_EN = ?) ");

            PreparedStatement ps = connection.prepareStatement(searchSql.toString());
            ps.setString(p++, UUID);
            ps.setString(p++, lineName);
            ps.setString(p++, lineName);

            ResultSet result = ps.executeQuery();

            if(result.next()){
                resultFlag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultFlag;
    }
}
