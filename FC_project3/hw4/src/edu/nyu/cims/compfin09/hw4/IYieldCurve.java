package edu.nyu.cims.compfin09.hw4;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 4. 4
 * Time: PM. 2:18
 * To change this template use File | Settings | File Templates.
 */
public interface IYieldCurve {
    public double getInterestRate(double time);
    public double getForwardRate(double t0, double t1);
    public double getDiscountFactor(double t);

    public void addBond(Bond bond);
    public void calculate();
}
