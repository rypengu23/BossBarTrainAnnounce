package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.util.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.ConvertUtil;
import com.github.rypengu23.bossbartrainannounce.util.StationLocationJudgeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Command_line {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_line(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 路線関連コマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        ConvertUtil convertUtil = new ConvertUtil();
        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("registline")){

            if(args.length == 6){
                //登録(環状線でない)
                registLine(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), args[3], convertUtil.convertPercentAndColorCode(args[4]), convertUtil.convertPercentAndColorCode(args[5]), "false");
            }else if(args.length == 7){
                //登録(環状線)
                registLine(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), args[3], convertUtil.convertPercentAndColorCode(args[4]), convertUtil.convertPercentAndColorCode(args[5]), args[6]);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }

        }else if(args[0].equalsIgnoreCase("removeline")){

            if(args.length == 2){
                //路線削除
                removeLine(player, convertUtil.convertDoublePercentToSpace(args[1]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("changelinename")){

            if(args.length == 4){
                //路線名変更
                changeLineName(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]));
            }else{
                //不正
                player.sendMessage("[§c"+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("changelinecolor")){

            if(args.length == 3){
                //路線カラー変更
                changeLineColor(player, convertUtil.convertDoublePercentToSpace(args[1]), args[2]);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("addtype")){

            if(args.length == 4){
                //種別追加
                addType(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertPercentAndColorCode(args[2]), convertUtil.convertPercentAndColorCode(args[3]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("removetype")){

            if(args.length == 3){
                //種別削除
                removeType(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertPercentAndColorCode(args[2]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("changeTypeName")){

            if(args.length == 5){
                //種別削除
                changeTypeName(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertPercentAndColorCode(args[2]), convertUtil.convertPercentAndColorCode(args[3]), convertUtil.convertPercentAndColorCode(args[4]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else{
            //不正
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
            return;
        }
    }

    /**
     * 引数のコマンドが路線関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("registline");
        commandList.add("removeline");
        commandList.add("changelinename");
        commandList.add("changelinecolor");
        commandList.add("addtype");
        commandList.add("removetype");
        commandList.add("changetypename");

        return commandList.contains(command.toLowerCase());
    }

    /**
     * 路線情報を登録
     * @param player
     * @param lineNameJp
     * @param lineNameEn
     * @param lineColor
     * @param typeJP
     * @param typeEN
     * @return
     */
    public boolean registLine(Player player, String lineNameJp, String lineNameEn, String lineColor, String typeJP, String typeEN, String loop){

        LineDao lineDao = new LineDao();
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registLine")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //入力値チェック
        //路線名JP
        //存在チェック
        if(lineDao.checkLineExit(player.getUniqueId().toString(), lineNameJp) || lineDao.checkLineExit(player.getUniqueId().toString(), lineNameEn)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f同じ路線名が存在します。");
            return false;
        }

        //路線名EN
        //英数字記号チェック
        if(!checkUtil.checkAlphabetOrSymbol(lineNameEn)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名(英語)はアルファベット・数字・記号で入力してください");
            return false;
        }

        //ラインカラー
        //色存在チェック
        if(!checkUtil.checkColorExist(lineColor)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f色名/色コード："+ lineColor +"は存在しません。");
            return false;
        }

        //種別名EN
        //英数字記号チェック
        if(!checkUtil.checkAlphabetOrSymbol(typeEN)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別名(英語)はアルファベット・数字・記号で入力してください");
            return false;
        }

        //環状線フラグチェック
        if(!(loop.equalsIgnoreCase("true") || loop.equalsIgnoreCase("false"))){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f環状線フラグはtrueと入力して下さい。");
            return false;
        }
        boolean loopFlag = false;
        if(loop.equalsIgnoreCase("true")){
            loopFlag = true;
        }

        LineModel lineModel = new LineModel();
        HashMap<String, String> typeHashMap = lineModel.convertTypeHashMap(typeJP, typeEN);

        if(lineDao.insertLine(new LineModel(player.getUniqueId().toString() ,lineNameJp, lineNameEn, lineColor, typeHashMap, loopFlag)) > 0){
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f路線を登録しました！");
            player.sendMessage("§f路線名(和名): "+ lineNameJp +" §f路線名(英語): "+ lineNameEn);
            return true;
        }

        return false;
    }

    /**
     * 路線を削除
     * @param player
     * @param lineName
     */
    private boolean removeLine(Player player, String lineName) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.removeLine")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }
        //路線名を漢字に変換
        lineName = lineDao.getLineNameJP(player.getUniqueId().toString(), lineName);

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), lineName);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線情報を削除
        if(lineDao.removeLine(player.getUniqueId().toString(), lineName) > 0){

            player.sendMessage("§b["+ mainConfig.getPrefix() +"] §f路線を削除しました！");
            player.sendMessage("§f路線名: "+ lineName);

            //登録されているアナウンスを削除
            announceInfoDao.removeAnnounceFromLine(player.getUniqueId().toString(), lineName);
            //駅削除
            stationDao.removeStationFromStationName(player.getUniqueId().toString(), lineName);

            //アナウンスロケーション情報のアップデート
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();
            stationLocationJudgeUtil.updateStationListCache();

            return true;
        }
        return false;
    }

    /**
     * 路線名を変更
     * @param player
     * @param oldLineName
     * @param newLineNameJP
     * @param newLineNameEN
     */
    private boolean changeLineName(Player player, String oldLineName, String newLineNameJP, String newLineNameEN) {

        LineDao lineDao = new LineDao();
        StationDao stationDao = new StationDao();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.changeLineName")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), oldLineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }
        //路線名を漢字に変換
        oldLineName = lineDao.getLineNameJP(player.getUniqueId().toString(), oldLineName);

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), oldLineName);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //入力値チェック
        //路線名JP
        //存在チェック
        if(lineDao.checkLineExit(player.getUniqueId().toString(), newLineNameJP) || lineDao.checkLineExit(player.getUniqueId().toString(), newLineNameEN)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f同じ路線名が存在します。");
            return false;
        }

        //路線名EN
        //英数字記号チェック
        if(!checkUtil.checkAlphabetOrSymbol(newLineNameEN)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名(英語)はアルファベット・数字・記号で入力してください");
            return false;
        }

        //更新
        if(lineDao.changeLineName(player.getUniqueId().toString(), oldLineName, newLineNameJP, newLineNameEN) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f路線名を変更しました！");
            player.sendMessage("§f旧路線名: "+ oldLineName +" 新路線名(和名): "+ newLineNameJP+" 新路線名(英語): "+ newLineNameEN);

            //アナウンス情報テーブルも更新
            announceInfoDao.changeLineName(player.getUniqueId().toString(), oldLineName, newLineNameJP);
            //駅情報テーブルも更新
            stationDao.changeLineName(player.getUniqueId().toString(), oldLineName, newLineNameJP);
            return true;
        }

        return false;
    }

    /**
     * 路線カラーを変更
     * @param player
     * @param lineName
     * @param lineColor
     */
    private boolean changeLineColor(Player player, String lineName, String lineColor) {

        LineDao lineDao = new LineDao();
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.changeLineColor")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }
        //路線名を漢字に変換
        lineName = lineDao.getLineNameJP(player.getUniqueId().toString(), lineName);

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), lineName);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //ラインカラー
        //色存在チェック
        if(!checkUtil.checkColorExist(lineColor)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f色名/色コード："+ lineColor +"は存在しません。");
            return false;
        }

        //更新
        lineModel.setLineColor(lineColor.toUpperCase());
        if(lineDao.changeLineInfo(player.getUniqueId().toString(), lineModel) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f路線カラーを変更しました！");
            return true;
        }

        return false;
    }

    /**
     * 種別を追加
     * @param player
     * @param lineName
     * @param typeJP
     * @param typeEN
     */
    private boolean addType(Player player, String lineName, String typeJP, String typeEN) {

        LineDao lineDao = new LineDao();
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.addType")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }
        //路線名を漢字に変換
        lineName = lineDao.getLineNameJP(player.getUniqueId().toString(), lineName);

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), lineName);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //種別存在チェック
        if(lineDao.checkTypeExit(player.getUniqueId().toString(), lineName, typeJP)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別名(日本語)は既に登録されています。");
            return false;
        }
        if(lineDao.checkTypeExit(player.getUniqueId().toString(), lineName, typeEN)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別名(英語)は既に登録されています。");
            return false;
        }

        //種別名EN
        //英数字記号チェック
        if(!checkUtil.checkAlphabetOrSymbol(typeEN)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別名(英語)はアルファベット・数字・記号で入力してください");
            return false;
        }

        //更新
        HashMap<String,String> newType = lineModel.getType();
        newType.put(typeJP, typeEN);
        lineModel.setType(newType);
        if(lineDao.changeLineInfo(player.getUniqueId().toString(), lineModel) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f種別を追加しました！");
            player.sendMessage("§f新規種別(和名): "+ typeJP +" §f新規種別(英語): "+ typeEN);
            return true;
        }

        return false;
    }

    /**
     * 種別を削除
     * @param player
     * @param lineName
     * @param type
     */
    private boolean removeType(Player player, String lineName, String type) {

        LineDao lineDao = new LineDao();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.removeType")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }
        //路線名を漢字に変換
        lineName = lineDao.getLineNameJP(player.getUniqueId().toString(), lineName);

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), lineName);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //種別存在チェック
        if(!lineDao.checkTypeExit(player.getUniqueId().toString(), lineName, type)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別名が存在しません。");
            return false;
        }else{
            //種別を日本語に変更
            type = lineDao.getTypeJP(player.getUniqueId().toString(), lineName, type);
        }

        //更新
        HashMap<String,String> newType = lineModel.getType();
        newType.remove(type);
        lineModel.setType(newType);
        if(lineDao.changeLineInfo(player.getUniqueId().toString(), lineModel) > 0){

            player.sendMessage("§b["+ mainConfig.getPrefix() +"] §f種別を削除しました！");
            player.sendMessage("§f種別: "+ type);
            //削除された種別が登録されているアナウンス情報も削除
            announceInfoDao.removeAnnounceFromType(player.getUniqueId().toString(), lineModel.getLineNameJP(), type);
            //アナウンスロケーション情報のアップデート
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();
            return true;
        }

        return false;
    }

    /**
     * 種別名を変更
     * @param player
     * @param lineName
     * @param oldType
     * @param newTypeJP
     * @param newTypeEN
     */
    private boolean changeTypeName(Player player, String lineName, String oldType, String newTypeJP, String newTypeEN) {

        LineDao lineDao = new LineDao();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.changeTypeName")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }
        //路線名を漢字に変換
        lineName = lineDao.getLineNameJP(player.getUniqueId().toString(), lineName);

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), lineName);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //種別存在チェック
        if(!lineDao.checkTypeExit(player.getUniqueId().toString(), lineName, oldType)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別名が存在しません。");
            return false;
        }else{
            //種別を日本語に変更
            oldType = lineDao.getTypeJP(player.getUniqueId().toString(), lineName, oldType);
        }

        //更新
        HashMap<String,String> newType = lineModel.getType();
        newType.replace(oldType, newTypeJP);
        newType.put(newTypeJP, newTypeEN);
        lineModel.setType(newType);
        if(lineDao.changeLineInfo(player.getUniqueId().toString(), lineModel) > 0){

            player.sendMessage("§b["+ mainConfig.getPrefix() +"] §f種別名を変更しました！");
            player.sendMessage("§f旧種別名: "+ oldType +"§f新種別名(和名): "+ newTypeJP +"§f旧種別名(英語): "+ newTypeEN);
            //削除された種別が登録されているアナウンス情報も削除
            announceInfoDao.changeTypeName(player.getUniqueId().toString(), lineName, newTypeJP, newTypeEN);

            return true;
        }

        return false;
    }
}
