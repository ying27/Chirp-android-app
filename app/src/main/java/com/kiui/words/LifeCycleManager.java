package com.kiui.words;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.kiui.words.manager.LanguagesManager;
import com.kiui.words.manager.PointsManager;

/**
 * Created by ying on 06/04/2015.
 */
public class LifeCycleManager implements Application.ActivityLifecycleCallbacks {
    int resume = 0;
    int pause = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        resume++;
        Boolean st = resume >= pause;
        Log.d("resume", st.toString());
        //if (resume >= pause) FrasesManager.getInstance().load(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        pause++;
        Boolean st = resume <= pause;
        Log.d("pause", st.toString());
        if (pause >= resume){
            LanguagesManager.getInstance().save(activity);
            PointsManager.getInstance().save(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
