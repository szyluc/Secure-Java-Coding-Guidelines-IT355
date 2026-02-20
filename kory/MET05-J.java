/**
 * Superclass representing a vehicle
 * MET05-J: P8
 */
class Vehicle {
    /**
     * Constructor that calls a final method
     * if it called startEngine() it would be unsafe
     * as it calls an overridable method
     */
    public Vehicle() {
        initializeVehicle();
    }

    /**
     * This method is final so it cant be overridden
     * Meaning its safe to call from constructor
     */
    public final void initializeVehicle() {
        System.out.println("Vehicle initialized safely");
    }

    /**
     * Public methdo that can be overriden
     * Notice we did not call form constructor
     */
    public void startEngine() {
        System.out.println("Engine started");
    }
}

/*
* Sub class that is a car
*/
class Car extends Vehicle {
    /**
     * string reprsenting type of car
     */
    private String type = "SUV";
    /**
     * @Override method of startEngine
     */
    public void startEngine() {
        System.out.println(type + " Engine has started");
    }
}
/**
 * Main class to test
 */
public class Main {
    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle(); // Safely calls constructor
        Car car = new Car(); // constructor only calls final method
        car.startEngine(); // safe as subclass fields are initialized.
    }
}
