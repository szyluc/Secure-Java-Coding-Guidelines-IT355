/*
* Example showing OBJ05-J
 */ 
class ProfileManager {
    private String name;
    // Mutable because its value can be changed usign methods like setTime()
    // Cant return directly otherwise outside code may modify internal data
    private Date birthDate;

    // Constructor with defensive copying to protect internal state
    public ProfileManager(String name, Date birthDate) {
        this.name = name;

        // Create a copy of the data object to protect internal data
        // Prevents outside code from modifying our private variable
        this.birthDate = (Date) birthDate.clone();
    }
    // Getter for immutable field, String is immutable so safe to return 
    public String getName() {
        return name;
    }

    // Getter that returns a copy instead of the original object
    // External code cannot change the internal birthDate value
    public Date getBirthDate() {
        return (Date) birthDate.clone();
    }

    // Clone the input to avoid external modification
    // Because is mutable
    public void setBirthDate(Date birthDate) {
        this.birthDate = (Date) birthDate.clone();
    }
}
