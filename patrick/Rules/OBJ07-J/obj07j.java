/**
 * RULE: OBJ07J
 * Description: Sensitive classes must not let themselves be copied
 */

class SensitiveClass {
    private char[] fileName;
    private Boolean shared = false;

    SensitiveClass(String fileName) {
        this.fileName = fileName.toCharArray();
    }

    final void replace() {
        if (!shared) {
            for (int i=0; i < fileName.length; i++) {
                fileName[i] = "x";
            }
        }
    }

    final String get() {
        if (!shared) {
            shared = true;
            return String.valueOf(fileName);
        } else {
            throw new IllegalStateException("Failed to get instance");
        }
    }

    final void printFileName() {
        System.out.println(String.valueOf(fileName));
    }
}

