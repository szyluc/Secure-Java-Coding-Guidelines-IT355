public class Met00J {

    public static double calcSquareRoot(double num)
    {
        if (num < 0)
            throw new IllegalArgumentException("Cannot calculate square root of a negative number.");
        else
            return Math.sqrt(num);
    }
    public static void main(String[] args) {
        double valuesToCalculateSquare[] = {4.0, 0.0, -1.0};
        for( double value : valuesToCalculateSquare) {
            try {
                double result = calcSquareRoot(value);
                System.out.println("The square root of " + value + " is: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
