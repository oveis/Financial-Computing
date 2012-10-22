package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.IMessage;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Trader
 * Trader is observer to observe or replay the Simulator.
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class Trader implements Observer{
    private String traderId;
    private boolean isPrint;
    private Simulator simulator;

    /**
     * Constructor
     * @param simulator Simulator ID
     * @param traderId Trader ID
     * @param isPrint whether print or not
     */
    public Trader(Simulator simulator, String traderId, boolean isPrint){
        this.simulator = simulator;
        this.traderId = traderId;
        this.isPrint = isPrint;
    }

    /**
     * Method to update status by the simulator
     * @param o Simulator ID
     * @param arg String to print the update
     */
    @Override
    public void update(Observable o, Object arg){
        System.out.println((String)arg);
    }

    /**
     * Method to replay with saved transaction data
     * @param iterator transaction data to replay
     */
    public void runReplay(Iterator<IMessage> iterator){
        connection();
        while(iterator.hasNext()){
            IMessage msg = iterator.next();
            simulator.trades(this, msg);
        }
        disconnection();
    }

    /**
     * Method to trade Limit Order
     * @param orderId Order ID
     * @param qty Quantity
     * @param limit Limit Price
     * @param symbol Symbol
     */
    public void tradeLimitOrder(String orderId, double qty, double limit, String symbol){
        simulator.trades(this, new LimitOrder(orderId, qty, limit, symbol));
    }

    /**
     * Method to trade Market Order
     * @param orderId Order ID
     * @param qty Quantity
     * @param symbol Symbol
     */
    public void tradeMarketOrder(String orderId, double qty, String symbol){
        simulator.trades(this, new MarketOrder(orderId, qty, symbol));
    }

    /**
     * Method to trade Cancel / Replace Order
     * @param orderId Order ID
     * @param qty New Quantity
     * @param limit New Limit
     * @param symbol Symbol
     */
    public void tradeCxRxOrder(String orderId, double qty, double limit, String symbol){
        simulator.trades(this, new CxRxOrder(orderId, qty, limit, symbol));
    }

    /**
     * Method to register as observer to the simulator
     */
    public void connection(){
        simulator.addObserver(this);
    }

    /**
     * Method to disconnect as observer of the simulator
     */
    public void disconnection(){
        simulator.deleteObserver(this);
    }

    /**
     * Method to return Trader ID
     * @return Trader ID
     */
    public String getTraderId(){
        return traderId;
    }

    /**
     * Method to return whether print or not
     * @return whether print or not
     */
    public boolean getIsPrint(){
        return isPrint;
    }


}

