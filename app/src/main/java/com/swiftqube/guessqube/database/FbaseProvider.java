package com.swiftqube.guessqube.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swiftqube.guessqube.people.People;

import java.util.HashMap;

/**
 * Created by Mendhie on 3/25/2018
 */

public class FbaseProvider {


    private DatabaseReference mRef;
    private static DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mfirebaseAuth;
    private People people = new People();

    int hScore, life, saved;
    int pix_1, pix_2, pix_3;
    String b1, b2, b3, b4;
    String lb1, lb2, lb3, rb1, rb2, rb3;
    String tb1, tb2, mb1, mb2, bb1, bb2;

    public FbaseProvider(){
        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mfirebaseAuth.getCurrentUser();
        if(user!=null){
            mDatabase =  mDatabase.child("UserDatabase").child(user.getUid()).child("Data");
            mDatabase.keepSynced(true);
        }
    }

    public void getState(int row){
            String[] category = {"1Gent", "2Ladies", "3Both", "4Comics", "5Places", "6Fmaous"};
            mRef = mDatabase.child(category[--row]);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hScore = dataSnapshot.child("highest_score").getValue(long.class).intValue();
                    life = dataSnapshot.child("life").getValue(long.class).intValue();
                    saved = dataSnapshot.child("saved").getValue(long.class).intValue();

                    pix_1 = dataSnapshot.child("pix_1").getValue(long.class).intValue();
                    pix_2 = dataSnapshot.child("pix_2").getValue(long.class).intValue();
                    pix_3 = dataSnapshot.child("pix_3").getValue(long.class).intValue();

                    b1 = dataSnapshot.child("b1").getValue(String.class);
                    b2 = dataSnapshot.child("b2").getValue(String.class);
                    b3 = dataSnapshot.child("b3").getValue(String.class);
                    b4 = dataSnapshot.child("b4").getValue(String.class);

                    lb1 = dataSnapshot.child("lb1").getValue(String.class);
                    lb2 = dataSnapshot.child("lb2").getValue(String.class);
                    lb3 = dataSnapshot.child("lb3").getValue(String.class);
                    rb1 = dataSnapshot.child("rb1").getValue(String.class);
                    rb2 = dataSnapshot.child("rb2").getValue(String.class);
                    rb3 = dataSnapshot.child("rb3").getValue(String.class);

                    tb1 = dataSnapshot.child("tb1").getValue(String.class);
                    tb2 = dataSnapshot.child("tb2").getValue(String.class);
                    mb1 = dataSnapshot.child("mb1").getValue(String.class);
                    mb2 = dataSnapshot.child("mb2").getValue(String.class);
                    bb1 = dataSnapshot.child("bb1").getValue(String.class);
                    bb2 = dataSnapshot.child("bb2").getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        people.setState(hScore, life, saved);
    }

    public void getSavedData(int level) {
        switch (level){
            case 1:
            case 4:
                people.savedGame(pix_1, b1, b2, b3, b4); break;
            case 2:
                people.savedGame(pix_1, pix_2, lb1, lb2, lb3, rb1, rb2, rb3); break;
            case 3:
                people.savedGame(pix_1, pix_2, pix_3, tb1, tb2, mb1, mb2, bb1, bb2); break;
        }

        Log.i("Afo udoha idad", "rand is " + String.valueOf(pix_1) + ", and b2 is " + b2);
    }

    public void saveState(int index, int hScore, int life, int saved, int pix_1, String b1, String b2, String b3, String b4){
        String[] category = {"1Gent", "2Ladies", "3Both", "4Comics", "5Places", "6Fmaous"};
        DatabaseReference mRef = mDatabase.child(category[--index]);
        HashMap<String, Object> dataValues =  new HashMap<>();
        dataValues.put("highest_score", hScore);
        dataValues.put("life", life);
        dataValues.put("saved", saved);
        dataValues.put("pix_1", pix_1);
        dataValues.put("b1", b1);
        dataValues.put("b2", b2);
        dataValues.put("b3", b3);
        dataValues.put("b4", b4);
        mRef.updateChildren(dataValues);
    }

    public void saveState(int index, int hScore, int life, int saved, int pix_1, int pix_2, String lb1, String lb2, String lb3,
                          String rb1, String rb2, String rb3){
        String[] category = {"1Gent", "2Ladies", "3Both", "4Comics", "5Places", "6Fmaous"};
        DatabaseReference mRef = mDatabase.child(category[--index]);
        HashMap<String, Object> dataValues =  new HashMap<>();
        dataValues.put("highest_score", hScore);
        dataValues.put("life", life);
        dataValues.put("saved", saved);
        dataValues.put("pix_1", pix_1);
        dataValues.put("pix_2", pix_2);
        dataValues.put("lb1", lb1);
        dataValues.put("lb2", lb2);
        dataValues.put("lb3", lb3);
        dataValues.put("rb1", rb1);
        dataValues.put("rb2", rb2);
        dataValues.put("rb3", rb3);
        mRef.updateChildren(dataValues);
    }

    public void saveState(int index, int hScore, int life, int saved, int pix_1, int pix_2, int pix_3, String tb1,
                          String tb2, String mb1, String mb2, String bb1, String bb2){
        String[] category = {"1Gent", "2Ladies", "3Both", "4Comics", "5Places", "6Fmaous"};
        DatabaseReference mRef = mDatabase.child(category[--index]);
        HashMap<String, Object> dataValues =  new HashMap<>();
        dataValues.put("highest_score", hScore);
        dataValues.put("life", life);
        dataValues.put("saved", saved);
        dataValues.put("pix_1", pix_1);
        dataValues.put("pix_2", pix_2);
        dataValues.put("pix_3", pix_3);
        dataValues.put("tb1", tb1);
        dataValues.put("tb2", tb2);
        dataValues.put("mb1", mb1);
        dataValues.put("mb2", mb2);
        dataValues.put("bb1", bb1);
        dataValues.put("bb2", bb2);
        mRef.updateChildren(dataValues);
    }
}
