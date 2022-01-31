package com.github.rypengu23.bossbartrainannounce.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> onCompList = new ArrayList<>();

        if(args.length == 1) {
            //路線関連
            if(sender.hasPermission("bossBarTrainAnnounce.registLine")){
                onCompList.add("registLine");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.removeLine")){
                onCompList.add("removeLine");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.changeLineName")){
                onCompList.add("changeLineName");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.changeLineColor")){
                onCompList.add("changeLineColor");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.addType")){
                onCompList.add("addType");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.removeType")){
                onCompList.add("removeType");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.changeTypeName")){
                onCompList.add("changeTypeName");
            }

            //駅関連
            if(sender.hasPermission("bossBarTrainAnnounce.registStation")){
                onCompList.add("registStation");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.removeStation")){
                onCompList.add("removeStation");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.changeStationName")){
                onCompList.add("changeStationName");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.changeStationPosition")){
                onCompList.add("moveStation");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.changeStationNumber")){
                onCompList.add("changeStationNumber");
            }

            //アナウンス関連
            if(sender.hasPermission("bossBarTrainAnnounce.registAnnounce")){
                onCompList.add("next");
                onCompList.add("soon");
            }
            if(sender.hasPermission("bossBarTrainAnnounce.removeAnnounce")){
                onCompList.add("remove");
            }

            //フラグ関連
            if(sender.hasPermission("bossBarTrainAnnounce.registBound") ||
                    sender.hasPermission("bossBarTrainAnnounce.removeBound") ||
                    sender.hasPermission("bossBarTrainAnnounce.registTerminal") ||
                sender.hasPermission("bossBarTrainAnnounce.registDirection")){
                onCompList.add("flag");
            }

            //情報
            if(sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("info");
            }

            //選択
            if(sender.hasPermission("bossBarTrainAnnounce.select")){
                onCompList.add("select");
            }

            //プレイヤーデータ関連
            if(sender.hasPermission("bossBarTrainAnnounce.toggleSetting")){
                onCompList.add("speed");
                onCompList.add("show");
                onCompList.add("hide");
            }

            //Config関連
            if(sender.hasPermission("bossBarTrainAnnounce.reload")){
                onCompList.add("reload");
            }

            //Database関連
            if(sender.hasPermission("bossBarTrainAnnounce.database")){
                onCompList.add("updateDatabase");
            }

        }else if(args.length == 2){

            //路線関連
            if(args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")){
                onCompList.add("路線名(和名)");
            }
            if(args[0].equalsIgnoreCase("removeline") && sender.hasPermission("bossBarTrainAnnounce.removeLine")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("changelinename") && sender.hasPermission("bossBarTrainAnnounce.changeLineName")){
                onCompList.add("現在の路線名");
            }
            if(args[0].equalsIgnoreCase("changelinecolor") && sender.hasPermission("bossBarTrainAnnounce.changeLineColor")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("addtype") && sender.hasPermission("bossBarTrainAnnounce.addType")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("removetype") && sender.hasPermission("bossBarTrainAnnounce.removeType")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("changeTypeName") && sender.hasPermission("bossBarTrainAnnounce.changeTypeName")){
                onCompList.add("路線名");
            }

            //駅関連
            if(args[0].equalsIgnoreCase("registstation") && sender.hasPermission("bossBarTrainAnnounce.registStation")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("removestation") && sender.hasPermission("bossBarTrainAnnounce.removeStation")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("changestationname") && sender.hasPermission("bossBarTrainAnnounce.changeStationName")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("movestation") && sender.hasPermission("bossBarTrainAnnounce.changeStationPosition")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("changestationnumber") && sender.hasPermission("bossBarTrainAnnounce.changeStationNumber")){
                onCompList.add("路線名");
            }

            //アナウンス関連
            if((args[0].equalsIgnoreCase("next") || args[0].equalsIgnoreCase("soon")) && sender.hasPermission("bossBarTrainAnnounce.registAnnounce")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("bossBarTrainAnnounce.removeAnnounce")){
                onCompList.add("next");
                onCompList.add("soon");
            }

            //フラグ関連
            if(args[0].equalsIgnoreCase("flag") && (sender.hasPermission("bossBarTrainAnnounce.registBound") || sender.hasPermission("bossBarTrainAnnounce.removeBound"))){
                onCompList.add("bound");
            }
            if(args[0].equalsIgnoreCase("flag") && sender.hasPermission("bossBarTrainAnnounce.registTerminal")){
                onCompList.add("terminal");
            }
            if(args[0].equalsIgnoreCase("flag") && (sender.hasPermission("bossBarTrainAnnounce.registRedstone") || sender.hasPermission("bossBarTrainAnnounce.removeRedstone"))){
                onCompList.add("redstone");
            }
            if(args[0].equalsIgnoreCase("flag") && (sender.hasPermission("bossBarTrainAnnounce.registDirection") || sender.hasPermission("bossBarTrainAnnounce.removeRedstone"))){
                onCompList.add("direction");
            }
            if(args[0].equalsIgnoreCase("flag") && (sender.hasPermission("bossBarTrainAnnounce.registFastFlag") || sender.hasPermission("bossBarTrainAnnounce.removeRedstone"))){
                onCompList.add("fast");
            }

            //情報関連
            if(args[0].equalsIgnoreCase("info") && sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("line");
                onCompList.add("station");
                onCompList.add("announce");
            }

            //選択
            if(args[0].equalsIgnoreCase("select") && sender.hasPermission("bossBarTrainAnnounce.select")){
                onCompList.add("pos1");
                onCompList.add("pos2");
            }

            //プレイヤーデータ関連
            if((args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("hide")) && sender.hasPermission("bossBarTrainAnnounce.toggleSetting")){
                onCompList.add("bossbar");
                onCompList.add("announce");
            }

            //Database関連
            if(args[0].equalsIgnoreCase("updateDatabase") && sender.hasPermission("bossBarTrainAnnounce.database")){
                onCompList.add("1.0");
            }

        }else if(args.length == 3){

            //路線関連
            if(args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")){
                onCompList.add("路線名(英語)");
            }
            if(args[0].equalsIgnoreCase("changelinename") && sender.hasPermission("bossBarTrainAnnounce.changeLineName")){
                onCompList.add("新しい路線名(和名)");
            }
            if(args[0].equalsIgnoreCase("changelinecolor") && sender.hasPermission("bossBarTrainAnnounce.changeLineColor")){
                onCompList.add("PINK");
                onCompList.add("BLUE");
                onCompList.add("RED");
                onCompList.add("GREEN");
                onCompList.add("YELLOW");
                onCompList.add("PURPLE");
                onCompList.add("WHITE");
                onCompList.add("路線カラー");
            }
            if(args[0].equalsIgnoreCase("addtype") && sender.hasPermission("bossBarTrainAnnounce.addType")){
                onCompList.add("種別名(和名)");
            }
            if(args[0].equalsIgnoreCase("removetype") && sender.hasPermission("bossBarTrainAnnounce.removeType")){
                onCompList.add("種別名");
            }
            if(args[0].equalsIgnoreCase("changeTypeName") && sender.hasPermission("bossBarTrainAnnounce.changeTypeName")){
                onCompList.add("現在の種別名");
            }

            //駅関連
            if(args[0].equalsIgnoreCase("registstation") && sender.hasPermission("bossBarTrainAnnounce.registStation")){
                onCompList.add("駅名(和名)");
            }
            if(args[0].equalsIgnoreCase("removestation") && sender.hasPermission("bossBarTrainAnnounce.removeStation")){
                onCompList.add("駅名");
            }
            if(args[0].equalsIgnoreCase("changestationname") && sender.hasPermission("bossBarTrainAnnounce.changeStationName")){
                onCompList.add("現在の駅名");
            }
            if(args[0].equalsIgnoreCase("movestation") && sender.hasPermission("bossBarTrainAnnounce.changeStationPosition")){
                onCompList.add("駅名");
            }
            if(args[0].equalsIgnoreCase("changestationnumber") && sender.hasPermission("bossBarTrainAnnounce.changeStationNumber")){
                onCompList.add("駅名");
            }

            //アナウンス関連
            if((args[0].equalsIgnoreCase("next") || args[0].equalsIgnoreCase("soon")) && sender.hasPermission("bossBarTrainAnnounce.registAnnounce")){
                onCompList.add("種別");
            }
            if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("bossBarTrainAnnounce.removeAnnounce")){
                onCompList.add("路線名");
            }

            //フラグ関連
            if(args[0].equalsIgnoreCase("flag") && args[1].equalsIgnoreCase("bound") && sender.hasPermission("bossBarTrainAnnounce.registBound")){
                onCompList.add("行き先(和名)");
            }
            if(args[0].equalsIgnoreCase("flag") && args[1].equalsIgnoreCase("redstone") && sender.hasPermission("bossBarTrainAnnounce.registRedstone")){
                onCompList.add("on");
                onCompList.add("off");
            }

            //情報関連
            if(args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("line") && sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("station") && sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("路線名");
            }
            if(args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("announce") && sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("路線名");
            }

            //選択
            if(args[0].equalsIgnoreCase("select") && sender.hasPermission("bossBarTrainAnnounce.select")){
                onCompList.add("x");
            }

        }else if(args.length == 4){

            //路線関連
            if(args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")){
                onCompList.add("PINK");
                onCompList.add("BLUE");
                onCompList.add("RED");
                onCompList.add("GREEN");
                onCompList.add("YELLOW");
                onCompList.add("PURPLE");
                onCompList.add("WHITE");
                onCompList.add("路線カラー");
            }
            if(args[0].equalsIgnoreCase("changelinename") && sender.hasPermission("bossBarTrainAnnounce.changeLineName")){
                onCompList.add("新しい路線名(英語)");
            }
            if(args[0].equalsIgnoreCase("addtype") && sender.hasPermission("bossBarTrainAnnounce.addType")){
                onCompList.add("種別名(英語)");
            }
            if(args[0].equalsIgnoreCase("changeTypeName") && sender.hasPermission("bossBarTrainAnnounce.changeTypeName")){
                onCompList.add("新しい種別名(和名)");
            }


            //駅関連
            if(args[0].equalsIgnoreCase("registstation") && sender.hasPermission("bossBarTrainAnnounce.registStation")){
                onCompList.add("駅名(英語)");
            }
            if(args[0].equalsIgnoreCase("changestationname") && sender.hasPermission("bossBarTrainAnnounce.changeStationName")){
                onCompList.add("新しい駅名(和名)");
            }
            if(args[0].equalsIgnoreCase("changestationnumber") && sender.hasPermission("bossBarTrainAnnounce.changeStationNumber")){
                onCompList.add("新しいナンバリング");
            }

            //アナウンス関連
            if((args[0].equalsIgnoreCase("next") || args[0].equalsIgnoreCase("soon")) && sender.hasPermission("bossBarTrainAnnounce.registAnnounce")){
                onCompList.add("次の駅名");
            }
            if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("bossBarTrainAnnounce.removeAnnounce")){
                onCompList.add("種別");
            }

            //フラグ関連
            if(args[0].equalsIgnoreCase("flag") && args[1].equalsIgnoreCase("bound") && sender.hasPermission("bossBarTrainAnnounce.registBound")){
                onCompList.add("行き先(英語)");
            }

            //情報関連
            if(args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("station") && sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("駅名");
            }
            if(args[0].equalsIgnoreCase("info") && args[1].equalsIgnoreCase("announce") && sender.hasPermission("bossBarTrainAnnounce.info")){
                onCompList.add("次の駅名");
            }

            //選択
            if(args[0].equalsIgnoreCase("select") && sender.hasPermission("bossBarTrainAnnounce.select")){
                onCompList.add("y");
            }

        }else if(args.length == 5){

            //路線関連
            if(args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")){
                onCompList.add("各駅停車");
                onCompList.add("快速");
                onCompList.add("特別快速");
                onCompList.add("普通");
                onCompList.add("準急");
                onCompList.add("急行");
                onCompList.add("特急");
                onCompList.add("ホリデー快速おくたま号");
                onCompList.add("種別名(和名)");
            }
            if(args[0].equalsIgnoreCase("changeTypeName") && sender.hasPermission("bossBarTrainAnnounce.changeTypeName")){
                onCompList.add("新しい種別名(英語)");
            }


            //駅関連
            if(args[0].equalsIgnoreCase("registstation") && sender.hasPermission("bossBarTrainAnnounce.registStation")){
                onCompList.add("駅名(カタカナ)");
            }
            if(args[0].equalsIgnoreCase("changestationname") && sender.hasPermission("bossBarTrainAnnounce.changeStationName")){
                onCompList.add("新しい駅名(英語)");
            }

            //アナウンス関連
            if(args[0].equalsIgnoreCase("next") && sender.hasPermission("bossBarTrainAnnounce.registAnnounce")){
                onCompList.add("RIGHT");
                onCompList.add("LEFT");
                onCompList.add("降車口(オプション)");
            }
            if(args[0].equalsIgnoreCase("soon") && sender.hasPermission("bossBarTrainAnnounce.registAnnounce")){
                onCompList.add("RIGHT");
                onCompList.add("LEFT");
                onCompList.add("降車口");
            }
            if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("bossBarTrainAnnounce.removeAnnounce")){
                onCompList.add("次の駅名");
            }

            //フラグ関連
            if(args[0].equalsIgnoreCase("flag") && args[1].equalsIgnoreCase("bound") && sender.hasPermission("bossBarTrainAnnounce.registBound")){
                onCompList.add("直通先路線名(オプション)");
            }

            //選択
            if(args[0].equalsIgnoreCase("select") && sender.hasPermission("bossBarTrainAnnounce.select")){
                onCompList.add("z");
            }

        }else if(args.length == 6){

            //路線関連
            if(args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")){
                onCompList.add("local");
                onCompList.add("rapid");
                onCompList.add("special%%rapid");
                onCompList.add("semi%%Express");
                onCompList.add("express");
                onCompList.add("special%%express");
                onCompList.add("okutama%%holiday%%rapid");
                onCompList.add("種別名(英語)");
            }

            //駅関連
            if(args[0].equalsIgnoreCase("registstation") && sender.hasPermission("bossBarTrainAnnounce.registStation")){
                onCompList.add("ナンバリング(任意)");
            }
            if(args[0].equalsIgnoreCase("changestationname") && sender.hasPermission("bossBarTrainAnnounce.changeStationName")){
                onCompList.add("新しい駅名(カタカナ)");
            }

            //フラグ関連
            if(args[0].equalsIgnoreCase("flag") && args[1].equalsIgnoreCase("bound") && sender.hasPermission("bossBarTrainAnnounce.registBound")){
                for(Player player:Bukkit.getServer().getOnlinePlayers()){
                    onCompList.add(player.getName());
                }
                onCompList.add("直通先路線所有者(オプション)");
            }

        }else if(args.length == 7) {

            //路線関連
            if (args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")) {
                onCompList.add("true");
                onCompList.add("false");
                onCompList.add("環状線フラグ(任意)");
            }
        }else if(args.length == 8) {

            //路線関連
            if (args[0].equalsIgnoreCase("registline") && sender.hasPermission("bossBarTrainAnnounce.registLine")) {
                onCompList.add("true");
                onCompList.add("false");
                onCompList.add("地下鉄フラグ(任意)");
            }
        }

        if(command.getName().equalsIgnoreCase("bbta") && onCompList.size() > 0){
            return onCompList;
        }
        return null;
    }




}
