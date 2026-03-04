
/**
 * Class to implement recommendation OBJ55-J.
 * Remove short-lived objects from long-lived container objects
 */
class MyString {
    private String string;

    /**
     * Main method
     * @param args console args
     */
    public static void main(String[] args) {
        MyString s1 = new MyString("str");
        s1.print();
        s1.setNull();
    }

    /**
     * Constructor for MyString
     * @param string my string
     */
    public MyString(String string) {
        this.string = string;
    }

    /**
     * Prints contents of my string
     */
    public void print() {
        System.out.println(string);
    }

    /**
     * Sets my string to null
     */
    public void setNull() {
        this.string = null;
    }
}