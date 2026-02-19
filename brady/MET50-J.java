/* MET50-J. Avoid ambiguous or confusing uses of overloading

   You want to avoid ambiguous method or constructing overloading, especially with similar types,
        reordered parameters, autoboxing, or generics.
   Because it can silently change behavior and introduce bugs.

   The solution is to use clearly named methods

*/

class UserAccount {
    private String username;
    private int accountId;

    //Instead of making an overloaded method like getUser(int id), getUser(String name)
    //We should use specific method names for debugging and readability purposes.

    public String getUserByUsername(String username) {
        return "Username: " + username + " Account ID: " + accountId;
    }
    public String getUserByUsername(int accountID) {
        return "Username: " + username + " Account ID: " + accountId;
    }
}
