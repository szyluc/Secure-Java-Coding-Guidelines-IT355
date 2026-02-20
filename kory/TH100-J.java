/**
 * Testing thread
 * TH100-j P6
 */
public final class Test implements Runnable{
    
    @Override
    /**
     * Haft to override the run method in Runnable
     */
    public void run() {
        System.out.println("Thread is running");
    }
    public static void main(String[] args) {
        // creating a new thread
        Test task = new Test();

        // calling tast.run() would execute it using the main thread

        // instead we do start() to create a new thread to execute task
        // this runs concurrently with the main thread
        Thread thread = new Thread(task);
        thread.start();
    }

}
