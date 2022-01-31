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
    public boolean checkAnnouncePosition(Location location){

        HashSet<AnnounceInfoModel> announceInfoListWork = BossBarTrainAnnounce.announceInfoList;

        for(AnnounceInfoModel announceInfo:announceInfoListWork){
            boolean resultFlag = true;
            if(!announceInfo.getWorldName().equals(location.getWorld().getName())){
                resultFlag = false;
            }
            if(announceInfo.getPosX() != location.getBlockX()){
                resultFlag = false;
            }
            if(announceInfo.getPosY() != location.getBlockY()){
                resultFlag = false;
            }
            if(announceInfo.getPosZ() != location.getBlockZ()){
                resultFlag = false;
            }
            if(resultFlag){
                return true;
            }
        }
        return false;
    }
}
