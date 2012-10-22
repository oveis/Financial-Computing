package edu.nyu.cims.compfin09.hw4;

import edu.nyu.cims.cims.compfin09.hw2.interfaces.ICxRxMessage;
import edu.nyu.cims.cims.compfin09.hw2.interfaces.ILimitOrder;
import edu.nyu.cims.cims.compfin09.hw2.interfaces.IMarketOrder;
import edu.nyu.cims.cims.compfin09.hw2.interfaces.IMessage;

import java.util.*;

/**
 * Exchanging Simulator
 * @author Jinil Jang (N10917970)
 * @version v1.0
 */
public class Simulator extends Observable{
    private ArrayList<Trader> observerList = new ArrayList<Trader>();
    private final int LIMITORDER = 1;
    private final int MARKETORDER = 2;
    private final int CXRXORDER = 3;
    private final int BUYORDER = 4;
    private final int SELLORDER = 5;
    // The key value of book map : Symbol of share.
    private HashMap<String, Book> bookHashMap = new HashMap<String, Book>();
    private String resultString = "";
    private Trader curTrader;
    private boolean systemVisibility;

    /**
     * Constructor
     * @param systemVisibility Display each result or not. (True : Display, False : Not display)
     */
    public Simulator(boolean systemVisibility){       
        this.systemVisibility = systemVisibility;
    }

    /**
     * Method to trade received order
     * @param trader Trader which send this order
     * @param message Order Message (such as, Market order, Limit order or Cancel/Replace order)
     */
    public void trades(Trader trader, IMessage message){
        curTrader = trader;
        Order order;
        // Get the Message
        // and Input the message into book.
        if(message instanceof ILimitOrder){
            order = new Order(message, LIMITORDER);
            processLimitOrder(order);
        }else if(message instanceof ICxRxMessage){
            order = new Order(message, CXRXORDER);
            processCxRxOrder(order);
        }else if(message instanceof IMarketOrder){
            order = new Order(message, MARKETORDER);
            processMarketOrder(order);
        }
    }

    /**
     * Method to input limit order into Bid book or Ask book
     * @param order Limit order to input
     * @param book Book to save this limit order. That is, the order's symbol book.
     */
    private void inputLimitOrder(Order order, Book book){
        // Check if this price of order is exist in the Ask book
        // It yes, input this order into that price order list
        // It not, make new order list, and then input this.
        ILimitOrder limitOrder = (ILimitOrder)order.getMessage();                 
        double limitPrice = limitOrder.getLimitPrice();
        double quantity = limitOrder.getQuantity();
        TreeMap<Double, OrderList> askBidBook;
        OrderList orderList;
        int transactionType;
        if(quantity > 0){
            transactionType = BUYORDER;
            askBidBook = book.getBidBook();
        }else{
            transactionType = SELLORDER;
            askBidBook = book.getAskBook();
        }

        if((orderList = askBidBook.get(limitPrice)) != null){
            orderList.putOrder(order);
        }else{
            orderList = new OrderList(order);
            askBidBook.put(limitPrice, orderList);
            switch (transactionType){
                case BUYORDER:
                    if(limitPrice > book.getBidPrice())
                        book.setBidPrice(limitPrice);
                    break;
                case SELLORDER:
                    if(limitPrice < book.getAskPrice())
                        book.setAskPrice(limitPrice);
                    break;
            }
        }
    }

    /**
     * Method to check if this order is valid or not.
     * @param order Order to check (The order can be any type, such as Market, Limit or CxRx)
     * @return Validity of this order. If it is an invalid order, return false. Otherwise, return True.
     */
    private boolean preChecking(Order order){
        boolean isValid = true;
        switch(order.getOrderType()){
            case LIMITORDER:
                ILimitOrder limitOrder = (ILimitOrder)order.getMessage();
                if(limitOrder.getOrderID() == null ||
                        limitOrder.getOrderID().equals("") ||
                        limitOrder.getQuantity() == 0 ||
                        limitOrder.getLimitPrice() == 0 ||
                        limitOrder.getSymbol() == null ||
                        limitOrder.getSymbol().equals(""))
                    isValid = false;
                break;
            case MARKETORDER:
                IMarketOrder marketOrder = (IMarketOrder)order.getMessage();
                if(marketOrder.getOrderID() == null ||
                        marketOrder.getOrderID().equals("") ||
                        marketOrder.getQuantity() == 0 ||
                        marketOrder.getSymbol() == null ||
                        marketOrder.getSymbol().equals(""))
                    isValid = false;
                break;
            case CXRXORDER:
                ICxRxMessage cxRxMessage = (ICxRxMessage)order.getMessage();
                if(cxRxMessage.getOrderID() == null ||
                        cxRxMessage.getOrderID().equals("") ||
                        cxRxMessage.getNewQuantity() == 0 ||
                        cxRxMessage.getNewLimitPrice() == 0 ||
                        cxRxMessage.getSymbol() == null ||
                        cxRxMessage.getSymbol().equals(""))
                    isValid = false;
                break;
        }
        if(!isValid && systemVisibility)
            notifyObserver(curTrader, "<< Invalid Order founded!! Skip this Order >>");
        return isValid;
    }

    /**
     * Method to process Buy/Sell transaction
     * (Processing flow)
     *    1) Get first order of the price
     *    2) Transaction
     *    3) If (first order quantity) > (required quantity), input the remain quantity of the first order into the first order position.
     *    4) If (first order quantity) <= (required quantity), remove first order and get the next order to continue transaction.
     *    5) If there are no any more available quantity for transaction, discard the remain quantity of the order.
     * @param order Order to transact (Market order / Limit order)
     */
    private void transactShares(Order order){

        int transactionType;
        double requiredQty = 0d;
        double originRequiredQty;
        String orderId="";
        String symbol="";
        double limitPrice = 0d;
        int orderType;

        switch (orderType = order.getOrderType()){
            case LIMITORDER:
                ILimitOrder limitOrder = (ILimitOrder)order.getMessage();
                requiredQty = limitOrder.getQuantity();
                limitPrice = limitOrder.getLimitPrice();
                orderId = limitOrder.getOrderID();
                symbol = limitOrder.getSymbol();
                orderType = LIMITORDER;
                break;
            case MARKETORDER:
                IMarketOrder marketOrder = (IMarketOrder)order.getMessage();
                requiredQty = marketOrder.getQuantity();
                orderId = marketOrder.getOrderID();
                symbol = marketOrder.getSymbol();
                orderType = MARKETORDER;
                break;
        }
        if(requiredQty > 0){
            transactionType = BUYORDER;
        }else{
            transactionType = SELLORDER;
            requiredQty = - requiredQty;
        }
        originRequiredQty = requiredQty;
        Book book = bookHashMap.get(symbol);
        if(book == null && systemVisibility){
            notifyObserver(curTrader, "There are no '" + symbol + "' books");
            return;
        }
            
        String orderIdList="";
        double totalPrice=0;

        while(requiredQty > 0){
            // Limitation Check.
            if((transactionType == BUYORDER) &&                                         // Buy Order  &&
                    ((orderType==LIMITORDER && limitPrice < book.getAskPrice()) ||      // (Limit order limitation  ||  Market order limitation) 
                     (book.getAskBook().size() == 0))){
                break;
            }
                
            else if((transactionType == SELLORDER) &&                                   // Sell Order  &&
                    ((orderType==LIMITORDER && limitPrice > book.getBidPrice()) ||       // (Limit order limitation  ||  Market order limitation)
                     (book.getBidBook().size() == 0)))
                break;
            
            OrderList orderList = null;
            try{
                switch(transactionType){
                    case BUYORDER:
                        orderList =  book.getAskPriceOrderList();
                        break;
                    case SELLORDER:
                        orderList = book.getBidPriceOrderList();
                        break;
                }
            }catch(NullPointerException e){
                System.out.println("The symbol book has not Bid book or Ask book");
                return;
            }

            Order firstOrder = orderList.getFirstOrder();
            ILimitOrder firstLimitOrder = (ILimitOrder)firstOrder.getMessage();
            double firstLimitOrderQty = Math.abs(firstLimitOrder.getQuantity());
            double firstLimitOrderPrice = firstLimitOrder.getLimitPrice();

            if(firstLimitOrderQty <= requiredQty){
                orderIdList += firstLimitOrder.getOrderID() + " ";
                totalPrice += firstLimitOrderQty * firstLimitOrderPrice;
                requiredQty = requiredQty - firstLimitOrderQty;

                // Remove first order of the order list.
                // If needed, remove the order list of the book.
                orderList.removeFirstOrder();
                if(orderList.getOrderListSize() == 0)
                    switch(transactionType){
                        case BUYORDER:
                            book.removeAskOrderList();
                            break;
                        case SELLORDER:
                            book.removeBidOrderList();
                            break;
                    }   
            }
            else{
                orderIdList += firstLimitOrder.getOrderID() + " ";
                totalPrice += firstLimitOrderQty * firstLimitOrderPrice;

                // Input the remain order quantity on the first order.
                double remainQty = firstLimitOrderQty - requiredQty;
                requiredQty = 0;
                LimitOrder replaceLimitOrder = new LimitOrder(firstLimitOrder.getOrderID(), remainQty, firstLimitOrder.getLimitPrice(), firstLimitOrder.getSymbol());
                firstOrder.setMessage(replaceLimitOrder);
                orderList.putFirstOrder(firstOrder);
            }
        }

        if(systemVisibility){
            resultString += "Order " + orderId + " traded with order " + orderIdList + "\n";
            resultString += "(Transaction) " + (originRequiredQty-requiredQty) + " " + symbol + " @ " + (totalPrice/(originRequiredQty-requiredQty)) + "\n";
        }
    }

    /**
     * Method to cancel previous order with CxRx Message
     * @param cxRxMessage CxRxMessage to Cancel previous order.
     */
    private void removeCertainOrder(ICxRxMessage cxRxMessage){
        String orderId = cxRxMessage.getOrderID();
        String symbol = cxRxMessage.getSymbol();
        double newLimitPrice = cxRxMessage.getNewLimitPrice();
        double newQuantity = cxRxMessage.getNewQuantity();
        int transactionType;
        if(newQuantity > 0)
            transactionType = BUYORDER;
        else
            transactionType = SELLORDER;
        
        Book book = bookHashMap.get(symbol);
        if(book == null && systemVisibility){
            notifyObserver(curTrader, "There are no '" + symbol + "' books");
            return;
        }
        TreeMap<Double, OrderList> bookList = null;
        OrderList orderList;
        try{
            switch(transactionType){
                case BUYORDER:
                    bookList = book.getBidBook();
                    break;
                case SELLORDER:
                    bookList = book.getAskBook();
                    break;
            }
        }catch(NullPointerException e){
            System.out.println("The book has not Bid book or Ask book");
            return;
        }

        orderList = bookList.get(newLimitPrice);
        if(orderList == null && systemVisibility){
            notifyObserver(curTrader, "There are no orders with '" + newLimitPrice + "' prices.");
            return;
        }

        if(systemVisibility){
            if(orderList.removeCertainOrder(orderId))
                resultString += "Order " + orderId + " is canceled.\n";
            else
                notifyObserver(curTrader, "This is wrong message : [ " + orderId + ", " + newQuantity + " , " + newLimitPrice + " , " + symbol + " ]");
        }
    }

    /**
     * Method to replace previous order with CxRx Message
     * (Processing flow)
     *    1) Search and remove the previous order
     *    2) Input new order into the rear of the order list.
     * @param cxRxMessage CxRxMessage to replace previous order.
     */
    private void replaceCertainOrder(ICxRxMessage cxRxMessage){

        // 1) Search the order : The difference between this and removeCertainOrder() is that we don't know the price of the previous Order Id exists.
        String orderId = cxRxMessage.getOrderID();
        String symbol = cxRxMessage.getSymbol();
        double newLimitPrice = cxRxMessage.getNewLimitPrice();
        double newQuantity = cxRxMessage.getNewQuantity();
        int transactionType;
        if(newQuantity > 0)
            transactionType = BUYORDER;
        else
            transactionType = SELLORDER;
        Book book = bookHashMap.get(symbol);
        TreeMap<Double, OrderList> bookList = null;

        switch(transactionType){
            case BUYORDER:
                bookList = book.getBidBook();
                break;
            case SELLORDER:
                bookList = book.getAskBook();
                break;
        }                        

        boolean isSuccess = false;
        Set<Double> keySetOfBookList = bookList.keySet();
        for(Double price : keySetOfBookList){
            OrderList orderList = bookList.get(price);
            // 2)  Delete the order
            if(orderList.getCertainOrder(orderId) != null){
                orderList.removeCertainOrder(orderId);
                isSuccess = true;
                if(orderList.size() == 0)
                    bookList.remove(price);
                break;
            }
        }

        // 3) insert new order
        LimitOrder newLimitOrder = new LimitOrder(orderId, newQuantity, newLimitPrice, symbol);
        Order newOrder = new Order(newLimitOrder, LIMITORDER);
        inputLimitOrder(newOrder, book);

        // Print the result
        if(systemVisibility){
            if(isSuccess)
                resultString += "Order " + orderId + " is replaced.\n";
            else
                notifyObserver(curTrader, "This is wrong message : [ " + orderId + ", " + newQuantity + " , " + newLimitPrice + " , " + symbol + " ]");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //
    //      P R O C E S S     O R D E R S
    //
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to process a limit order
     * @param order Limit order
     */
    protected void processLimitOrder(Order order){
        if(!preChecking(order))
            return;
        printOrder(order);
        ILimitOrder limitOrder = (ILimitOrder)order.getMessage();
        String symbol = limitOrder.getSymbol();
        double limitPrice = limitOrder.getLimitPrice();
        double quantity = limitOrder.getQuantity();
        int transactionType;
        if(quantity > 0)
            transactionType = BUYORDER;
        else
            transactionType = SELLORDER;

        // Check this symbol is exist in the book hash map.
        // If not, create this symbol's book, insert this order, and then done.
        Book book;
        if((book = bookHashMap.get(symbol)) == null){
            book = new Book();
            inputLimitOrder(order, book);
            bookHashMap.put(symbol, book);
            if(systemVisibility)
                printBook(symbol);
            return;
        }

        switch(transactionType){
            // Buy Order
            // 1) If there are already appropriate orders in the list, execute this order.
            // 2) If not, add this order in the list.
            case BUYORDER:
                if(limitPrice >= book.getAskPrice())     // 1) buy
                    transactShares(order);
                else                                    // 2) input list
                    inputLimitOrder(order, book);
                break;
            // Sell Order
            // 1) If there are already appropriate orders in the list, execute this order
            // 2) If not, add this order in the list.
            case SELLORDER:
                if(limitPrice <= book.getBidPrice())     // 1) sell
                    transactShares(order);
                else                                    // 2) input list
                    inputLimitOrder(order, book);
                break;
        }
        if(systemVisibility)
            printBook(symbol);
    }

    /**
     * Method to process Market order
     * @param order Market order
     */
    protected void processMarketOrder(Order order){
        if(!preChecking(order))
            return;
        printOrder(order);
        transactShares(order);
        IMarketOrder marketOrder = (IMarketOrder)order.getMessage();
        String symbol = marketOrder.getSymbol();
        if(systemVisibility)
            printBook(symbol);
    }

    /**
     * Method to process Ccancel/Replace Order
     * @param order Cancel/Replace order
     */
    protected void processCxRxOrder(Order order){
        if(!preChecking(order))
            return;
        printOrder(order);
        ICxRxMessage cxRxMessage = (ICxRxMessage)order.getMessage();
        double newQuantity = cxRxMessage.getNewQuantity();
        String symbol = cxRxMessage.getSymbol();

        // if quantity = 0, just delete the order in the book.
        // else, replace the order in the book with this new order.
        if(newQuantity == 0){
            removeCertainOrder(cxRxMessage);
        }else{
            replaceCertainOrder(cxRxMessage);
        }
        if(systemVisibility)
            printBook(symbol);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //
    //      P R I N T
    //
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to print an order information
     * @param order Any kind of Order (Market / Limit / CxRx)
     */
    private void printOrder(Order order){
        switch (order.getOrderType()){
            case LIMITORDER:
                ILimitOrder limitOrder = (ILimitOrder)order.getMessage();
                if(systemVisibility)
                    resultString += "[Limit Order] " + limitOrder.getOrderID() + " " + limitOrder.getQuantity()
                        + " " + limitOrder.getSymbol() + " @ " + limitOrder.getLimitPrice() + "\n";
                break;
            case CXRXORDER:
                ICxRxMessage cxRxMessage = (ICxRxMessage)order.getMessage();
                if(systemVisibility)
                    resultString += "[Cancel/Replace Order] " + cxRxMessage.getOrderID() + " " + cxRxMessage.getNewQuantity()
                        + " " + cxRxMessage.getSymbol() + " @ " + cxRxMessage.getNewLimitPrice() + "\n";
                break;
            case MARKETORDER:
                IMarketOrder marketOrder = (IMarketOrder)order.getMessage();
                if(systemVisibility)
                    resultString += "[Market Order] " + marketOrder.getOrderID() + " " + marketOrder.getQuantity()
                        + " " + marketOrder.getSymbol() + "\n";
                break;
        }
    }

    /**
     * Method to print current all books information.
     * Print all symbols' books and their Bid / Ask books.
     * @param symbol Symbol to print
     */
    private void printBook(String symbol){
        resultString +="--------------------------------\n";
        resultString +=" [[ " + symbol + " ]] \n";
        Book book = bookHashMap.get(symbol);
        resultString += book.printAll();
    
        notifyObservers();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //
    //      O B S E R V E R     P A T T E R N
    //
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to add an observer. In here, an observer is a Trader.
     * @param trader Trader to observe this simulator or replay saved transactions.
     */
    public void addObserver(Trader trader){
        observerList.add(trader);
        if(trader.getIsPrint())
            trader.update(this, " << " + trader.getTraderId() + " Connected >> ");
    }

    /**
     * Method to delete an observer.
     * @param trader Trader to delete.
     */
    public void deleteObserver(Trader trader){
        int index = observerList.indexOf(trader);
        observerList.remove(index);
        if(trader.getIsPrint())
            trader.update(this, " << " + trader.getTraderId() + " Disconnected >> ");
    }

    /**
     * Method to notify all observers
     * If this simulator is not visible, observers cannot get any notifications.
     * If this simulator is visible, but the each observer don't want to get the notification, the notification is not sent.
     * If this simulator is visible, and the each observer want to get the notification, send it.
     */
    @Override
    public void notifyObservers(){
        if(!systemVisibility)
            return;
        for(Trader trader : observerList){
            if(trader.getIsPrint())
                trader.update(this, resultString);
        }
        resultString = "";
    }

    /**
     * Method to notify certain observer.
     * This notification is some error message by order of certain observer, such as wrong order.
     * @param trader Trader to notify this message
     * @param printStr Message to send.
     */
    public void notifyObserver(Trader trader, String printStr){
        if(trader.getIsPrint())
            trader.update(this, printStr);
    }
}
