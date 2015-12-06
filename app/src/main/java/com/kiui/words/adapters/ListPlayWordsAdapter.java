package com.kiui.words.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.data.Language;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ying on 02/05/2015.
 */
public class ListPlayWordsAdapter extends BaseAdapter {

    private int R_layout_IdView;
    private Context contexto;
    private ArrayList<String> words;
    private boolean checkWord;

    private String lmain, wmain, lsecond, wsecond;

    public ListPlayWordsAdapter(Context contexto, int R_layout_IdView, ArrayList<String> words) {
        this.R_layout_IdView = R_layout_IdView;
        this.contexto = contexto;
        this.words = words;
        Collections.sort(this.words);
        this.checkWord = false;
    }

    public void setCheck(String lmain, String wmain, String lsecond, String wsecond ){
        checkWord = true;
        this.lmain = lmain;
        this.lsecond = lsecond;
        this.wmain = wmain;
        this.wsecond = wsecond;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        if (checkWord == true) {
            if (LanguagesManager.getInstance().hasTranslationTo(lmain, wmain, lsecond, words.get(posicion))){
                CardView card = (CardView)view.findViewById(R.id.card_view);
                card.setCardBackgroundColor(Color.parseColor("#00E676"));
            ((TextView) view.findViewById(R.id.text)).setTextColor(Color.parseColor("#FFFFFF"));
            }
            else if (words.get(posicion).equals(wsecond)){
                    //view.findViewById(R.id.card_view).setBackgroundColor(Color.parseColor("#EF5350"));
                    ((TextView)view.findViewById(R.id.text)).setTextColor(Color.parseColor("#EF5350"));
            }
        }

        ((TextView) view.findViewById(R.id.text)).setText(words.get(posicion));

        return view;
    }

    public ArrayList<String> getAllItems() {
        return words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int posicion) {
        return words.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }
}


