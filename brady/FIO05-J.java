/* FIO05-J. Do not expose buffers or their backing arrays methods to untrusted code.
   
   Java NIO buffer classes can wrap arrays or create duplicates/slices of existing buffers.
   These buffers are often backed by the same array, exposing them to untrusted code that allows modification of the array.
   Methods that expose the backing array include: wrap(), duplicate(), slice(), subsequence(), and array().

   The solution is to only return a read-only view of the char array in the form of a read-only CharBuffer.
   The library CharBuffer guarentees that attempts to modify the elements of a CharBuffer will result in a ReadOnlyBufferException.

 */

import java.nio.CharBuffer;

final class MyClass {
    private char[] myArray;

    public MyClass(){
        myArray = new char[10];
    }

    public CharBuffer getBufferCopy() {
        return CharBuffer.wrap(myArray).asReadOnlyBuffer();
    }
}
