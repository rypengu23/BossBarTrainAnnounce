package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;
import org.bukkit.Location;

import java.util.ArrayList;

public class StationLocationJudgeUtil {

    /**
     * アナウンス地点の更新
     */
    public void updateStationListCache(){
        StationDao stationDao = new StationDao();
        BossBarTrainAnnounce.stationInfoList = stationDao.getStationListAll();
    }

    /**
     * アナウンス地点に居るか判定
     * @param location
     * @return
     */
    public boolean checkAnnouncePosition(Location location){

        CheckUtil checkUtil = new CheckUtil();

        ArrayList<StationModel> stationInfoListWork = BossBarTrainAnnounce.stationInfoList;

        for(StationModel stationInfo:stationInfoListWork){
            boolean resultFlag = true;
            SelectPositionModel stationLocation = stationInfo.getSelectPositionModel();
            if(!stationLocation.getWorldName().equals(location.getWorld().getName())){
                resultFlag = false;
            }
            if(!checkUtil.checkNumericRange(location.getBlockX(), stationLocation.getPos1X(), stationLocation.getPos2X())){
                resultFlag = false;
            }
            if(!checkUtil.checkNumericRange(location.getBlockY(), stationLocation.getPos1Y(), stationLocation.getPos2Y())){
                resultFlag = false;
            }
            if(!checkUtil.checkNumericRange(location.getBlockZ(), stationLocation.getPos1Z(), stationLocation.getPos2Z())){
                resultFlag = false;
            }
            if(resultFlag){
                return true;
            }
        }
        return false;
    }
}

