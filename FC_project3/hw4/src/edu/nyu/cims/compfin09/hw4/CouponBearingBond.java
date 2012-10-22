package edu.nyu.cims.compfin09.hw4;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 4. 4
 * Time: A.M. 11:06
 * To change this template use File | Settings | File Templates.
 */
class CouponBearingBond extends Bond{
    final static int CBB = 1;      // Coupon Bearing Bond
    private double couponPercent;
    // The length of the period between the annuity part payments.
    // Example : Annual = 1, Semi-annual = 0.5
    private double paymentFrequency;

    public CouponBearingBond(double faceValue, double maturity, double couponPercent, double paymentFrequency) throws Exception{
        super(CBB, faceValue, maturity);
        if(couponPercent < 0.0 || couponPercent > 1.0)
            throw new Exception("Coupon Percent should be between 0 and 1.");
        if(paymentFrequency < 0.0 || paymentFrequency > 1.0)
            throw new Exception("Payment Frequency should be between 0 and 1.");
        this.couponPercent = couponPercent;
        this.paymentFrequency = paymentFrequency;
        setCashFlow();
    }

    public double getCoupon(){
        return couponPercent;
    }

    public double getPaymentFrequency(){
        return paymentFrequency;
    }

    public void setCashFlow(){
        for(double d=paymentFrequency; d<=maturity; d+=paymentFrequency){
            double couponPrice = faceValue * couponPercent * paymentFrequency;
            if(d == maturity){
                cashFlow.put(d, couponPrice + getFaceValue());
            }else{
                cashFlow.put(d, couponPrice);
            }
        }
    }

    @Override
    public String toString(){
        String str = "Coupon Bearing Bond [ ";
        str += "(face) $" + faceValue + " (maturity) " + maturity + "year ";
        str += String.format("(coupon) %.2f%% ", (couponPercent*100));
        str += " (payment frequency) " + paymentFrequency + "year ]";
        return str;
    }
}
