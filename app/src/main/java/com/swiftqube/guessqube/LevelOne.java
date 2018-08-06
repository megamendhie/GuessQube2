package com.swiftqube.guessqube;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.swiftqube.guessqube.database.DbProvider;
import com.swiftqube.guessqube.database.FbaseProvider;
import com.swiftqube.guessqube.people.Music;
import com.swiftqube.guessqube.people.People;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class LevelOne extends Activity implements View.OnClickListener, ViewSwitcher.ViewFactory {

    Button Holder, btnAsk, btnTimer;
    Button btn[] = new Button[5];
    TextView txtHighestScore;
    ImageSwitcher picture;
    ImageView imgBanner;
    public static int rand;
    LinearLayout l;
    String correctAnswer;
    int hScore, mm;
    public static String[] bx = new String[5];
    int mode; int life;
    Random random = new Random();
    People people = new People();
    ArrayList<String> names = new ArrayList<String>(4);
    DbProvider provider;
    FbaseProvider fbaseProvider = new FbaseProvider();
    int saved;
    CountDownTimer cc; int time, startTime = 7000;
    ImageView[] imgLife = new ImageView[6];
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);
        provider = new DbProvider(getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        Initialize();

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareIt();
            }
        });
        hScore = people.getHScore(); life = people.getLife(); saved = people.getSaved();

        txtHighestScore.setText(String.valueOf(hScore));

        if(saved==0){
            if(people.getMode()){
                mode = 1+random.nextInt(2);
                rand = people.RandomizeBoth(mode);
                loadImage(mode);
                loadNames(mode);
            }
            else{
                rand = people.Randomize(1);
                loadImage();
                loadNames();
            }
        }
        else if(saved==1)
        {
            fbaseProvider.getSavedData(1);
            savedState();
            for (int i=1; i<=4; i++){
                btn[i].setText(bx[i]);
            }
            if(people.getMode()){
                mode = getPreferences(MODE_PRIVATE).getInt("Mode", 1);
                loadImage(mode);
                switch(mode){
                    case 1:
                        correctAnswer = provider.getName(rand, "guys_Table"); break;
                    case 2:
                        correctAnswer = provider.getName(rand, "ladies_Table"); break;
                }
            }
            else{
                loadImage();
                correctAnswer = provider.getName(rand, "");
            }
        }

        Log.i("Uyo", "We're running Level 1");
        saved = 1;
        life(life);
    }

    @Override
    public void onClick(View v) {
        if(cc != null) {
            cc.cancel();
            Log.i("Uyoo", "timer paused");
            cc = null;
        }

        Holder = (Button) v;
        Confirm();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(hScore > 195){
                    saved=0;
                    nextLevel();}
                else{
                    Resolve();
                }
            }
        }, 800);
    }

    public void nextLevel(){
        mm=2;
        onPause();
        //Intent intent = new Intent(this, LevelTwo.class);
        //startActivity(intent);
        finish();

    }

    public void loadNames(){
        for (int i = 0; i < l.getChildCount(); i++){
            View child = l.getChildAt(i);
            child.setEnabled(true);
            child.setBackground(getResources().getDrawable(R.drawable.btn_border5));}
        correctAnswer = provider.getName(rand, "");
        ArrayList<Integer> newNames = new ArrayList<Integer>(provider.getLength());
        for(int i=1; i<=provider.getLength(); i++){
            newNames.add(i);
        }
        //clears holder for the names
        names.clear();
        Log.i("whileLoop", correctAnswer);
        for(int i=1; i<=4; i++){
            int n = random.nextInt(newNames.size());
            n = newNames.remove(n);
            String receivedName = provider.getNames("", n);
            while(names.contains(receivedName)){
                n = random.nextInt(newNames.size());
                n = newNames.remove(n);
                receivedName = provider.getNames("", n);
            }
            names.add(receivedName);
            btn[i].setText(receivedName);
        }

        Log.i("Uyo", "Image id is: " + names);
        if(!names.contains(correctAnswer)){
            int num = 1 + random.nextInt(4);
            btn[num].setText(correctAnswer);
        }
    }


    public void loadNames(int mode){
        String table="";
        Log.i("Uyo", String.valueOf(mode));
        for (int i = 0; i < l.getChildCount(); i++){
            View child = l.getChildAt(i);
            child.setEnabled(true);
            child.setBackground(getResources().getDrawable(R.drawable.btn_border5));}

        switch(mode){
            case 1:
                table = "guys_Table"; break;
            case 2:
                table = "ladies_Table"; break;
        }
        correctAnswer = provider.getName(rand, table);
        ArrayList<Integer> newNames = new ArrayList<Integer>(provider.getLength());
        for(int i=1; i<=provider.getLength(); i++){
            newNames.add(i);
        }
        //clears holder for the names
        names.clear();
        for(int i=1; i<=4; i++){
            int n = random.nextInt(newNames.size());
            n = newNames.remove(n);
            String receivedName = provider.getNames(table, n);
            while(names.contains(receivedName)){
                n = random.nextInt(newNames.size());
                n = newNames.remove(n);
                receivedName = provider.getNames(table, n);
            }
            names.add(receivedName);
            btn[i].setText(receivedName);
        }
        //Log.i("Uyo", ""+String.valueOf(rand)+" "+table);
        //Log.i("Uyo", "Image id is: " + names);
        if(!names.contains(correctAnswer)){
            int num = 1 + random.nextInt(4);
            btn[num].setText(correctAnswer);
        }
    }

    public void loadImage(){
        int imageId = getResources().getIdentifier((people.getPrefix()+String.valueOf(rand)), "drawable", getPackageName());
        Log.i("Uyo", "Image id is: " + imageId);
        picture.setImageResource(imageId);
    }

    //loads image from drawable
    public void loadImage(int mode){
        String prefix="";
        switch(mode){
            case 1:
                prefix = "g"; break;
            case 2:
                prefix = "l"; break;
        }
        int imageId = getResources().getIdentifier((prefix+String.valueOf(rand)), "drawable", getPackageName());
        Log.i("Uyo", "Image id is: " + imageId);
        picture.setImageResource(imageId);
    }

    public void Confirm(){
        for (int i = 0; i < l.getChildCount(); i++){
            View child = l.getChildAt(i);
            child.setEnabled(false);}

        if(Holder.getText().toString().equals(correctAnswer)){
            Holder.setBackground(getResources().getDrawable(R.drawable.btn_right));
            hScore+=10;
            life+=1;
            life = Math.min(5, life);
        }
        else{
            Holder.setBackground(getResources().getDrawable(R.drawable.btn_wrong));
            life-=1;
        }
        txtHighestScore.setText(String.valueOf(hScore));
    }

    //takes care of image Slide
    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new
                ImageSwitcher.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public void onPause(){
        super.onPause();
        startTime = time;
        if(cc != null) {
            cc.cancel();
            cc = null;
        }
        state();
        people.saveAll(hScore, life, saved);
        fbaseProvider.saveState(people.getIndex(), hScore, life, saved, rand, bx[1], bx[2], bx[3], bx[4]);

        if(people.getMode()){
            getPreferences(MODE_PRIVATE).edit().putInt("Mode", mode).apply();
        }
    }

    public void life(int life){

        for(int j = 1; j<=5; j++){
            imgLife[j].setImageResource(R.drawable.nolove);
        }

        for(int j = 1; j<=life; j++){
            imgLife[j].setImageResource(R.drawable.love);
        }

    }

    public void state(){
        for(int i=1; i<=4; i++){
            bx[i] = btn[i].getText().toString();}
    }

    public void savedState(){
        rand = people.pix;
        bx = people.bx;

        Log.i("Afo udoha idad", "rand is " + String.valueOf(rand) + ", and buttons are: " + bx.toString());
    }

    //sets default values to each variable
    public void Initialize(){
        btnAsk = findViewById(R.id.btnAsk);
        btnTimer = findViewById(R.id.btnTimer);
        txtHighestScore = findViewById(R.id.txtHighestScore);
        btn[1] = findViewById(R.id.btnOne); btn[1].setOnClickListener(this);
        btn[2] = findViewById(R.id.btnTwo); btn[2].setOnClickListener(this);
        btn[3] = findViewById(R.id.btnThree); btn[3].setOnClickListener(this);
        btn[4] = findViewById(R.id.btnFour); btn[4].setOnClickListener(this);
        l = findViewById(R.id.lnrBtn);


        imgLife[1]= findViewById(R.id.imgLife1); imgLife[2]= findViewById(R.id.imgLife2);
        imgLife[3]= findViewById(R.id.imgLife3); imgLife[4]= findViewById(R.id.imgLife4);
        imgLife[5]= findViewById(R.id.imgLife5);

        imgBanner = findViewById(R.id.imgBanner);
        imgBanner.setImageResource(getResources().getIdentifier(people.getBanner(), "drawable", getPackageName()));

        picture = findViewById(R.id.imgPics);
        picture.setFactory(this);
        picture.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        picture.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));

        Holder = new Button(this);

    }

    public void Resolve(){
        int hs;
        startTime = 7000;
        if(cc != null) {
            cc.cancel();
            Log.i("Uyoo", "timer paused");
            cc = null;
        }
        if(people.getMode()){
            if(life>=0){
                mode = 1+random.nextInt(2);
                rand = people.RandomizeBoth(mode);
                loadImage(mode);
                loadNames(mode);
                life(life);
                timer();}
            else{
                hs = hScore;
                hScore = saved = 0; life = 5;
                onPause();
                endGame(hs);
                finish();
            }
        }else{
            if(life>=0){
                rand = people.Randomize(2);
                loadImage();
                loadNames();
                life(life);
                timer();}
            else{
                hs = hScore;
                hScore = saved = 0; life = 5;
                onPause();
                endGame(hs);
                finish();
            }
        }
    }

    public int getStartTime(){
        return startTime;
    }

    public void timer(){
        Log.i("Uyoo", "timer started");
        int start = getStartTime();
        cc = new CountDownTimer(start, 1000) {

            public void onTick(long millisUntilFinished) {
                time = (int) millisUntilFinished;
                btnTimer.setText(String.format("00:%d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                life-=1;
                Resolve();
            }
        }.start();
    }

    public void shareIt(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");

        //get rootview of activity
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View screenView = rootView.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap adv = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);

        File f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/GuessQube");

        if(!f.exists())
            f.mkdirs();
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+ "/GuessQube", "forSharing.jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            adv.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Toast.makeText(this, "flusing error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        sharingIntent.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(file));
        String sAux = "https://play.google.com/store/apps/details?id=com.swiftqube.guessqube \n\n";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(sharingIntent, "Ask via"));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(prefs.getBoolean("SOUND", true)){
            mm = 1;
            Music.play(getApplicationContext(), R.raw.sound, prefs.getBoolean("SOUND", true));}
        if(startTime>0){
            timer();}
        else{
            startTime = 7000;
            timer();}
    }

    @Override
    public void onStop(){
        if(mm==1)
            Music.stop(getApplicationContext());
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        mm=2;
        Intent intentPause = new Intent(this, PauseActivity.class);
        startActivityForResult(intentPause, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(resultCode==1){
            finish();}
        else if(resultCode==0 || resultCode==2){}
    }

    private void endGame(int HighScore){
        mm=2;
        String HIGHSCORE = people.getHVar();
        String DTH = people.getDTH();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        StringBuilder dateTimeHash = new StringBuilder();
        int date = calendar.get(Calendar.DATE),		month = calendar.get(Calendar.MONTH)+1 ,
                minute = calendar.get(Calendar.MINUTE),		hour = calendar.get(Calendar.HOUR_OF_DAY);

        String hash = people.endGame(HighScore, date +month, hour + minute);
        dateTimeHash.append(date + "/" + month + " " + hour + ":" + minute + "\t\t\t" + hash);

        if(HighScore > prefs.getInt(HIGHSCORE, 0)) {
            editor.putInt(HIGHSCORE, HighScore);
            editor.putString(DTH, String.valueOf(dateTimeHash));
            editor.apply();
        }
        /*
        Intent scoreIntent = new Intent(this, HighScore.class);
        scoreIntent.putExtra("HighScore", HighScore);
        scoreIntent.putExtra("DateTime", String.valueOf(dateTimeHash));
        startActivity(scoreIntent);
        */
    }
}
