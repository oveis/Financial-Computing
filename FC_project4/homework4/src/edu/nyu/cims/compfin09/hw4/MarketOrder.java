package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.IMarketOrder;

/**
 * Market Order
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class MarketOrder implements IMarketOrder{
    private String orderId;
    private double qty;
    private String symbol;

    /**
     * Constructor
     * @param orderId Order ID
     * @param qty Quantity
     * @param symbol Symbol
     */
    public MarketOrder(String orderId, double qty, String symbol){
        this.orderId = orderId;
        this.qty = qty;
        this.symbol = symbol;
    }

    /**
     * Method to return Order ID
     * @return Order ID
     */
    public String getOrderID(){
        return orderId;
    }

    /**
     * Method to return Quantity
     * @return Quantity
     */
    public double getQuantity(){
        return qty;
    }

    /**
     * Method to return Symbol
     * @return Symbol
     */
    public String getSymbol(){
        return symbol;
    }
}
