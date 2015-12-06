package com.kiui.words.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.data.Language;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ying on 02/05/2015.
 */
public class ListPlainWordsAdapter extends BaseAdapter {

    private int R_layout_IdView;
    private Context contexto;
    private ArrayList<String> words;

    public ListPlainWordsAdapter(Context contexto, int R_layout_IdView, ArrayList<String> words) {
        this.R_layout_IdView = R_layout_IdView;
        this.contexto = contexto;
        this.words = words;
        Collections.sort(this.words);
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        ((TextView) view.findViewById(R.id.text)).setText(words.get(posicion));

        return view;
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


