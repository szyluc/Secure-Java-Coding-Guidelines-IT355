/**
 * Class to test if two things are equal
 * 2 Examples.
 * Description: ava has different way to see if something is equal to each other, its important to use the correct way to check. 
 * To test reference equality use ==. Objects.equals() test the content of two objects are equal. 
 * For string based numeric types like Integer or double use .equals()
 */
public class Equaltest {
    public static void main(String[] args) {
        
        /*
        Two integers with same value
         */
        int a = 10;
        int b = 10;

        // Using the correct operation to test equals in integers
        if(a==b) {
            System.out.println("True");
        }

        /*
        Two strings objects with same content
        */
       String string1 = new String("Hi");
       String string2 = new String("Hi");

       // Using .equals to check if the content of the objects are equal
       if(string1.equals(string2)) {
            System.out.println("True");
       }
    }
}
