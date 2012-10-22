import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 18
 * Time: P.M 10:46
 * To change this template use File | Settings | File Templates.
 */
public class Run {

    public static void main(String [] args){
        /*
           Condition : 1) stock price = 100 $
                       2) volatility : 10 % per year
                       3) interest rate : 1 %
                       4) expiration : 1 year
        */
        double stockPrice = 100;
        double accuracy = 0.01;
        double strikePrice = 100;
        int expirationYear = 1;
        double volatilityPerYear = 0.1;                // per 1 year
        double interestRatePerYear = 0.01;             // per 1 year

        int expirationMonth = expirationYear * 12;                         // For Asian Option Calculation
        double volatilityPerMonth = volatilityPerYear / Math.sqrt(12);
        double interestRatePerMonth = interestRatePerYear / 12;

        // Statistical Collector
        StatisticalCollector europeanPutCollector;
        StatisticalCollector europeanCallCollector;
        StatisticalCollector asianPutCollector;
        StatisticalCollector asianCallCollector;

        // ** Simulation Manager **
        SimulationManager simulationManager;

        ////////////////////////////////////////////////////
        //
        //        E U R O P E A N      O P T I O N
        //
        ////////////////////////////////////////////////////

        // Random Vector Generator
        RandomVectorGenerator atvg = new AntiTheticVectorGenerator(new UniformRandomNumberGenerator(expirationYear));
        // Path Generator
        PathGenerator pathGenerator = new GBMRandomPathGenerator(interestRatePerYear, expirationYear, volatilityPerYear , stockPrice, atvg, expirationYear);

        // European Call Option
        PayOut europeanCallOption = new EuropeanCallOption(strikePrice);
        simulationManager = new SimulationManager(pathGenerator, europeanCallOption, accuracy, strikePrice, interestRatePerYear, expirationYear);
        europeanCallCollector = simulationManager.generate();
        printResult("European Call Option Price", europeanCallCollector);

        // European Put Option
        PayOut europeanPutOption = new EuropeanPutOption(strikePrice);
        simulationManager = new SimulationManager(pathGenerator, europeanPutOption, accuracy, strikePrice, interestRatePerYear, expirationYear);
        europeanPutCollector = simulationManager.generate();
        printResult("European Put Option Price", europeanPutCollector);

        ////////////////////////////////////////////////////
        //
        //        A S I A N      O P T I O N
        //
        ////////////////////////////////////////////////////

        // Random Vector Generator
        atvg = new AntiTheticVectorGenerator(new UniformRandomNumberGenerator(expirationMonth));
        // Path Generator
        pathGenerator = new GBMRandomPathGenerator(interestRatePerMonth, expirationMonth, volatilityPerMonth, stockPrice, atvg, expirationYear);

        // Asian Call Option
        PayOut asianCallOption = new AsianCallOption(strikePrice);
        simulationManager = new SimulationManager(pathGenerator, asianCallOption, accuracy, strikePrice, interestRatePerMonth, expirationMonth);
        asianCallCollector = simulationManager.generate();
        printResult("Asian Call Option Price", asianCallCollector);

        // Asian Put Option
        PayOut asianPutOption = new AsianPutOption(strikePrice);
        simulationManager = new SimulationManager(pathGenerator, asianPutOption, accuracy, strikePrice, interestRatePerMonth, expirationMonth);
        asianPutCollector = simulationManager.generate();
        printResult("Asian Put Option Price", asianPutCollector);
    }

    public static void printResult(String str, StatisticalCollector sc){
        Map<String, Double> map = sc.getResults();
        System.out.printf("\n[T0] %.1f   [T1] %.1f   [Delta] %.3f   [Samples] %.1f\n", map.get("T0"), map.get("T1"), map.get("Delta"), map.get("NumberOfSample"));
        System.out.println(str + " : " + map.get("OptionPrice"));
    }
}
