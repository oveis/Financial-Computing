import java.util.Map;
/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 18
 * Time: P.M 3:48
 * To change this template use File | Settings | File Templates.
 */
public interface IStatisticalCollector {
    public void addValue(double value);
    public Map<String, Double> getResults();
    public double getMean();
    public double getVariance();
}
