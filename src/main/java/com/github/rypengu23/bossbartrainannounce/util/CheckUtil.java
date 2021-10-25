package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
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

        for(int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
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
    public boolean checkSameLocation(Location location1, Location location2){

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

}
