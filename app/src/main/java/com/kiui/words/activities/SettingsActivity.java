package com.kiui.words.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiui.words.R;
import com.kiui.words.adapters.ListPlainWordsAdapter;
import com.kiui.words.data.Language;
import com.kiui.words.manager.LanguagesManager;

import java.util.ArrayList;

public class SettingsActivity extends ActionBarActivity {

    private Toast toast;
    private AlertDialog alertDialog;

    private ListView list;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_settings);

        ArrayList<String> files = LanguagesManager.getInstance().getImportFiles();

        list = (ListView)findViewById(R.id.import_list);

        list.setAdapter(new ListPlainWordsAdapter(this, R.layout.list_words_play,
                files));
        list.addFooterView(new LinearLayout(this));
        list.setVisibility(View.VISIBLE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.show();
                alertDialog.setContentView(R.layout.confirm_alertdialog);
                alertDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        LanguagesManager.getInstance().loadFile(getApplicationContext(), ((TextView) view.findViewById(R.id.text)).getText().toString());
                        toast("File loaded successfully");
                    }
                });
            }
        });

        /*
        findViewById(R.id.save_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.show();

                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                alertDialog.setContentView(R.layout.alertdialog_save_file);
                alertDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileName = ((EditText) alertDialog.findViewById(R.id.file_name)).getText().toString();
                        LanguagesManager.getInstance().saveToInternal(getApplicationContext(), fileName);
                        alertDialog.dismiss();
                        list.setAdapter(new ListPlainWordsAdapter(context, R.layout.list_plain_words_layout,
                                LanguagesManager.getInstance().getImportFiles()));
                    }
                });
            }
        });*/

        findViewById(R.id.action_bar_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void toast(String texto) {
        if (toast != null) toast.cancel();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(texto);
        toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }



}
