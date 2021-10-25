package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.util.BossBarUtil;
import com.github.rypengu23.bossbartrainannounce.util.TaskUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listener_PlayerLogout implements Listener {

    @EventHandler
    /**
     * ログアウト時にBossBar・アナウンスの削除
     * セレクトポジションリストからの削除を行う
     * @param event
     */
    public void checkPlayerLogout(PlayerQuitEvent event){

        Player player = event.getPlayer();
        BossBarUtil bossBarUtil = new BossBarUtil();
        TaskUtil taskUtil = new TaskUtil();

        //タスク終了
        taskUtil.removeTask(player);
        //監視リスト削除
        taskUtil.removeMonitorList(player);
        //ボスバー削除
        bossBarUtil.removeBossBar(player);
        //セレクトポジション削除
        BossBarTrainAnnounce.selectPosition.remove(player);
    }
}

