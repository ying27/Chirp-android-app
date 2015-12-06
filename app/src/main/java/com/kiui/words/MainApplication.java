package com.kiui.words;

import android.app.Application;

import com.kiui.words.manager.LanguagesManager;
import com.kiui.words.manager.PointsManager;

/**
 * Created by ying on 06/04/2015.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LanguagesManager.getInstance().load(this);
        PointsManager.getInstance().load(this);
        registerActivityLifecycleCallbacks(new LifeCycleManager());
    }



}

