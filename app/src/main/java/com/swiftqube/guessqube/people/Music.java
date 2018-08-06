package com.swiftqube.guessqube.people;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {

    static MediaPlayer mp = null;

    public static void play(Context context, int resources, boolean playSound){
        if(playSound) {
            if (mp == null) {
                stop(context);
                mp = MediaPlayer.create(context, resources);
                mp.setLooping(true);
                mp.start();
            }
        }
    }

    public static void stop(Context context){
        if(mp!=null){
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}
