package com.kiui.words.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.adapters.ListPlayWordsAdapter;
import com.kiui.words.manager.LanguagesManager;

public class PlayActivity extends ActionBarActivity {

    private String main;
    private String second;

    private CountDownTimer finish;

    private Context context = this;

    private boolean clickable;

    private AlertDialog alertDialog;

    private TextView maintv;
    private TextView secondtv;

    private int score;
    private int progress;
    private ListView list;
    private TextView word;
    private String nowWord;

    Vibrator v;

    private int pbProgress;

    private boolean withTime;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        score = 0;
        progress = 0;
        pbProgress = 0;
        clickable = true;

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_play);

        findViewById(R.id.action_bar_cancel).setOnClickListener(new View.OnClickListener() {
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
                        if (finish != null) finish.cancel();
                        finish();
                    }
                });


            }
        });

        list = (ListView)findViewById(R.id.lista_traducciones);

        Intent intent = getIntent();
        main  = intent.getStringExtra("main");
        second = intent.getStringExtra("second");
        withTime = intent.getBooleanExtra("timing", false);

        maintv = (TextView)findViewById(R.id.main);
        secondtv = (TextView)findViewById(R.id.second);

        maintv.setText(main);
        secondtv.setText(second);

        word = (TextView)findViewById(R.id.main_word);

        setWords();

        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setProgress(pbProgress);
        if(withTime)startTrial();
    }

    private void setWords(){

        nowWord = LanguagesManager.getInstance().getRandomWord(main, second);

        Log.v("Main Language", main);
        Log.v("Now Word", nowWord);
        Log.v("Secondary Language", second);

        word.setText(nowWord);

        final ListPlayWordsAdapter lPlayAdapter = new ListPlayWordsAdapter(this,
                R.layout.list_words_play,
                LanguagesManager.getInstance().getPlayWords(nowWord, main, second, 5));

        Log.v("PlayActivity", "getting play words");

        list.setAdapter(lPlayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clickable) {


                    lPlayAdapter.setCheck(main, nowWord, second, ((TextView) view.findViewById(R.id.text)).getText().toString());
                    lPlayAdapter.notifyDataSetChanged();

                    if (!withTime && progress == 4) {
                        findViewById(R.id.cinco).setBackgroundColor(Color.parseColor("#CDDC39"));
                    }

                    if (LanguagesManager.getInstance().hasTranslationTo(main, nowWord, second, ((TextView) view.findViewById(R.id.text)).getText().toString())) {
                        v.vibrate(200);
                        score += 5;
                        ((TextView) findViewById(R.id.score)).setText(score + "");
                        startTimer(650);
                    } else {
                        v.vibrate(600);
                        startTimer(1000);
                    }
                    clickable = false;
                }
            }
        });

    }

    private void startTimer(int k){
        new CountDownTimer(k, k) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                if (!withTime) {
                    progress++;
                    updateProgressBar(progress);
                    if (progress == 5) {
                        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                        intent.putExtra("puntos", ((TextView) findViewById(R.id.score)).getText().toString());
                        startActivity(intent);
                    } else setWords();
                }
                else setWords();
                clickable = true;
            }
        }.start();
    }

    private void updateProgressBar(int k){
        if (k == 1) findViewById(R.id.uno).setBackgroundColor(Color.parseColor("#CDDC39"));
        else if (k == 2) findViewById(R.id.dos).setBackgroundColor(Color.parseColor("#CDDC39"));
        else if (k == 3) findViewById(R.id.tres).setBackgroundColor(Color.parseColor("#CDDC39"));
        else if (k == 4) findViewById(R.id.cuatro).setBackgroundColor(Color.parseColor("#CDDC39"));
        else {
            findViewById(R.id.uno).setBackgroundColor(Color.parseColor("#00000000"));
            findViewById(R.id.dos).setBackgroundColor(Color.parseColor("#00000000"));
            findViewById(R.id.tres).setBackgroundColor(Color.parseColor("#00000000"));
            findViewById(R.id.cuatro).setBackgroundColor(Color.parseColor("#00000000"));
            findViewById(R.id.cinco).setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    private void startTrial(){
        finish = new CountDownTimer(50000, 500) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(pbProgress++);
                if (pbProgress > 90)v.vibrate(100);
                else if (pbProgress > 80  & pbProgress%2 == 0)v.vibrate(100);
                else if (pbProgress > 70  & pbProgress%4 == 0)v.vibrate(100);
            }
            public void onFinish() {
               Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                intent.putExtra("puntos", ((TextView) findViewById(R.id.score)).getText().toString());
                startActivity(intent);
            }
        }.start();
    }

    @Override
    public void onBackPressed(){
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
                if (finish != null) finish.cancel();
                finish();
            }
        });
    }
}
