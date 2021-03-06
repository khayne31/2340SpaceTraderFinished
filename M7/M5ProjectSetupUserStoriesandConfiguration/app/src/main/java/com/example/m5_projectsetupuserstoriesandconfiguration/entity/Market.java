package com.example.m5_projectsetupuserstoriesandconfiguration.entity;

import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class used to facilitate transactions between a Player and a Planet
 */
public class Market implements Serializable {
    private final int marketSize;
    //first integer is number of goods second integer is price of goods
    private final List<Item> itemSellList; // item {type, name, quantity, price}
    private final Planet planet;
    private int credits;
    private final int minCredits = 100000;
    private final double bigOneHundred = 100.0;
    private final double onePointFive = 1.5;
    private static  final int MIN_NUMBER_CREDITS = 100000;
    private static  final double BIG100 = 100.0;
    private static  final double ONE_POINT_FIVE = 1.5;


    private final Random random;

    /**
     * A constructor method for the planet
     * @param planet The planet whose market is being created
     */
    public Market(Planet planet){
        itemSellList = new ArrayList<>();
        random = new Random();
        int MAX_MARKET_SIZE = 100;
        marketSize = random.nextInt(MAX_MARKET_SIZE);
        this.planet = planet;
        //Get rid of this, market people don't need to have money

        credits = random.nextInt(MIN_NUMBER_CREDITS) + MIN_NUMBER_CREDITS;
        initializeMarketInventory();
        //Log.v("Test", "price: "+ goodList.get(GoodType.values()[0])[1]);
    }

    /**
     * A method used to create the inventory (itemSellList) for the market instance
     */
    private void initializeMarketInventory(){
        for(GoodType g : GoodType.values()) {

            //if the planet has the required tech level to sell a good, add it to the market
            if(planet.getT_lvl() >= g.getMtlp()) {
                int quant = random.nextInt(marketSize +1);
                Log.d("MarketPrice", "Checkpoint-2");
                //int price = generateMarketPrice(g);
                Item i = new Item(g, g.getName(), quant, generateMarketPrice(g));
                itemSellList.add(i);
            } else {//else set the number of the good to zero in the item;
                Item i = new Item(g, g.getName(), 0, 0);
                itemSellList.add(i);
            }

        }
    }

    /**
     * A method used to generate prices for a GoodType
     * @param gt The GoodType to generate a price for
     * @return The generated price
     */
    private int generateMarketPrice(GoodType gt){
        //may need to refine the event multiplier if its too difficult to put in UI
        int price = gt.getBasePrice();                         // set start price to be base price
        // this val will change according to multiplier
        double multiplier = 1;
        // starts as 1, as if no effective events
        // won't affect price

        //private final int QUANTITY_INDEX = 0;
        //private final int PRICE_INDEX = 1;
        double variance = (gt.getVar()/ BIG100) * gt.getBasePrice();
        // the variance of price from base price (always
        // applied, kind of like a item tax)

        double techBal = ((planet.getT_lvl() - gt.getMtlp()) / BIG100) * gt.getBasePrice();
        // re-balance prices based on diff between planet tech and item tech production lvl
        // treat the diff as a percentage increase on base price
        // we don't have to worry about this being negative bc for the planet to sell the good
        // planet tech >= item tech so planet tech - item tech >= 0
        RadicalEvent planetEvent = planet.getEvent();
        if( planetEvent.equals(gt.getIe()) ) {
            // RadicalEvents match --> increase prices
            multiplier = multiplier* ONE_POINT_FIVE;
        }
//        if( planet.getResources().getResourceName().equals(gt.getCr().getResourceName()) ) {
// cheap resource condition
//             multiplier = multiplier * .5;
//        }
//        if( planet.getResources().getResourceName().equals(gt.getEr().getResourceName()) ) {
// expensive resource condition
//             multiplier = multiplier * 2;
//        }

        price = (int) ((price * multiplier) + variance + techBal);
        // use the price multiplier to accommodate event effects
        // add variance to price
        // add tech price re-balance
        return price;   // price is an int so we don't have to worry about awkward
        // subtraction and partial credits

//        return  (int)(gt.getBasePrice() * ((planet.getEvent()).equals(gt.getEr()) ?
// gt.getBasePrice() : 1)
//                * (planet.getResources().equals(gt.getEr()) ? 2 : 1)
//                *  (planet.getResources().equals(gt.getCr()) ? .5 : 1)
//                + (Tech.values().length - gt.getMtlp())/(Tech.values().length + 0.0)
//                + gt.getVar()/100.0 * gt.getBasePrice());
    }

    /**
     * A method where the player purchases items from the market
     * @param p The player who will be making the purchase
     * @param currentGood The GoodType the player is purchasing
     * @param numberOfGood The amount of the good the player is purchasing
     * @return A string to display as a toast in the UI about the status of the purchase
     */
    public String tradeBuy(Player p, GoodType currentGood, int numberOfGood){
        String returnString;
        int position = getTradeGoodPosition(currentGood);
        if(p.getCredits() <= 0){
            returnString = "You have no money to spend";
        } else if(itemSellList.get(position).getQuantity() == 0){
            returnString = "There are no items left to trade for";
        } else if(itemSellList.get(position).getQuantity() < numberOfGood){
            returnString = "There aren't enough of that good left to trade for";
        }  else if(p.getCredits() < (itemSellList.get(position).getPrice() * numberOfGood)){
            returnString = "You don't have enough credits to buy these items";
        } else if ((p.getMyShip().getCurrCargoSize() + numberOfGood) >
                p.getMyShip().getType().getMaxCargo()) {
            returnString = "You don't have enough cargo space left to store that good";
        }else{

            Item updateItem = itemSellList.get(position);

            if (p.getMyShip().addGood(updateItem.getType(), numberOfGood)){
                updateItem.sellQuantity(numberOfGood);
                itemSellList.set(position, updateItem);
                returnString = "The purchase was successful!";
                int moneyTraded =  numberOfGood * updateItem.getPrice();
                p.setCredits(p.getCredits() - moneyTraded);
                //deprecated
                credits += moneyTraded;
            } else{
                returnString = "The purchase failed";
            }

        }
        return returnString;
    }

    /**
     * A method to help the player to sell items to the market
     * @param p The player who will be selling items
     * @param currentGood The current good being sold
     * @param numberOfGood The amount of that good being sold
     * @return A string to display as a toast in the UI about the status of the sale
     */
    public String tradeSell(Player p, GoodType currentGood, int numberOfGood){

        //deprecated
        int position = getTradeGoodPosition(currentGood);

        String returnString;
        returnString = "The purchase failed!";
        if(p.getMyShip().getCurrCargoSize() == 0){
            returnString = "You have no items to sell";
        } else if(p.getMyShip().getGoodList().get(currentGood) == 0){
            returnString = "You have no items of that type to sell";
        } else{
            if (p.getMyShip().sellGood(currentGood, numberOfGood)){
                returnString = "The sale was successful!";
                int moneyTraded =  numberOfGood * getTradeGoodPrice(currentGood);
                credits -= moneyTraded;
                Item updateItem = itemSellList.get(position);
                int currentQuantity = updateItem.getQuantity();
                updateItem.setQuantity(currentQuantity + numberOfGood);
                itemSellList.set(position, updateItem);
                p.setCredits(p.getCredits() + moneyTraded);
            }
        }
        return returnString;
    }

    // get list of items that are available in the market (aka sell quantity > 0)

    // get list of items that player has
    //deprecate

    /**
     * The list of items that the player can sell to the market
     * @param p The player who is selling to the market
     * @return A list of Items that the player can sell to the market
     */
    private List<Item> getBuyItems(Player p) {
        List<Item> itemBuyList = new ArrayList<>();

        for(GoodType g : p.getMyShip().getGoodList().keySet()) {
            int quant = p.getMyShip().getGoodList().get(g); // gets the quantity owned
            //This is the problem line, below (now fixed)
            int price = getTradeGoodPrice(g);
            Item i = new Item(g, g.getName(), quant, price);
            itemBuyList.add(i);
        }
        return itemBuyList;
    }

    /**
     * A method to return the current quantity for a particular trade good in the market
     * @param currentGood The GoodType who's quantity is being requested
     * @return The quantity of that GoodType
     */
    public int getTradeGoodQuantity(GoodType currentGood) {
        for (Item item : itemSellList) {
            if (item.getType() == currentGood) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    /**
     * A method to return the current price for a particular trade good in the market
     * @param currentGood The GoodType who's price is being requested
     * @return The price of that GoodType
     */
    public int getTradeGoodPrice(GoodType currentGood) {
        for (Item item : itemSellList) {
            if (item.getType() == currentGood) {
                return item.getPrice();
            }
        }
        return 0;
    }

    /**
     * A method to return the current position for a particular trade good
     * in the market itemSellList
     * @param currentGood The GoodType who's position is being requested
     * @return The position of that GoodType
     */
    private int getTradeGoodPosition(GoodType currentGood) {
        int positionCount = 0;
        for (Item item : itemSellList) {
            if (item.getType() == currentGood) {
                return positionCount;
            }
            positionCount++;
        }
        return positionCount;


    }

    /**
     * A method to update the itemSellList once a purchase/sale has been made
     * @param good The GoodType being updated in an Item
     * @param newNum The new number of goods associated with that Item
     */
    public void updateSellList(GoodType good, int newNum) {
        int position = getTradeGoodPosition(good);
        Item updateItem = itemSellList.get(getTradeGoodPosition(good));
        updateItem.setQuantity(newNum);
        itemSellList.set(position, updateItem);
    }

}