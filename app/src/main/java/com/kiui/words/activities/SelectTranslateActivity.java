package com.kiui.words.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.adapters.ListPlainWordsAdapter;
import com.kiui.words.fragments.TabbedFragments;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;
import java.util.List;

public class SelectTranslateActivity extends ActionBarActivity {

    Spinner langSelection;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_select_translate);



        Intent intent = getIntent();
        String h = intent.getStringExtra("LenguajeOrigen");

        ((ImageView)findViewById(R.id.action_bar_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        langSelection = (Spinner) findViewById(R.id.language_spinner);
        ArrayList<String> langs = new ArrayList<String>();
        langs.add("Select a language");

        if (h == null) langs.addAll(LanguagesManager.getInstance().getLanguages());
        else langs.addAll(LanguagesManager.getInstance().getLanguages(h));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, langs);
        adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);

        langSelection.setAdapter(adapter);

        langSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setupWords(position == 0, langSelection.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        list = (ListView)findViewById(R.id.words_list);

        /*
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("lang", langSelection.getSelectedItem().toString());
                intent.putExtra("word", ((TextView)view.findViewById(R.id.text)).getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setupWords(boolean empty, String lang){
        if (empty){
            findViewById(R.id.blank).setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.blank).setVisibility(View.INVISIBLE);
            list.setAdapter(new ListPlainWordsAdapter(this,
                    R.layout.list_plain_words_layout,
                    LanguagesManager.getInstance().getObjectLanguage(lang).getWords()));
            //list.addFooterView(new LinearLayout(this));
            list.setVisibility(View.VISIBLE);
        }
    }
}
