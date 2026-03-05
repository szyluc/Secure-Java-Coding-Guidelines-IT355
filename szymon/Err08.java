


public class Err08 {

    public static void main(String[] args) {

        String someString = null;

        int stringLength = checkStringLength(someString);

        System.out.println("The length of someString is: " + stringLength);

    }

    private static int checkStringLength(String str){
        if (str == null)
            return -1;
        else
            return str.length();
    }
}