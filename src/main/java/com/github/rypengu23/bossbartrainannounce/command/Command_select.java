package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.SelectUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Command_select {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_select(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta selectコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        Player player = (Player)sender;

        //フラグ
        if(args.length >= 2) {
            if (args[1].equalsIgnoreCase("pos1")) {

                if (args.length == 5) {
                    //pos1選択
                    selectPosition(player, 0, args[2], args[3], args[4]);
                } else {
                    //不正
                    player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f" + CommandMessage.CommandFailure);
                    return;
                }

            } else if (args[1].equalsIgnoreCase("pos2")) {

                if (args.length == 5) {
                    //pos2選択
                    selectPosition(player, 1, args[2], args[3], args[4]);
                } else {
                    //不正
                    player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f" + CommandMessage.CommandFailure);
                    return;
                }
            } else {
                //不正
                player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f" + CommandMessage.CommandFailure);
                return;
            }
        } else {
            //不正
            player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f" + CommandMessage.CommandFailure);
            return;
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
        commandList.add("select");

        return commandList.contains(command.toLowerCase());
    }

    /**
     * コマンドから座標を選択
     * @param player
     * @param pos1OrPos2
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean selectPosition(Player player, int pos1OrPos2, String x, String y, String z){

        CheckUtil checkUtil = new CheckUtil();
        UUID uuid = player.getUniqueId();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.select")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //数値チェック
        if(!checkUtil.checkNumeric(x) && !checkUtil.checkNumeric(y) && !checkUtil.checkNumeric(z)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f数値以外が入力されています。");
            return false;
        }

        int numX = Integer.parseInt(x);
        int numY = Integer.parseInt(y);
        int numZ = Integer.parseInt(z);

        //ポジションリストに登録されていない場合、登録
        if(!BossBarTrainAnnounce.selectPosition.containsKey(uuid)){
            BossBarTrainAnnounce.selectPosition.put(uuid, new SelectPositionModel());
        }

        //ポジションを記録
        SelectPositionModel oldSelectPosition = BossBarTrainAnnounce.selectPosition.get(uuid);
        SelectPositionModel newSelectPosition = new SelectPositionModel();

        if(pos1OrPos2 == 0){
            newSelectPosition.setWorldName(player.getLocation().getWorld().getName());
            newSelectPosition.setPos1X(numX);
            newSelectPosition.setPos1Y(numY);
            newSelectPosition.setPos1Z(numZ);
            newSelectPosition.setSelectPos1(true);

            //既に選択しているpos2のワールドと同一の場合、選択を保持
            if(oldSelectPosition != null) {
                if (oldSelectPosition.isSelectPos2() && player.getLocation().getWorld().getName().equalsIgnoreCase(oldSelectPosition.getWorldName())) {
                    newSelectPosition.setPos2X(oldSelectPosition.getPos2X());
                    newSelectPosition.setPos2Y(oldSelectPosition.getPos2Y());
                    newSelectPosition.setPos2Z(oldSelectPosition.getPos2Z());
                    newSelectPosition.setSelectPos2(true);
                }
            }

        }else{
            newSelectPosition.setWorldName(player.getLocation().getWorld().getName());
            newSelectPosition.setPos2X(numX);
            newSelectPosition.setPos2Y(numY);
            newSelectPosition.setPos2Z(numZ);
            newSelectPosition.setSelectPos2(true);

            //既に選択しているpos1のワールドと同一の場合、選択を保持
            if(oldSelectPosition != null) {
                if (oldSelectPosition.isSelectPos1() && player.getLocation().getWorld().getName().equalsIgnoreCase(oldSelectPosition.getWorldName())) {
                    newSelectPosition.setPos1X(oldSelectPosition.getPos1X());
                    newSelectPosition.setPos1Y(oldSelectPosition.getPos1Y());
                    newSelectPosition.setPos1Z(oldSelectPosition.getPos1Z());
                    newSelectPosition.setSelectPos1(true);
                }
            }
        }

        BossBarTrainAnnounce.selectPosition.put(uuid, newSelectPosition);

        //ブロック数計算
        if(checkUtil.checkSelectPositionALL(player)) {
            //2点選択済み
            SelectUtil selectUtil = new SelectUtil();
            int area = selectUtil.calculationArea(newSelectPosition);

            if (pos1OrPos2 == 0) {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f開始位置を (" + newSelectPosition.getPos1X() + " ," + newSelectPosition.getPos1Y() + " ," + newSelectPosition.getPos1Z() + ") (面積:"+ area +") に設定しました。");
            } else {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f終了位置を (" + newSelectPosition.getPos2X() + " ," + newSelectPosition.getPos2Y() + " ," + newSelectPosition.getPos2Z() + ") (面積:"+ area +") に設定しました。");
            }
        }else{
            if (pos1OrPos2 == 0) {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f開始位置を (" + newSelectPosition.getPos1X() + " ," + newSelectPosition.getPos1Y() + " ," + newSelectPosition.getPos1Z() + ") に設定しました。");
            } else {
                player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f終了位置を (" + newSelectPosition.getPos2X() + " ," + newSelectPosition.getPos2Y() + " ," + newSelectPosition.getPos2Z() + ") に設定しました。");
            }
        }

        return true;
    }

}
