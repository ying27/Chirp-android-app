package com.kiui.words.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kiui.words.R;
import com.kiui.words.activities.PlayActivity;
import com.kiui.words.activities.RankingActivity;
import com.kiui.words.activities.SelectTranslateActivity;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;

public class Play extends Fragment {



    private Spinner spinnerP;
    private Spinner spinnerS;
    private Spinner spinnerT;

    private Toast toast;

    public Play() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();

        spinnerP = (Spinner) getActivity().findViewById(R.id.spinner_language_orig);
        spinnerS = (Spinner) getActivity().findViewById(R.id.spinner_language_trans);
        spinnerT = (Spinner) getActivity().findViewById(R.id.spinner_timing);

        ArrayList<String> mode = new ArrayList<String>();
        mode.add("Time trial");
        mode.add("5 words");
        ArrayAdapter<String> adapterT = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, mode);
        adapterT.setDropDownViewResource(R.layout.spinner_layout_dropdown);

        spinnerT.setAdapter(adapterT);

        ArrayList<String> langs = new ArrayList<String>();
        langs.add("Select a language");
        langs.addAll(LanguagesManager.getInstance().getLanguages());
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, langs);
        adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);

        spinnerP.setAdapter(adapter);

        spinnerP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add("- - -");
                    ArrayAdapter<String> adapterS = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, temp);
                    adapterS.setDropDownViewResource(R.layout.spinner_layout_dropdown);
                    spinnerS.setAdapter(adapterS);
                } else {
                    ArrayList<String> tmp = LanguagesManager.getInstance().getLanguagesPlay(spinnerP.getSelectedItem().toString());
                    ArrayAdapter<String> adapterS;
                    if (tmp.size() == 0) {
                        tmp = new ArrayList<String>();
                        tmp.add("- - -");
                    }
                    adapterS = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, tmp);
                    adapterS.setDropDownViewResource(R.layout.spinner_layout_dropdown);
                    spinnerS.setAdapter(adapterS);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        getActivity().findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String main = spinnerP.getSelectedItem().toString();
                String second = spinnerS.getSelectedItem().toString();
                boolean timing = spinnerT.getSelectedItem().toString().equals("Time trial");

                if (!main.equals("Select a language")) {
                    if (second.equals("- - -"))
                        toast(main + " does not have any translation");

                    else if (LanguagesManager.getInstance().getTotalNumWords() < 6)
                        toast("Not enough words for playing");

                    else {
                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        intent.putExtra("main", main);
                        intent.putExtra("second", second);
                        intent.putExtra("timing", timing);
                        startActivity(intent);
                    }
                }
                else {
                    toast("Select a language first");
                }
            }
        });

        getActivity().findViewById(R.id.view_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RankingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    private void toast(String texto) {
        if (toast != null) toast.cancel();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(texto);
        toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
