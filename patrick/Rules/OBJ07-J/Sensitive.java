/**
 * Class to implement rule OBJ07-J. 
 * Sensitive classes must not let themselves be copied.
 */
public final class Sensitive {
    private char[] fileName;
    // no need for flag since class is final

    /**
     * Main method
     * @param args console args
     */
    public static void main(String[] args) {
        Sensitive s1 = new Sensitive("fileLoc.zip");
        s1.replace();
        s1.printFileName();
    }

    /**
     * Constructor for the Sensitive object
     * @param fileName The file name to pass
     */
    Sensitive(String fileName) {
        this.fileName = fileName.toCharArray();
    }

    /**
     * Replaces all characters of filename with %
     */
    private final void replace() {
        for (int i=0; i<fileName.length; i++) {
            fileName[i] = '%';
        }
    }

    /**
     * Gets the string representation of filename
     * @return String form of filename
     */
    private final String get() {
        return String.valueOf(fileName);
    }

    /**
     * Prints the filename to standard out
     */
    private final void printFileName() {
        System.out.println(get());
    }
}

