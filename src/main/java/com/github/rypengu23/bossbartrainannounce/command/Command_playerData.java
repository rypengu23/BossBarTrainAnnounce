package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.PlayerDataDao;
import com.github.rypengu23.bossbartrainannounce.model.PlayerDataModel;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Command_playerData {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_playerData(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta showコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        Player player = (Player)sender;

        if(args[0].equalsIgnoreCase("speed")){

            if(args.length == 1){
                //速度変更
                toggleData(player, 0, false);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
            }

        }else if(args[0].equalsIgnoreCase("show")){

            if(args.length == 2){
                //ボスバー
                if(args[1].equalsIgnoreCase("bossbar")){
                    toggleData(player, 1, true);
                }else if(args[1].equalsIgnoreCase("announce")){
                    toggleData(player, 2, true);
                }
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
            }

        }else if(args[0].equalsIgnoreCase("hide")){

            if(args.length == 2){
                //ボスバー
                if(args[1].equalsIgnoreCase("bossbar")){
                    toggleData(player, 1, false);
                }else if(args[1].equalsIgnoreCase("announce")){
                    toggleData(player, 2, false);
                }
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
            }

        }else{
            //不正
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
        }
    }

    /**
     * 引数のコマンドがフラグ関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("speed");
        commandList.add("show");
        commandList.add("hide");

        return commandList.contains(command.toLowerCase());
    }

    /**
     * ボスバー・アナウンスの表示設定を編集
     * @param player
     * @return
     */
    public boolean toggleData(Player player, int type, boolean flag){

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.toggleSetting")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        UUID uuid = player.getUniqueId();

        //現在の設定を読み込み
        PlayerDataDao playerDataDao = new PlayerDataDao();
        PlayerDataModel playerData = playerDataDao.getPlayerData(uuid.toString());

        if(type==0) {
            //高速化
            flag = !playerData.isSpeedUpFlag();

            playerData.setSpeedUpFlag(flag);

            if(flag){
                //高速化
                BossBarTrainAnnounce.useMinecartSpeedUpPlayerList.add(uuid);
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_SetSpeed);
            }else{
                //標準速度
                BossBarTrainAnnounce.useMinecartSpeedUpPlayerList.remove(uuid);
                //減速処理
                if(player.getVehicle() != null){
                    Minecart minecart = (Minecart) player.getVehicle();
                    minecart.setMaxSpeed(0.4);
                }
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_LiftSpeed);
            }

        }else if(type==1){
            //BossBar
            if(playerData.isShowBossBar() == flag){
                //設定済の場合
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_AlreadySetting);
                return true;
            }

            playerData.setShowBossBar(flag);

            if(flag){
                //表示
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_ShowBossBar);
            }else{
                //非表示
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_HideBossBar);
            }

        }else if(type==2){
            //ChatAnnounce
            if(playerData.isShowChatAnnounce() == flag){
                //設定済の場合
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_AlreadySetting);
                return true;
            }

            playerData.setShowChatAnnounce(flag);

            if(flag){
                //表示
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_ShowAnnounce);
            }else{
                //非表示
                player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f"+ CommandMessage.Command_playerData_HideAnnounce);
            }
        }

        //メモリ更新
        BossBarTrainAnnounce.playerDataList.put(uuid, playerData);
        //DB更新
        playerDataDao.updatePlayerData(uuid.toString(), playerData);

        return true;
    }


}
