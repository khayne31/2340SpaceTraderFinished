package com.example.m5_projectsetupuserstoriesandconfiguration.entity;

import android.util.Log;

import java.util.Arrays;

public class Planet {
    private int[] coords;
    private String name;
    private Tech t_lvl;

    public Planet(int[] coordinates, String planetName, Tech tech_lvl){
        Log.d("UniverseLogCat", "inside planet constructor before coords");
        if(coordinates.length >= 2){
            Log.d("UniverseLogCat", coordinates[0]+", " + coordinates[1]+ ", "
                    + coordinates.length);
            coords = coordinates;
            Log.d("UniverseLogCat", coords+"");
            Log.d("UniverseLogCat", "inside planet constructor inside coords");
        } else{
            Log.d("UniverseLogCat", "inside planet constructor before cdoords");
            coords[1] = 0;
            coords[0] = 0;
        }
        Log.d("UniverseLogCat", "inside planet constructor after coords");
        name = planetName;
        t_lvl = tech_lvl;

    }
    public int[] getCoords() {
        return coords;
    }

    @Override
    public String toString() {
        return " -Planet{" +
                "coords=" + Arrays.toString(coords) +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
