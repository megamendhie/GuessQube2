package com.swiftqube.guessqube.people;

import java.util.HashMap;

/**
 * Created by Mendhie on 3/24/2018
 */

public class UserDetails {
    public  UserDetails(){}

    public  HashMap<String, Object> getUserData(String name){
        String[] category = {"1Gent", "2Ladies", "3Both", "4Comics", "5Places", "6Fmaous"};

        HashMap<String, Object> set = new HashMap<>();
        set.put("score", 0);
        set.put("code", "Not available");

        //HashMap for each category score
        HashMap<String, HashMap> score = new HashMap<>();
        for(int i = 1; i <= category.length; i++){
            score.put(category[i-1], set);
        }

        //HashMap for coins
        String[] names = {"Wizkid", "Davido", "Obasanjo", "LindaIkeji", "Zuma Rock"};
        HashMap<String, Object> coins = new HashMap<>();
        for(int i = 1; i <= names.length; i++){
            coins.put(names[i-1], 1);
        }

        //HashMap for data
        HashMap<String, Object> dataValues =  new HashMap<>();
        dataValues.put("highest_score", 0);
        dataValues.put("current_score", 0);
        dataValues.put("pix_1", 0);
        dataValues.put("pix_2", 0);
        dataValues.put("pix_3", 0);
        dataValues.put("timer", 0);
        dataValues.put("life", 5);
        dataValues.put("saved", 0);
        dataValues.put("b1", "nothing");
        dataValues.put("b2", "nothing");
        dataValues.put("b3", "nothing");
        dataValues.put("b4", "nothing");
        dataValues.put("lb1", "nothing");
        dataValues.put("lb2", "nothing");
        dataValues.put("lb3", "nothing");
        dataValues.put("rb1", "nothing");
        dataValues.put("rb2", "nothing");
        dataValues.put("rb3", "nothing");
        dataValues.put("tb1", "nothing");
        dataValues.put("tb2", "nothing");
        dataValues.put("mb1", "nothing");
        dataValues.put("mb2", "nothing");
        dataValues.put("bb1", "nothing");
        dataValues.put("bb2", "nothing");
        dataValues.put("nname", "nothing");
        dataValues.put("px1", 0);
        dataValues.put("px2", 0);
        dataValues.put("px3", 0);
        dataValues.put("px4", 0);

        HashMap<String, Object> dataset =  new HashMap<>();
        for(int i = 1; i <= category.length; i++){
            dataset.put(category[i-1], dataValues);
        }

        HashMap<String, Object> userdata =  new HashMap<>();
        userdata.put("Name", name);
        userdata.put("Score", score);
        userdata.put("Coins", coins);
        userdata.put("Data", dataset);

        return userdata;
    }
}
