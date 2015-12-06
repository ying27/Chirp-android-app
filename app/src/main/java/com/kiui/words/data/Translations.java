package com.kiui.words.data;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by ying on 05/04/2015.
 */

//this class includes all the translations of a word
public class Translations {
    //a nivel de lengua
    private HashMap<String, HashSet<String>> entries;

    public Translations() {
        entries = new HashMap<String, HashSet<String>>();
    }

    public Translations(HashMap<String, HashSet<String>> entries) {

        this.entries = entries;
    }

    public ArrayList<String> getLanguages(){
        ArrayList<String> temp = new ArrayList<String>();
        for (String key : entries.keySet()) {
            temp.add(key);
        }
        return temp;
    }

    public int getNumTranslations(){
        int x = 0;
        for (HashSet<String> value : entries.values()) {
            x += value.size();
        }
        return x;
    }

    public HashSet<String> getTrans(String lang){
        return entries.get(lang);
    }

    /****************************/
    public class transStruct{
        public String wordName;
        public String langName;
    }
    /****************************/

    public ArrayList<transStruct> getTranslationsList() {
        ArrayList<transStruct> temp = new ArrayList<transStruct>();
        for (HashMap.Entry<String, HashSet<String>> entry : entries.entrySet()) {
            for (String s : entry.getValue()) {
                transStruct tempStruct = new transStruct();
                tempStruct.langName = entry.getKey();
                tempStruct.wordName = s;
                temp.add(tempStruct);
            }
        }
        return temp;
    }

    public void addEntry(String language, String word){
        HashSet<String> tmp;
        if(entries.containsKey(language)) tmp = entries.get(language);
        else {
            tmp = new HashSet<String>();
        }
        tmp.add(word);
        entries.put(language, tmp);
    }

    public void deleteEntry(String language, String word){
        HashSet<String> temp = entries.get(language);
        temp.remove(word);
        entries.put(language, temp);
    }

    public void deleteLanguageEntry(String l){
        entries.remove(l);
    }

    public boolean hasEntry(String lang, String word){
        return entries.get(lang).contains(word);
    }

    public boolean hasLangTranslate(String l){
        return entries.containsKey(l);
    }

    public String getRandomTranslation(String l){

        HashSet<String> temp = entries.get(l);

        Log.v("OUTPUT", temp+"");
        //if (temp == null) temp = entries.get(l);

        Random rand = new Random();
        int randPos = rand.nextInt(temp.size());
        int count = 0;

        for (String key : temp) {
            if (count == randPos) return key;
            count++;
        }
        return "";
    }
}
