import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
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
    Account currentAccount;

    public void startMenu() {
        System.out.println("Welcome to the ISU Library!");
        System.out.println("----------");
    }
    
    public void loginMenu() throws SQLException {
        System.out.println("(1) Log In"); // leads to log in through UUID
        System.out.println("(2) Create Account"); // leads to account creation
        System.out.println("(3) Exit"); // exits program
        handleLogin();
    }

    public void userMainMenu() throws SQLException {
        if (currentAccount.getAccountHolderRole() == Role.ADMIN) {
            adminMainMenu();
        } else {
            memberMainMenu();
        }
    }

    private void memberMainMenu() throws SQLException {
        System.out.println("(1) View Account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Log Out"); // logs user out
        System.out.println("(4) Exit"); // exits program
        handleUserMainMenu();
    }

    private void adminMainMenu() throws SQLException {
        System.out.println("(1) View Account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Look up another account"); // leads to separate menu for UUID search
        System.out.println("(4) Add a book"); // leads to separate menu for adding book
        System.out.println("(5) Delete a book"); // leads to separate menu for deleting book
        System.out.println("(6) Log Out"); // logs user out
        System.out.println("(7) Exit"); // exits program
        handleAdminMainMenu();
    }

    private void searchMenu() throws SQLException{
        System.out.println("Select which filter to search by:");
        System.out.println("(1) Search by book name");
        System.out.println("(2) Search by book author");
        System.out.println("(3) Search by book genre");
        System.out.println("(4) Return to main menu");
        handleSearchMenu();
    }

    private void adminAccountLookUpMenu() throws SQLException
    {
        // need a null check
        System.out.println("Look up Account ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        ModifyAccounts modifyAccounts = new ModifyAccounts();
        currentAccount = modifyAccounts.getAccount(id);
        System.out.println("Account Name: " + currentAccount.getAccountHolderName());
        System.out.println("Account Birth Date: " + currentAccount.getAccountHolderBirthDate());
    }

    private void adminAddBookMenu() throws SQLException{
        System.out.println("Enter book name: ");
        String bookName = scanner.nextLine();
        System.out.println("Enter book author: ");
        String bookAuthor = scanner.nextLine();
        System.out.println("Enter book genre: ");
        String bookGenre = scanner.nextLine();
        Book newBook = new Book(bookName, bookAuthor, bookGenre);
        ModifyBooks modifyBooks = new ModifyBooks();
        modifyBooks.addBook(newBook);
        if (currentAccount.getAccountHolderRole() == Role.ADMIN) {
            adminMainMenu();
        } else {
            memberMainMenu();
        }
    }

    private void adminDeleteBookMenu() throws SQLException {
        System.out.println("Enter book ID: ");
        UUID bookId = UUID.fromString(scanner.nextLine());
        ModifyBooks modifyBooks = new ModifyBooks();
        Book bookToDelete = modifyBooks.getBook(bookId);
        modifyBooks.deleteBook(bookToDelete);
        if (currentAccount.getAccountHolderRole() == Role.ADMIN) {
            adminMainMenu();
        } else {
            memberMainMenu();
        }
    }

    private void handleSearchMenu() throws SQLException {
        int choice = scanner.nextInt();
        ModifyBooks modifyBooks = new ModifyBooks();
        List<Book> books;
        switch (choice) {
            case 1:
                System.out.println("Enter book name: ");
                String bookName = scanner.nextLine();
                books = modifyBooks.getBookByName(bookName);
                for (Book book : books) {
                    System.out.println("ID: " + book.getBookId());
                    System.out.println("Name: " + book.getBookName());
                    System.out.println("Author: " + book.getBookAuthor());
                    System.out.println("Genre: " + book.getBookCategory());
                }
                break;
            case 2:
                System.out.println("Enter book author: ");
                String bookAuthor = scanner.nextLine();
                books = modifyBooks.getBookByAuthor(bookAuthor);
                for (Book book : books) {
                    System.out.println("ID: " + book.getBookId());
                    System.out.println("Name: " + book.getBookName());
                    System.out.println("Author: " + book.getBookAuthor());
                    System.out.println("Genre: " + book.getBookCategory());
                }
                break;
            case 3:
                System.out.println("Enter book genre: ");
                String bookGenre = scanner.nextLine();
                books = modifyBooks.getBookByGenre(bookGenre);
                for (Book book : books) {
                    System.out.println("ID: " + book.getBookId());
                    System.out.println("Name: " + book.getBookName());
                    System.out.println("Author: " + book.getBookAuthor());
                    System.out.println("Genre: " + book.getBookCategory());
                }
                break;
            case 4:
                Role role = currentAccount.getAccountHolderRole();
                boolean isAdmin = role == Role.ADMIN;
                if (isAdmin) {
                    adminMainMenu();
                } else {
                    memberMainMenu();
                }
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 4.");
        }
    }

    private void handleLogin() throws SQLException {
        ModifyAccounts modifyAccounts = new ModifyAccounts();
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter Account ID: ");
                UUID id = UUID.fromString(scanner.nextLine());
                
                currentAccount = modifyAccounts.getAccount(id);
                break;
            case 2:
                System.out.println("Enter Account Name: ");
                String name = scanner.nextLine();
                System.out.println("Enter Account Birth Date (YYYY-MM-DD): ");
                String birthDate = scanner.nextLine();
                LocalDate date = LocalDate.parse(birthDate);
                Account newAccount = new Account(name, date, Role.MEMBER);
                modifyAccounts.addAccount(newAccount);
                currentAccount = newAccount;
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 3.");
        }
    }

    private void handleUserMainMenu() throws SQLException {
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
                ModifyBooks modifyBooks = new ModifyBooks();
                List<RentedBook> rentedBooks = modifyRentedBooks.getRentedBooks(currentAccount.getAccountId());
                System.out.println("Rented Books");
                for (RentedBook rentedBook : rentedBooks) {
                    Book book = modifyBooks.getBook(rentedBook.getBookID());
                    System.out.println(book.getBookName());
                }
                break;
            case 2:
                searchMenu();
                break;
            case 3:
                currentAccount = null;
                loginMenu();
                break;
            case 4:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 4.");
        }
    }

    private void handleAdminMainMenu() throws SQLException {
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
                ModifyBooks modifyBooks = new ModifyBooks();
                List<RentedBook> rentedBooks = modifyRentedBooks.getRentedBooks(currentAccount.getAccountId());
                System.out.println("Rented Books");
                for (RentedBook rentedBook : rentedBooks) {
                    Book book = modifyBooks.getBook(rentedBook.getBookID());
                    System.out.println(book.getBookName());
                }
                break;
            case 2:
                searchMenu();
                break;
            case 3:
                adminAccountLookUpMenu();
                break;
            case 4:
                adminAddBookMenu();
                break;
            case 5:
                adminDeleteBookMenu();
                break;
            case 6:
                currentAccount = null;
                loginMenu();
                break;
            case 7:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 7.");
        }
    }
}
