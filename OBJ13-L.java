import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents a collection of users
   Rule: OBJ13-J. P12
 */
public class Users  {

    /**
     * Internal mutable list
     * This is private so no outside classes can acess it directly
     */
    private final List<String> users;
    /**
     * Constructs users object with defensive copy
     * @param copyUsers
     */
    public Users(List<String> copyUsers) {
    /* Instead of storing the clients list directly
       We create a copy which prevents the caller
       from modifying the list after object is constructed
    */
        this.users = new ArrayList<>(copyUsers);
    }
    /**
     * 
     * @return unmodifiable list of users
     */
    public List<String> getUsers() {
        /*
        Instead of returning the list directly
        We return an unmodifiable view
        unmodifiableList prevents the list from modification
        but the elements in the list can be modified
        */
       return Collections.unmodifiableList(users);
    }
    /**
     * Adds a new user to internal list
     * @param userName
     */
    public void addUser(String userName) {
        // Modification through class method
        // Ensures validation
        if(userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cant be empty");
        }
        users.add(userName);
    }

}
