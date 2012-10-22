/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 12
 * Time: A.M 12:19
 * To change this template use File | Settings | File Templates.
 */
public class AsianCallOption implements PayOut{
    private double K;       // K : strike price

    public AsianCallOption(double K){
        this.K = K;
    }

    @Override
    public double getPayout(Path path){
        double[] prices = path.getPrices();
        double pricesAvg = 0.0;
        for(int i=1; i<prices.length; i++){
            pricesAvg += prices[i];
        }
        pricesAvg /= (prices.length-1);
        return  Math.max(0, pricesAvg - K);
    }
}
