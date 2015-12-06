package com.kiui.words.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.data.Points;
import com.kiui.words.manager.PointsManager;

import java.util.ArrayList;

/**
 * Created by ying on 09/05/2015.
 */
public class ListRankingAdapter extends BaseAdapter {

    Context context;
    private int R_layout_IdView;
    private ArrayList<Points> ranking;

    private String user;
    private String points;

    public ListRankingAdapter(Context context, int R_layout_IdView, String user, String points){
        this.R_layout_IdView = R_layout_IdView;
        this.context = context;
        ranking = PointsManager.getInstance().getScores();
        this.user = user;
        this.points = points;

    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        String u = ranking.get(posicion).getUser();
        String p = ranking.get(posicion).getPoints()+"";

        TextView utv = ((TextView) view.findViewById(R.id.user_name));
        TextView ptv = ((TextView) view.findViewById(R.id.score_points));

        if (u.equals(user) && p.equals(points)) {
            utv.setTypeface(null, Typeface.BOLD);
            ptv.setTypeface(null, Typeface.BOLD);
        }

        else {
            utv.setTypeface(null, Typeface.NORMAL);
            ptv.setTypeface(null, Typeface.NORMAL);
        }
        utv.setText(u);
        ptv.setText(p);

        return view;
    }


    @Override
    public int getCount() {
        return ranking.size();
    }

    @Override
    public Object getItem(int position) {
        return ranking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
