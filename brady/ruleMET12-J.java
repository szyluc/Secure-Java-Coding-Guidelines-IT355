// APPLY TO RECEIPT FUNCTION, DON'T RELY ON FINALIZE FOR CLOSING FILE HANDLES

/* MET12-J. Do not use finalizers
   
   Finalizers(finalize() methods) are unreliable and unsafe for cleaning up resources in Java.
   They are normally executed after the object becomes unreachable.
   The problem is that:
    1. JVM decides when to run finalizers meaning that they may be delayed or never executed if the JVM exits early.
    2. Exceptions thrown in finalizers may be ignored or can terminate the finalization process.
    3. There is no guaranteed order of execution between multiple finalizers.
    4. Finalizers increase garbage collection overhead and object lifetimes.
    5. If a subclass overrides finalize(), calling super.finalize() incorrectly can leave objects in an inconsitent state.

   The way to solve this is to not use finalizers in new classes.
   Instead you can release resources explicitly with cleanup methods such as stop() or close()
   Basically you need to handle garbage manually instead of using finalizers.
 */

import java.io.Closeable;

class NewClass implements Closeable{
    private boolean closed = false;
    private final Object finalizerGuardian = new Object(){
       /**
       * A method that overrides the finalize method so that we can use a safer method
       * @throws Throwable if an error occurs during execution
       */
        @Override
        protected void finalize() throws Throwable{
            try{
                NewClass.this.cleanup();
            }
            finally{
                super.finalize();
            }
        }
    };
      /**
       * A close method which would be called instead of finalize
       */
    public void close() {
        cleanup();
    }
   /**
       * A method that can release resources if not already closed
       */
    private void cleanup() {
        if (!closed) {
            System.out.println("Releasing resources");
            closed = true;
        }
    }
      /**
       * A method that overrides finalize with the cleanup() method
       * @throws Throwable if an error occurs during execution
       */
    @Override
    protected void finalize() throws Throwable {
        try{
            cleanup();
        }
        finally {
            super.finalize();
        }
    }
}


