package com.kiui.words.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kiui.words.R;
import com.kiui.words.activities.GameOverActivity;
import com.kiui.words.activities.NewLanguageActivity;
import com.kiui.words.adapters.ListLangAdapter;
import com.kiui.words.adapters.ListWordsAdapter;
import com.kiui.words.data.Language;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;

public class Home extends Fragment {

    private ListView lista;
    private ListAdapter ladapter;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();

        View addButton = (View) getActivity().findViewById(R.id.action_bar_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewLanguageActivity.class);
                getActivity().startActivity(intent);
            }
        });

        lista = (ListView)getActivity().findViewById(R.id.lang_list);
        ladapter = new ListLangAdapterR(getActivity());
        lista.setAdapter(ladapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewLanguageActivity.class);
                intent.putExtra("language", ((TextView) view.findViewById(R.id.lang_name)).getText().toString());
                getActivity().startActivity(intent);
            }
        });

        setBackground();
    }

    public void setBackground(){

        TextView blank_panel = ((TextView) getActivity().findViewById(R.id.texto_add));
        TextView click_add = ((TextView) getActivity().findViewById(R.id.texto_click));

        if (LanguagesManager.getInstance().getNumLang() == 0) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
            blank_panel.setVisibility(View.VISIBLE);
            click_add.setVisibility(View.VISIBLE);
            click_add.setTypeface(font);
        }

        else {
            blank_panel.setVisibility(View.GONE);
            click_add.setVisibility(View.GONE);
        }
    }

    private class ListLangAdapterR extends ListLangAdapter{

        private CountDownTimer finish;

        public ListLangAdapterR(Context contexto) {
            super(contexto);
        }

        @Override
        public void refreshList(final String l){

            final Language temp = LanguagesManager.getInstance().getObjectLanguage(l);
            LanguagesManager.getInstance().removeLanguage(l);

            setBackground();
            setPlaySpinners();

            getActivity().findViewById(R.id.undo_bar).setVisibility(View.VISIBLE);

            RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.bottom);
            rl.getLayoutParams().height = getdp(136);

            getActivity().findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().findViewById(R.id.undo_bar).setVisibility(View.INVISIBLE);
                    RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.bottom);
                    rl.getLayoutParams().height = getdp(80);
                    LanguagesManager.getInstance().addLanguage(temp);
                    finish.cancel();
                    setBackground();
                    setPlaySpinners();
                    reloadLangNames();
                    notifyData();
                }
            });

            finish = new CountDownTimer(3000,1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    getActivity().findViewById(R.id.undo_bar).setVisibility(View.INVISIBLE);
                    RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.bottom);
                    rl.getLayoutParams().height = getdp(80);
                }
            }.start();
            super.refreshList(l);
        }
    }
    private void setPlaySpinners(){
        final Spinner spinnerP = (Spinner) getActivity().findViewById(R.id.spinner_language_orig);
        final Spinner spinnerS = (Spinner) getActivity().findViewById(R.id.spinner_language_trans);

        /*Comportamiento inicial spinner secundario*/
        ArrayList<String> temp = new ArrayList<String>();
        temp.add("- - -");
        final ArrayAdapter<String> adapterS = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, temp);
        adapterS.setDropDownViewResource(R.layout.spinner_layout_dropdown);
        spinnerS.setAdapter(adapterS);
        /******************************************/

        /**Comportamiento spinner principal*/
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

    }

    private int getdp(int dpValue){
        float d = getActivity().getResources().getDisplayMetrics().density;
        return (int)(dpValue * d);
    }
}
