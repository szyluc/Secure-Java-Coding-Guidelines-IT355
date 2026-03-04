/**
 * RULE: OBJ08-J
 */

/**
 * Class to implement rule OBJ08-J. 
 * Do not expose private members of an outer class within a nested class
 * 
 * NOTE: This method would otherwise crash if a main method existed.
 * Thus, we define a class to showcase this below.
 */
class Ticket {
    private TicketID t_id;

    private class TicketID {
        private int id;
        /**
         * Prints the current id to standard out
         */
        private void getID() {
            System.out.println(id);
        }
        /**
         * Sets the current id
         * @param id the id to set to this object's id
         */
        private void setID(int id) {
            this.id=id;
        }
    }
}