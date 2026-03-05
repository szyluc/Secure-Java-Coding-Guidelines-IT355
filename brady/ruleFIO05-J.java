// BEST USED FOR THE SEARCH FOR BOOKS FEATURE

/* FIO05-J. Do not expose buffers or their backing arrays methods to untrusted code.
   
   When using Java NIO buffer classes, wrapping an array or creating duplicates/slices of
   a buffer can result in multiple buffers sharing the same underlying array. If such a
   buffer is handed to untrusted code, that code may be able to read or alter data it
   should not have access to. Dangerous methods include: wrap(), duplicate(), slice(),
   subsequence(), and array().
   
   The recommended fix is to return a read-only view using asReadOnlyBuffer(), which
   ensures that any attempt to modify the buffer's contents will throw a
   ReadOnlyBufferException rather than silently corrupting the original data.
 */

import java.nio.CharBuffer;

final class BookTitle {
    private char[] titleChars;
    /**
    * A constructor method for a BookTitle object
    * @param title is the title of the book
    **/
    public BookTitle(String title) {
        titleChars = title.toCharArray();
    }

    // Safe: returns a read-only view so untrusted code cannot modify the title
   /**
    * A method for returning the title of a book buffer
    * @return the title buffer of a book safely so it cannot be modified
    **/
    public CharBuffer getTitleBuffer() {
        return CharBuffer.wrap(titleChars).asReadOnlyBuffer();
    }
}



