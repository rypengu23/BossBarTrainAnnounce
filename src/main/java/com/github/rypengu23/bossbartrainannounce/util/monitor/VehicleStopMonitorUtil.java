package com.github.rypengu23.bossbartrainannounce.util.monitor;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.PlayerDataModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import com.github.rypengu23.bossbartrainannounce.util.BossBarUtil;
import com.github.rypengu23.bossbartrainannounce.util.monitor.StationLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Boss;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class VehicleStopMonitorUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    private HashMap<Player, Location> playerLocationList = new HashMap<>();

    public VehicleStopMonitorUtil(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 駅内で停車したプレイヤーにボスバーを設定する
     */
    public void vehicleStopEvent(){

        Runnable vehicleStopMonitor = new Runnable() {
            int i = 0;
            @Override
            public void run() {

                //オンラインプレイヤーから、トロッコに乗車中のプレイヤーを取得
                Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
                for(Player player:playerList){
                    if(player.getVehicle() == null){
                        continue;
                    }else if(player.getVehicle().getType() != EntityType.MINECART){
                        continue;
                    }

                    UUID uuid = player.getUniqueId();
                    //現在地が駅内か確認
                    //駅内に居ない場合、次のプレイヤーへ
                    StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
                    if(!stationLocationJudgeUtil.checkAnnouncePosition(player.getVehicle().getLocation())){
                        continue;
                    }

                    //プレイヤーが駅内で動いたか確認する
                    //プレイヤーロケーションリストに登録されているか
                    if (!playerLocationList.containsKey(player)) {
                        //されていなければ新規追加
                        playerLocationList.put(player, player.getVehicle().getLocation());
                        continue;
                    } else {
                        //されていた場合 → 登録値を取得
                        Location playerLocationHistory = playerLocationList.get(player);

                        //登録値と現在地の値を比較 → 停止中の場合、ボスバーをセット
                        CheckUtil checkUtil = new CheckUtil();
                        if (!checkUtil.checkSameLocation(player.getVehicle().getLocation(), playerLocationHistory)) {
                            //動いていたので、ロケーションリストを更新する
                            playerLocationList.put(player, player.getVehicle().getLocation());

                            BossBarTrainAnnounce.stopInStationPlayerList.remove(uuid);
                            continue;

                        } else if(!BossBarTrainAnnounce.stopInStationPlayerList.contains(uuid)) {
                            //動いていないので、BossBarをセット

                            //停車中ステータス設定
                            BossBarTrainAnnounce.stopInStationPlayerList.add(uuid);

                            //プレイヤー情報取得
                            PlayerDataModel playerData = BossBarTrainAnnounce.playerDataList.get(uuid);
                            //駅情報取得
                            StationDao stationDao = new StationDao();
                            StationModel stationModel = stationDao.getStationForCoordinate(player.getVehicle().getLocation());
                            if (playerData.isShowBossBar()) {
                                BossBarUtil bossBar = new BossBarUtil();
                                bossBar.setBossBar(2, player, null, stationModel);
                            }
                        }
                    }
                }
            }
        };

        Bukkit.getServer().getScheduler().runTaskTimer(BossBarTrainAnnounce.getInstance(), vehicleStopMonitor, 0, 20);

    }

}
