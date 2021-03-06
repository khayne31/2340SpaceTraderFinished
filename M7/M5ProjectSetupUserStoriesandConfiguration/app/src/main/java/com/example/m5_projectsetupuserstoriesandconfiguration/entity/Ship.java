package com.example.m5_projectsetupuserstoriesandconfiguration.entity;

import android.util.Log;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
/**
 * the ship class
 */
public class Ship implements Serializable {
    public enum shipType {
        //(Name, ID, cargo, range, hull, crew, wp, gad, sh, fuel)
        FL("Flea", "FL", 7, 20, 5, 0, 0, 0, 0, 500),
        // cargoSize not given (on wiki) supposed to be "few"
        GN("Gnat", "GN", 15, 14, 10, 0, 1, 1, 0, 700),
        FI("Firefly", "FI", 20, 8, 25, 0, 1, 1, 1, 1000),
        MO("Mosquito", "MO", 15, 6, 30, 0, 2, 1, 1, 2000),
        BB("Bumblebee", "BB", 20, 7, 25, 1, 1, 2, 2, 3000),
        BE("Beetle", "BE", 50, 14, 5, 3, 0, 1, 1, 4000),
        HO("Hornet", "HO", 20, 16, 10, 2, 3, 1, 2, 5000),
        GR("Grasshopper", "GR", 30, 7, 35, 3, 2, 3, 2, 10000),
        TE("Termite", "TE", 60, 6, 50, 3, 1, 2, 3, 15000),
        WA("Wasp", "WA", 35, 7, 50, 3, 3, 2, 2, 20000);

        private final String type;
        private final String id;
        private final int cargoSize;
        private final int range;
        private final int hullStrength;
        private final int crewSize;
        private final int wpSlots;
        private final int gadSlots;
        private final int shSlots;
        private final int maxfuel;

        // need hullStrength, crewSize, wpSlots, gadSlots, shSlots

        shipType(String sType, String sId, int cSize, int r, int hull, int crew, int wp, int gad,
                 int sh, int max) {
            type = sType;
            id = sId;
            cargoSize = cSize;
            range = r;
            hullStrength = hull;
            crewSize = crew;
            wpSlots = wp;
            gadSlots = gad;
            shSlots = sh;
            maxfuel = max;
        }

        /**
         * getter for range variable
         * @return returns the range variable
         */
        int getRange() {
            return range;
        }

        /**
         * getter for maxfuel variable
         * @return returns the maxfuel variable
         */
        public int getMaxfuel() {return maxfuel;}

        /**
         * getter for cargoSize variable
         * @return returns the cargoSize variable
         */
        public int getMaxCargo() {return cargoSize;}
    }

//    private enum weaponType {
//        PL("Pulse Lasers", "PL", 1),
//        BL("Beam Lasers", "BL", 5),
//        ML("Military Lasers", "ML", 10);
//
//        private final String type;
//        private final String id;
//        private final int dmg;
//
//        weaponType(String wType, String wID, int wDMG) {
//            type = wType;
//            id = wID;
//            dmg = wDMG;
//        }
//    }
//
//    private enum shieldType {
//        ES("Energy Shields", "ES", 10, 0),
//        RS("Reflective Shields", "RS", 20, 5);
//
//        private final String type;
//        private final String id;
//        private final int sp;                   // sp = shield power
//        private final int dmg;
//
//        shieldType(String sType, String sID, int sPower, int sDMG) {
//            type = sType;
//            id = sID;
//            sp = sPower;
//            dmg = sDMG;
//        }
//    }
//
//    private enum gadgetType {
//        CB("Extra Cargo Bays (5)", "CB"),
//        NS("Navigation System", "NS"),
//        AS("Auto-Repair System", "AS"),
//        TS("Targeting System", "TS"),
//        CD("Cloaking Device", "CD");
//
//        private final String type;
//        private final String id;
//
//        gadgetType(String gType, String gID) {
//            type = gType;
//            id = gID;
//        }
//
//
//    }

    private shipType type;
    private int fuel;
    private int hp;
    private int cargo;
    private final Hashtable<GoodType, Integer> goodList;
    private List<Item> itemList;
    private int range;
    private  int maxfuel;
    private final int maxhp;


    /**
     * getter for hp variable
     * @return returns the hp variable
     */
    public int getHp() {
        return hp;
    }

    /**
     * constructor for the Ship class
     * @param type the type of the ship
     * @param hp the amount of initial health for the ship
     * @param  cargo size of the cargo
     */
    public Ship(shipType type, int hp, int cargo) {
        this.type = type;
        this.fuel = this.type.maxfuel;
        this.hp = hp;
        maxhp = hp;
        this.cargo = cargo;
        maxfuel = type.maxfuel;
        goodList = new Hashtable<>();
        this.range = this.type.getRange();
        initializeGoods();
    }


    /**
     * getter for cargo variable
     * @return returns the cargo variable
     */
    public int getCurrCargoSize() { return cargo; }



    private void initializeGoods(){
        goodList.put(GoodType.values()[0], 0);
        goodList.put(GoodType.values()[1], 0);
        goodList.put(GoodType.values()[2], 0);
        goodList.put(GoodType.values()[3], 0);
        goodList.put(GoodType.values()[4], 0);
        goodList.put(GoodType.values()[5], 0);
        goodList.put(GoodType.values()[6], 0);
        goodList.put(GoodType.values()[7], 0);
        goodList.put(GoodType.values()[8], 0);
        goodList.put(GoodType.values()[9], 0);
    }

    /**
     * method which adds a specified type and number of a good
     * @param g represents the good to be added to the cargo
     * @param numberOfGoods represents the number of good to be added to the cargo
     * @return returns a boolean that specifies if the addition was successful or not
     */
    public boolean addGood(GoodType g, int numberOfGoods){
        if((numberOfGoods >= 0) && ((numberOfGoods + cargo) <= type.cargoSize)){
            goodList.put(g, goodList.get(g) + numberOfGoods);
            cargo += numberOfGoods;
            return true;
        }
        return false;

    }
    /**
     * method which sells a specified type and number of a good
     * @param g represents the good to be sold
     * @param numberOfGoods represents the number of good to be sold
     * @return returns a boolean that specifies if the sell was successful or not
     */
    public  boolean sellGood(GoodType g, int numberOfGoods){
        int currentNumber = goodList.get(g);
        if((currentNumber - numberOfGoods) >= 0){
            goodList.put(g, currentNumber - numberOfGoods);
            cargo -= numberOfGoods;
            return true;
        }
        return false;
    }

    /**
     * getter for goodList variable
     * @return returns the goodList variable
     */
    public Hashtable<GoodType, Integer> getGoodList() {
        return goodList;
    }

    /**
     * getter for type variable
     * @return returns the type variable
     */
    public shipType getType() {
        return type;
    }

    /*
    sets the fuel to the amount specified if less than maximum capacity
     */
    /**
     * setter for fuel
     * @param  fuel int representing the amount of fuel to set the fuel to
     */
    public void setFuel(int fuel) {
        if (fuel < this.maxfuel) {
            this.fuel = fuel;
        }
    }

    /**
     * getter for fuel variable
     * @return returns the fuel variable
     */
    public int getFuel() {
        return fuel;
    }

    /**
     * getter for range variable
     * @return returns the range variable
     */
    public int getRange() {
        return range;
    }

    /**
     * setter for range
     * @param  range int representing the range of the ship
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * setter for cargo
     * @param  cargo int representing the amount of fuel to be gained by the ship
     */
    public void setCargo(int cargo) {this.cargo = cargo;}

    /**
     * method for losing hp
     * @param  hp int representing the amount of hp to be lost by the ship
     */
    public void loseHp(int hp) {
        if((this.hp - hp) <= 0){
            //TODO: do something when hp hits zero
            this.hp = 0;

        } else{
            this.hp -= hp;
        }
    }
    /**
     * methond for upgrading the ship
     */
    public void upgradeShip(){
        int i;
        shipType[] temp =  shipType.values();
        for(i = 0; i < (temp.length - 1); i++){
            if(type.equals(temp[i])){
                break;
            }
        }
        Log.d("junit1", temp.toString());
        Log.d("junit1", type+"");
        type = temp[i + 1];
        Log.d("junit1", type+"");

    }
    /**
     * method for losing fuel
     * @param  fuel int representing the amount of fuel to be lost by the ship
     */
    public void loseFuel(int fuel) {
        if((this.fuel - fuel) <= 0) {
            // TODO: do something when fuel hits zero
            this.fuel = 0;
            range = 0;
        } else {
            this.fuel -= fuel;
        }
    }

    public int getMaxfuel() {
        return maxfuel;
    }

    public int getMaxhp() {
        return maxhp;
    }
}
