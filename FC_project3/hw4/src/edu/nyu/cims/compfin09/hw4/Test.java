package edu.nyu.cims.compfin09.hw4;

import java.text.NumberFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 30
 * Time: AM. 12:40
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    
    public static void main(String[] args) throws Exception{
        Simulator simulator = new Simulator();

        // Result 1
        // Instantiate the yield curve that is described in question 3, and print the object
        System.out.println("\n[ Result 1 ]");
        Map<Double, Double> yieldCurveMap = new TreeMap<Double, Double>();
        yieldCurveMap.put(1.0, 0.02);
        yieldCurveMap.put(2.0, 0.023);
        yieldCurveMap.put(3.0, 0.03);
        YieldCurve yieldCurve1 = new YieldCurve(yieldCurveMap);
        System.out.println(yieldCurve1);

        // Result 2
        // Create two given bonds objects and use them to instantiate a yield curve objects.
        // Print the yield curve.
        System.out.println("\n[ Result 2 ]");
        List<Bond> bonds = new ArrayList<Bond>();
        Bond zeroCouponBond1 = new ZeroCouponBond(100, 0.5, 95);
        Bond zeroCouponBond2 = new ZeroCouponBond(1000, 1, 895);
        bonds.add(zeroCouponBond1);
        bonds.add(zeroCouponBond2);
        YieldCurve yieldCurve2 = new YieldCurve(bonds);
        System.out.println(zeroCouponBond1);
        System.out.println(zeroCouponBond2);
        System.out.println(yieldCurve2);

        // What is getInterestRate(0.75) returns? print it.
        double year = 0.75;
        double interestRate = yieldCurve2.getInterestRate(year);
        String result2 = String.format("rate at %.2f is %.2f%%\n", year, (interestRate*100));
        System.out.println(result2);

        // Result 3
        // Print given Coupon Bearing Bond's price and its YTM.
        System.out.println("\n[ Result 3 ]");
        Bond couponBearingBond = new CouponBearingBond(500, 3, 0.05, 0.5);
        System.out.println(couponBearingBond);
        double price = simulator.getPrice(yieldCurve1, couponBearingBond);
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
        System.out.println("price : " + fmt.format(price));

        double ytm = simulator.getYTM(couponBearingBond, price);
        System.out.printf("YTM : %.2f%%\n", (ytm*100));
    }
}
