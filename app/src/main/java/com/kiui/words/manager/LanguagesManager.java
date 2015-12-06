package com.kiui.words.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kiui.words.MainApplication;
import com.kiui.words.R;
import com.kiui.words.activities.MainActivity;
import com.kiui.words.data.*;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by ying on 05/04/2015.
 */
public class LanguagesManager {
    private ArrayList<Language> lenguas;

    private Toast toast;

    private final Gson gson;
    private static LanguagesManager instance = new LanguagesManager();

    public static LanguagesManager getInstance() {
        return instance;
    }

    public LanguagesManager() {
        //lenguas = new ArrayList<Language>();
        this.gson = new Gson();
    }

    public LanguagesManager(ArrayList<Language> lenguas) {
        this.lenguas = lenguas;
        this.gson = new Gson();
    }

    public int getTotalNumWords(){
        int count = 0;
        for (int j = 0; j < lenguas.size(); j++){
            count += lenguas.get(j).getNumWords();
        }
        return count;
    }

    public int getNumLang(){
        return lenguas.size();
    }

    public int hasLanguage(String l){
        for (int j = 0; j < lenguas.size();j++){
            if (lenguas.get(j).getLanguage().equals(l)) return j;
        }
        return -1;
    }

    //cal eliminarlo primer de la llista
    public void addLanguage(Language language){
      //  int k = hasLanguage(language.getLanguage());
       // if (k != -1) lenguas.remove(k);

        ArrayList<String> words = language.getWords();

        for (int j = 0;j < words.size();j++){
            ArrayList<Translations.transStruct> tstruct = language.getObjectTranslations(words.get(j)).getTranslationsList();
            for (int s = 0;s < tstruct.size();s++){
                addTranslation(tstruct.get(s).langName, tstruct.get(s).wordName, language.getLanguage(), words.get(j));
            }
        }
        lenguas.add(language);
    }

    public void addWord(String language, String word){
        boolean stop = false;
        for (int j = 0; stop || j < lenguas.size(); j++){
            if (lenguas.get(j).getLanguage().equals(language)){
                stop = true;
                lenguas.get(j).addWord(word);
            }
        }
    }

    public Language getObjectLanguage(String langName){
        for (int j = 0; j < lenguas.size(); j++){
            if (lenguas.get(j).getLanguage().equals(langName)){
                return lenguas.get(j);
            }
        }
        return lenguas.get(0);
    }

    public ArrayList<String> getLanguages(){
        ArrayList<String> ret = new ArrayList<String>();
        for(int j = 0;j < lenguas.size();j++){
            ret.add(lenguas.get(j).getLanguage());
        }
        Collections.sort(ret);
        return ret;
    }

    //Devuelve todos los lenguajes menos uno
    public ArrayList<String> getLanguages(String l){
        ArrayList<String> ret = new ArrayList<String>();
        for(int j = 0;j < lenguas.size();j++){
            String k = lenguas.get(j).getLanguage();
            if (!k.equals(l)) ret.add(k);
        }
        Collections.sort(ret);
        return ret;
    }

    public void addTranslation(String langNameO, String transO, String langNameD, String transD){
        for (int j = 0; j < lenguas.size(); j++){
            if (lenguas.get(j).getLanguage().equals(langNameO)){
                lenguas.get(j).addTranslate(transO, transD, langNameD);
            }
        }
    }

    public int getNumWordsLang(String lang){
        for (int j = 0; j < lenguas.size(); j++){
            if (lenguas.get(j).getLanguage().equals(lang)){
                return lenguas.get(j).getNumWords();
            }
        }
        return -1;
    }

    private void firstTimeSetup(){
        lenguas = new ArrayList<Language>();
        Language english = new Language("English", new ArrayList<String>());
        Language spanish = new Language("Spanish", new ArrayList<String>());

        english.addWord("Hello");
        english.addWord("ByeBye");
        english.addWord("Idiot");
        english.addWord("Sorry");
        english.addWord("Everybody");

        spanish.addWord("Hola");
        spanish.addWord("Adios");
        spanish.addWord("Idiota");
        spanish.addWord("Perdon");
        spanish.addWord("Todos");

        spanish.addTranslate("Hola", "Hello", "English");
        spanish.addTranslate("Adios", "ByeBye", "English");
        spanish.addTranslate("Idiota", "Idiot", "English");
        spanish.addTranslate("Perdon", "Sorry", "English");
        spanish.addTranslate("Todos", "Everybody", "English");

        lenguas.add(english);
        addLanguage(spanish);
    }

    public void load(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("LanguageManager", Context.MODE_PRIVATE);
        if(prefs.getBoolean("FirstTime", true)){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTime", false);
            editor.commit();
            firstTimeSetup();
        }


        else if (lenguas == null) {
            Log.d("LanguageManager", "loading");
            File fdir = context.getFilesDir();
            checkDirectory(fdir);
            String path = fdir.getAbsolutePath() + "/lenguas.ki";
            File json = new File(path);
            if (json.exists()) {
                try {
                    FileReader fr = new FileReader(json);
                    Type type = new TypeToken<ArrayList<Language>>() {}.getType();
                    lenguas = gson.fromJson(fr, type);
                    if (lenguas == null)
                        lenguas = new ArrayList<>();
                } catch (Exception e) {
                    json.delete();
                    lenguas = new ArrayList<>();
                    e.printStackTrace();
                }
            } else
                lenguas = new ArrayList<>();
        }
    }

    private static void checkDirectory(File directory) {
        if (!directory.exists())
            directory.mkdirs();
    }

    public void save(Context context) {
        Log.d("load", "saving");
        File fileDir = context.getFilesDir();
        checkDirectory(fileDir);
        String jsonPath = fileDir.getAbsolutePath() + "/lenguas.ki";
        File json = new File(jsonPath);
        if (json.exists())
            json.delete();
        String jsonString = gson.toJson(lenguas);
        PrintWriter out = null;
        try {
            out = new PrintWriter(json);
            out.write(jsonString);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
        }
    }

    public void removeLanguage(String l){
        int k = hasLanguage(l);
        if (k != -1){
            String temp = lenguas.get(k).getLanguage();
            lenguas.remove(k);
            for (int j = 0;j < lenguas.size(); j++){
                lenguas.get(j).deleteAllTranslations(temp);
            }
        }
    }

    public ArrayList<String> getLanguagesPlay(String l){
        ArrayList<String> h = new ArrayList<String>();
        for (int j = 0;j < lenguas.size(); j++){
            if (lenguas.get(j).hasLangTranslate(l)) h.add(lenguas.get(j).getLanguage());
        }
        return h;
    }

    private int getPosLang(String l){
        for (int j = 0;j < lenguas.size(); j++){
            if (lenguas.get(j).getLanguage().equals(l)) return j;
        }
        return 0;
    }

    public String getRandomWord(String langMain, String langSecond){

        Language langtemp = lenguas.get(getPosLang(langMain));
        Log.v("getRandomWord", langtemp.getLanguage());
        return langtemp.getRandomWord(langSecond);
    }


    public ArrayList<String> getPlayWords(String wordm, String lmain, String lsecond, int size){
        Random rand = new Random();
        int randPos = rand.nextInt(size);

        String correctResult = lenguas.get(getPosLang(lmain)).getRandomTranslation(lsecond, wordm);

        ArrayList<String> h = new ArrayList<String>();

        int contador = 0;

        while (contador < size){
            if (contador == randPos) {
                h.add(correctResult);
                contador++;
            }
            else {
                int randLang = rand.nextInt(lenguas.size());
                String k = lenguas.get(randLang).getRandomWord();
                if (!k.equals(correctResult) && !h.contains(k)){
                    h.add(k);
                    contador++;
                }
            }
        }
        return h;
    }

    public boolean hasTranslationTo(String lmain, String wmain, String lsecond, String wsecond){
        return lenguas.get(getPosLang(lmain)).hasTranslate(wmain, lsecond, wsecond);
    }

    private boolean hasString(ArrayList<String> al, String result){
        int size = al.size();
        for (int j = 0;j < size; j++){
            if (al.get(j).equals(result)) return true;
        }
        return false;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getImportFiles(){
        ArrayList<String> ret = new ArrayList<String>();
        File f = new File(Environment.getExternalStorageDirectory(), "Chirp");
        checkDirectory(f);

        File[] files = new File(f.getAbsolutePath()).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                ret.add(file.getName());
            }
        }

        return ret;
    }

    public void loadFile(Context context, String file){
        File f = new File(Environment.getExternalStorageDirectory(), "Chirp");
        checkDirectory(f);

        String jsonPath = f.getAbsolutePath() + "/"+file;
        Log.v("LoadPath", jsonPath);
        File json = new File(jsonPath);
        if (json.exists()) {
            try {
                FileReader fr = new FileReader(json);
                Type type = new TypeToken<ArrayList<Language>>() {}.getType();
                lenguas = gson.fromJson(fr, type);
                if (lenguas == null)
                    lenguas = new ArrayList<>();
            } catch (Exception e) {
                json.delete();
                //lenguas = new ArrayList<>();
                e.printStackTrace();
            }
        } else
            lenguas = new ArrayList<>();
    }

    public void saveToInternal(Context context, String name){

        File f = new File(Environment.getExternalStorageDirectory(), "Chirp");
        checkDirectory(f);
        String savePath = f.getAbsolutePath() + "/"+name+".chi";
        Log.v("Path", savePath);


        File json = new File(savePath);
        if (json.exists())json.delete();

        String jsonString = gson.toJson(lenguas);
        PrintWriter out = null;
        try {
            out = new PrintWriter(json);
            out.write(jsonString);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
        }
    }


}
