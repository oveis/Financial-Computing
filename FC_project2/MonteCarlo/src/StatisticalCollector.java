import java.util.*;
/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 17
 * Time: P.M 4:48
 * To change this template use File | Settings | File Templates.
 */
public class StatisticalCollector implements IStatisticalCollector{
    private Map<String, Double> map;

    public StatisticalCollector(){
        map = new HashMap<String, Double>();
        map.put("NumberOfSample", 0.0);
        map.put("SumStockPrice", 0.0);
        map.put("SumStockPrice2", 0.0);
        map.put("Mean", 0.0);
        map.put("Variance", 0.0);
    }
    
    public StatisticalCollector(double num, double sum, double sum2, double mean, double variance){
        map = new HashMap<String, Double>();
        map.put("NumberOfSample", num);
        map.put("SumStockPrice", sum);
        map.put("SumStockPrice2", sum2);
        map.put("Mean", mean);
        map.put("Variance", variance);
    }
    
    public void addValue(double value){
        // Get Values
        double numberOfSample = map.get("NumberOfSample");
        double sumStockPrice = map.get("SumStockPrice");
        double sumStockPrice2 = map.get("SumStockPrice2");

        // Calculation with new value
        numberOfSample++;
        sumStockPrice += value;
        sumStockPrice2 += value * value;
        double part1 = sumStockPrice2/numberOfSample;
        double part2 = Math.pow(sumStockPrice/numberOfSample, 2);

        // Update the map
        map.put("NumberOfSample", numberOfSample);
        map.put("SumStockPrice", sumStockPrice);
        map.put("SumStockPrice2", sumStockPrice2);
        map.put("Mean", sumStockPrice/numberOfSample);
        map.put("Variance", Math.sqrt(part1 - part2));
    }
    
    public Map<String, Double> getResults(){
        return map;
    }
    
    public void setMap(Map<String, Double> map){
        this.map = map;
    }
    
    public void putValue(String str, Double dou){
        map.put(str, dou);
    }
    
    public double getMean(){
        return map.get("Mean");
    }
    
    public double getVariance(){
        return map.get("Variance");
    }
}
