package com.github.rypengu23.bossbartrainannounce.command;

import com.github.rypengu23.bossbartrainannounce.BossBarTrainAnnounce;
import com.github.rypengu23.bossbartrainannounce.config.CommandMessage;
import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.AnnounceInfoDao;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.SelectPositionModel;
import com.github.rypengu23.bossbartrainannounce.util.tools.CheckUtil;
import com.github.rypengu23.bossbartrainannounce.util.tools.ConvertUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Command_flag {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public Command_flag(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    /**
     * /bbta flagコマンドを振り分ける
     * @param sender
     * @param args
     */
    public void sort(CommandSender sender, String args[]){

        ConvertUtil convertUtil = new ConvertUtil();
        Player player = (Player)sender;

        //フラグ
        if(args[1].equalsIgnoreCase("bound")){

            if(args.length == 4) {
                //登録(直通なし)
                registBound(player, convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]), null, null);
            }else if(args.length == 6){
                //登録(直通あり)
                registBound(player, convertUtil.convertDoublePercentToSpace(args[2]), convertUtil.convertDoublePercentToSpace(args[3]),  convertUtil.convertDoublePercentToSpace(args[4]), args[5]);

            }else if(args.length == 2){
                //削除
                removeBound(player);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }

        }else if(args[1].equalsIgnoreCase("terminal")){

            if(args.length == 2){
                //終着駅登録
                registTerminal(player);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[1].equalsIgnoreCase("redstone")){

            if(args.length == 3) {
                //レッドストーン登録
                registRedstone(player, args[2]);
            }else if(args.length == 2){
                //レッドストーン削除
                removeRedstone(player);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[1].equalsIgnoreCase("direction")){

            if(args.length == 2) {
                //レッドストーン登録・解除
                registDirection(player);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else if(args[1].equalsIgnoreCase("fast")){

            if(args.length == 2) {
                //アナウンススピードアップ設定
                registFast(player);
            }else{
                //不正
                player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
                return;
            }
        }else{
            //不正
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
            return;
        }
    }


    /**
     * 引数のコマンドがフラグ関連のコマンドか判定
     * @param command
     * @return
     */
    public boolean checkCommandExit(String command){

        CheckUtil checkUtil = new CheckUtil();
        if(checkUtil.checkNullOrBlank(command)){
            return false;
        }

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("flag");

        return commandList.contains(command.toLowerCase());
    }

    /**
     * 行き先を登録
     * @param player コマンド送信者
     * @param boundJP 行き先(日本語)
     * @param boundEN 行き先(英語)
     * @param throughService 直通先路線名
     */
    public void registBound(Player player, String boundJP, String boundEN, String throughService, String throughLineOwnerName){

        CheckUtil checkUtil = new CheckUtil();
        LineDao lineDao = new LineDao();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registBound")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //文字数チェック
        if(checkUtil.checkLength(boundJP, 50) || checkUtil.checkLength(boundEN, 50)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandFailure);
            return;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点が選択されていません。");
            return;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }


        //直通先が指定されている場合
        if(throughService != null) {

            //直通先をリスト化
            String[] throughServiceList = throughService.split(",");
            String[] throughLineOwnerList = throughLineOwnerName.split(",");

            //直通先路線とオーナーの個数チェック
            if(throughLineOwnerList.length != throughServiceList.length){
                player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f直通先リストの路線名とオーナーの数が一致しません。");
                return;
            }

            for(int i=0; i<throughServiceList.length; i++) {

                //直通先路線所持プレイヤー存在チェック
                Player throughLineOwner = Bukkit.getServer().getPlayer(throughLineOwnerList[i]);
                if (throughLineOwner == null) {
                    player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f指定されたプレイヤーは存在しません。");
                    return;
                }

                //直通先路線名存在チェック
                if (!lineDao.checkLineExit(throughLineOwner.getUniqueId().toString(), throughServiceList[i])) {
                    player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f直通先の路線が存在しません。");
                    return;
                } else {
                    //路線名を漢字に変換
                    throughServiceList[i] = lineDao.getLineNameJP(throughLineOwner.getUniqueId().toString(), throughServiceList[i]);
                }

            }

            //路線リストを上書き
            StringBuilder throughServiceWork = new StringBuilder();
            for(int i=0; i<throughServiceList.length; i++){
                if(i!=0) {
                    throughServiceWork.append(",");
                }
                throughServiceWork.append(throughServiceList[i]);
            }
            throughService = throughServiceWork.toString();

            //オーナーリストを上書き
            StringBuilder throughLineOwnerNameWork = new StringBuilder();
            for(int i=0; i<throughLineOwnerList.length; i++){
                if(i!=0) {
                    throughLineOwnerNameWork.append(",");
                }
                throughLineOwnerNameWork.append(Bukkit.getServer().getPlayer(throughLineOwnerList[i]).getUniqueId().toString());
            }
            throughLineOwnerName = throughLineOwnerNameWork.toString();
        }

        boolean overwrite = false;
        //現時点で行き先が登録されているか
        if (!checkUtil.checkNullOrBlank(announceInfo.getBoundJP()) || !checkUtil.checkNullOrBlank(announceInfo.getBoundEN())){
            overwrite = true;
        }

        //行き先をセット
        announceInfo.setBoundJP(boundJP);
        announceInfo.setBoundEN(boundEN);
        //直通先路線をセット
        announceInfo.setViaLineNameJP(throughService);
        announceInfo.setViaLineOwnerUUID(throughLineOwnerName);

        //登録
        announceInfoDao.updateAnnounceInfo(announceInfo);
        if(overwrite){
            player.sendMessage("§a[" + mainConfig.getPrefix() + "] §f行き先を上書き登録しました。");
        }else {
            player.sendMessage("§a[" + mainConfig.getPrefix() + "] §f" + CommandMessage.Command_Flag_RegistBound);
        }
    }

    /**
     * 行き先を削除
     * @param player コマンド送信者
     */
    public void removeBound(Player player){

        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.removeBound")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点が選択されていません。");
            return;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //現時点で行き先が登録されているか
        if (checkUtil.checkNullOrBlank(announceInfo.getBoundJP()) || checkUtil.checkNullOrBlank(announceInfo.getBoundEN())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f行き先・直通先が登録されていません。");
            return;
        }

        //行き先をセット
        announceInfo.setBoundJP(null);
        announceInfo.setBoundEN(null);

        //直通先路線をセット
        announceInfo.setViaLineNameJP(null);
        announceInfo.setViaLineOwnerUUID(null);

        //登録
        announceInfoDao.updateAnnounceInfo(announceInfo);
        player.sendMessage("§b["+ mainConfig.getPrefix() +"] §f" + CommandMessage.Command_Flag_RemoveBound);
    }

    /**
     * 終着駅フラグを登録・解除
     * @param player コマンド送信者
     */
    public void registTerminal(Player player){

        CheckUtil checkUtil = new CheckUtil();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registTerminal")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点が選択されていません。");
            return;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //終着駅フラグを設定
        if(!announceInfo.isTerminal()){
            announceInfo.setTerminal(true);
        }else{
            announceInfo.setTerminal(false);
        }

        //登録
        announceInfoDao.updateAnnounceInfo(announceInfo);
        if(announceInfo.isTerminal()){
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f" + CommandMessage.Command_Flag_RegistTerminal);
        }else{
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f" + CommandMessage.Command_Flag_RemoveTerminal);
        }

    }

    /**
     * レッドストーン制御を設定する
     * @param player
     * @param onOrOffStr
     */
    private void registRedstone(Player player, String onOrOffStr) {

        CheckUtil checkUtil = new CheckUtil();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registRedstone")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //on off入力値チェック
        if(!(onOrOffStr.equalsIgnoreCase("on") || onOrOffStr.equalsIgnoreCase("off"))){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f レッドストーン制御を登録する場合、ONまたはOFFを指定してください。");
            return;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点(pos1)が選択されていません。");
            return;
        }
        if(!checkUtil.checkSelectPositionPos2(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fレッドストーン入力チェック地点(pos2)が選択されていません。");
            return;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //値をセット
        if(onOrOffStr.equalsIgnoreCase("on")) {
            announceInfo.setOnOrOff(1);
        }else{
            announceInfo.setOnOrOff(0);
        }
        announceInfo.setRedstone(true);
        announceInfo.setRedstonePosX(selectPosition.getPos2X());
        announceInfo.setRedstonePosY(selectPosition.getPos2Y());
        announceInfo.setRedstonePosZ(selectPosition.getPos2Z());

        //レッドストーンID生成
        announceInfoDao.insertAnnounceRedstone(announceInfo);
        //レッドストーンIDを取得
        int redstoneID = announceInfoDao.getRedstoneIDOfLatest();
        if(redstoneID == -1){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fレッドストーン制御の作成に失敗しました。");
            return;
        }
        //レッドストーンID登録
        announceInfoDao.addRedstoneIDOfAnnounceInfo(announceInfo, redstoneID);
        player.sendMessage("§a["+ mainConfig.getPrefix() +"] §fレッドストーン制御を追加しました！");
    }

    /**
     * レッドストーン制御を解除する。
     * @param player
     */
    private void removeRedstone(Player player) {

        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();
        CheckUtil checkUtil = new CheckUtil();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.removeRedstone")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点が選択されていません。");
            return;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }
        //選択した位置にredstoneは設定されているか
        if(!announceInfo.isRedstone()){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にレッドストーン制御は登録されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //レッドストーンIDを取得
        int redstoneID = announceInfoDao.getRedstoneIDForAnnouncePosition(announceInfo.getWorldName(), announceInfo.getPosX(), announceInfo.getPosY(), announceInfo.getPosZ());
        if(redstoneID == -1){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fレッドストーン制御の削除に失敗しました。");
            return;
        }

        //登録
        announceInfoDao.removeRedstoneIDOfAnnounceInfo(redstoneID);
        announceInfoDao.removeRedstoneIDOfAnnounceRedstone(redstoneID);
        player.sendMessage("§b["+ mainConfig.getPrefix() +"] §f選択した地点のレッドストーン制御を解除しました！");
    }

    /**
     * アナウンスに方角制御を追加・解除
     * @param player
     */
    private void registDirection(Player player) {

        CheckUtil checkUtil = new CheckUtil();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registDirection")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //セレクトポジションチェック(pos1)
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fpos1が選択されていません。");
            return;
        }

        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }

        //セレクトポジションチェック(pos2)
        if(!checkUtil.checkSelectPositionPos2(player) && announceInfo.getDirection() != 0){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fpos2が選択されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        int direction = 0;
        if(announceInfo.getDirection() == 0) {
            direction = checkUtil.checkPositionAdjacent(selectPosition);
            System.out.println(direction);
            //pos1とpos2が隣接しているか
            if (direction == -1 || direction == 0) {
                player.sendMessage("§c[" + mainConfig.getPrefix() + "] §f隣接するブロックを選択して下さい。");
                return;
            }
        }

        //登録
        announceInfo.setDirection(direction);
        announceInfoDao.updateAnnounceInfo(announceInfo);
        if(direction == 0){
            player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f選択した地点の方角制御を解除しました！");
        }else {
            player.sendMessage("§b[" + mainConfig.getPrefix() + "] §f選択した地点に方角制御を設定しました！");
        }
    }

    /**
     * アナウンススピードアップフラグを登録・解除
     * @param player コマンド送信者
     */
    public void registFast(Player player){

        CheckUtil checkUtil = new CheckUtil();
        AnnounceInfoDao announceInfoDao = new AnnounceInfoDao();

        //権限チェック
        if(!player.hasPermission("bossBarTrainAnnounce.registFastFlag")){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //プレイヤーが選択しているアナウンス情報を取得
        if(!checkUtil.checkSelectPositionPos1(player)){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §fアナウンス地点が選択されていません。");
            return;
        }
        //選択した位置にアナウンス地点が存在するか
        SelectPositionModel selectPosition = BossBarTrainAnnounce.selectPosition.get(player.getUniqueId());
        AnnounceInfoModel announceInfo = announceInfoDao.getAnnounceForCoordinate(selectPosition.getWorldName(), selectPosition.getPos1X(), selectPosition.getPos1Y(), selectPosition.getPos1Z());
        if(announceInfo == null){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f選択した地点にアナウンスは登録されていません。");
            return;
        }

        //路線の所有者チェック
        if(!announceInfo.getUUID().equalsIgnoreCase(player.getUniqueId().toString())){
            player.sendMessage("§c["+ mainConfig.getPrefix() +"] §f" + CommandMessage.CommandDoNotHavePermission);
            return;
        }

        //アナウンススピードアップフラグを設定
        if(!announceInfo.isAnnounceFastFlag()){
            announceInfo.setAnnounceFastFlag(true);
        }else{
            announceInfo.setAnnounceFastFlag(false);
        }

        //登録
        announceInfoDao.updateAnnounceInfo(announceInfo);
        if(announceInfo.isAnnounceFastFlag()){
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f" + CommandMessage.Command_Flag_RegistFast);
        }else{
            player.sendMessage("§a["+ mainConfig.getPrefix() +"] §f" + CommandMessage.Command_Flag_RemoveFast);
        }

    }
}
