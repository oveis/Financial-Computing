import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 2. 17
 * Time: 오후 3:36
 * To change this template use File | Settings | File Templates.
 */
public class TradeIterator {

    private BufferedReader file = null;

    // Constructor
    public TradeIterator(String tradeData) throws FileNotFoundException{
        file = new BufferedReader(new FileReader(tradeData));
    }

    // Descructor
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        closeDataFile();
    }

    public boolean hasNext() throws IOException{
        try{
            if(file != null && file.ready())
                return true;
            else
                closeDataFile();
        }catch(IOException e){
            closeDataFile();
            throw e;
        }
        return false;
    }

    public Trade next() throws Exception{
        if(this.hasNext()){
                try{
                    String data = file.readLine();
                    //return new Trade(str.split(","));
                    return validateTrade(data);
                } catch(IOException e){
                    throw e;
                }
        } else{
            throw new Exception("<< ERROR : There are no more trade data. >>");
        }
    }

    // Purpose : Check whether the trade data is validate or not.
    private Trade validateTrade(String data){
        String err = "";
        Trade trade;
        String[] fields = data.split(",");

        // Check validate of trade data
        if(!(err = TradeChecker.tradeCheckFacade(data)).equals("")){             // Check Trade data
            trade = new Trade();
            trade.setError(err);
        }else                                                                    // Trade data is clean
            trade = new Trade(fields);

        return trade;
    }

    // Purpose : Close BufferedReader
    private void closeDataFile(){
        if(file != null){
            try{
                file.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        file = null;
    }
}
