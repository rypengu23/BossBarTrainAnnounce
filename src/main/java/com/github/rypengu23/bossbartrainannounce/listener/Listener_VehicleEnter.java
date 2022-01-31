package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.UUID;

public class Listener_VehicleEnter implements Listener {

    /**
     * トロッコ乗車時にロケーションリストに追加
     * @param event
     */
    public void addPlayerLocationList(VehicleEnterEvent event){

        //プレイヤーかどうか
        if(event.getEntered().getType() != EntityType.PLAYER){
            return;
        }

        //乗車したものはトロッコかどうか
        if(event.getVehicle().getType() != EntityType.MINECART){
            return;
        }

        Player player = (Player) event.getEntered();
        UUID uuid = player.getUniqueId();

        //追加
        BossBarTrainAnnounce.playerLocationList.put(uuid, event.getVehicle().getLocation());
        BossBarTrainAnnounce.playerBefore1BlockLocationList.put(uuid, event.getVehicle().getLocation());

    }
}
