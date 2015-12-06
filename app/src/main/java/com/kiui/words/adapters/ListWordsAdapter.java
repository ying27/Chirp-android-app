package com.kiui.words.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.activities.NewLanguageActivity;
import com.kiui.words.data.Language;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class ListWordsAdapter extends BaseAdapter {

    private Language lenguaje;
    private int R_layout_IdView;
    private Context contexto;
    private ArrayList<String> words;
    private boolean creation;




    public ListWordsAdapter(Context contexto, int R_layout_IdView, Language lenguaje, boolean creation) {
        this.lenguaje = lenguaje;
        this.R_layout_IdView = R_layout_IdView;
        this.contexto = contexto;
        this.words = lenguaje.getWords();
        Collections.sort(this.words);
        this.creation = creation;
    }

    @Override
    public View getView(final int posicion, View view, ViewGroup pariente) {

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        TextView tv = (TextView)view.findViewById(R.id.num_trans);
        tv.setText(lenguaje.getNumTranslation(words.get(posicion)) + " translations");
        ((TextView)view.findViewById(R.id.word_text)).setText(words.get(posicion));

        final View button = view.findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(contexto, button);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ((NewLanguageActivity)contexto).removeWordLang(words.get(posicion));
                        words.remove(posicion);
                        refreshList();
                        ((NewLanguageActivity)contexto).setBackground();

                        return true;
                    }
                });
                popup.show();
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public String getItem(int posicion) {
        return words.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    public void remove(String w){
        lenguaje.deleteWord(w);
        words = lenguaje.getWords();
    }

    public void setWords(Language leng){
        this.lenguaje = leng;
        this.words = lenguaje.getWords();
        Collections.sort(this.words);
    }

    public void refreshList(){
        this.notifyDataSetChanged();
    }
}