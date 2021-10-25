package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.util.BossBarUtil;
import com.github.rypengu23.bossbartrainannounce.util.AnnounceUtil;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import com.github.rypengu23.bossbartrainannounce.util.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.StationLocationJudgeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.HashMap;

public class Listener_VehicleMove implements Listener {

    private HashMap<String, Location> playerLocationList = new HashMap<String, Location>();

    @EventHandler
    /**
     * アナウンス・駅内走行を判定する
     */
    public void Listener_VehicleMoveEvent(VehicleMoveEvent event){

        AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
        StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();

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

        Player player = (Player)event.getVehicle().getPassengers().get(0);
        Vehicle vehicle = event.getVehicle();
        Location vehicleLocation = vehicle.getLocation();

        //プレイヤーが1マス動いたか確認する
        //プレイヤーロケーションリストに登録されているか
        if(!playerLocationList.containsKey(player.getUniqueId().toString())) {
            //されていなければ新規追加
            playerLocationList.put(player.getUniqueId().toString(), vehicle.getLocation());
        }else{
            //されていた場合 → 登録値を取得
            Location playerLocationHistory = playerLocationList.get(player.getUniqueId().toString());
            //登録値と現在地の値を比較 → 1マス以上動いていない場合、処理終了
            if(vehicleLocation.distance(playerLocationHistory) < 0.5){
                return;
            }else{
                //1マス以上動いていたので、ロケーションリストを更新する
                playerLocationList.put(player.getUniqueId().toString(), vehicle.getLocation());
            }
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
            if(BossBarTrainAnnounce.moveInStationPlayerList.contains(player) && BossBarTrainAnnounce.lcdTask.containsKey(player)){
                //既に駅内走行中の場合、処理終了
                return;
            }else{
                //駅内走行中新規処理
                BossBarTrainAnnounce.moveInStationPlayerList.add(player);

                //停車中の駅情報取得
                stationModel = stationDao.getStationForCoordinate(vehicleLocation);
                announceInfo = null;
                nextOrSoonOrStopOrMove = 3;
            }
        }else{
            //駅外に居る場合
            BossBarTrainAnnounce.moveInStationPlayerList.remove(player);
            return;
        }

        //レッドストーンステータス確認
        if(announceInfo != null) {
            if (announceInfo.isRedstone()) {

                Location location = new Location(Bukkit.getWorld(announceInfo.getWorldName()), announceInfo.getRedstonePosX(), announceInfo.getRedstonePosY(), announceInfo.getRedstonePosZ());

                if (announceInfo.getOnOrOff() == 0 && !location.getBlock().isBlockPowered()) {
                    //登録されているレッドストーン箇所に入力がない & 設定がON
                    return;
                } else if (announceInfo.getOnOrOff() == 1 && location.getBlock().isBlockPowered()) {
                    //登録されているレッドストーン箇所に入力がある & 設定がオフ
                    return;
                }
            }
        }


        //BossBarのセット
        BossBarUtil bossBarUtil = new BossBarUtil();
        if(announceInfo != null) {
            bossBarUtil.setBossBar(nextOrSoonOrStopOrMove, player, announceInfo, stationModel);
        }else{
            bossBarUtil.setBossBar(nextOrSoonOrStopOrMove, player, null, stationModel);
        }

        if(nextOrSoonOrStopOrMove != 2 && nextOrSoonOrStopOrMove != 3) {
            //チャットアナウンスの送信
            AnnounceUtil announceUtil = new AnnounceUtil();
            announceUtil.sendChatAnnounceOfNextSoon(announceInfo, player);
        }
    }
}
