/* SER04-J. Do not allow serialization and deserialization to bypass the security manager.
   Serialization and deserialization features can be exploited to bypass security manager checks.
   Normally, a serializable class may contain security checks in its constructors for various reasons.

   If a class enables a caller to retrieve sensitive data upon security checks,
   those checks should be replicated during deserialization to ensure an attacker cannot extract sensitive information.

   The way an attacker can abuse this is by using the writeObject() and readObject() methods that are used in the serializations/deserialization process.
   If the security manager is only written in the constructor, using these methods can omit the security manager.
   This ommission allows untrusted code to maliciously create instances of the class.
   
   To fix this, you can modify the default serialization methods that java has to include a security manager that checks valid code.
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.ObjectOutputStream;

class UserAccount implements Serializable{
    // other methods can go here

    //modify the writeObject() and readObject() methods
    private void writeObject(ObjectOutputStream out) throws IOException {
        performSecurityCheck();
        out.writeObject(account);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        in.defaultReadObject();
        //If the deserialized name does not match the default value created at construction time, duplicate the checks.

        //UNKNOWN is a constant that represents the identification string of the account.
        if (!UNKNOWN.equals(account)) {
            performSecurityCheck();
            validateInput(account);
        }

    }
   }
