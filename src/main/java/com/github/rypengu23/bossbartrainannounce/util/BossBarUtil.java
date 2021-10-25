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
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BossBarUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private MessageConfig messageConfig;

    private ArrayList<String> lcdList = new ArrayList<>();

    public BossBarUtil() {
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    public void setBossBar(int nextOrSoonOrStopOrMove, Player player, AnnounceInfoModel announceInfoModel, StationModel stationModel) {


        //delayTime
        int period = 1;

        //表示するメッセージをセット
        lcdList = new ArrayList<>();

        if(announceInfoModel != null) {
            lcdList = createBossBarTextList(nextOrSoonOrStopOrMove, player, announceInfoModel);
        }else{
            lcdList = createBossBarTextList(nextOrSoonOrStopOrMove, player, stationModel);
        }

        if (nextOrSoonOrStopOrMove == 0) {
            //次は～の場合
            period = 50;
        } else if (nextOrSoonOrStopOrMove == 1) {
            //まもなく～の場合
            period = 50;
        } else if (nextOrSoonOrStopOrMove == 2) {
            //停車中の場合
            period = 50;
        } else if (nextOrSoonOrStopOrMove == 3) {
            //駅内走行中の場合
            period = 100;
        }

        //BossBarの作成
        NamespacedKey namespacedKey = NamespacedKey.fromString(player.getUniqueId().toString());
        if (namespacedKey == null) {
            return;
        }
        //ボスバーが既に表示されている場合 → 消さずにタスクを止める
        if (BossBarTrainAnnounce.lcdTask.containsKey(player)) {

            BossBarTrainAnnounce.lcdTask.get(player).cancel();
        }

        KeyedBossBar bossBar;
        if (Bukkit.getBossBar(namespacedKey) != null) {
            bossBar = Bukkit.getBossBar(namespacedKey);
        } else {
            bossBar = Bukkit.createBossBar(namespacedKey, "", BarColor.WHITE, BarStyle.SOLID);
        }

        LineDao lineDao = new LineDao();
        LineModel lineModel = new LineModel();
        if(announceInfoModel != null) {
            lineModel = lineDao.getLine(announceInfoModel.getUUID(), announceInfoModel.getLineNameJP());
        }else {
            lineModel = lineDao.getLine(stationModel.getUUID(), stationModel.getLineNameJp());
        }

        if (lineModel.getLineColor().equalsIgnoreCase("GREEN")) {
            bossBar.setColor(BarColor.GREEN);
        } else if (lineModel.getLineColor().equalsIgnoreCase("BLUE")) {
            bossBar.setColor(BarColor.BLUE);
        } else if (lineModel.getLineColor().equalsIgnoreCase("YELLOW")) {
            bossBar.setColor((BarColor.YELLOW));
        } else if (lineModel.getLineColor().equalsIgnoreCase("PINK")) {
            bossBar.setColor((BarColor.PINK));
        } else if (lineModel.getLineColor().equalsIgnoreCase("RED")) {
            bossBar.setColor(BarColor.RED);
        } else if (lineModel.getLineColor().equalsIgnoreCase("PURPLE")) {
            bossBar.setColor(BarColor.PURPLE);
        }


        Runnable bossBarLCDAnimation = new Runnable() {
            int i = 0;
            ArrayList<String> lcdListWorking = lcdList;
            @Override
            public void run() {
                if(lcdListWorking.size() != 0) {
                    bossBar.setTitle(lcdListWorking.get(i));
                    bossBar.setProgress(1);
                    bossBar.addPlayer(player);
                    i++;
                }
                if (i >= this.lcdListWorking.size()) {
                    i = 0;
                }
            }
        };

        BossBarTrainAnnounce.lcdTask.put(player, Bukkit.getServer().getScheduler().runTaskTimer(BossBarTrainAnnounce.getInstance(), bossBarLCDAnimation, 0, period));
    }

    /**
     * プレイヤーに表示されているボスバーを削除
     *
     * @param player
     */
    public void removeBossBar(Player player) {

        NamespacedKey namespacedKey = NamespacedKey.fromString(player.getUniqueId().toString());
        if (namespacedKey == null) {
            return;
        }
        KeyedBossBar bossBar = Bukkit.getBossBar(namespacedKey);
        if (bossBar != null) {
            bossBar.removePlayer(player);
            Bukkit.removeBossBar(namespacedKey);
        }
    }

    /**
     * ボスバーの表示内容リスト作成
     *
     * @param nextOrSoonOrStopOrMove
     * @param player
     * @param announceInfoModel
     * @return
     */
    public ArrayList<String> createBossBarTextList(int nextOrSoonOrStopOrMove, Player player, AnnounceInfoModel announceInfoModel) {

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();


        //取得したアナウンス情報から、次の駅の情報を取得
        StationModel stationModel = stationDao.getStationForStationName(announceInfoModel.getUUID(), announceInfoModel.getNextStationNameKanji(), announceInfoModel.getLineNameJP());
        //取得したアナウンス情報から、路線情報を取得
        LineModel lineModel = lineDao.getLine(announceInfoModel.getUUID(), announceInfoModel.getLineNameJP());

        AnnouncePlaceHolderUtil announcePlaceHolderUtil = new AnnouncePlaceHolderUtil(lineModel, stationModel, announceInfoModel);

        //表示するメッセージをセット
        ArrayList<String> lcdList = new ArrayList<>();

        if (nextOrSoonOrStopOrMove == 0) {
            //次は～の場合
            lcdList.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextKanji()));
            lcdList.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextEN()));
            lcdList.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getNextKatakana()));
        } else if (nextOrSoonOrStopOrMove == 1) {
            //まもなく～の場合
            lcdList.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonKanji()));
            lcdList.add(announcePlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonEN()));
        } else if (nextOrSoonOrStopOrMove == 2) {
            //停車中の場合
            lcdList.add(stationModel.getStationNameKanji());
            lcdList.add("");
        } else if (nextOrSoonOrStopOrMove == 3) {
            //駅内走行中の場合
            lcdList.add("");
        }

        return lcdList;
    }

    /**
     * ボスバーの表示内容リスト作成
     *
     * @param nextOrSoonOrStopOrMove
     * @param player
     * @param stationModel
     * @return
     */
    public ArrayList<String> createBossBarTextList(int nextOrSoonOrStopOrMove, Player player, StationModel stationModel) {

        LineDao lineDao = new LineDao();

        //取得した駅情報から、路線情報を取得
        LineModel lineModel = lineDao.getLine(stationModel.getUUID(), stationModel.getLineNameJp());

        StationPlaceHolderUtil stationPlaceHolderUtil = new StationPlaceHolderUtil(lineModel, stationModel);

        //表示するメッセージをセット
        ArrayList<String> lcdList = new ArrayList<>();

        if (nextOrSoonOrStopOrMove == 0) {
            //次は～の場合
            lcdList.add(stationPlaceHolderUtil.replaceMessageConfig(messageConfig.getNextKanji()));
            lcdList.add(stationPlaceHolderUtil.replaceMessageConfig(messageConfig.getNextEN()));
            lcdList.add(stationPlaceHolderUtil.replaceMessageConfig(messageConfig.getNextKatakana()));
        } else if (nextOrSoonOrStopOrMove == 1) {
            //まもなく～の場合
            lcdList.add(stationPlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonKanji()));
            lcdList.add(stationPlaceHolderUtil.replaceMessageConfig(messageConfig.getSoonEN()));
        } else if (nextOrSoonOrStopOrMove == 2) {
            //停車中の場合
            lcdList.add(stationModel.getStationNameKanji());
            lcdList.add(stationModel.getStationNameEnglish());
        } else {
            //駅内走行中の場合
            lcdList.add("");
        }

        return lcdList;
    }
}
