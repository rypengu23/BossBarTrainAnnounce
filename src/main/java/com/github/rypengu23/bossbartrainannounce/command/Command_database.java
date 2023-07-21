package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.ConnectDao;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Command_database {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_database(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta updateDatabaseコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        Player player = (Player)sender;

        if(args[0].equalsIgnoreCase("updateDatabase")){

            if(args.length == 2){
                //アップデート
                updateDatabase(player, args[1]);
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
     * 引数のコマンドがDatabase関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("updateDatabase");

        return commandList.contains(command.toLowerCase());
    }

    public boolean updateDatabase(Player player, String version){

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.database")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return false;
        }

        if(version.equals("1.0")){
            updateFrom10();
            return true;
        }

        return false;
    }

    private int updateFrom10(){
        ConnectDao connectDao = new ConnectDao();
        Connection connection = connectDao.getConnection();
        int result = 0;

        try {
            int p = 1;

            String sql1 = "ALTER TABLE BBTA_AnnounceInfo ADD FAST_FLAG INT NOT NULL DEFAULT 0 ";

            PreparedStatement ps = connection.prepareStatement(sql1);
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
