import java.util.Scanner;
import java.util.UUID;
public class InputController {
    /**
     * Options:
     * 1) Login
     * 2) Create account
     * 
     * USER:
     * 1) Rent book
     * 2) Return book
     * 3) Search book
     *    - search by name, author, category
     * 4) View rented books (account info)
     * 
     * ADMIN:
     * 1) Add book
     * 2) Remove book
     * 3) View all rented books (view by ID)
     * 4) Search account
     */

    Scanner scanner = new Scanner(System.in);

    void runningMenu() {
        System.out.println("Welcome to the ISU Library!");
        System.out.println("----------");
    }
    
    void loginMenu()
    {
        System.out.println("(1) Log In"); // leads to log in through UUID
        System.out.println("(2) Create Account"); // leads to account creation
    }

    void userMainMenu()
    {
        System.out.println("(1) View Account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Log Out"); // logs user out
    }

    void adminMainMenu()
    {
        System.out.println("(1) View Account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Look up another account"); // leads to separate menu for UUID search
        System.out.println("(4) Add a book"); // leads to separate menu for adding book
        System.out.println("(5) Delete a book"); // leads to separate menu for deleting book
        System.out.println("(6) Log Out"); // logs user out
    }

    void searchMenu()
    {
        System.out.println("Select which filter to search by:");
        System.out.println("(1) Search by book name");
        System.out.println("(2) Search by book author");
        System.out.println("(3) Search by book genre");
    }

    void adminAccountLookUpMenu()
    {
        // need a null check
        System.out.println("Look up Account ID: ")
        UUID id = UUID.fromString(scanner.nextLine());
        ModifyAccounts acc = new ModifyAccounts();
        acc.getAccount(id); 
    }

    void adminAddBookMenu()
    {

    }

    void adminDeleteBookMenu()
    {

    }

}
