/**
 * RULE: SER01J
 */

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Ser implements Serializable {
    private final long serialVersionID = 123456789;
    private Ser() {
        // initialize some class
    }

    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}

