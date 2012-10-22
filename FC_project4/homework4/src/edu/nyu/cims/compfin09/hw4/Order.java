package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.IMessage;

/**
 * Order
 * Order is wrapper of any kind of oder.
 * This contain next order ID for quick transaction processing
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class Order {
    private IMessage message;
    private int orderType;
    private String nextOrderId;

    /**
     * Constructor
     * @param message Order message
     * @param orderType Type of oder (Market / Limit / CxRx)
     */
    public Order(IMessage message, int orderType){
        this.message = message;
        this.orderType = orderType;
    }

    /**
     * Method to return Order Type
     * @return Order Type
     */
    protected int getOrderType(){
        return orderType;
    }

    /**
     * Method to return Order Message
     * @return Order Message
     */
    protected IMessage getMessage(){
        return message;
    }

    /**
     * Method to return Next Order ID
     * @return Next Order ID
     */
    protected String getNextOrderId(){
        return nextOrderId;
    }

    /**
     * Method to set Next Order ID
     * @param nextOrderId Next Order ID
     */
    protected void setNextOrderId(String nextOrderId){
        this.nextOrderId = nextOrderId;
    }

    /**
     * Method to set Order Message
     * This method is used when first order is transacted, but quantity of this order is remained.
     * @param message Order Message
     */
    protected void setMessage(IMessage message){
        this.message = message;
    }
}
