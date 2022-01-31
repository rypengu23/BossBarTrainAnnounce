package com.github.rypengu23.bossbartrainannounce.util.tools;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;

import java.util.regex.Pattern;

public class ConvertUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public ConvertUtil(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    public String convertPercentAndColorCode(String str){

        return convertColorCode(convertDoublePercentToSpace(str));
    }

    public String convertDoublePercentToSpace(String str){

        return str.replace("%%", " ");
    }

    public String convertColorCode(String str){

        return str.replace("&", "§");
    }

    public String removeLocalService(String str){

        if(str.toLowerCase().contains(messageConfig.getRemoveWord1())){
            return replaceAll( messageConfig.getRemoveWord1(), "", str);
        }else if(str.toLowerCase().contains(messageConfig.getRemoveWord2())){
            return replaceAll(messageConfig.getRemoveWord2(),"",  str);
        }else{
            return str;
        }
    }

    public String placeholderUtil(String beforeReplaceWord, String afterReplaceWord, String message){

        if(beforeReplaceWord != null && afterReplaceWord != null && message != null) {
            return message.replace(beforeReplaceWord, afterReplaceWord);
        }else{
            return null;
        }
    }

    public String placeholderUtil(String beforeReplaceWord1, String afterReplaceWord1, String beforeReplaceWord2, String afterReplaceWord2, String message){

        message = placeholderUtil(beforeReplaceWord1, afterReplaceWord1, message);
        return placeholderUtil(beforeReplaceWord2, afterReplaceWord2, message);
    }

    public String placeholderUtil(String beforeReplaceWord1, String afterReplaceWord1, String beforeReplaceWord2, String afterReplaceWord2, String beforeReplaceWord3, String afterReplaceWord3, String message){

        message = placeholderUtil(beforeReplaceWord1, afterReplaceWord1, message);
        message = placeholderUtil(beforeReplaceWord2, afterReplaceWord2, message);
        return placeholderUtil(beforeReplaceWord3, afterReplaceWord3, message);
    }

    public String convertBooleanToString(Boolean flag){
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }

    /**
     * 大文字小文字を区別せずにreplaceAllします
     * @param regex 置き換えたい文字列
     * @param reql 置換後文字列
     * @param text 置換対象文字列
     */
    public String replaceAll(String regex ,String reql,String text){
        String retStr = "";
        retStr = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text).replaceAll(reql);
        return retStr;
    }
}
