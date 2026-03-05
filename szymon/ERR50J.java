public class ERR50J {
    // This method uses a for loop to add integers to a result. 
    // This is much safer than using an ArrayIndexOutOfBoundsException
    // to detect the end of the array
    public int addInt(Integer[] integers){
        int result = 0;
        for(int i = 0; i < integers.length; i++){
            result += integers[i];
        }
        return result;
    }
}
