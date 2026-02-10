import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * This program showcases rule IDS16-J which covers how to 
 * avoid XML injection by using input validation or checking
 * the resulting XML against a schema of what you expect. 
 * The route we took here to prevent it was just performing
 * input validation on the grade we got before creating the
 * report card in an XML document. Either way, this prevents
 * XML injection via the user input which coud otherwise
 * allow attackers a way to exploit your application and 
 * do some bad things that we want to avoid. Once we have the
 * grade, we create a simple xml document with the grade data.
 */
public class ReportCard {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Student Grade: ");
        int grade = Integer.parseUnsignedInt(input.nextLine());
        while (grade < 0 || grade > 100) {
            System.out.println("Please provide a grade between 0 and 100: ");
            grade = Integer.parseUnsignedInt(input.nextLine());
        }
        String report = "<report>\n" + "<name>Jane Doe</name>\n" + "<grade>" + grade + "</grade>\n" + "</report>";
        File reportCard = new File("ReportCard.xml");
        FileWriter writer;
        try {
             writer = new FileWriter(reportCard);
             writer.write(report);
             writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        input.close();

    }
}
