package com.github.rypengu23.bossbartrainannounce.util.monitor;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.*;
import com.github.rypengu23.bossbartrainannounce.util.AnnounceUtil;
import com.github.rypengu23.bossbartrainannounce.util.BossBarUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;

public class VehicleMoveMonitorUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    private HashMap<Player, Location> playerLocationList = new HashMap<>();

    public VehicleMoveMonitorUtil(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 高速鉄道用 トロッコ乗車リスナー
     */
    public void vehicleMoveEvent() {

        TimerTask task = new TimerTask() {
            int i = 0;

            @Override
            public void run() {

                //オンラインプレイヤーから、トロッコに乗車中のプレイヤーを取得
                Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
                for (Player player : playerList) {
                    if (player.getVehicle() == null) {
                        continue;
                    } else if (player.getVehicle().getType() != EntityType.MINECART) {
                        continue;
                    }

                    UUID uuid = player.getUniqueId();
                    //位置取得
                    Location vehicleLocation = player.getVehicle().getLocation();

                    //登録されていない場合、プレイヤーロケーションリストを更新
                    if (!playerLocationList.containsKey(player)) {
                        playerLocationList.put(player, vehicleLocation);
                    }

                    //登録地と現在地を比較
                    if (vehicleLocation.distance(playerLocationList.get(player)) != 0) {
                        //動いた場合 → ロケーションリストを更新する
                        playerLocationList.put(player, vehicleLocation);
                    } else {
                        //動いていないため、何もしない
                        continue;
                    }

                    //プレイヤーが1マス動いたか確認する
                    //プレイヤーロケーションリストに登録されているか
                    if (!BossBarTrainAnnounce.playerLocationList.containsKey(uuid)) {
                        //されていなければ新規追加
                        BossBarTrainAnnounce.playerLocationList.put(uuid, vehicleLocation);
                    } else {
                        //されていた場合 → 登録値を取得
                        Location playerLocationHistory = BossBarTrainAnnounce.playerLocationList.get(uuid);
                        //登録値と現在地の値を比較 → 1マス以上動いていない場合、処理終了
                        if (vehicleLocation.distance(playerLocationHistory) <= 1) {
                            continue;
                        }

                        //1マス以上動いていたので、ロケーションリストを更新する
                        BossBarTrainAnnounce.playerBefore1BlockLocationList.put(uuid, BossBarTrainAnnounce.playerLocationList.get(uuid));
                        BossBarTrainAnnounce.playerLocationList.put(uuid, vehicleLocation);

                    }

                    AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
                    StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
                    AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
                    StationDao stationDao = new StationDao();

                    AnnounceInfoModel announceInfo = new AnnounceInfoModel();
                    StationModel stationModel = new StationModel();
                    int nextOrSoonOrStopOrMove = 0;

                    //高速化 減速判定
                    CheckUtil checkUtil = new CheckUtil();
                    if(mainConfig.isUseMinecartSpeedup() && BossBarTrainAnnounce.useMinecartSpeedUpPlayerList.contains(uuid)){
                        //進行方向チェック
                        Location before1BlockLocation = BossBarTrainAnnounce.playerBefore1BlockLocationList.get(uuid);
                        Minecart minecart = (Minecart) player.getVehicle();
                        int judge = checkUtil.checkHighSpeedMinecartDecelerate(vehicleLocation, before1BlockLocation);
                        if(judge == 1) {
                            //減速
                            minecart.setMaxSpeed(0.4);
                        }else if(judge == 2){
                            //カーブレール減速
                            if(mainConfig.getMaxMinecartSpeed() > 0.5){
                                minecart.setMaxSpeed(0.5);
                            }else{
                                minecart.setMaxSpeed(mainConfig.getMaxMinecartSpeed());
                            }
                        }else{
                            //加速
                            minecart.setMaxSpeed(mainConfig.getMaxMinecartSpeed());
                        }
                    }

                    //アナウンス位置・駅位置判定
                    AnnounceInfoModel announceLocation = announceLocationJudgeUtil.checkAnnouncePosition(vehicleLocation);
                    if(announceLocation != null){
                        //アナウンス地点の場合

                        //アナウンス情報取得
                        announceInfo = announceInfoDao.getAnnounceForCoordinate(vehicleLocation.getWorld().getName(), announceLocation.getPosX(), announceLocation.getPosY(), announceLocation.getPosZ());
                        stationModel = stationDao.getStationForStationName(announceInfo.getUUID(), announceInfo.getNextStationNameKanji(), announceInfo.getLineNameJP());

                        nextOrSoonOrStopOrMove = announceInfo.getNextOrSoon();
                    } else if (stationLocationJudgeUtil.checkAnnouncePosition(vehicleLocation)) {

                        //駅内に居る場合
                        //停車中または走行中判定
                        if (BossBarTrainAnnounce.moveInStationPlayerList.contains(uuid) && BossBarTrainAnnounce.lcdTask.containsKey(uuid)) {
                            //既に駅内走行中の場合、処理終了
                            continue;
                        } else {
                            //駅内走行中新規処理
                            BossBarTrainAnnounce.moveInStationPlayerList.add(uuid);

                            //停車中の駅情報取得
                            stationModel = stationDao.getStationForCoordinate(vehicleLocation);
                            announceInfo = null;
                            nextOrSoonOrStopOrMove = 3;
                        }
                    } else {
                        //駅外に居る場合
                        BossBarTrainAnnounce.moveInStationPlayerList.remove(uuid);
                        continue;
                    }

                    //レッドストーンステータス確認
                    if (announceInfo != null) {
                        if (announceInfo.isRedstone()) {

                            Location location = new Location(Bukkit.getWorld(announceInfo.getWorldName()), announceInfo.getRedstonePosX(), announceInfo.getRedstonePosY(), announceInfo.getRedstonePosZ());

                            if (announceInfo.getOnOrOff() == 1 && !location.getBlock().isBlockPowered()) {
                                // 設定がON & 登録されているレッドストーン箇所に入力がない
                                continue;
                            } else if (announceInfo.getOnOrOff() == 0 && location.getBlock().isBlockPowered()) {
                                // 設定がオフ & 登録されているレッドストーン箇所に入力がある
                                continue;
                            }
                        }
                    }

                    //方角制御確認
                    if (announceInfo != null) {
                        if (announceInfo.getDirection() != 0) {

                            Location before1BlockLocation = BossBarTrainAnnounce.playerBefore1BlockLocationList.get(uuid);
                            int direction = checkUtil.checkPositionAdjacent(new SelectPositionModel(player.getWorld().getName(), vehicleLocation.getBlockX(), vehicleLocation.getBlockY(), vehicleLocation.getBlockZ(), before1BlockLocation.getBlockX(), before1BlockLocation.getBlockY(), before1BlockLocation.getBlockZ()));

                            if (direction == -1) {
                                //登録した方角が隣接していない
                                continue;
                            } else if (announceInfo.getDirection() != direction) {
                                //設定された方角から来ていない
                                continue;
                            }
                        }
                    }

                    //プレイヤーデータ取得
                    PlayerDataModel playerDataModel = BossBarTrainAnnounce.playerDataList.get(uuid);

                    if(playerDataModel.isShowBossBar()) {
                        //BossBarのセット
                        BossBarUtil bossBarUtil = new BossBarUtil();
                        if (announceInfo != null) {
                            bossBarUtil.setBossBar(nextOrSoonOrStopOrMove, player, announceInfo, stationModel);
                        } else {
                            bossBarUtil.setBossBar(nextOrSoonOrStopOrMove, player, null, stationModel);
                        }
                    }

                    if(playerDataModel.isShowChatAnnounce()) {
                        if (nextOrSoonOrStopOrMove != 2 && nextOrSoonOrStopOrMove != 3) {
                            //チャットアナウンスの送信
                            AnnounceUtil announceUtil = new AnnounceUtil();
                            announceUtil.sendChatAnnounceOfNextSoon(announceInfo, player);
                        }
                    }
                }
            }

        };
        BossBarTrainAnnounce.vehicleMoveEvent.scheduleAtFixedRate(task,0,20);
    }
}