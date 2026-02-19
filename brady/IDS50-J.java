/* IDS50-J. Use conservative file naming conventions

   This guideline warns that file and path names can contain dangerous characters
    that can cause security problems or unexpected behavior

   Certain characters can cause problems:
   (leading -) can be interpreted as a command-line option
   (\n) can break shell scripts or logging
   (spaces without quotes) can cause parsing problems
   (using inconsistent naming conventions) can cause bugs or hurt readability

*/

//While it is hard to code something that follows these exact guidelines,
//  we can create an example of how to validate input file names

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyClass {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Error("No argument given");
        }
        String fileName = args[0];

        //We can use the regex class to take in files that only use basic characters, we want to look for files that don't contain blacklisted characters.
        //The ^ means not, so if we can find any characters not part of the given characters, then there is a blacklisted character
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            throw new Error("Invalid file name");
        }

        File f1 = new File(fileName);
        Scanner scan = new Scanner(f1);

        while (scan.hasNext()) {
            System.out.println(scan.next());
        }

        scan.close();
        
    }
}
