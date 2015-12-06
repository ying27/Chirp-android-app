package com.kiui.words.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.adapters.ListPlainWordsAdapter;
import com.kiui.words.adapters.ListRankingAdapter;
import com.kiui.words.manager.LanguagesManager;
import com.kiui.words.manager.PointsManager;

public class RankingActivity extends ActionBarActivity {

    private ListView list;

    private String nowUser;
    private String nowPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_ranking);

        Intent intent = getIntent();
        nowUser = intent.getStringExtra("user");
        nowPoints = intent.getStringExtra("points");

        list = (ListView)findViewById(R.id.ranking_list);
        list.setAdapter(new ListRankingAdapter(this, R.layout.list_ranking_layout, nowUser, nowPoints));

        findViewById(R.id.action_bar_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
    }
}
