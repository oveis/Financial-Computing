/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 2. 17
 * Time: 오후 3:19
 * To change this template use File | Settings | File Templates.
 */
import java.math.BigDecimal;

public class Trade
{
    private int time;
    private String symbol;
    private int quantity;
    private double price;
    private String error = "";

    // Constructor
    public Trade(String[] fields){
        this.setTime(Integer.parseInt(fields[0]));
        this.setSymbol(fields[1]);
        this.setQuantity(Integer.parseInt(fields[2]));
        this.setPrice(Double.parseDouble(fields[3]));
    }

    public Trade(){
        this.setTime(0);
        this.setSymbol("");
        this.setQuantity(0);
        this.setPrice(0.0);
    }

    // Getter
    public int getTime(){return time;}
    public String getSymbol(){return symbol;}
    public int getQuantity(){return quantity;}
    public double getPrice(){return price;}
    public String getError(){return error;}

    // Setter
    public void setTime(int time){this.time = time;}
    public void setSymbol(String symbol){this.symbol = symbol;}
    public void setQuantity(int quantity){this.quantity = quantity;}
    public void setPrice(double price){this.price = price;}
    public void setError(String error){this.error += error;}

    public String toString(){
        if(!this.isFlawless())
            return " << ERROR : " + getError() + " >> ";
        return String.format("Trade Time: %4d. Symbol: %4s, Price: %3.4f, Quantity: %4d", this.getTime(), this.getSymbol(), this.getPrice(), this.getQuantity());
    }

    public boolean isFlawless(){
        return this.getError().equals("");
    }
}
