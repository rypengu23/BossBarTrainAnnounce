package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import org.bukkit.entity.Player;

public class SelectUtil {

    public SelectUtil() {
    }

    public void initPlayerData(Player player){
        BossBarTrainAnnounce.selectPosition.put(player.getUniqueId(), new SelectPositionModel());
    }

    public void unloadPlayerData(Player player){
        BossBarTrainAnnounce.selectPosition.remove(player.getUniqueId());
    }

    /**
     * 選択したエリアから面積を計算
     * @param selectPositionModel
     * @return
     */
    public int calculationArea(SelectPositionModel selectPositionModel){

        int x = 0;
        int y = 0;
        int z = 0;

        //X
        if(selectPositionModel.getPos1X() < 0 && selectPositionModel.getPos2X() < 0){
            //どちらもマイナス
            if(selectPositionModel.getPos1X() < selectPositionModel.getPos2X()){
                //pos1の方が小さい
                x = Math.abs(selectPositionModel.getPos1X()) - Math.abs(selectPositionModel.getPos2X()) + 1;
            }else if(selectPositionModel.getPos1X() > selectPositionModel.getPos2X()){
                //pos2の方が小さい
                x = Math.abs(selectPositionModel.getPos2X()) - Math.abs(selectPositionModel.getPos1X()) + 1;
            }else{
                //同じ
                x = 1;
            }
        }else if(selectPositionModel.getPos1X() >= 0 && selectPositionModel.getPos2X() >= 0){
            //どちらもプラスあるいは0
            if(selectPositionModel.getPos1X() < selectPositionModel.getPos2X()){
                //pos1の方が小さい
                x = selectPositionModel.getPos2X() - selectPositionModel.getPos1X() + 1;
            }else if(selectPositionModel.getPos1X() > selectPositionModel.getPos2X()){
                //pos2の方が小さい
                x = selectPositionModel.getPos1X() - selectPositionModel.getPos2X() + 1;
            }else{
                //同じ
                x = 1;
            }
        }else{
            //片方がマイナス
            if(selectPositionModel.getPos1X() < selectPositionModel.getPos2X()){
                //pos1の方が小さい
                x = selectPositionModel.getPos2X() - selectPositionModel.getPos1X() + 1;
            }else if(selectPositionModel.getPos1X() > selectPositionModel.getPos2X()){
                //pos2の方が小さい
                x = selectPositionModel.getPos1X() - selectPositionModel.getPos2X() + 1;
            }else{
                //同じ
                x = 1;
            }
        }

        //Y
        if(selectPositionModel.getPos1Y() < 0 && selectPositionModel.getPos2Y() < 0){
            //どちらもマイナス
            if(selectPositionModel.getPos1Y() < selectPositionModel.getPos2Y()){
                //pos1の方が小さい
                y = Math.abs(selectPositionModel.getPos1Y()) - Math.abs(selectPositionModel.getPos2Y()) + 1;
            }else if(selectPositionModel.getPos1Y() > selectPositionModel.getPos2Y()){
                //pos2の方が小さい
                y = Math.abs(selectPositionModel.getPos2Y()) - Math.abs(selectPositionModel.getPos1Y()) + 1;
            }else{
                //同じ
                y = 1;
            }
        }else if(selectPositionModel.getPos1Y() >= 0 && selectPositionModel.getPos2Y() >= 0){
            //どちらもプラスあるいは0
            if(selectPositionModel.getPos1Y() < selectPositionModel.getPos2Y()){
                //pos1の方が小さい
                y = selectPositionModel.getPos2Y() - selectPositionModel.getPos1Y() + 1;
            }else if(selectPositionModel.getPos1Y() > selectPositionModel.getPos2Y()){
                //pos2の方が小さい
                y = selectPositionModel.getPos1Y() - selectPositionModel.getPos2Y() + 1;
            }else{
                //同じ
                y = 1;
            }
        }else{
            //片方がマイナス
            if(selectPositionModel.getPos1Y() < selectPositionModel.getPos2Y()){
                //pos1の方が小さい
                y = selectPositionModel.getPos2Y() - selectPositionModel.getPos1Y() + 1;
            }else if(selectPositionModel.getPos1Y() > selectPositionModel.getPos2Y()){
                //pos2の方が小さい
                y = selectPositionModel.getPos1Y() - selectPositionModel.getPos2Y() + 1;
            }else{
                //同じ
                y = 1;
            }
        }

        if(selectPositionModel.getPos1Z() < 0 && selectPositionModel.getPos2Z() < 0){
            //どちらもマイナス
            if(selectPositionModel.getPos1Z() < selectPositionModel.getPos2Z()){
                //pos1の方が小さい
                z = Math.abs(selectPositionModel.getPos1Z()) - Math.abs(selectPositionModel.getPos2Z()) + 1;
            }else if(selectPositionModel.getPos1Z() > selectPositionModel.getPos2Z()){
                //pos2の方が小さい
                z = Math.abs(selectPositionModel.getPos2Z()) - Math.abs(selectPositionModel.getPos1Z()) + 1;
            }else{
                //同じ
                z = 1;
            }
        }else if(selectPositionModel.getPos1Z() >= 0 && selectPositionModel.getPos2Z() >= 0){
            //どちらもプラスあるいは0
            if(selectPositionModel.getPos1Z() < selectPositionModel.getPos2Z()){
                //pos1の方が小さい
                z = selectPositionModel.getPos2Z() - selectPositionModel.getPos1Z() + 1;
            }else if(selectPositionModel.getPos1Z() > selectPositionModel.getPos2Z()){
                //pos2の方が小さい
                z = selectPositionModel.getPos1Z() - selectPositionModel.getPos2Z() + 1;
            }else{
                //同じ
                z = 1;
            }
        }else{
            //片方がマイナス
            if(selectPositionModel.getPos1Z() < selectPositionModel.getPos2Z()){
                //pos1の方が小さい
                z = selectPositionModel.getPos2Z() - selectPositionModel.getPos1Z() + 1;
            }else if(selectPositionModel.getPos1Z() > selectPositionModel.getPos2Z()){
                //pos2の方が小さい
                z = selectPositionModel.getPos1Z() - selectPositionModel.getPos2Z() + 1;
            }else{
                //同じ
                z = 1;
            }
        }

        return x*y*z;
    }
}
