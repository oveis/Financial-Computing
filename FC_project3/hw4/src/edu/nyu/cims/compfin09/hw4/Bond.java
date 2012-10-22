package edu.nyu.cims.compfin09.hw4;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 3. 30
 * Time: AM. 12:40
 * To change this template use File | Settings | File Templates.
 */
public abstract  class Bond {
    protected double faceValue;
    protected double price = 0.0;
    protected double maturity;
    protected int type;
    protected Map<Double, Double> cashFlow;

    // Constructor
    public Bond(int type, double faceValue, double maturity) throws Exception{
        if(faceValue < 0)
            throw new Exception("Face Value should be positive number.");
        if(maturity < 0)
            throw new Exception("Maturity should be positive number.");
        this.type = type;
        this.faceValue = faceValue;
        this.maturity = maturity;
        cashFlow = new TreeMap<Double, Double>();
    }

    // Getter Method
    public double getFaceValue(){
        return faceValue;
    }
    public double getPrice(){
        return price;
    }
    public double getMaturity(){
        return maturity;
    }
    public int getBondType(){
        return type;
    }
    public Map<Double, Double> getCashFlow(){
        return cashFlow;
    }

    // Abstract Method
    public abstract void setCashFlow();

}




