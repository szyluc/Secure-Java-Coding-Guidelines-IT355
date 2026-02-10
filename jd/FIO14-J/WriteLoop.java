import java.io.File;
import java.io.FileWriter;

/**
 * The program below showcases the FIO14-J rule which covers 
 * how to properly clean up resources when the program is 
 * terminated to ensure that we dont have any loss of data or 
 * any other issues that could arise from failure to properly 
 * close the resources. In this program, we simply have a loop 
 * that will run for 100 seconds if uninterrupted writing a 
 * progress message to a file as well as the console every 
 * second. Here, when the program is terminated, we have a 
 * shutdown hook that runs and writes a final message to the 
 * file as well console before terminating.
 */
public class WriteLoop {
    public static void main(String[] args) {
        File outFile = new File ("Output.txt");
        FileWriter writer;
        int count = 1;
        try {
            writer = new FileWriter(outFile);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Looping Stopped");
                        writer.write("Looping Stopped\n");
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
            while (count < 100) {
                Thread.sleep(1000);
                if (count > 1) {
                    writer.write(count + " Loops Run\n");
                    System.out.println(count + " Loops Run");
                } else {
                    writer.write("1 Loop Run\n");
                    System.out.println("1 Loop Run");
                }
                count++;
            }
            Runtime.getRuntime().exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
