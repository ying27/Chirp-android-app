package com.kiui.words.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kiui.words.Listeners.SwipeDismissListViewTouchListener;
import com.kiui.words.R;
import com.kiui.words.adapters.ListTransAdapter;
import com.kiui.words.adapters.ListWordsAdapter;
import com.kiui.words.data.Language;
import com.kiui.words.data.Translations;
import com.kiui.words.manager.LanguagesManager;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewLanguageActivity extends ActionBarActivity {


    private ListWordsAdapter ladapter;
    private ListView lista;
    private Language lang;
    private View tick;
    private EditText langName;
    private ImageView tapToAdd;
    private AlertDialog alertDialog;

    private Toast toast;

    //used for saving
    private String lengua;

    /*AlertDialog*/
    private ListView trans_list;
    private Translations trans;
    private TextView num_translations;

    private Context context;

    private int mLastPosition = -1;

    boolean fromBack;

    private CountDownTimer finish;

    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = new Language("Default", new ArrayList<String>());
        this.gson = new Gson();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_new_language);

        langName = (EditText)findViewById(R.id.edit_text_bar);
        langName.setHintTextColor(getResources().getColor(R.color.edittext_hint));
        fromBack = false;

        Intent intent = getIntent();
        lengua = intent.getStringExtra("language");
        langName = (EditText) findViewById(R.id.edit_text_bar);
        langName.setText(lengua);
        if(lengua != null) {
            lang = LanguagesManager.getInstance().getObjectLanguage(lengua);
            hideKeyboard();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //TODO: tener en cuenta el caso de que no estas creando

        context = this;


        tick = (View) findViewById(R.id.drawer_icon);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (lengua == null && LanguagesManager.getInstance().hasLanguage(langName.getText().toString()) != -1){
                    toast("Language already exists");
                }
                else if (!langName.getText().toString().equals("")) {
                    LanguagesManager.getInstance().removeLanguage(lengua);
                    lang.setLanguageName(langName.getText().toString());
                    LanguagesManager.getInstance().addLanguage(lang);
                    finish();
                }
                else {
                    if (lengua != null) toast("Language name cannot be empty");
                    else finish();
                }*/
                backActions();
            }
        });

        lista = (ListView)findViewById(R.id.words_list);
        lista.addFooterView(new LinearLayout(this));

        tapToAdd = (ImageView)findViewById(R.id.add_button);
        tapToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(null);
            }
        });

        ladapter = new ListWordsAdapter(this, R.layout.list_words_layout, lang, true);
        lista.setAdapter(ladapter);
        /*
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                lista,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {

                            ladapter.remove(ladapter.getItem(position));
                            setBackground();
                        }
                        ladapter.notifyDataSetChanged();
                    }
                });

        lista.setOnTouchListener(touchListener);

        lista.setOnScrollListener(touchListener.makeScrollListener());
        */

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                showAlertDialog(((TextView) v.findViewById(R.id.word_text)).getText().toString());
            }
        });

        setBackground();
    }

    public void showAlertDialog(final String h) {

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        alertDialog.setContentView(R.layout.alertdialog_add_word);

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        trans_list = (ListView) alertDialog.findViewById(R.id.trans_list);

        //LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //LinearLayout listHeaderView = (LinearLayout) inflater.inflate(R.layout.add_trans_header, null);
        //trans_list.addFooterView(listHeaderView);

        //trans_list.addFooterView(new LinearLayout(getApplicationContext()));

        String l = langName.getText().toString();

        if (h != null){
            hideKeyboard();
            EditText et = (EditText) alertDialog.findViewById(R.id.edittext);
            et.setText(h);
            trans = lang.getObjectTranslations(h);
        }
        else trans = new Translations();

        ListTransAdapter transAdapter = new ListTransAdapter(this, trans);
        trans_list.setAdapter(transAdapter);

        TextView add = (TextView) alertDialog.findViewById(R.id.add);
        TextView cancel = (TextView) alertDialog.findViewById(R.id.cancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = ((EditText) alertDialog.findViewById(R.id.edittext)).getText().toString();

                if (h != null)lang.deleteWord(h);
                lang.addWord(message);

                ArrayList<Translations.transStruct> temp = trans.getTranslationsList();
                for (int j = 0; j < temp.size(); j++) {
                    lang.addTranslate(message, temp.get(j).wordName, temp.get(j).langName);
                }

                ladapter = new ListWordsAdapter(context, R.layout.list_words_layout, lang, true);
                lista.setAdapter(ladapter);

                setBackground();

                alertDialog.dismiss();
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                hideKeyboard();
                final AlertDialog ad;
                ad = new AlertDialog.Builder(context).create();
                ad.show();
                ad.setContentView(R.layout.confirm_alertdialog);
                ad.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                        alertDialog.show();
                        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    }
                });
                ad.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                        //alertDialog.dismiss();
                    }
                });
            }

        });

        num_translations = (TextView) alertDialog.findViewById(R.id.num_trans);
        num_translations.setText(trans.getNumTranslations() + " translations");

        View addTranslate = ((View) alertDialog.findViewById(R.id.tap_to_add));
        addTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectTranslateActivity.class);
                intent.putExtra("LenguajeOrigen", lengua);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {

        backActions();
    }

    private void backActions(){
        if (lengua == null && LanguagesManager.getInstance().hasLanguage(langName.getText().toString()) != -1){
            toast("Language already exists");
        }

        else if (!langName.getText().toString().equals("")) {
            LanguagesManager.getInstance().removeLanguage(lengua);
            lang.setLanguageName(langName.getText().toString());
            LanguagesManager.getInstance().addLanguage(lang);
            finish();
        }
        else {
            if (lengua != null) toast("Language name cannot be empty");
            else if(ladapter.getCount() > 0) {
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
                        finish();
                    }
                });
                ((TextView)alertDialog.findViewById(R.id.text)).setText("Language name is empty. Changes won't be saved. Are you sure?");
            }
            else finish();
        }
    }

    public void updateAlertDialogList(){
        ((BaseAdapter)trans_list.getAdapter()).notifyDataSetChanged();
    }

    public void setBackground(){
        TextView blank_panel = ((TextView) this.findViewById(R.id.blank_text));
        TextView click_add = ((TextView) this.findViewById(R.id.add_word));
        if (lang.getNumWords() == 0) {
            Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
            blank_panel.setVisibility(View.VISIBLE);
            click_add.setVisibility(View.VISIBLE);
            click_add.setTypeface(font);
        }
        else {
            blank_panel.setVisibility(View.GONE);
            click_add.setVisibility(View.GONE);
        }
    }

    public void refreshNumTrans(){
        num_translations.setText(trans.getNumTranslations() + " translations");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            //TODO: check word edittext not empty
            String translatedLang = data.getStringExtra("lang");
            String translatedWord = data.getStringExtra("word");

            trans.addEntry(translatedLang, translatedWord);

            num_translations.setText(trans.getNumTranslations() + " translations");

            ListTransAdapter transAdapter = new ListTransAdapter(this, trans);

            trans_list.setAdapter(transAdapter);
            //ladapter = new ListWordsAdapter(this, R.layout.list_words_layout, lang, true);
            //lista.setAdapter(ladapter);
            ladapter.notifyDataSetChanged();
            fromBack = true;
        }

        SharedPreferences prefs = getSharedPreferences("NewLanguageActivity", Context.MODE_PRIVATE);
        String h = prefs.getString("language", "TESTING");
        langName = (EditText)findViewById(R.id.edit_text_bar);
        langName.setText(h);

        Type type = new TypeToken<Language>() {}.getType();
        lang = gson.fromJson(prefs.getString("Words_list", ""), type);
        ladapter = new ListWordsAdapter(this, R.layout.list_words_layout, lang, true);
        lista.setAdapter(ladapter);

    }


    @Override
    public void onPause(){
        super.onPause();

        //Log.v("onPauseLangName", langName.getText().toString());

        //Log.v("onPause: ", "He pauseado");

        SharedPreferences prefs = getSharedPreferences("NewLanguageActivity",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", langName.getText().toString());
        editor.putString("Words_list", gson.toJson(lang));
        editor.commit();

        //Log.v("AfterCommit", ""+prefs.getString("language",""));

    }

    private void hideKeyboard(){
        if (getCurrentFocus() != null) {
            Log.v("Hide", "Keyboard");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public int getLastPosition(){
        return mLastPosition;
    }

    public void setLastPosition(int lp){
        mLastPosition = lp;
    }

    public void removeWordLang(final String w){
        final Translations trans = lang.getObjectTranslations(w);
        lang.deleteWord(w);

        findViewById(R.id.undo_bar).setVisibility(View.VISIBLE);

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.bottom);
        rl.getLayoutParams().height = getdp(136);

        findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.undo_bar).setVisibility(View.INVISIBLE);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.bottom);
                rl.getLayoutParams().height = getdp(80);

                lang.addTranslate(w, trans);
                finish.cancel();
                setBackground();
                ladapter.setWords(lang);
                ladapter.notifyDataSetChanged();
            }
        });

        finish = new CountDownTimer(3000,1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                findViewById(R.id.undo_bar).setVisibility(View.INVISIBLE);
                RelativeLayout rl = (RelativeLayout)findViewById(R.id.bottom);
                rl.getLayoutParams().height = getdp(80);
            }
        }.start();

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

    private int getdp(int dpValue){
        float d = getResources().getDisplayMetrics().density;
        return (int)(dpValue * d);
    }

}
