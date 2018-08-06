package com.swiftqube.guessqube;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.swiftqube.guessqube.people.Music;

public class PauseActivity extends Activity implements View.OnClickListener {
    Button btnResume, btnMenu;
    SharedPreferences prefs;
    int mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        btnResume = findViewById(R.id.btnResume); btnResume.setOnClickListener(this);
        btnMenu = findViewById(R.id.btnMenu); btnMenu.setOnClickListener(this);
    }


    @Override
    public void onBackPressed()
    {
        //Do nohing
    }


    @Override
    public void onClick(View v) {
        mm=2;
        switch(v.getId()){
            case R.id.btnMenu:
                setResult(1);
                finish(); break;
            case R.id.btnResume:
                setResult(2);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getBoolean("SOUND", true)){
            mm = 1;
            Music.play(getApplicationContext(), R.raw.sound, prefs.getBoolean("SOUND", true));}
    }

    @Override
    protected void onStop() {
        if(mm==1)
            Music.stop(getApplicationContext());
        super.onStop();
    }
}