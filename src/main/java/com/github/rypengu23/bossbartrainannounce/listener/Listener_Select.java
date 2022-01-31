package com.github.rypengu23.bossbartrainannounce.listener;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.SelectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class Listener_Select implements Listener {

    private ConfigLoader configLoader;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;

    public Listener_Select(){
        updateConfig();
    }

    public void updateConfig(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * 選択情報を記録
     * @param event
     */
    @EventHandler
    public void selectPos(PlayerInteractEvent event){

        updateConfig();
        CheckUtil checkUtil = new CheckUtil();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int hand = 0;

        //ブロックをクリックか判定
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            hand = 0;
        }else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            hand = 1;
        }else{
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.select")){
            return;
        }

        //メインハンドに持っているアイテムが、選択用アイテムかどうか
        if(!player.getInventory().getItemInMainHand().getType().name().equalsIgnoreCase(mainConfig.getSelectItem())){
            return;
        }

        //ポジションリストに登録されていない場合、登録
        if(!BossBarTrainAnnounce.selectPosition.containsKey(player)){
            BossBarTrainAnnounce.selectPosition.put(uuid, new SelectPositionModel());
        }

        //ポジションを記録
        SelectPositionModel oldSelectPosition = BossBarTrainAnnounce.selectPosition.get(player);
        SelectPositionModel newSelectPosition = new SelectPositionModel();

        //同じ位置を選択した場合、何もしない
        if(hand == 0){
            if(oldSelectPosition.getWorldName().equalsIgnoreCase(event.getClickedBlock().getWorld().toString()) && oldSelectPosition.getPos1X() == event.getClickedBlock().getX() &&oldSelectPosition.getPos1Y() == event.getClickedBlock().getY() && oldSelectPosition.getPos1Z() == event.getClickedBlock().getZ()){
                return;
            }
        }else{
            if(oldSelectPosition.getWorldName().equalsIgnoreCase(event.getClickedBlock().getWorld().toString()) && oldSelectPosition.getPos2X() == event.getClickedBlock().getX() &&oldSelectPosition.getPos2Y() == event.getClickedBlock().getY() && oldSelectPosition.getPos2Z() == event.getClickedBlock().getZ()){
                return;
            }
        }


        if(hand == 0){
            newSelectPosition.setWorldName(event.getClickedBlock().getWorld().getName());
            newSelectPosition.setPos1X(event.getClickedBlock().getX());
            newSelectPosition.setPos1Y(event.getClickedBlock().getY());
            newSelectPosition.setPos1Z(event.getClickedBlock().getZ());
            newSelectPosition.setSelectPos1(true);

            //既に選択しているpos2のワールドと同一の場合、選択を保持
            if(oldSelectPosition.isSelectPos2() && event.getClickedBlock().getWorld().getName().equalsIgnoreCase(oldSelectPosition.getWorldName())){
                newSelectPosition.setPos2X(oldSelectPosition.getPos2X());
                newSelectPosition.setPos2Y(oldSelectPosition.getPos2Y());
                newSelectPosition.setPos2Z(oldSelectPosition.getPos2Z());
                newSelectPosition.setSelectPos2(true);
            }

        }else{
            newSelectPosition.setWorldName(event.getClickedBlock().getWorld().getName());
            newSelectPosition.setPos2X(event.getClickedBlock().getX());
            newSelectPosition.setPos2Y(event.getClickedBlock().getY());
            newSelectPosition.setPos2Z(event.getClickedBlock().getZ());
            newSelectPosition.setSelectPos2(true);

            //既に選択しているpos1のワールドと同一の場合、選択を保持
            if(oldSelectPosition.isSelectPos1() && event.getClickedBlock().getWorld().getName().equalsIgnoreCase(oldSelectPosition.getWorldName())){
                newSelectPosition.setPos1X(oldSelectPosition.getPos1X());
                newSelectPosition.setPos1Y(oldSelectPosition.getPos1Y());
                newSelectPosition.setPos1Z(oldSelectPosition.getPos1Z());
                newSelectPosition.setSelectPos1(true);
            }
        }

        BossBarTrainAnnounce.selectPosition.put(uuid, newSelectPosition);

        //ブロック数計算
        if(checkUtil.checkSelectPositionALL(player)) {
            //2点選択済み
            SelectUtil selectUtil = new SelectUtil();
            int area = selectUtil.calculationArea(newSelectPosition);

            if (hand == 0) {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f開始位置を (" + newSelectPosition.getPos1X() + " ," + newSelectPosition.getPos1Y() + " ," + newSelectPosition.getPos1Z() + ") (面積:"+ area +") に設定しました。");
            } else {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f終了位置を (" + newSelectPosition.getPos2X() + " ," + newSelectPosition.getPos2Y() + " ," + newSelectPosition.getPos2Z() + ") (面積:"+ area +") に設定しました。");
            }
        }else{
            if (hand == 0) {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f開始位置を (" + newSelectPosition.getPos1X() + " ," + newSelectPosition.getPos1Y() + " ," + newSelectPosition.getPos1Z() + ") に設定しました。");
            } else {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f終了位置を (" + newSelectPosition.getPos2X() + " ," + newSelectPosition.getPos2Y() + " ," + newSelectPosition.getPos2Z() + ") に設定しました。");
            }
        }
    }

    /**
     * 選択したブロックを破壊してしまった場合、キャンセル
     * @param event
     */
    @EventHandler
    public void checkSelectBlockBreak(BlockBreakEvent event) {

        updateConfig();

        CheckUtil checkUtil = new CheckUtil();
        Player player = event.getPlayer();
        Block block = event.getBlock();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.select")){
            return;
        }
        //メインハンドに持っているアイテムが、選択用アイテムかどうか
        if(!player.getInventory().getItemInMainHand().getType().name().equalsIgnoreCase(mainConfig.getSelectItem())){
            return;
        }
        //ポジションを選択しているか
        if(!BossBarTrainAnnounce.selectPosition.containsKey(player)){
            return;
        }
        //選択したポジションが壊れたブロックと同一か
        SelectPositionModel selectPositionModel = BossBarTrainAnnounce.selectPosition.get(player);
        if(!checkUtil.checkSameLocation(block.getLocation(), new Location(Bukkit.getWorld(selectPositionModel.getWorldName()), selectPositionModel.getPos1X(), selectPositionModel.getPos1Y(), selectPositionModel.getPos1Z()))){
            return;
        }

        //キャンセル
        event.setCancelled(true);
    }


}
