
package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.ILimitOrder;

import java.util.HashMap;

/**
 * Order List
 * This is the list of all orders that have same price.
 * FCFS concept is applied in here.
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class OrderList {
    // The Key of the Order : Order ID
    private HashMap<String, Order> orderHashMap = new HashMap<String, Order>();
    private String firstOrderId;
    private String lastOrderId;

    /**
     * Constructor
     * @param order Order to contain Order List
     */
    public OrderList(Order order){
        ILimitOrder limitOrder = (ILimitOrder)order.getMessage();
        String orderId = limitOrder.getOrderID();
        orderHashMap.put(orderId, order);
        firstOrderId = orderId;
        lastOrderId = orderId;
    }

    /**
     * Method to return first order in order list
     * @return first order
     */
    protected Order getFirstOrder(){
        return orderHashMap.get(firstOrderId);
    }

    /**
     * Method to return order list size
     * @return order list size
     */
    protected int getOrderListSize(){
        return orderHashMap.size();
    }

    /**
     * Method to return certain order in the order list
     * @param orderId Order Id to retrieve
     * @return Order
     */
    protected Order getCertainOrder(String orderId){
        return orderHashMap.get(orderId);
    }

    /**
     * Method to remove first order in order list
     */
    protected void removeFirstOrder(){
        Order firstOrder = getFirstOrder();
        String preFirstOrderId = firstOrderId;
        if(firstOrder.getNextOrderId() != null){
            firstOrderId = firstOrder.getNextOrderId();
        }                
        orderHashMap.remove(preFirstOrderId);
    }

    /**
     * Method to remove certain order in order list
     * @param orderId Order ID to remove
     * @return the result of this process. True : success / False : failed
     */
    protected boolean removeCertainOrder(String orderId){
        boolean isSuccess = false;
        if(orderHashMap.remove(orderId) != null)
            isSuccess = true;
        return isSuccess;
    }

    /**
     * Method to set first order in the order list
     * @param firstOrder order to set into the first position of order list
     */
    protected void putFirstOrder(Order firstOrder){
        orderHashMap.put(firstOrderId, firstOrder);
    }

    /**
     * Method to set certain order in the rear of the order list
     * Connect with the previous last order with this new last order.
     * @param inputOrder Order to put the order list
     */
    protected void putOrder(Order inputOrder){
        ILimitOrder inputLimitOrder = (ILimitOrder)inputOrder.getMessage();
        String inputOrderId = inputLimitOrder.getOrderID();
        Order lastOrder = orderHashMap.get(lastOrderId);
        lastOrder.setNextOrderId(inputOrderId);
        lastOrderId = inputOrderId;
        orderHashMap.put(inputOrderId, inputOrder);
    }

    /**
     * Method to get size of the order list
     * @return size of the order list
     */
    protected int size(){
        return orderHashMap.size();
    }

    /**
     * Method to print the order list's information
     * @return String of this order list's information
     */
    @Override
    public String toString(){
        Order order = orderHashMap.get(firstOrderId);
        ILimitOrder limitOrder = (ILimitOrder)order.getMessage();
        double qtySum = limitOrder.getQuantity();
        
        String nextOrderId;
        while((nextOrderId = order.getNextOrderId()) != null){
            order = orderHashMap.get(nextOrderId);
            limitOrder = (ILimitOrder)order.getMessage();
            qtySum += limitOrder.getQuantity();
        }
        if(qtySum < 0)
            qtySum = -qtySum;
        return limitOrder.getLimitPrice() + " x " + (int)qtySum;
    }

}
