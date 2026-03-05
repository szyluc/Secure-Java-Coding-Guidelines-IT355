public class ERR51J {
    // This code demonstrates how user-defined exceptions like DivisionByZeroException
    // are better to use for clarity and maintainability.
    public static void main(String[] args)
    {
        try{
            divideTwoInts(50, 0);
        } catch(DivisionByZeroException e){
            System.out.println("An error occurred. You cannot divide by zero.");
        }
    }

    static int divideTwoInts(int a, int b) throws DivisionByZeroException{
        if (b == 0){
            throw new DivisionByZeroException();
        }
        return a / b;
    }

    static class DivisionByZeroException extends Exception{

    }

}
