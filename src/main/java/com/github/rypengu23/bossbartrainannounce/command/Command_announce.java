package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;

import com.github.rypengu23.bossbartrainannounce.util.monitor.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.ConvertUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class Command_announce {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_announce(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta nextまたはsoonまたはremoveコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        ConvertUtil convertUtil = new ConvertUtil();
        Player player = (Player)sender;

        if(args[0].equalsIgnoreCase("next")){
            //次は～ 登録

            if(args.length == 5) {
                //登録(出口あり)
                registAnnounceLocation(player, 0, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertPercentAndColorCode(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), args[4]);
            }else if(args.length == 4){
                //登録(出口なし)
                registAnnounceLocation(player, 0, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertPercentAndColorCode(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), null);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }

        }else if(args[0].equalsIgnoreCase("soon")){
            //まもなく～ 登録

            if(args.length == 5) {
                //登録(出口あり)
                registAnnounceLocation(player, 1, convertUtil.convertDoublePercentToSpace(args[1]), convertUtil.convertPercentAndColorCode(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), args[4]);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[0].equalsIgnoreCase("remove")){
            //アナウンス地点削除

            if(args.length == 1){
                //削除
                removeAnnounce(player);

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
     * 引数のコマンドがアナウンス関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("next");
        commandList.add("soon");
        commandList.add("remove");

        return commandList.contains(command.toLowerCase());
    }

    /**
     * 次は～のアナウンス開始地点情報を登録
     * @param player
     * @param nextOrSoon
     * @param stationNameKanji
     * @param lineName
     * @param type
     * @param exit
     * @return
     */
    public boolean registAnnounceLocation(Player player, int nextOrSoon, String lineName, String type, String stationNameKanji, String exit){

        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();

        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registAnnounce")){
            player.sendMessage("[§c"+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //プレイヤーの地点選択情報を確認
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());

        //入力値チェック

        //セレクトポジションチェック
        //pos1 選択チェック
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fpos1が選択されていません。");
            return false;
        }

        //路線名存在チェック
        if(!lineDao.checkLineExit(player.getUniqueId().toString(), lineName)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f路線が存在しません。");
            return false;
        }else{
            //路線名を漢字に変換
            lineName = lineDao.getLineNameJP(player.getUniqueId().toString(), lineName);
        }

        //種別存在チェック
        if(!lineDao.checkTypeExit(player.getUniqueId().toString(), lineName, type)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f種別が存在しません。");
            return false;
        }else{
            //種別名を漢字に変換
            type = lineDao.getTypeJP(player.getUniqueId().toString(), lineName, type);
        }

        //次駅名存在チェック
        StationModel stationInfo = stationDao.getStationForStationName(player.getUniqueId().toString(), stationNameKanji, lineName);
        if(stationInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f駅が存在しません。");
            return false;
        }else{
            //引数の駅名を漢字に変換
            stationNameKanji = stationInfo.getStationNameKanji();
        }

        //降車口チェック
        if(exit != null) {
            if (!(exit.equalsIgnoreCase("right") || exit.equalsIgnoreCase("left"))){
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f降車口はrightまたはleftを指定してください。");
                return false;
            }
        }

        //アナウンス地点存在チェック
        if(announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z()) != null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択された地点には既にアナウンス地点が登録されています。");
            return false;
        }

        if(announceInfoDao.insertAnnounceInfo(new AnnounceInfoModel(player.getUniqueId().toString(), nextOrSoon, selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z(), stationNameKanji, lineName, null, null, type, exit, false, 0,null,  null, false, 0, 0, 0,0,false)) > 0){
            //アナウンスロケーション情報のアップデート
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();

            String nextOrSoonInfo = "";
            if(nextOrSoon == 0){
                nextOrSoonInfo = "NEXT";
            }else{
                nextOrSoonInfo = "SOON";
            }
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §fアナウンス地点を登録しました！");
            player.sendMessage("§f路線名: "+ lineName +" 種別:"+ type +" ｱﾅｳﾝｽ種別:"+ nextOrSoonInfo +" 次の駅名:"+ stationNameKanji);
            return true;
        }
        return false;
    }

    /**
     * 次は～・まもなく～アナウンス地点を削除
     * @param player
     * @return
     */
    public boolean removeAnnounce(Player player){

        CheckUtil checkUtil = new CheckUtil();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.removeAnnounce")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点が選択されていません。");
            return false;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return false;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //登録されているアナウンスを削除
        if(announceInfoDao.removeAnnounce(announceInfo) > 0){
            //アナウンスロケーション情報のアップデート
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();
            player.sendMessage("§b["+ mainConfig.getPrefix() +"] §fアナウンス地点を削除しました！");
            return true;
        }

        return false;
    }
}
