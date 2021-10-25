package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;

public class TaskUtil {

    /**
     * アナウンス・ボスバーのタスクを終了する
     * @param player
     */
    public void removeTask(Player player){

        //アナウンスタスク終了
        if(BossBarTrainAnnounce.announceTask.containsKey(player)) {
            BossBarTrainAnnounce.announceTask.get(player).cancel();
            BossBarTrainAnnounce.announceTask.remove(player);
        }
        //ボスバータスク終了
        if(BossBarTrainAnnounce.lcdTask.containsKey(player)){
            BossBarTrainAnnounce.lcdTask.get(player).cancel();
            BossBarTrainAnnounce.lcdTask.remove(player);
        }
    }

    /**
     * プレイヤー乗車監視リストから削除する
     * @param player
     */
    public void removeMonitorList(Player player){

        //乗車中リスト削除
        BossBarTrainAnnounce.moveInStationPlayerList.remove(player);
        BossBarTrainAnnounce.stopInStationPlayerList.remove(player);
    }
}
