package com.kiui.words.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kiui.words.data.Language;
import com.kiui.words.data.Points;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ying on 05/04/2015.
 */
public class PointsManager {

    private ArrayList<Points> points;
    private final Gson gson;
    private static PointsManager instance = new PointsManager();

    public static PointsManager getInstance() {
            return instance;
        }

    public PointsManager() {
        //lenguas = new ArrayList<Language>();
        this.gson = new Gson();
    }

    public void addScore(String user, String p){
        Points pp = new Points(user, Integer.parseInt(p));
        Log.v("addScore", pp.getUser()+" "+pp.getPoints());
        if (!hasPoints(pp)) points.add(pp);
        Log.v("PointsSize", points.size()+"");
    }

    public boolean hasPoints(Points p){
        for (int j = 0;j < points.size();j++){
            if (points.get(j).getUser().equals(p.getUser()) && points.get(j).getPoints() == p.getPoints()) return true;
        }
        return false;
    }

    public ArrayList<Points> getScores(){
        Collections.sort(points, new CustomComparator());
        return  points;
    }

    public class CustomComparator implements Comparator<Points> {
        @Override
        public int compare(Points o1, Points o2) {
            int i1 = o1.getPoints();
            int i2 = o2.getPoints();
            if ( i1 < i2 ) {
                return 1;
            } else if ( i1 > i2 ) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public void load(Context context) {
        if (points == null) {
            Log.d("PointsManager", "loading");
            File fdir = context.getFilesDir();
            checkDir(fdir);
            String path = fdir.getAbsolutePath() + "/points.ki";
            File json = new File(path);
            if (json.exists()) {
                try {
                    FileReader fr = new FileReader(json);
                    Type type = new TypeToken<ArrayList<Points>>() {}.getType();
                    points = gson.fromJson(fr, type);
                    if (points == null)
                        points = new ArrayList<>();
                } catch (Exception e) {
                    json.delete();
                    points = new ArrayList<>();
                    e.printStackTrace();
                }
            } else
                points = new ArrayList<>();
        }
    }

    private static void checkDir(File dir) {
        if (!dir.exists())
            dir.mkdirs();
    }

    public void save(Context context) {
        Log.d("PointsManager", "saving");
        File fileDir = context.getFilesDir();
        checkDir(fileDir);
        String jsonPath = fileDir.getAbsolutePath() + "/points.ki";
        File json = new File(jsonPath);
        if (json.exists())
            json.delete();
        String jsonString = gson.toJson(points);
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
