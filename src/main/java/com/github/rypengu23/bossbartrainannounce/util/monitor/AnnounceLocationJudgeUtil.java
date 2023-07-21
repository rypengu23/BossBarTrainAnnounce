package com.github.rypengu23.bossbartrainannounce.util.monitor;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashSet;

public class AnnounceLocationJudgeUtil {

    public void updateAnnounceListCache(){
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
        BossBarTrainAnnounce.announceInfoList = announceInfoDao.getAnnounceInfoListAll();
    }

    /**
     * 引数の座標がアナウンス地点かチェック
     * @param location
     * @return
     */
    public AnnounceInfoModel checkAnnouncePosition(Location location){

        HashSet<AnnounceInfoModel> announceInfoListWork = BossBarTrainAnnounce.announceInfoList;

        for(AnnounceInfoModel announceInfo:announceInfoListWork){
            boolean resultFlag = true;
            if(!announceInfo.getWorldName().equals(location.getWorld().getName())){
                resultFlag = false;
            }
            if(!checkRange05(announceInfo.getPosX(), location.getX())){
                resultFlag = false;
            }
            if(!checkRange15(announceInfo.getPosY(), location.getY())){
                resultFlag = false;
            }
            if(!checkRange05(announceInfo.getPosZ(), location.getZ())){
                resultFlag = false;
            }
            if(resultFlag){
                return announceInfo;
            }
        }
        return null;
    }

    /**
     * 座標が0.5マス以内か
     * @param vehicleLocation
     * @return
     */
    public boolean checkRange05(int registLocation, double vehicleLocation){

        if(vehicleLocation - (double)registLocation >= 0 && vehicleLocation - (double)registLocation <= 0.6){
            return true;
        }else if(vehicleLocation - (double)registLocation >= -0.6 && vehicleLocation - (double)registLocation <= 0){
            return true;
        }
        return false;
    }

    /**
     * 座標が1.5マス以内か
     * @param vehicleLocation
     * @return
     */
    public boolean checkRange15(int registLocation, double vehicleLocation){

        if(vehicleLocation - (double)registLocation >= 0 && vehicleLocation - (double)registLocation <= 1.5){
            return true;
        }else if(vehicleLocation - (double)registLocation >= -1.5 && vehicleLocation - (double)registLocation <= 0){
            return true;
        }
        return false;
    }
}
