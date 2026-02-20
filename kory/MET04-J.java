/**
 * Super class representing a animal
 * MET04-J: P8
 */
class Animal {
    
    /**
     * Secret method
     * Protected method for sensitive information
     */
    protected final void secret() {
        System.out.println("A animals secret behaivor");
    }
    /**
     * Print method
     * Public method safe to expose
     */
    public void eat() {
        System.out.println("Animal is eating");
    }
}

/**
 * Subclass of animal representing dog
 */
public class Dog extends Animal {
    // Notice we cant override secret becasue its final to animal
    // Follows our rule
    // But if it wasnt final we should not do
    /*
    public void secret()
    This changes access level
    */
    
    /**
     * Overriding animals method eat
     * 
     */
    public void eat() {
        System.out.println("Dog eats");
    }
}
