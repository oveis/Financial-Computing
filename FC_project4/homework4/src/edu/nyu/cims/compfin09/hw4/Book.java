package edu.nyu.cims.compfin09.hw4;

import java.util.TreeMap;

/**
 * Simbol Book
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class Book {

    private TreeMap<Double, OrderList> askBook = new TreeMap<Double, OrderList>();   // Key value of Order List : Price
    private TreeMap<Double, OrderList> bidBook = new TreeMap<Double, OrderList>();  // Key value of Order List : Price
    private double askPrice = 1000000000;
    private double bidPrice = 0;

    /**
     * Method to return Ask Book
     * @return Ask Book
     */
    protected TreeMap<Double, OrderList> getAskBook(){
        return askBook;
    }

    /**
     * Method to return Bid Book
     * @return Bid Book
     */
    protected TreeMap<Double, OrderList> getBidBook(){
        return bidBook;
    }

    /**
     * Method to return Order list of ask price
     * @return Order list of ask price
     */
    protected OrderList getAskPriceOrderList(){
        return askBook.get(askPrice);
    }

    /**
     * Method to return Order list of bid price
     * @return Order list of bid price
     */
    protected OrderList getBidPriceOrderList(){
        return bidBook.get(bidPrice);
    }

    /**
     * Method to return Ask price
     * @return Ask price
     */
    protected double getAskPrice(){
        return askPrice;
    }

    /**
     * Method to return Bid price
     * @return Bid price
     */
    protected double getBidPrice(){
        return bidPrice;
    }

    /**
     * Method to set Ask price
     * @param askPrice Ask price to set
     */
    protected void setAskPrice(double askPrice){
        this.askPrice = askPrice;
    }

    /**
     * Method to set Bid price
     * @param bidPrice Bid price to set
     */
    protected void setBidPrice(double bidPrice){
        this.bidPrice = bidPrice;
    }

    /**
     * Method to remove the order list of ask price
     * Set next price as new ask price
     */
    protected void removeAskOrderList(){
        // remove order list which has the ask price
        askBook.remove(askPrice);
        // reset the ask price with next ask price
        if(askBook.size()>0)
            askPrice = askBook.firstKey();
        else
            askPrice = 1000000000;
    }

    /**
     * Method to remove the order list of bid price
     * Set next price as new bid price
     */
    protected void removeBidOrderList(){
        // remove order list which has the bid price
        bidBook.remove(bidPrice);
        // reset the bid price with next bid price
        if(bidBook.size()>0)
            bidPrice = bidBook.lastKey();
        else
            bidPrice = 0;
    }

    /**
     * Method to print all information of this book.
     * @return String to print
     */
    protected String printAll(){
        String str="";
        str += "--------------------------------\n";
        str += "( Ask )\n";
        OrderList askOrderList = askBook.get(askPrice);
        if(askOrderList == null)
            str += "NaN\n";
        else
            str += askOrderList + "\n";
        
        str += "\n( Bid )\n";
        OrderList bidOrderList = bidBook.get(bidPrice);
        if(bidOrderList == null)
            str += "NaN\n";
        else
            str += bidOrderList + "\n";

        str += "--------------------------------\n\n";
        return str;
    }
}
