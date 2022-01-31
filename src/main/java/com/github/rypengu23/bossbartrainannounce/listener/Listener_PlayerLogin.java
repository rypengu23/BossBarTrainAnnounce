package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.util.tools.MemoryUtil;
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
    public void loadData(PlayerLoginEvent event){

        Player player = event.getPlayer();

        MemoryUtil memoryUtil = new MemoryUtil();
        memoryUtil.loadMemory(player);
    }
}
