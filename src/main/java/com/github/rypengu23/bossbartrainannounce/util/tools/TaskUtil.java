package com.github.rypengu23.bossbartrainannounce.util.tools;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TaskUtil {

    /**
     * アナウンス・ボスバーのタスクを終了する
     * @param player
     */
    public void removeTask(Player player){

        UUID uuid = player.getUniqueId();

        //アナウンスタスク終了
        if(BossBarTrainAnnounce.announceTask.containsKey(uuid)) {
            BossBarTrainAnnounce.announceTask.get(uuid).cancel();
            BossBarTrainAnnounce.announceTask.remove(uuid);
        }
        //ボスバータスク終了
        if(BossBarTrainAnnounce.lcdTask.containsKey(uuid)){
            BossBarTrainAnnounce.lcdTask.get(uuid).cancel();
            BossBarTrainAnnounce.lcdTask.remove(uuid);
        }
    }

    /**
     * プレイヤー乗車監視リストから削除する
     * @param player
     */
    public void removeMonitorList(Player player){

        UUID uuid = player.getUniqueId();

        //乗車中リスト削除
        BossBarTrainAnnounce.moveInStationPlayerList.remove(uuid);
        BossBarTrainAnnounce.stopInStationPlayerList.remove(uuid);
    }
}
