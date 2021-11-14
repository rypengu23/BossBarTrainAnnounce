package com.github.rypengu23.bossbartrainannounce;

import com.github.rypengu23.bossbartrainannounce.command.*;
import com.github.rypengu23.bossbartrainannounce.config.*;
import com.github.rypengu23.bossbartrainannounce.dao.ConnectDao;
import com.github.rypengu23.bossbartrainannounce.listener.*;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import com.github.rypengu23.bossbartrainannounce.util.AnnounceLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.StationLocationJudgeUtil;
import com.github.rypengu23.bossbartrainannounce.util.VehicleStopMonitorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public final class BossBarTrainAnnounce extends JavaPlugin implements Listener {

    //バージョン
    public static double pluginVersion = 1.0;

    //インスタンス
    private static BossBarTrainAnnounce instance = null;

    //Config
    private ConfigLoader configLoader;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;

    //メモリ
    public static ArrayList<Player> moveInStationPlayerList = new ArrayList<>();
    public static ArrayList<Player> stopInStationPlayerList = new ArrayList<>();
    public static HashMap<String, Location> playerLocationList = new HashMap<String, Location>();
    public static HashMap<String, Location> playerBefore1BlockLocationList = new HashMap<String, Location>();
    public static HashMap<Player, SelectPositionModel> selectPosition = new HashMap<>();

    //メモリ(アナウンス情報)
    public static ArrayList<AnnounceInfoModel> announceInfoList = new ArrayList<>();
    public static ArrayList<StationModel> stationInfoList = new ArrayList<>();

    //タスク
    public static HashMap<Player, BukkitTask> announceTask = new HashMap<>();
    public static HashMap<Player, BukkitTask> lcdTask = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        configLoader.reloadConfig();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();

        //起動メッセージ
        //Startup message
        Bukkit.getLogger().info("[BossBarTrainAnnounce] == BossBarTrainAnnounce Ver" + pluginVersion + " ==");
        Bukkit.getLogger().info("[BossBarTrainAnnounce] " + ConsoleMessage.BossBarTrainAnnounce_startupPlugin);

        //Configの更新確認
        ConfigUpdater configUpdater = new ConfigUpdater();
        if(configUpdater.configUpdateCheck()){
            configLoader = new ConfigLoader();
            configLoader.reloadConfig();
            mainConfig = configLoader.getMainConfig();
            messageConfig = configLoader.getMessageConfig();
        }

        //データベース接続
        ConnectDao connectDao = new ConnectDao();
        if(connectDao.connectionCheck()){
            Bukkit.getLogger().info("[BossBarTrainAnnounce] データベースへの接続に成功しました。");

            //アナウンスロケーションの読み込み
            AnnounceLocationJudgeUtil announceLocationJudgeUtil = new AnnounceLocationJudgeUtil();
            StationLocationJudgeUtil stationLocationJudgeUtil = new StationLocationJudgeUtil();
            announceLocationJudgeUtil.updateAnnounceListCache();
            stationLocationJudgeUtil.updateStationListCache();
        }else{
            Bukkit.getLogger().warning("[BossBarTrainAnnounce] データベースへの接続に失敗しました。");
        }

        //リスナー登録
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Listener_Select(), this);
        pm.registerEvents(new Listener_VehicleMove(), this);
        pm.registerEvents(new Listener_VehicleExit(), this);
        pm.registerEvents(new Listener_PlayerLogin(), this);
        pm.registerEvents(new Listener_PlayerLogout(), this);

        //トロッコ停車監視
        VehicleStopMonitorUtil vehicleStopMonitorUtil = new VehicleStopMonitorUtil();
        vehicleStopMonitorUtil.vehicleStopEvent();

        //コマンド入力時の入力補助
        TabComplete tabComplete = new TabComplete();
        getCommand("bbta").setTabCompleter(tabComplete);

        Bukkit.getLogger().info("[BossBarTrainAnnounce] プラグインの起動が完了しました。");
    }

    public static BossBarTrainAnnounce getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("bbta")) {
            //引数があるかどうか
            if(args.length != 0){
                Command_announce command_announce = new Command_announce();
                Command_line command_line = new Command_line();
                Command_station command_station = new Command_station();
                Command_flag command_flag = new Command_flag();
                Command_info command_info = new Command_info();
                Command_select command_select = new Command_select();
                Command_Config command_config = new Command_Config();

                if (sender instanceof Player) {
                    if (command_announce.checkCommandExit(args[0])) {
                        command_announce.sort(sender, args);
                    } else if (command_line.checkCommandExit(args[0])) {
                        command_line.sort(sender, args);
                    } else if (command_station.checkCommandExit(args[0])) {
                        command_station.sort(sender, args);
                    } else if (command_flag.checkCommandExit(args[0])) {
                        command_flag.sort(sender, args);
                    } else if (command_info.checkCommandExit(args[0])) {
                        command_info.sort(sender, args);
                    } else if (command_select.checkCommandExit(args[0])) {
                        command_select.sort(sender, args);
                    } else if (command_config.checkCommandExit(args[0])) {
                        command_config.sort(sender, args);
                    } else {
                        sender.sendMessage("§c[" + mainConfig.getPrefix() + "] §f" + CommandMessage.CommandFailure);
                    }
                }else{
                    sender.sendMessage("§c[" + mainConfig.getPrefix() + "] §f" + CommandMessage.CommandFailure);
                }

            }else{
                //無ければバージョン情報
                sender.sendMessage("BossBarTrainAnnounce "+ pluginVersion);
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
