package com.kiui.words.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kiui.words.MainApplication;
import com.kiui.words.R;
import com.kiui.words.activities.NewLanguageActivity;
import com.kiui.words.data.Language;
import com.kiui.words.data.Translations;

import java.util.ArrayList;

/**
 * Created by ying on 01/05/2015.
 */
public class ListTransAdapter extends BaseAdapter {
    private int R_layout_IdView;
    private Context contexto;
    private Translations trans;
    private ArrayList<Translations.transStruct> trans_langs;

    public ListTransAdapter(Context contexto, Translations trans) {
        this.trans = trans;
        this.R_layout_IdView = R.layout.list_trans_layout;
        this.contexto = contexto;
        trans_langs = trans.getTranslationsList();
    }

    @Override
    public View getView(final int posicion, View view, ViewGroup pariente) {

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        final Translations.transStruct output = trans_langs.get(posicion);
        ((TextView)view.findViewById(R.id.trans_word)).setText(output.wordName);
        ((TextView)view.findViewById(R.id.trans_lang)).setText(output.langName);


        final View button = view.findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(contexto, button);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        trans.deleteEntry(output.langName, output.wordName);
                        trans_langs.remove(posicion);
                        refreshList();
                        return true;
                    }
                });
                popup.show();
            }

        });




        return view;
    }

    private void refreshList(){
        ((NewLanguageActivity) contexto).refreshNumTrans();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return trans.getNumTranslations();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }
}
