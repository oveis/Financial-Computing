package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.ILimitOrder;

/**
 * Limit Order
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class LimitOrder implements ILimitOrder {
    private String orderId;
    private double qty;
    private double limit;
    private String symbol;

    /**
     * Constructor
     * @param orderId Order ID
     * @param qty Quantity
     * @param limit Limit Price
     * @param symbol Symbol
     */
    public LimitOrder(String orderId, double qty, double limit, String symbol){
        this.orderId = orderId;
        this.qty = qty;
        this.limit = limit;
        this.symbol = symbol;
    }

    /**
     * Method to return Limit Price
     * @return Limit Price
     */
    public double getLimitPrice(){
        return limit;
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
