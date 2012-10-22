/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 15
 * Time: A.M 11:59
 * To change this template use File | Settings | File Templates.
 */
public class EuropeanPutOption implements PayOut {
    private double K;

    public EuropeanPutOption(double K){
        this.K = K;
    }

    @Override
    public double getPayout(Path path) {
        double[] prices = path.getPrices();
        return Math.max(0,K - prices[prices.length-1]);     // prices[prices.length-1] : S
    }

}
