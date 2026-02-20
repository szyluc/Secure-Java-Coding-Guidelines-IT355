/**
 * Simple total class
 * Description:The method should inform the caller about what happend. 
 * It should indicate whether is succeeded or failed. 
 * If performing an action silently without return a value or throwing exception it leaves the caller guessing 
 */
public class Total {
    /**
     * holds count value
     */
    private int total = 0;

    /*
    *  Increase total by value 
       only increase if value is greater then 1
       return true if succeeded otherwise false
    */
    public boolean increase(int value) {
        // if value is negative return false
        if(value < 0 ) {
            return false;
        }
        total += value;
        return true;
    }
}
