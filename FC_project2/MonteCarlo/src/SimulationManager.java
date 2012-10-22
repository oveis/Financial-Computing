import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 9
 * Time: P.M 2:20
 * To change this template use File | Settings | File Templates.
 */

// Purpose : (Constructor) path generator, payout function, desired accuracy
//           (Function) Generates samples and stop
public class SimulationManager {
    private PathGenerator pathGenerator;
    private PayOut payOut;
    private double accuracy = 0.0;
    private double strikePrice = 0.0;
    private double interestRate = 0.0;                      // per 1 month
    private int expiration = 0;                              // unit : months
    private StatisticalCollector statisticalCollector;
    
    //Vector<Path> pathVector = new Vector<Path>();

    public SimulationManager(PathGenerator pathGenerator, PayOut payOut, double accuracy, double strikePrice,
                              double interestRate, int expiration){
        this.pathGenerator = pathGenerator;
        this.payOut = payOut;
        this.accuracy = accuracy;
        this.strikePrice = strikePrice;
        this.interestRate = interestRate;
        this.expiration = expiration;
    }

    public StatisticalCollector generate(){
        boolean isStop = false;
        double sumStockPrice = 0.0;
        double sumStockPrice2 = 0.0;
        double numberOfSample = 0.0;
        double mean = 0.0;
        double varianceStockPrice = 0.0;

        while(!isStop){
            // generate samples
            Path path = pathGenerator.getPath();
            double stockPrice = payOut.getPayout(path);
            numberOfSample++;

            // variance update
            sumStockPrice2 += stockPrice * stockPrice;
            sumStockPrice += stockPrice;
            mean = sumStockPrice/numberOfSample;
            double part1 = sumStockPrice2/numberOfSample;
            double part2 = Math.pow(mean, 2);
            varianceStockPrice =  Math.sqrt(part1 - part2);
           
            // Check stop
            // Assumption : The number of samples should be more than 100.
            if(3 * varianceStockPrice / Math.sqrt(numberOfSample) <= 0.01 && numberOfSample > 1000){
                break;
            }
        }
        // Create Statistical Collection Instance
        statisticalCollector = new StatisticalCollector(numberOfSample, sumStockPrice, sumStockPrice2, mean, varianceStockPrice);

        // Save Other Information
        Path path = pathGenerator.getPath();
        statisticalCollector.putValue("T0", path.getT0());
        statisticalCollector.putValue("T1", path.getT1());
        statisticalCollector.putValue("Delta", path.getDelta());
        statisticalCollector.putValue("OptionPrice", getOptionPrice());

        return statisticalCollector;
    }

    public double getOptionPrice(){
        return Math.exp(-interestRate * expiration)*(statisticalCollector.getMean());
    }
}
