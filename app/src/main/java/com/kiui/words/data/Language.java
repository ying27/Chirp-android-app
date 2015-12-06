package com.kiui.words.data;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by ying on 05/04/2015.
 */
public class Language {
    private String language;
    private HashMap<String, Translations> words;
    //HashMap<palabras, traducciones>



    public Language(String language, ArrayList<String> wordList) {
        this.language = language;
        words = new HashMap<String, Translations>();
        for (int j = 0;j < wordList.size();j++) {
            words.put(wordList.get(j), new Translations());
        }
    }

    public void setLanguageName(String name){
        language = name;
    }

    public String getLanguage() {
        return language;
    }

    public int getNumWords(){
       return words.size();
    }

    public ArrayList<String> getWords(){
        ArrayList<String> ret = new ArrayList<String>();
        for (String key : words.keySet()) {
            ret.add(key);
        }
        return ret;
    }

    public int getNumTranslation(String word){
        return words.get(word).getNumTranslations();
    }

    public void addWord(String word){
        words.put(word, new Translations());
    }

    public void addTranslate(String word, String translated, String transLanguage){
        Translations trans = words.get(word);
        trans.addEntry(transLanguage,translated);
        words.put(word, trans);
    }

    public void addTranslate(String word, Translations trans){
        words.put(word, trans);
    }

    //return the translations of the removed word
    public Translations deleteWord(String word){
        /*
        ArrayList<String> ret = new ArrayList<String>();
        ArrayList<Translate> temp = words.get(word);
        for (int j = 0;j < temp.size();j++){
            ret.add(temp.get(j).getLanguage());
        }*/
        words.remove(word);
        return words.get(word);
    }

    public void deleteAllTranslations(String l){
        for (String key : words.keySet()) {
            words.get(key).deleteLanguageEntry(l);
        }
    }

    public void deleteTranslate(String LangOrigen, HashSet<String> entries, String wordToBeRemoved){

        for (String s : entries) {
            Translations trans = words.get(s);
            trans.deleteEntry(LangOrigen, wordToBeRemoved);
            words.put(s, trans);
        }

    }

    public boolean hasTranslate(String word, String transLanguage, String translated){
        return words.get(word).hasEntry(transLanguage,translated);
    }

    //TODO: tratar el caso de la creadora
    public Translations getObjectTranslations(String word){
        return words.get(word);
    }

    public boolean hasLangTranslate(String l){
        for (Translations trans : words.values()) {
            if (trans.hasLangTranslate(l)) return true;
        }
        return false;
    }


    //spanish
    public String getRandomWord(String l){

        ArrayList<String> k = new ArrayList<String>();

        for (HashMap.Entry<String, Translations> entry : words.entrySet()) {
            if (entry.getValue().hasLangTranslate(l)) k.add(entry.getKey());
        }

        Log.v("Language", k+"");

        Random rand = new Random();
        int randPos;
        randPos = rand.nextInt(k.size());

        return k.get(randPos);
    }

    public String getRandomWord(){

        Random rand = new Random();
        int randPos = rand.nextInt(words.size());
        int count = 0;
        for (String key : words.keySet()) {
            if (count == randPos) return key;
            count++;
        }
        return "";
    }

    public String getRandomTranslation(String l, String w){

        return words.get(w).getRandomTranslation(l);

    }
}
