/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 2. 17
 * Time: 오후 3:37
 * To change this template use File | Settings | File Templates.
 */
public class TradeProcessor {

    public static void main(String args[]){
        if(args.length < 1){
            System.out.println(" << WARNING : Please input file location. >>");
            System.exit(-1);
        }else if(args.length > 1){
            System.out.println(" << WARNING : Cannot use more than 2 files. Please input only one file location. >>");
            System.exit(-1);
        }
        try{
            TradeIterator tradeIterator = new TradeIterator(args[0]);
            while(tradeIterator.hasNext()){
                Trade trade = tradeIterator.next();
                System.out.println(trade.toString());
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
