package com.kiui.words.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.kiui.words.R;
import com.kiui.words.adapters.ListPlainWordsAdapter;
import com.kiui.words.fragments.TabbedFragments;
import com.kiui.words.manager.LanguagesManager;


public class MainActivity extends ActionBarActivity {

    private Context context = this;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TabbedFragments()).commit();
        //getSupportActionBar().setElevation(0);
        final View button = findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, button);
                popup.getMenuInflater().inflate(R.menu.settings, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Import from file")) {
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);
                        } else if (item.getTitle().equals("Export to file")) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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
                                    toast(fileName+".chi exported succesfully");
                                    alertDialog.dismiss();
                                }
                            });

                        } else if (item.getTitle().equals("Help")) {
                            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                            startActivity(intent);
                        } else if (item.getTitle().equals("About")) {
                            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();
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
