package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.model.PlayerDataModel;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.util.*;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import com.github.rypengu23.bossbartrainannounce.util.monitor.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.monitor.StationLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.UUID;

public class Listener_VehicleMove implements Listener {

    @EventHandler
    /**
     * アナウンス・駅内走行を判定する
     */
    public void Listener_VehicleMoveEvent(VehicleMoveEvent event){

        //動いているエンティティがトロッコか
        if(event.getVehicle().getType() != EntityType.MINECART) {
            return;
        }
        //トロッコに乗客が居るか
        if(event.getVehicle().getPassengers().size() == 0){
            return;
        }
        //トロッコの乗客はプレイヤーか
        if(event.getVehicle().getPassengers().get(0).getType() != EntityType.PLAYER) {
            return;
        }

        AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
        StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();

        Player player = (Player)event.getVehicle().getPassengers().get(0);
        UUID uuid = player.getUniqueId();
        Vehicle vehicle = event.getVehicle();
        Location vehicleLocation = vehicle.getLocation();

        //プレイヤーが1マス動いたか確認する
        //プレイヤーロケーションリストに登録されているか
        if(!BossBarTrainAnnounce.playerLocationList.containsKey(uuid)) {
            //されていなければ新規追加
            BossBarTrainAnnounce.playerLocationList.put(uuid, vehicle.getLocation());
        }else{
            //されていた場合 → 登録値を取得
            Location playerLocationHistory = BossBarTrainAnnounce.playerLocationList.get(uuid);
            //登録値と現在地の値を比較 → 1マス以上動いていない場合、処理終了
            if(vehicleLocation.distance(playerLocationHistory) < 0.5){
                return;
            }

            //1マス以上動いていたので、ロケーションリストを更新する
            BossBarTrainAnnounce.playerBefore1BlockLocationList.put(uuid, BossBarTrainAnnounce.playerLocationList.get(uuid));
            BossBarTrainAnnounce.playerLocationList.put(uuid, vehicle.getLocation());

        }

        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
        StationDao stationDao = new StationDao();

        AnnounceInfoModel announceInfo = new AnnounceInfoModel();
        StationModel stationModel = new StationModel();
        int nextOrSoonOrStopOrMove = 0;

        //アナウンス位置・駅位置判定
        if(announceLocationJudgeUtil.checkAnnouncePosition(vehicleLocation)){
            //アナウンス地点の場合

            //アナウンス情報取得
            announceInfo = announceInfoDao.getAnnounceForCoordinate(vehicleLocation.getWorld().getName(), vehicleLocation.getBlockX(), vehicleLocation.getBlockY(), vehicleLocation.getBlockZ());
            stationModel = stationDao.getStationForStationName(announceInfo.getUUID(), announceInfo.getNextStationNameKanji(), announceInfo.getLineNameJP());

            nextOrSoonOrStopOrMove = announceInfo.getNextOrSoon();
        }else if(stationLocationJudgeUtil.checkAnnouncePosition(vehicleLocation)){

            //駅内に居る場合
            //停車中または走行中判定
            if(BossBarTrainAnnounce.moveInStationPlayerList.contains(uuid) && BossBarTrainAnnounce.lcdTask.containsKey(uuid)){
                //既に駅内走行中の場合、処理終了
                return;
            }else{
                //駅内走行中新規処理
                BossBarTrainAnnounce.moveInStationPlayerList.add(uuid);

                //停車中の駅情報取得
                stationModel = stationDao.getStationForCoordinate(vehicleLocation);
                announceInfo = null;
                nextOrSoonOrStopOrMove = 3;
            }
        }else{
            //駅外に居る場合
            BossBarTrainAnnounce.moveInStationPlayerList.remove(uuid);
            return;
        }

        //レッドストーンステータス確認
        if(announceInfo != null) {
            if (announceInfo.isRedstone()) {

                Location location = new Location(Bukkit.getWorld(announceInfo.getWorldName()), announceInfo.getRedstonePosX(), announceInfo.getRedstonePosY(), announceInfo.getRedstonePosZ());

                if (announceInfo.getOnOrOff() == 1 && !location.getBlock().isBlockPowered()) {
                    // 設定がON & 登録されているレッドストーン箇所に入力がない
                    return;
                } else if (announceInfo.getOnOrOff() == 0 && location.getBlock().isBlockPowered()) {
                    // 設定がオフ & 登録されているレッドストーン箇所に入力がある
                    return;
                }
            }
        }

        //方角制御確認
        CheckUtil checkUtil = new CheckUtil();
        if(announceInfo != null) {
            if (announceInfo.getDirection() != 0) {

                Location before1BlockLocation = BossBarTrainAnnounce.playerBefore1BlockLocationList.get(uuid);
                int direction = checkUtil.checkPositionAdjacent(new SelectPositionModel(player.getWorld().getName(), vehicle.getLocation().getBlockX(), vehicle.getLocation().getBlockY(), vehicle.getLocation().getBlockZ(), before1BlockLocation.getBlockX(), before1BlockLocation.getBlockY(), before1BlockLocation.getBlockZ()));

                if(direction == -1){
                    //登録した方角が隣接していない
                    return;
                }else if(announceInfo.getDirection() != direction){
                    //設定された方角から来ていない
                    return;
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
