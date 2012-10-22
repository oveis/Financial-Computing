package edu.nyu.cims.compfin09.hw4;

import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 4. 8
 * Time: PM. 5:09
 * To change this template use File | Settings | File Templates.
 */
public class Simulator {

    // Compute Price with Yield Curve and Bond
    public static double getPrice(YieldCurve ycm, Bond bond){
        double price = 0;
        Map<Double, Double> cashFlow = bond.getCashFlow();
        Set<Double> keySet = cashFlow.keySet();

        for(Double time : keySet){
            price += cashFlow.get(time) / ycm.getDiscountFactor(time);
        }
        return price;
    }

    // Compute Yield To Maturity with given bond and its price.
    // To calculate this,the Newton Method Algorithm is used in here.
    public static double getYTM(Bond bond, double price) throws Exception{

        if(price < 0.0)
            throw new Exception("Price should be positive number.");

        double ytmOld = 0.0, ytmNew = 0.05;
        double temp = 0;

        //Newton Method Algorithm
        while (Math.abs(ytmNew-ytmOld)>0.0000001){
            ytmOld = ytmNew;
            Map<Double, Double> cashFlow = bond.getCashFlow();
            Set<Double> keySet = cashFlow.keySet();
            
            for(Double time : keySet){
                temp += time * cashFlow.get(time) / Math.exp(ytmOld * time);
            }

            ytmNew = ytmOld - (price - getPrice(bond, ytmOld))/temp;
        }
        return ytmNew;
    }

    // Compute Price with bond and YTM.
    public static double getPrice(Bond bond, double ytm){
        double price = 0;
        Map<Double, Double> cashFlow = bond.getCashFlow();
        Set<Double> keySet = cashFlow.keySet();
        for(Double time : keySet){
            price += cashFlow.get(time) * Math.exp(-time * ytm);
        }
        return price;
    }
}
