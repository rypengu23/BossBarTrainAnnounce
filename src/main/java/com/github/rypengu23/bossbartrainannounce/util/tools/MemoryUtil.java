package com.github.rypengu23.bossbartrainannounce.util.tools;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.util.PlayerDataUtil;
import com.github.rypengu23.bossbartrainannounce.util.SelectUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MemoryUtil {

    public void loadMemory(Player player){

        UUID uuid = player.getUniqueId();

        //SelectPosition
        SelectUtil selectUtil = new SelectUtil();
        selectUtil.initPlayerData(player);

        //PlayerData
        PlayerDataUtil playerDataUtil = new PlayerDataUtil();
        playerDataUtil.loadPlayerData(player);

        //Location
        BossBarTrainAnnounce.playerLocationList.put(uuid, player.getLocation());
        BossBarTrainAnnounce.playerBefore1BlockLocationList.put(uuid, player.getLocation());
    }

    public void unloadMemory(Player player){

        UUID uuid = player.getUniqueId();

        //SelectPosition
        SelectUtil selectUtil = new SelectUtil();
        selectUtil.unloadPlayerData(player);

        //PlayerData
        PlayerDataUtil playerDataUtil = new PlayerDataUtil();
        playerDataUtil.unloadPlayerData(player);

        //Location
        BossBarTrainAnnounce.playerLocationList.remove(uuid);
        BossBarTrainAnnounce.playerBefore1BlockLocationList.remove(uuid);
    }
}
