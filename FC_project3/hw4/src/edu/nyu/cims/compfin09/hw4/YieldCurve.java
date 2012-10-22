package edu.nyu.cims.compfin09.hw4;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 30
 * Time: AM. 12:40
 * To change this template use File | Settings | File Templates.
 */
public class YieldCurve implements IYieldCurve{
    final static int ZCB = 0;      // Zero Coupon Bond
    final static int CBB = 1;      // Coupon Bearing Bond
    
    private List<Bond> bonds;
    private Map<Double, Double> yieldCurve = new TreeMap<Double, Double>();

    // Yield Curve constructor with Yield Curve Map.
    public YieldCurve(Map<Double, Double> yieldCurve){
        this.yieldCurve = yieldCurve;
    }

    // Yield Curve constructor with Bond list.
    public YieldCurve(List<Bond> bonds){
        this.bonds = bonds;
        calculate();
    }
    
    // Calculate the interest rate for a given duration in continues compounding
    public double getInterestRate(double time){

        // If the interest rate is exist in the Yield Curve, return the value.
        if(yieldCurve.get(time) != null)
            return yieldCurve.get(time);

        // Selection two interest rates to compute the linear interpolation
        // Let's assumes that key = year, value = interest rate for convenience.
        double largestLowerKey = 0.0, smallestUpperKey = 100000;
        Set<Double> keySet = yieldCurve.keySet();
        for(Double key : keySet){
            if(key < time && key > largestLowerKey)
                largestLowerKey = key;
            else if(key > time && key < smallestUpperKey)
                smallestUpperKey = key;
        }

        // Limitation.
        // If the required time is less than any one in the yield curve,
        // return the smallest yield curve year (in this case the interest year of 1 year)
        // In opposite case, it will be the largest yield curve year.
        if(largestLowerKey == 0.0)
            return yieldCurve.get(smallestUpperKey);
        else if(smallestUpperKey == 100000)
            return yieldCurve.get(largestLowerKey);

        // Calculate the interest rate with linear interpolation method.
        while(true){
            double middleKey = (largestLowerKey + smallestUpperKey) / 2;
            double middleValue = (yieldCurve.get(largestLowerKey) + yieldCurve.get(smallestUpperKey)) / 2;
            yieldCurve.put(middleKey, middleValue);
            if(middleKey == time)
                return middleValue;
            else if(time > middleKey)
                largestLowerKey = middleKey;
            else if(time < middleKey)
                smallestUpperKey = middleKey;
        }
    }

    // When new bond is founded, Add into the bond list
    // and calculate the Yield Curve again with this new bond.
    public void addBond(Bond bond){
        bonds.add(bond);
        calculate();
    }

    // Calculate the Yield Curve.
    // Input time and interest rate into Yield Curve Map.
    public void calculate(){               
        for(Bond bond : bonds){
            int bondType = bond.getBondType();
            switch(bondType){
                case ZCB :
                    double faceValue = bond.getFaceValue();
                    double price = bond.getPrice();
                    double maturity = bond.getMaturity();
                    double interestRate = Math.log(faceValue / price) /maturity;
                    yieldCurve.put(maturity, interestRate);
                    break;
            }
        }
    }

    // Get the forward interest rate from t0 to t1.
    public double getForwardRate(double t0, double t1){
        double rateT0 = getInterestRate(t0);
        double rateT1 = getInterestRate(t1);
        return (rateT1 * t1) / (rateT0 * t0 * (t1 - t0));
    }

    // return the discount factor for a given duration.
    public double getDiscountFactor(double t){
        return Math.exp(getInterestRate(t) * t);
    }

    @Override
    public String toString(){
        Set<Double> keySet = yieldCurve.keySet();
        
        String str = "\n[ Yield Curve Table ]\n";
        str += "------------------\n";
        str += "|  Year |  rate  |\n";
        str += "------------------\n";
        for(Double time : keySet){
            double rate = yieldCurve.get(time);
            str += String.format("|  %.2f |  %.2f%% |\n", time, (rate*100));
        }
        str += "------------------\n";
        return str;
    }
}
