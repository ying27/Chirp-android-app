package com.kiui.words.fragments.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.kiui.words.R;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;


public class LanguageHelpFragment extends Fragment {

    private Spinner spinner;

    public LanguageHelpFragment() {
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
        return inflater.inflate(R.layout.fragment_language_help, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        spinner = (Spinner)getActivity().findViewById(R.id.spinner_language);

        ArrayList<String> options = new ArrayList<String>();

        options.add("How to add a language");
        options.add("How to edit a language");
        options.add("How to remove a language");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    getActivity().findViewById(R.id.language_add).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.language_edit).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.language_delete).setVisibility(View.GONE);
                } else if (position == 1) {
                    getActivity().findViewById(R.id.language_add).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.language_edit).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.language_delete).setVisibility(View.GONE);
                } else {
                    getActivity().findViewById(R.id.language_add).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.language_edit).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.language_delete).setVisibility(View.VISIBLE);
                }

                ((ScrollView)getActivity().findViewById(R.id.language_scroll)).fullScroll(ScrollView.FOCUS_UP);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        //getChildFragmentManager().beginTransaction().replace(R.id.help_content, new TabbedHelp()).commit();
    }


}
