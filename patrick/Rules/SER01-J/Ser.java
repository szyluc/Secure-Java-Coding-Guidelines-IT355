import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Class to implement rule SER01-J. 
 * Do not derivate from the proper signatures of serialization methods
 */
public class Ser implements Serializable {
    private final long serialVersionID = 123456789;

    /**
     * Writes to some output stream.
     * @param stream Output stream
     * @throws IOException
     */
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    /**
     * Reads from some input stream
     * @param stream Input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}

