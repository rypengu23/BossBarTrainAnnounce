package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

    /**
     * 与えられた引数が数値か判定
     * @param str
     * @return
     */
    public boolean checkNumeric(String str){

        if(str == null){
            return false;
        }

        Pattern pattern = Pattern.compile("^[0-9]+$|-[0-9]+$");

        return pattern.matcher(str).matches();
    }

    /**
     * 与えられた引数が英数字・記号か判定
     * @param str
     * @return
     */
    public boolean checkAlphabetOrSymbol(String str){

        if(checkNullOrEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Za-z0-9ā-ū-_・･/<>@=%~～ー§,() 　]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 与えられた引数がカタカナ・数値記号か判定
     * @param str
     * @return
     */
    public boolean checkKatakanaOrSymbol(String str){

        if(checkNullOrEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Za-zā-ūァ-ヶ0-9-_・･/<>@=~%～ー§ 　]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 与えられた引数がnullか空白か判定
     * @param str
     * @return
     */
    public static boolean checkNullOrEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }

    /**
     * ターゲットの数値が2つの範囲の間にいるか判定
     * @param
     * @return
     */
    public boolean checkNumericRange(int target, int range1, int range2){

        int rangeMin = range1;
        int rangeMax = range2;

        if(range1 == range2){
            if(target == range1){
                return true;
            }else{
                return false;
            }
        }
        if(range1 > range2) {
            rangeMin = range2;
            rangeMax = range1;
        }

        if(rangeMin <= target && target <= rangeMax){
            return true;
        }
        return false;
    }

    /**
     * 引数の色名が存在するかチェック
     * @param str
     * @return
     */
    public boolean checkColorExist(String str){

        //引数がnull
        if(checkNullOrEmpty(str)){
            return false;
        }

        final String[] listName = {"PINK", "BLUE", "RED", "GREEN", "YELLOW", "PURPLE", "WHITE"};

        //色名リストに含まれているか
        for(String key:listName){
            if(str.equalsIgnoreCase(key)){
                return true;
            }
        }

        return false;
    }

    /**
     * プレイヤーがPos1・Pos2の両方を選択しているか判定。
     * 選択していればtrue していなければfalse
     * @param player
     * @return
     */
    public boolean checkSelectPositionALL(Player player){

        if(!BossBarTrainAnnounce.selectPosition.containsKey(player)){
            return false;
        }

        return BossBarTrainAnnounce.selectPosition.get(player).isSelectPos1() && BossBarTrainAnnounce.selectPosition.get(player).isSelectPos2();
    }

    /**
     * プレイヤーがPos1を選択しているか判定。
     * 選択していればtrue していなければfalse
     * @param player
     * @return
     */
    public boolean checkSelectPositionPos1(Player player){

        if(!BossBarTrainAnnounce.selectPosition.containsKey(player)){
            return false;
        }

        return BossBarTrainAnnounce.selectPosition.get(player).isSelectPos1();
    }

    /**
     * プレイヤーがPos2を選択しているか判定。
     * 選択していればtrue していなければfalse
     * @param player
     * @return
     */
    public boolean checkSelectPositionPos2(Player player){

        if(!BossBarTrainAnnounce.selectPosition.containsKey(player)){
            return false;
        }

        return BossBarTrainAnnounce.selectPosition.get(player).isSelectPos2();
    }

    /**
     * 引数がnullまたは空白か判定
     * @param str
     * @return
     */
    public boolean checkNullOrBlank(String str){

        if(str == null){
            return true;
        }
        if(str.equals("")){
            return true;
        }
        return false;
    }

    /**
     * 引数のロケーション2つが同一か判定
     * @param location1
     * @param location2
     * @return
     */
    public static boolean checkSameLocation(Location location1, Location location2){

        if(location1.getWorld() == location2.getWorld() && location1.getX() == location2.getX() && location1.getY() == location1.getY() && location1.getZ() == location2.getZ()){
            return true;
        }
        return false;
    }

    /**
     * 引数の文字列が、引数の文字数を超えているか判定
     * @param word 文字列
     * @param max 最大文字数
     * @return
     */
    public boolean checkLength(String word,int max){

        if(word.length() > max){
            return true;
        }
        return false;
    }

    /**
     * 選択した２箇所の座標が隣接している場合、走行する方角を送信。
     * -1:隣接していない 1:北 2:南 3:東 4:西
     * 隣接していない場合、nullを返す
     * @param selectPositionModel
     * @return
     */
    public int checkPositionAdjacent(SelectPositionModel selectPositionModel){

        //pos1とpos2が同一
        if(checkSameLocation(new Location(Bukkit.getServer().getWorld(selectPositionModel.getWorldName()), selectPositionModel.getPos1X(), selectPositionModel.getPos1Y(), selectPositionModel.getPos1Z()), new Location(Bukkit.getServer().getWorld(selectPositionModel.getWorldName()), selectPositionModel.getPos2X(), selectPositionModel.getPos2Y(), selectPositionModel.getPos2Z()))){
            return -1;
        }
        //Y座標が違う
        if(selectPositionModel.getPos1Y() != selectPositionModel.getPos2Y()){
            return -1;
        }

        //隣接チェック
        if(Math.abs(selectPositionModel.getPos1X() - selectPositionModel.getPos2X()) != 1 && selectPositionModel.getPos1Z() == selectPositionModel.getPos2Z()){
            return -1;
        }
        if(Math.abs(selectPositionModel.getPos1Z() - selectPositionModel.getPos2Z()) != 1 && selectPositionModel.getPos1X() == selectPositionModel.getPos2X()){
            return -1;
        }

        //北
        if(selectPositionModel.getPos1Z() - selectPositionModel.getPos2Z() == -1){
            return 1;
        }
        //南
        if(selectPositionModel.getPos1Z() - selectPositionModel.getPos2Z() == 1){
            return 2;
        }
        //東
        if(selectPositionModel.getPos1X() - selectPositionModel.getPos2X() == 1){
            return 3;
        }
        //西
        if(selectPositionModel.getPos1X() - selectPositionModel.getPos2X() == -1){
            return 4;
        }

        return -1;
    }

}
