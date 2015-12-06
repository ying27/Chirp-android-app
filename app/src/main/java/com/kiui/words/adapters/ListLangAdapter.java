package com.kiui.words.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.activities.MainActivity;
import com.kiui.words.activities.NewLanguageActivity;
import com.kiui.words.data.Translations;
import com.kiui.words.fragments.Home;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;

/**
 * Created by ying on 09/04/2015.
 */
public class ListLangAdapter extends BaseAdapter {

    private int R_layout_IdView;
    private Context contexto;
    private ArrayList<String> langNames;


    public ListLangAdapter() {
    }

    public ListLangAdapter(Context contexto) {
        this.R_layout_IdView = R.layout.list_lang_layout;
        this.contexto = contexto;
        this.langNames = LanguagesManager.getInstance().getLanguages();
    }

    @Override
    public View getView(final int posicion, View view, ViewGroup pariente) {

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        TextView tv = (TextView)view.findViewById(R.id.num_words);
        tv.setText(""+LanguagesManager.getInstance().getNumWordsLang(langNames.get(posicion))+" words");

        ((TextView)view.findViewById(R.id.lang_name)).setText(langNames.get(posicion));


        final View button = view.findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(contexto, button);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //LanguagesManager.getInstance().removeLanguage(langNames.get(posicion));
                        String temp = langNames.get(posicion);
                        langNames.remove(posicion);
                        refreshList(temp);
                        return true;
                    }
                });
                popup.show();
            }

        });

        return view;
    }

    public void reloadLangNames(){
        langNames = LanguagesManager.getInstance().getLanguages();
    }


    public void notifyData(){
        this.notifyDataSetChanged();
    }

    public void refreshList(String l){
        notifyData();
    }

    @Override
    public int getCount() {
        return langNames.size();
    }

    @Override
    public Object getItem(int posicion) {
        return langNames.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }
}
