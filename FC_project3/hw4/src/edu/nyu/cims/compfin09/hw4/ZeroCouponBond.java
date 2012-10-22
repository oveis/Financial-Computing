package edu.nyu.cims.compfin09.hw4;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 4. 4
 * Time: A.M. 11:07
 * To change this template use File | Settings | File Templates.
 */
public class ZeroCouponBond extends Bond{
    final static int ZCB = 0;      // Zero Coupon Bond

    // Constructor
    public ZeroCouponBond(double faceValue, double maturity, double price) throws Exception{
        super(ZCB, faceValue, maturity);
        if(price < 0)
            throw new Exception("Price should be positive number");
        this.price = price;
        setCashFlow();
    }
    public ZeroCouponBond(double faceValue, double maturity) throws Exception{
        super(ZCB, faceValue, maturity);
        setCashFlow();
    }

    // Implement Method
    public void setCashFlow(){
        cashFlow.put(maturity, faceValue);
    }
    
    @Override
    public String toString(){
        String str = "Zero Coupon Bond [ ";
        str += "(face) $" + faceValue + " (maturity) " + maturity + "year ";
        if(price != 0.0)
            str +=  "(price) $" + price;
        str += " ]";
        return str;
    }
}
