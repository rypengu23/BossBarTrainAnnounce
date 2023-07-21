package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import com.github.rypengu23.bossbartrainannounce.util.monitor.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.ConvertUtil;
import com.github.rypengu23.bossbartrainannounce.util.monitor.StationLocationJudgeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Command_station {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_station(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 駅関連コマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        ConvertUtil convertUtil = new ConvertUtil();
        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("registstation")){

            if(args.length == 5) {
                //登録(ナンバリング無し)
                registStation(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), convertUtil.convertDoublePercentToSpace(args[4]), null);
            }else if(args.length == 6){
                //登録(ナンバリング有り)
                registStation(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), convertUtil.convertDoublePercentToSpace(args[4]), convertUtil.convertDoublePercentToSpace(args[5]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }

        }else if(args[0].equalsIgnoreCase("removestation")){

            if(args.length == 3){
                //削除
                removeStation(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("changestationname")){

            if(args.length == 6){
                //駅名変更
                changeStationName(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), convertUtil.convertDoublePercentToSpace(args[4]), convertUtil.convertDoublePercentToSpace(args[5]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("movestation")){

            if(args.length == 3){
                //駅座標移動
                changeStationPosition(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("changestationnumber")){

            if(args.length == 4){
                //ナンバリング変更
                changeStationNumber(player, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]));
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
        commandList.add("registstation");
        commandList.add("removestation");
        commandList.add("changestationname");
        commandList.add("movestation");
        commandList.add("changestationnumber");

        return commandList.contains(command.toLowerCase());
    }

    /**
     * 駅情報を登録
     * @param player
     * @param lineNameJP
     * @param stationNameKanji
     * @param stationNameEn
     * @param stationNameKatakana
     * @param number
     * @return
     */
    public boolean registStation(Player player, String lineNameJP, String stationNameKanji, String stationNameEn, String stationNameKatakana, String number){

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        SelectPositionModel select = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registStation")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名
        //存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineNameJP)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名が存在しません。");
            return false;
        }else {
            //路線名を漢字に変換
            lineNameJP = lineDao.getLineNameJP(player.getUniqueId().toString(), lineNameJP);
        }

        //路線の所有者チェック
        LineModel lineModel = lineDao.getLine(player.getUniqueId().toString(), lineNameJP);
        if(!lineModel.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }



        //駅名EN
        //英数字記号チェック
        if(!checkUtil.checkAlphabetOrSymbol(stationNameEn)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名(英語)はアルファベット・数字・記号で入力してください。");
            return false;
        }

        //駅名カタカナ
        //カタカナ数字記号チェック
        if(!checkUtil.checkKatakanaOrSymbol(stationNameKatakana)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名(カタカナ)はカタカナ・数字・記号で入力してください。");
            return false;
        }

        //セレクトポジションチェック
        //pos1 and pos2 選択チェック
        if(!checkUtil.checkSelectPositionALL(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fpos1またはpos2が選択されていません。");
            return false;
        }

        //positionのpos1が小さくなるように設定
        //X
        if(select.getPos1X() > select.getPos2X()){
            int work = select.getPos1X();
            select.setPos1X(select.getPos2X());
            select.setPos2X(work);
        }
        //Y
        if(select.getPos1Y() > select.getPos2Y()){
            int work = select.getPos1Y();
            select.setPos1Y(select.getPos2Y());
            select.setPos2Y(work);
        }
        //Z
        if(select.getPos1Z() > select.getPos2Z()){
            int work = select.getPos1Z();
            select.setPos1Z(select.getPos2Z());
            select.setPos2Z(work);
        }

        if(stationDao.registStation(new StationModel(player.getUniqueId().toString(), lineNameJP, stationNameKanji, stationNameEn, stationNameKatakana, select, number)) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅を登録しました！");
            player.sendMessage("§f路線名: "+ lineNameJP +" 駅名(和名): "+ stationNameKanji+" 駅名(英語): "+ stationNameEn+" 駅名(ｶﾀｶﾅ): "+ stationNameKatakana);
            //アナウンスロケーション情報のアップデート
            StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
            stationLocationJudgeUtil.updateStationListCache();
            return true;
        }
        return false;
    }

    /**
     * 駅情報の削除
     * @param player
     * @param lineName
     * @param stationName
     */
    private boolean removeStation(Player player, String lineName, String stationName) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.removeStation")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名
        //存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名が存在しません。");
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

        //駅名存在チェック(路線名+駅名の組み合わせ)
        if(!stationDao.checkStationExit(player.getUniqueId().toString(), lineName, stationName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名が存在しません。");
            return false;
        }
        //駅名を漢字に変換
        stationName = stationDao.getStationNameKanji(player.getUniqueId().toString(), lineName, stationName);

        if(stationDao.removeStationFromStationName(player.getUniqueId().toString(), lineName, stationName) > 0){

            player.sendMessage("§b["+ mainConfig.getPrefix() +"] §f駅を削除しました！");
            player.sendMessage("§f路線名: "+ lineName +" 駅名: "+ stationName);

            //削除された駅が登録されているアナウンス情報を削除
            AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
            announceInfoDao.removeAnnounceFromStation(player.getUniqueId().toString(), lineName, stationName);

            //アナウンスロケーション情報のアップデート
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();
            StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
            stationLocationJudgeUtil.updateStationListCache();
            return true;
        }
        return false;
    }

    /**
     * 駅名の変更
     * @param player
     * @param lineName
     * @param oldStationName
     * @param newStationNameKanji
     * @param newStationNameEn
     * @param newStationNameKatakana
     */
    private boolean changeStationName(Player player, String lineName, String oldStationName, String newStationNameKanji, String newStationNameEn, String newStationNameKatakana) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();

        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.changeStationName")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名
        //存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名が存在しません。");
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

        //駅名存在チェック(路線名+駅名の組み合わせ)
        if(!stationDao.checkStationExit(player.getUniqueId().toString(), lineName, oldStationName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名が存在しません。");
            return false;
        }
        //駅名を漢字に変換
        oldStationName = stationDao.getStationNameKanji(player.getUniqueId().toString(), lineName, oldStationName);

        //駅名EN
        //英数字記号チェック
        if(!checkUtil.checkAlphabetOrSymbol(newStationNameEn)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名(英語)はアルファベット・数字・記号で入力してください。");
            return false;
        }

        //駅名カタカナ
        //カタカナ数字記号チェック
        if(!checkUtil.checkKatakanaOrSymbol(newStationNameKatakana)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名(カタカナ)はカタカナ・数字・記号で入力してください。");
            return false;
        }

        //新駅名 存在チェック(日本語)
        if(stationDao.checkStationExit(player.getUniqueId().toString(), lineName, newStationNameKanji)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f指定された路線には既に同名の駅が登録されています。");
            return false;
        }
        //新駅名 存在チェック(英語)
        if(stationDao.checkStationExit(player.getUniqueId().toString(), lineName, newStationNameEn)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f指定された路線には既に同名の駅が登録されています。");
            return false;
        }
        //新駅名 存在チェック(カタカナ)
        if(stationDao.checkStationExit(player.getUniqueId().toString(), lineName, newStationNameKatakana)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f指定された路線には既に同名の駅が登録されています。");
            return false;
        }

        //登録
        if(stationDao.changeStationName(player.getUniqueId().toString(), lineName, oldStationName, newStationNameKanji, newStationNameEn, newStationNameKatakana) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅名を変更しました！");
            player.sendMessage("§f旧駅名: "+ oldStationName +" 新駅名(和名): "+ newStationNameKanji +" 新駅名(英語): "+ newStationNameEn +" 新駅名(ｶﾀｶﾅ): "+ newStationNameKatakana);

            //アナウンス情報に登録された駅名も変更
            AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
            announceInfoDao.changeStationName(player.getUniqueId().toString(), lineName, oldStationName, newStationNameKanji);
            return true;
        }
        return false;
    }

    /**
     * 駅の座標を変更
     * @param player
     * @param lineName
     * @param stationName
     */
    private boolean changeStationPosition(Player player, String lineName, String stationName) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        SelectPositionModel select = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());

        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.changeStationPosition")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名
        //存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名が存在しません。");
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

        //駅名存在チェック(路線名+駅名の組み合わせ)
        if(!stationDao.checkStationExit(player.getUniqueId().toString(), lineName, stationName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名が存在しません。");
            return false;
        }
        //駅名を漢字に変換
        stationName = stationDao.getStationNameKanji(player.getUniqueId().toString(), lineName, stationName);

        //セレクトポジションチェック
        //pos1 and pos2 選択チェック
        if(!checkUtil.checkSelectPositionALL(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fpos1またはpos2が選択されていません。");
            return false;
        }

        //positionのpos1が小さくなるように設定
        //X
        if(select.getPos1X() > select.getPos2X()){
            int work = select.getPos1X();
            select.setPos1X(select.getPos2X());
            select.setPos2X(work);
        }
        //Y
        if(select.getPos1Y() > select.getPos2Y()){
            int work = select.getPos1Y();
            select.setPos1Y(select.getPos2Y());
            select.setPos2Y(work);
        }
        //Z
        if(select.getPos1Z() > select.getPos2Z()){
            int work = select.getPos1Z();
            select.setPos1Z(select.getPos2Z());
            select.setPos2Z(work);
        }

        //登録
        StationModel stationModel = stationDao.getStationForStationName(player.getUniqueId().toString(),stationName, lineName);
        stationModel.setSelectPositionModel(select);
        if(stationDao.changeStationInfo(player.getUniqueId().toString() ,stationModel) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅の座標を変更しました！");
            player.sendMessage("§f駅名: "+ stationName);

            //アナウンスロケーション情報のアップデート
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();
            StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
            stationLocationJudgeUtil.updateStationListCache();

            return true;
        }
        return false;
    }

    /**
     * ナンバリングの変更
     * @param player
     * @param lineName
     * @param stationName
     * @param newStationNumber
     */
    private boolean changeStationNumber(Player player, String lineName, String stationName, String newStationNumber) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.changeStationNumber")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //路線名
        //存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線名が存在しません。");
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

        //駅名存在チェック(路線名+駅名の組み合わせ)
        if(!stationDao.checkStationExit(player.getUniqueId().toString(), lineName, stationName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅名が存在しません。");
            return false;
        }
        //駅名を漢字に変換
        stationName = stationDao.getStationNameKanji(player.getUniqueId().toString(), lineName, stationName);

        //登録
        StationModel stationModel = stationDao.getStationForStationName(player.getUniqueId().toString(),stationName, lineName);
        stationModel.setNumber(newStationNumber);
        if(stationDao.changeStationInfo(player.getUniqueId().toString() ,stationModel) > 0){

            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅のナンバリングを変更しました！");
            player.sendMessage("§f駅名: "+ stationName +" ナンバリング: "+ newStationNumber);
            return true;
        }
        return false;
    }

}

