package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.ICxRxMessage;

/**
 * CxRx Order
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class CxRxOrder implements ICxRxMessage{
    private String orderId;
    private double qty;
    private double limit;
    private String symbol;

    /**
     * Constructor
     * @param orderId Order Id
     * @param qty New Quantity
     * @param limit New Limit price
     * @param symbol Symbol
     */
    public CxRxOrder(String orderId, double qty, double limit, String symbol){
        this.orderId = orderId;
        this.qty = qty;
        this.limit = limit;
        this.symbol = symbol;
    }

    /**
     * Method to return Symbol
     * @return Symbol
     */
    public String getSymbol(){
        return symbol;
    }

    /**
     * Method to return New Quantity
     * @return Quantity
     */
    public double getNewQuantity(){
        return qty;
    }

    /**
     * Method to return Order ID
     * @return Order ID
     */
    public String getOrderID(){
        return orderId;
    }

    /**
     * Method to return New Limit Price
     * If the new limit price is 0, it means to cancel previous order with 'OrderID'.
     * @return New Limit Price
     */
    public double getNewLimitPrice(){
        return limit;
    }
}
