package com.example.simpleapiquiz.ui;

import android.app.Activity;
import android.content.SharedPreferences;

public class Pref {
    private SharedPreferences sharedPreferences;

    public Pref(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score)
    {
       int currentscore=score;

       int lastscore=sharedPreferences.getInt("high_score",0);

       if (currentscore>lastscore)
       {
           sharedPreferences.edit().putInt("high_score",currentscore).apply();
       }
    }

    public int  getHighestScore()
    {
        return sharedPreferences.getInt("high_score",0);
    }

    public void setcurrentState(int index)
    {
        int currentIndex=index;
        sharedPreferences.edit().putInt("set_index",index).apply();
    }

    public int getIndex()
    {
        return sharedPreferences.getInt("set_index",0);
    }


}
