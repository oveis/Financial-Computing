/**
 * Created by IntelliJ IDEA.
 * User: Jinil
 * Date: 12. 2. 22
 * Time: 오후 2:22
 * To change this template use File | Settings | File Templates.
 */
public class TradeChecker {

    public static String tradeCheckFacade(String data){
        String err = "";
        String[] fields = data.split(",");

        // Check validate of trade data
        if(!(err = blankCheck(data)).equals("")){                   // Check 1) Trade data line is blank
            return err;
        }
        else if(!(err = lengthCheck(fields)).equals("")){          // Check 2) The number of fields is less or more than 4
            return err;
        }
        else if(!(err = emptyCheck(fields)).equals("")){           // Check 3) One of fields is empty
            return err;
        }
        else if(!(err = typeCheck(fields)).equals("")){            // Check 4) The type of fields is illegal
            return err;
        }
        return err;
    }

    // Purpose : Check whether the trade data is blank or not
    private static String blankCheck(String data){
        String err = "";
        if(data.equals(""))
            err = "The trade inform is blank.";
        return err;
    }
    
    // Purpose : Check whether the trade data is enough or not
    private static String lengthCheck(String[] fields){
        String err = "";
        if(fields.length < 4)
            err = "The trade fields are not enough.";
        else if(fields.length > 4)
            err = "The trade fields are too much.";
        return err;
    }

    // Purpose : Check whether one of fields is empty or not
    private static String emptyCheck(String[] fields){
        String err = "";
        for(int i=0; i<4; i++)
            if(fields[i].equals(""))
                switch(i){
                    case 0:
                        err += " time";
                        break;
                    case 1:
                        err += " symbol";
                        break;
                    case 2:
                        err += " quantity";
                        break;
                    case 3:
                        err += " price";
                        break;
                }

        if(!err.equals(""))
            err += " is(are) empty.";
        return err;
    }

    // Purpose : Check whether the type of fields is legal or not
    private static String typeCheck(String[] fields){
        String err = "";
        int index = 0;
        try{
            int time = Integer.parseInt(fields[0].trim());          // int type check
            index++;
            String symbol = fields[1].trim();                       // String type check
            index++;
            int quantity = Integer.parseInt(fields[2].trim());      // int type check
            index++;
            if(!fields[3].contains("."))                            // double type check
                throw new NumberFormatException();                  // double type should contain "." character.
            double price = Double.parseDouble(fields[3].trim());
        }catch(Exception e){
            switch(index){
                case 0:
                    err += "time (int) : ";
                    break;
                case 1:
                    err += "symbol (String) : ";
                    break;
                case 2:
                    err += "quantity (int) : ";
                    break;
                case 3:
                    err += "price (double) : ";
                    break;
            }
            err += "type is wrong.";
        }
        return err;
    }

}
