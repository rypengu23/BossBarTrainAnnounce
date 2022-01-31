package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.util.BossBarUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.TaskUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.UUID;

public class Listener_VehicleExit implements Listener {

    @EventHandler
    /**
     * 降車時にBossBarを削除する
     */
    public void checkVehicleExit(VehicleExitEvent event){

        if(event.getVehicle().getType() != EntityType.MINECART){
            return;
        }

        if(event.getExited().getType() != EntityType.PLAYER){
            return;
        }

        BossBarUtil bossBarUtil = new BossBarUtil();
        TaskUtil taskUtil = new TaskUtil();
        Player player = (Player) event.getExited();
        UUID uuid = player.getUniqueId();

        //タスク終了
        taskUtil.removeTask(player);
        //監視リスト削除
        taskUtil.removeMonitorList(player);
        //ボスバー削除
        bossBarUtil.removeBossBar(player);

        //ロケーションリストから削除
        BossBarTrainAnnounce.playerLocationList.remove(uuid);
        BossBarTrainAnnounce.playerBefore1BlockLocationList.remove(uuid);

    }
}
