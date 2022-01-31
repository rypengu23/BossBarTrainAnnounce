package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.PlayerDataDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.model.PlayerDataModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.ConvertUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Command_info {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_info(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta infoコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        ConvertUtil convertUtil = new ConvertUtil();
        Player player = (Player)sender;

        //プレイヤー情報
        if(args.length == 1) {
            infomationOfPlayer(player);

        //フラグ
        }else if(args[1].equalsIgnoreCase("line")){

            if(args.length == 3){
                //路線情報
                informationOfLine(player, convertUtil.convertDoublePercentToSpace(args[2]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }

        }else if(args[1].equalsIgnoreCase("station")){

            if(args.length == 4){
                //駅情報
                informationOfStation(player, convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]));
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[1].equalsIgnoreCase("announce")){

            if(args.length == 4){
                //アナウンス情報
                informationOfAnnounce(player, convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]));
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
     * 引数のコマンドがフラグ関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("info");

        return commandList.contains(command.toLowerCase());
    }

    private boolean infomationOfPlayer(Player player){

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.info")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        PlayerDataDao playerDataDao = new PlayerDataDao();
        PlayerDataModel playerData = playerDataDao.getPlayerData(player.getUniqueId().toString());

        if(playerData == null){
            return false;
        }

        ConvertUtil convertUtil = new ConvertUtil();
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] "+ convertUtil.placeholderUtil("{player}", player.getName(), CommandMessage.Command_Info_SHOWSTATUS1));
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] "+ convertUtil.placeholderUtil("{speed}", convertUtil.convertBooleanToString(playerData.isSpeedUpFlag()), CommandMessage.Command_Info_SHOWSTATUS2));
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] "+ convertUtil.placeholderUtil("{bossbar}", convertUtil.convertBooleanToString(playerData.isShowBossBar()), CommandMessage.Command_Info_SHOWSTATUS3));
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] "+ convertUtil.placeholderUtil("{announce}", convertUtil.convertBooleanToString(playerData.isShowChatAnnounce()), CommandMessage.Command_Info_SHOWSTATUS4));
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] "+ CommandMessage.Command_Info_SHOWSTATUS5);

        return true;
    }

    /**
     * コマンド実行者が所有する路線を表示
     * @param player
     * @param lineName
     */
    private boolean informationOfLine(Player player, String lineName) {

        LineDao lineDao = new LineDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.info")){
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

        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f路線情報: "+ lineModel.getLineNameJP());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅名(和名): "+ lineModel.getLineNameJP() +" §f駅名(英語): "+ lineModel.getLineNameEN());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f路線カラー: "+ lineModel.getLineColor());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f種別(和名): "+ lineModel.convertTypeCommaStr(0, lineModel.getType()));
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f種別(英語): "+ lineModel.convertTypeCommaStr(1, lineModel.getType()));

        return true;
    }

    /**
     * コマンド実行者が所有する駅の情報を表示
     * @param player
     * @param lineName
     * @param stationName
     */
    private boolean informationOfStation(Player player, String lineName, String stationName) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.info")){
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
        StationModel stationModel = stationDao.getStationForStationName(player.getUniqueId().toString(), stationName, lineName);

        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅情報: "+ stationModel.getStationNameKanji());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f所属路線名: "+ lineModel.getLineNameJP());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅名(和名): "+ stationModel.getStationNameKanji() +" §f駅名(英語): "+ stationModel.getStationNameEnglish());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f駅名(カタカナ): "+ stationModel.getStationNameKatakana());
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f座標: ("+ stationModel.getSelectPositionModel().getPos1X() +", "+ stationModel.getSelectPositionModel().getPos1Y() +", "+ stationModel.getSelectPositionModel().getPos1Z() +") ("+ stationModel.getSelectPositionModel().getPos2X() +", "+ stationModel.getSelectPositionModel().getPos2Y() +", "+ stationModel.getSelectPositionModel().getPos2Z() +")");
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §fナンバリング): "+ stationModel.getNumber());

        return true;
    }

    /**
     * コマンド実行者が所有するアナウンス地点を表示
     * @param player
     * @param lineName
     * @param stationName
     */
    private boolean informationOfAnnounce(Player player, String lineName, String stationName) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.info")){
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
        StationModel stationModel = stationDao.getStationForStationName(player.getUniqueId().toString(), stationName, lineName);

        //指定した情報にアナウンス地点が存在するか
        ArrayList<AnnounceInfoModel> announceInfoList = announceInfoDao.getAnnounceForStationName(player.getUniqueId().toString(), lineName, stationModel.getStationNameKanji());
        if(announceInfoList == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f指定された路線・駅に関連するアナウンスは登録されていません。");
            return false;
        }

        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §fアナウンス情報 (路線名:"+ lineModel.getLineNameJP() +" 駅名:"+ stationModel.getStationNameKanji() +")");
        for(AnnounceInfoModel work:announceInfoList){
            String nextOrSoon = "";
            if(work.getNextOrSoon() == 0){
                nextOrSoon = "next";
            }else{
                nextOrSoon = "soon";
            }
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §fアナウンス種別:"+ nextOrSoon +" ワールド名:"+ work.getWorldName() +" 座標: ("+ work.getPosX() +", "+ work.getPosY() +", "+ work.getPosZ() +")");
            if(work.isRedstone()){
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §レッドストーン制御位置 座標: ("+ work.getRedstonePosX() +", "+ work.getRedstonePosY() +", "+ work.getRedstonePosZ() +")");
            }
        }

        return true;
    }
}
