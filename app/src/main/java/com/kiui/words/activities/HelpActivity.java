package com.kiui.words.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.kiui.words.R;
import com.kiui.words.fragments.help.TabbedHelp;

public class HelpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_help);

        findViewById(R.id.drawer_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TabbedHelp()).commit();
    }
}
