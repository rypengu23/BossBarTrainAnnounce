package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.util.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.StationLocationJudgeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Command_Config {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_Config(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta reloadコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        Player player = (Player)sender;

        if(args[0].equalsIgnoreCase("reload")){

            if(args.length == 1){
                //リロード
                reload(player);
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
     * 引数のコマンドがConfig関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("reload");

        return commandList.contains(command.toLowerCase());
    }

    public boolean reload(Player player){

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.reload")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        //Config再読み込み
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.reloadConfig();

        //ロケーションキャッシュ再読み込み
        AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
        announceLocationJudgeUtil.updateAnnounceListCache();
        StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
        stationLocationJudgeUtil.updateStationListCache();

        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §fConfig・キャッシュのリロードが完了しました。");

        return true;
    }
}
