package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.util.BossBarUtil;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.util.TaskUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class Listener_VehicleExit implements Listener {

    @EventHandler
    /**
     * 降車時にBossBarを削除する
     */
    public void checkVehicleExit(VehicleExitEvent event){

        if(event.getVehicle().getType() != EntityType.MINECART){
            return;
        }

        BossBarUtil bossBarUtil = new BossBarUtil();
        TaskUtil taskUtil = new TaskUtil();
        Player player = (Player) event.getExited();

        //タスク終了
        taskUtil.removeTask(player);
        //監視リスト削除
        taskUtil.removeMonitorList(player);
        //ボスバー削除
        bossBarUtil.removeBossBar(player);

    }
}
