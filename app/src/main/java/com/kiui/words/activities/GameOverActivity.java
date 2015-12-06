package com.kiui.words.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kiui.words.R;
import com.kiui.words.manager.PointsManager;

public class GameOverActivity extends ActionBarActivity {

    private AlertDialog alertDialog;
    private Context context = this;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_game_over);

        final Intent intent = getIntent();
        final String p = intent.getStringExtra("puntos");
        ((TextView)findViewById(R.id.score)).setText(p);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ((TextView) findViewById(R.id.player_name)).getText().toString();
                if (!user.equals("")) {
                    PointsManager.getInstance().addScore(user, p);
                    Intent intent1 = new Intent(getApplicationContext(), RankingActivity.class);
                    intent1.putExtra("user", user);
                    intent1.putExtra("points", p);
                    startActivity(intent1);
                }
                else {
                    toast("Input player name");
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.show();
                alertDialog.setContentView(R.layout.confirm_alertdialog);
                alertDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
    }

    private void toast(String texto) {
        if (toast != null) toast.cancel();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(texto);
        toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
