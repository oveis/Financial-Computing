package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.MessageIterator;
import edu.nyu.cims.cims.compfin09.hw2.interfaces.IMessage;

import java.util.Iterator;

/**
 * Silent Runner
 * To test this simulator and trader system.
 * Create simulator and traders, and then simulate them.
 * Not Print anythings of the result of the transaction.
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class SilentRunner {
    final static boolean VISIBLE = true;
    final static boolean UNVISIBLE = false;

    public static void main(String[] args){

        // Transaction Data to replay.
        Iterator<IMessage> iterator= MessageIterator.getMessageIterator();

        // Simulator
        Simulator simulator = new Simulator(UNVISIBLE);

        // Trader_silent Add
        Trader trader = new Trader(simulator, "Trader_silent", UNVISIBLE);
        trader.runReplay(iterator);

    }
}
