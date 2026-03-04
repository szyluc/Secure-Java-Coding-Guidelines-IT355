/**
 * Class to implement rule OBJ01-J. 
 * Limit accessibility of fields
 */
public class Plugin {
    private int pluginID; // declared private

    /**
     * Main method
     * @param args console args
     */
    public static void main(String[] args) {
        Plugin p1 = new Plugin(20);
        p1.add();
        p1.add();
        p1.remove();
        System.out.println(p1.getPluginID());
    }

    /**
     * Constructor for the Plugin object
     */
    public Plugin(int startingID) {
        pluginID = startingID;
    }

    /**
     * Gets the plugin's ID
     * @return plugin ID
     */
    public int getPluginID() { 
        return pluginID;
    }

    /**
     * Adds 1 to the current pluginID
     */
    private void add() {
        if (pluginID < Integer.MAX_VALUE) {
            pluginID++;
        } else {
            throw new ArithmeticException("overflow");
        }
    }

    /**
     * Removes 1 to the current pluginID
     */
    private void remove() {
        if (pluginID > 0) {
            pluginID--;
        } else {
            throw new ArithmeticException("overflow");
        }
    }
}