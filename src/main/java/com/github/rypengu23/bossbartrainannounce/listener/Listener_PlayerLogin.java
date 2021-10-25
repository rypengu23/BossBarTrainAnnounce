package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Listener_PlayerLogin implements Listener {

    /**
     * プレイヤーログイン時、選択情報を作成
     * @param event
     */
    @EventHandler
    public void createPositionData(PlayerLoginEvent event){

        Player player = event.getPlayer();

        BossBarTrainAnnounce.selectPosition.put(player, new SelectPositionModel());
    }
}
