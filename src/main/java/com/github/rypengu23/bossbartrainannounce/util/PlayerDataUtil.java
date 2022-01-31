package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.PlayerDataDao;
import com.github.rypengu23.bossbartrainannounce.model.PlayerDataModel;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerDataUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public PlayerDataUtil(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * プレイヤーデータを読み込み
     * @param player
     */
    public void loadPlayerData(Player player){

        PlayerDataDao playerDataDao = new PlayerDataDao();
        UUID uuid = player.getUniqueId();
        String uuidStr = uuid.toString();

        PlayerDataModel playerDataModel = playerDataDao.getPlayerData(uuidStr);

        if(playerDataModel == null){
            playerDataDao.insertPlayerData(uuidStr, new PlayerDataModel(mainConfig.isDefaultMode(), true, true));
            playerDataModel = playerDataDao.getPlayerData(uuidStr);
        }

        if(playerDataModel.isSpeedUpFlag()){
            BossBarTrainAnnounce.useMinecartSpeedUpPlayerList.add(uuid);
        }

        BossBarTrainAnnounce.playerDataList.put(uuid, playerDataModel);
    }

    /**
     * プレイヤーデータをアンロード
     * @param player
     */
    public void unloadPlayerData(Player player){

        PlayerDataDao playerDataDao = new PlayerDataDao();
        UUID uuid = player.getUniqueId();

        PlayerDataModel playerDataModel = playerDataDao.getPlayerData(uuid.toString());

        BossBarTrainAnnounce.playerDataList.remove(uuid);
    }

    /**
     * プレイヤーデータを更新
     * @param player
     * @param playerDataModel
     * @return
     */
    public PlayerDataModel updatePlayerData(Player player, PlayerDataModel playerDataModel){

        PlayerDataDao playerDataDao = new PlayerDataDao();
        String uuid = player.getUniqueId().toString();

        playerDataDao.updatePlayerData(uuid, playerDataModel);

        return playerDataDao.getPlayerData(uuid);
    }
}
