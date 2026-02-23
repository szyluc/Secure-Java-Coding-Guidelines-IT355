// FILE IMPORT/EXPORT

/* SER08-J. Minimize privileges before deserializing from a privileged context.
   
   The vulnerability occurs when a prgram deserializes untrusted input inside a privileged block

   An attacker can supply malicious serialized objects, those objects can have permissions that the attacker would not normally have.
   This can lead to privilege escalation, which can lead to arbitrary code execution.

   This vulnerability can be fixed by defining a new AccessControlContext INSTANCE, with a new ProtectionDomain.
   What this does is that it grants only the minimal permissions required and removes all other privileges.

*/

import java.io.FilePermission;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;

class PluginLoader {
    private static class PluginAccessControlContext {
        private static final AccessControlContext INSTANCE;

        static {
            // You can add the permissions to a directory and read it from a file.
            FilePermission perm = new FilePermission("dir", "read");
            PermissionCollection perms = perm.newPermissionCollection();
            perms.add(perm);
            INSTANCE = new AccessControlContext(new ProtectionDomain[] {
                    new ProtectionDomain(null, perms)
            });
        }
    }

    public static PluginConfig loadConfig(ObjectInputStream input) throws IOException, ClassNotFoundException {
        try {
            return AccessController.doPrivileged(
                    new PrivilegedExceptionAction<PluginConfig>() {
                        public PluginConfig run() throws Exception {
                            return (PluginConfig) input.readObject();
                        }
                    },
                    PluginAccessControlContext.INSTANCE); //Restrict permissions
        } catch (PrivilegedActionException pae) {
            throw new IOException("Deserialization failed", pae);
        }
    }

}

class PluginConfig implements Serializable {
    //code
}


