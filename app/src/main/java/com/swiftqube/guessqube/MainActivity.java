package com.swiftqube.guessqube;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swiftqube.guessqube.database.DbProvider;
import com.swiftqube.guessqube.database.FbaseProvider;
import com.swiftqube.guessqube.people.Music;
import com.swiftqube.guessqube.people.People;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btnGuys, btnLadies, btnBoth, btnAbout, btnHscore, btnRewards, btnPlaces, btnComics, btnFamous;
    ImageButton btnSound;
    int mm;
    boolean playSound;
    int hScore;
    Intent intent;
    People people = new People();
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mfirebaseAuth;
    static boolean calledAlready = false;

    FbaseProvider fbProvider;
    DbProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        prefs  = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        btnSound = findViewById(R.id.btnSound);
        playSound = prefs.getBoolean("SOUND", true);
        Music.play(getApplicationContext(), R.raw.sound, playSound);
        if(!playSound){
            btnSound.setImageResource(R.drawable.sndoff);
        }
        provider = new DbProvider(this);
        Log.i("Uyo", "I'm opening the floor");

        btnGuys = findViewById(R.id.btnGuys); 			btnGuys.setOnClickListener(this);
        btnLadies = findViewById(R.id.btnLadies);		btnLadies.setOnClickListener(this);
        btnBoth = findViewById(R.id.btnBoth); 			btnBoth.setOnClickListener(this);
        btnPlaces = findViewById(R.id.btnPlaces);		btnPlaces.setOnClickListener(this);
        btnHscore = findViewById(R.id.btnHscore);		btnHscore.setOnClickListener(this);
        btnAbout = findViewById(R.id.btnAbout);			btnAbout.setOnClickListener(this);
        btnRewards = findViewById(R.id.btnRewards);		btnRewards.setOnClickListener(this);
        btnComics = findViewById(R.id.btnComics);		btnComics.setOnClickListener(this);
        btnFamous = findViewById(R.id.btnFamous);		btnFamous.setOnClickListener(this);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playSound){
                    playSound = false;
                    editor.putBoolean("SOUND", playSound).apply();
                    btnSound.setImageResource(R.drawable.sndoff);
                    Music.stop(getApplicationContext());
                }
                else{
                    playSound = true;
                    editor.putBoolean("SOUND", playSound).apply();
                    btnSound.setImageResource(R.drawable.snd);
                    Music.play(getApplicationContext(), R.raw.sound, playSound);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        mm=2;
        switch(v.getId()){
            case R.id.btnGuys:
                if(user==null){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return;
                }
                people.setTable("guys_Table", 1, "who");
                provider.setTable("guys_Table");
                people.setTotal(provider.getTotal(""));
                fbProvider.getState(1);
                hScore = people.getHScore();
                gameLevel();
                break;

            case R.id.btnLadies:
                if(user==null){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return;
                }
                people.setTable("ladies_Table", 2, "who");
                provider.setTable("ladies_Table");
                people.setTotal(provider.getTotal(""));
                fbProvider.getState(2);
                hScore = people.getHScore();
                gameLevel();
                break;

            case R.id.btnBoth:
                if(user==null){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return;
                }
                int guys = provider.getTotal("guys_Table");
                int ladies = provider.getTotal("ladies_Table");
                people.bothEngine(guys, ladies, "who");
                fbProvider.getState(3);
                hScore = people.getHScore();
                gameLevel();
                break;

            case R.id.btnComics:
                if(user==null){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return;
                }
                people.setTable("comics_Table", 4, "who");
                provider.setTable("comics_Table");
                people.setTotal(provider.getTotal(""));
                fbProvider.getState(4);
                hScore = people.getHScore();
                gameLevel();
                break;

            case R.id.btnPlaces:
                if(user==null){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return;
                }
                people.setTable("places_Table", 5, "where");
                provider.setTable("places_Table");
                people.setTotal(provider.getTotal(""));
                fbProvider.getState(5);
                hScore = people.getHScore();
                gameLevel();
                break;

            case R.id.btnFamous:
                if(user==null){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return;
                }
                people.setTable("famous_Table", 6, "who");
                provider.setTable("famous_Table");
                people.setTotal(provider.getTotal(""));
                fbProvider.getState(6);
                hScore = people.getHScore();
                gameLevel();
                break;

            /*
            case R.id.btnAbout:
                intent = new Intent(this, AboutActivity.class); break;
            case R.id.btnHscore:
                intent = new Intent(this, Scores.class);
                break;
            case R.id.btnRewards:
                intent = new Intent(this, RewardActivity.class);
                break;

            */
        }
        if(intent!= null){
            startActivity(intent);}
    }

    public void gameLevel(){
            if (hScore <= 195) {
                intent = new Intent(this, LevelOne.class);
            }
            /*
            else if (hScore > 195 && hScore <= 395) {
                intent = new Intent(this, LevelTwo.class);
            } else if (hScore > 395 && hScore <= 695) {
                intent = new Intent(this, LevelThree.class);
            } else if (hScore > 695 && hScore <= 995) {
                intent = new Intent(this, LevelFour.class);
            } else if (hScore > 995 && hScore <= 1395) {
                intent = new Intent(this, LevelFive.class);
            } else if (hScore > 1395) {
                intent = new Intent(this, LevelSix.class);
            }
            */

    }

    public void Profile(View view){
        mm=2;
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getBoolean("SOUND", true)){
            mm=1;
            Music.play(getApplicationContext(), R.raw.sound, prefs.getBoolean("SOUND", true));}

        mfirebaseAuth = FirebaseAuth.getInstance();
        user = mfirebaseAuth.getCurrentUser();
        fbProvider = new FbaseProvider();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mm==1)
            Music.stop(getApplicationContext());
        Log.i("Uyo", "I'm closing for the good of Mendhie");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Music.stop(getApplicationContext());
    }
}