// RELEVANT TO THE BOOK OR USER CLASSES, NEEDS FINAL METHODS FOR CLONING

/* MET06-J. Do not invoke overridable methods in clone()
    
   Calling overridable methods from inside the clone() method is insecure because it can allow
   subclasses to interfere with the cloning process

   The main issue is that a subclass and override a method called from clone().
   This can allow an attacker to modify the cloned object, which can modify the original object, which leaves both in an unsafe state.
   During cloning, the new object may not be fully initialized yet.
   So, if an overridable method is called, a subclass can access the object before it is fully constructed.
   Or it may read or change internal data incorrectly.

   You can solve this by making methods final which can't be overwritten
*/

class UserProfile implements Cloneable {
    private String username;
    private int[] permissions;

    UserProfile(String username, int[] permissions) {
        this.username = username;
        this.permissions = permissions;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        UserProfile clone = (UserProfile) super.clone();
        clone.copyPermissions();
        return clone;
    }

    final void copyPermissions() {
        if (permissions != null) {
            int[] copy = new int[permissions.length];
            for (int i = 0; i < permissions.length; i++) {
                copy[i] = permissions[i];
            }
            permissions = copy;
        }
    }

    final void initializeDefaults() {
        //initialize default values
    }
}

