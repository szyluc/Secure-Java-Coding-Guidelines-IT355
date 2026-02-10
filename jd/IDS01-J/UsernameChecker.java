import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The simple program below covers the IDS01-J rule which 
 * details how to properly validate a username by ensuring 
 * that it is normalized before it is validated. Otherwise, 
 * it could lead to issues where dangerous input could be 
 * allowed through due to the ambiguity of characters before
 * they are normalized. You can see in this program below 
 * where we check if a username is valid that we first 
 * normalize it and then check if it matches the pattern we 
 * have defined for a valid username. This ensures that we 
 * don't allow any usernames that we don't want to or even 
 * worse allow some dangerous input that could allow for an 
 * attack on our system.
 */
public class UsernameChecker {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Username: ");
        String username = input.nextLine();
        username = Normalizer.normalize(username, Form.NFKC);
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        if (usernameMatcher.matches()) {
            System.out.println("The provided username is valid");
        } else {
            System.out.println("The provided username is invalid");
        }
        input.close();
    }
}
