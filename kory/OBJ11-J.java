public final class GymAccount {
    /*  
    Represents a GymAccount
    Rule: OBJ11-J. P12

    Description: An object is partially initialized when its constructor has started but has not yet completed. 
    During construction the object must not be exposed to other classes; 
    This reference must not escape, and other threads must not access it before initialization completes. 
    
    Final ensures value are set once and never changed
    Ensures safe publication in multithreaded enviroment
    This class completely prevents partially initalized or inconsistent states
    */
    /** Unique identifier for account */
    private final String accountID;
    /** Number of sets. Must be > 0 */
    private final double sets;
     /** Number of reps. Must be > 0 */
    private final double reps;

    /**
     * Constructs GymAccount 
     * @param accountID unique account ID
     * @param sets sets number of sets
     * @param reps sets number of reps
     */
    public GymAccount(String accountID, double sets, double reps) {

        // Validate input before assigning
        // If invalid throw exception and stop object creation
        // This does not prevent partailly initilized values only uninitalized values
        if(accountID == null || sets < 0 || reps < 0) {
            throw new IllegalArgumentException("Invalid data");
        }

        // Initialie fields inside constructor
        this.accountID = accountID;
        this.sets = sets;
        this.reps = reps;

        // Notice how "this." does not get passed anywhere else beside constructor
    }

    /**
     * Returns number of accountID
     * @return accountID
     */
    public String getAccountID() {
        return accountID;
    }
    /**
     * Returns number of sets
     * @return sets
     */
    public double getSets() {
        return sets;
    }
    /**
     * Returns number of reps
     * @return reps
     */
    public double getReps() {
        return reps;
    }
}
