package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class AnnounceUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private MessageConfig messageConfig;

    public AnnounceUtil(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    public void sendChatAnnounceOfNextSoon(AnnounceInfoModel announceInfo, Player player) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        CheckUtil checkUtil = new CheckUtil();
        ConvertUtil convertUtil = new ConvertUtil();
        ArrayList<String> sendMessageListJP = new ArrayList<>();
        ArrayList<String> sendMessageListEN = new ArrayList<>();


        //次駅情報取得
        StationModel stationInfo = stationDao.getStationForStationName(announceInfo.getUUID(), announceInfo.getNextStationNameKanji(), announceInfo.getLineNameJP());
        LineModel lineModel = lineDao.getLine(announceInfo.getUUID(), announceInfo.getLineNameJP());
        if(stationInfo == null || lineModel == null){
            return;
        }

        AnnouncePlaceHolderUtil announcePlaceHolderUtil = new AnnouncePlaceHolderUtil(lineModel, stationInfo, announceInfo);

        //路線案内
        if (!checkUtil.checkNullOrBlank(announceInfo.getBoundJP())) {
            if (!lineModel.isLoop() && checkUtil.checkNullOrBlank(announceInfo.getViaLineNameJP())) {
                //通常路線 - 直通なし
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getTrainInformationJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getTrainInformationENAnnounce()));
            } else if (lineModel.isLoop() && checkUtil.checkNullOrBlank(announceInfo.getViaLineNameJP())) {
                //環状線 - 直通なし
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getLoopTrainInformationJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getLoopTrainInformationENAnnounce()));
            } else if (!lineModel.isLoop() && !checkUtil.checkNullOrBlank(announceInfo.getViaLineNameJP())) {
                //通常路線 - 直通あり
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getViaTrainInformationJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getViaTrainInformationENAnnounce()));
            }
        }

        //次駅案内
        if (announceInfo.getNextOrSoon() == 0) {
            //次は～
            //次駅案内
            if(!announceInfo.isTerminal() && checkUtil.checkNullOrBlank(announceInfo.getExit())) {
                //終点でない
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationENAnnounce()));
            }else if(checkUtil.checkNullOrBlank(announceInfo.getExit())){
                //終点
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationTerminalJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationTerminalENAnnounce()));
            }else if(!announceInfo.isTerminal() && !checkUtil.checkNullOrBlank(announceInfo.getExit())){
                //終点でない(出口指定あり)
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationJPAnnounceExit()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationENAnnounceExit()));
            }else{
                //終点(出口)
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationTerminalJPAnnounceExit()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextStationTerminalENAnnounceExit()));
            }
        } else {
            //まもなく～
            //次駅案内
            if(!announceInfo.isTerminal()){
                //終点でない
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonStationJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonStationENAnnounce()));
            }else{
                //終点
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonStationTerminalJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonStationTerminalENAnnounce()));
            }

        }

        //降車口・乗り換え案内
        if (!checkUtil.checkNullOrBlank(announceInfo.getExit())) {

            if (announceInfo.getExit().equals("RIGHT")) {
                //出口案内
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getDoorRightSideJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getDoorRightSideENAnnounce()));

            } else if (announceInfo.getExit().equals("LEFT")) {
                //出口案内
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getDoorLeftSideJPAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getDoorLeftSideENAnnounce()));
            }

            //乗り換え案内
            if (announcePlaceHolderUtil.checkTransferExit()) {
                sendMessageListJP.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getTransferJPAnnounceAnnounce()));
                sendMessageListEN.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getTransferENAnnounceAnnounce()));
            }

        }

        //日本語・英語アナウンスリストを結合
        ArrayList<String> sendMessageList = sendMessageListJP;
        sendMessageList.addAll(sendMessageListEN);

        //アナウンスを遅延リピート実行
        Runnable announceTask = new Runnable() {
            int repeat = 0;
            @Override
            public void run() {
                //全てのアナウンスを流し終わった場合、タスクをキル
                if(repeat >= sendMessageList.size()) {
                    BossBarTrainAnnounce.announceTask.get(player).cancel();
                    BossBarTrainAnnounce.announceTask.remove(player);
                }else {
                    player.sendMessage(sendMessageList.get(repeat));
                    repeat++;
                }
            }
        };

        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(BossBarTrainAnnounce.getInstance(), announceTask, 10L, 50L);

        //タスクを保管
        if(BossBarTrainAnnounce.announceTask.containsKey(player)){
            //既にアナウンス実行中の場合、タスク終了
            BossBarTrainAnnounce.announceTask.get(player).cancel();
            BossBarTrainAnnounce.announceTask.remove(player);
        }
        BossBarTrainAnnounce.announceTask.put(player, task);
    }
}
