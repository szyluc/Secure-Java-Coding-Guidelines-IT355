import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        System.out.println("(1) Login"); // leads to log in through UUID
        System.out.println("(2) Create account"); // leads to account creation
        System.out.println("(3) Help"); // exits program
        System.out.println("(4) Exit"); // exits program
        handleLogin();
    }

    public void userMainMenu() throws SQLException, Exception {
        if (currentAccount.getAccountHolderRole() == Role.ADMIN) {
            while (true) {
            adminMainMenu();
            }
        } else {
            while (true) {
            memberMainMenu();
            }
        }
    }

    private void memberMainMenu() throws SQLException, Exception {
        System.out.println("(1) View account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Rent a book"); // leads user to menu for renting book
        System.out.println("(4) Return a book"); // leads user to menu for returning book
        System.out.println("(5) Help"); // displays helps information to user
        System.out.println("(6) Logout"); // logs user out
        System.out.println("(7) Exit"); // exits program
        handleUserMainMenu();
    }

    private void adminMainMenu() throws SQLException, Exception {
        System.out.println("(1) View account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Rent a book"); // leads user to menu for renting book
        System.out.println("(4) Return a book"); // leads user to menu for returning book
        System.out.println("(5) Look up another account"); // leads to separate menu for UUID search
        System.out.println("(6) Add a book"); // leads to separate menu for adding book
        System.out.println("(7) Delete a book"); // leads to separate menu for deleting book
        System.out.println("(8) Help"); // displays helps information to user
        System.out.println("(9) Logout"); // logs user out
        System.out.println("(10) Exit"); // exits program
        handleAdminMainMenu();
    }

    private void searchMenu() throws SQLException, Exception {
        System.out.println("Select which filter to search by:");
        System.out.println("(1) Search by book name");
        System.out.println("(2) Search by book author");
        System.out.println("(3) Search by book genre");
        System.out.println("(4) Help");
        System.out.println("(5) Back");
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

    private void adminAddBookMenu() throws SQLException, Exception {
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

    private void adminDeleteBookMenu() throws SQLException, Exception {
        System.out.println("Enter book ID: ");
        UUID bookId = UUID.fromString(scanner.nextLine());
        ModifyBooks modifyBooks = new ModifyBooks();
        Book bookToDelete = modifyBooks.getBook(bookId);
        modifyBooks.removeBook(bookToDelete.getBookId());
        if (currentAccount.getAccountHolderRole() == Role.ADMIN) {
            adminMainMenu();
        } else {
            memberMainMenu();
        }
    }

    private void handleSearchMenu() throws SQLException, Exception {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consumes the new line character
        ModifyBooks modifyBooks = new ModifyBooks();
        List<Book> books;
        switch (choice) {
            case 1:
                System.out.println("Enter book name: ");    
                String bookName = scanner.nextLine();
                books = modifyBooks.getBookByName(bookName);
                if(books.isEmpty()){
                    System.out.println("No books found with that name.");
                }
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
                if(books.isEmpty()){
                    System.out.println("No books found by that author.");
                }
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
                if(books.isEmpty()){
                    System.out.println("No books found in that genre.");
                }
                for (Book book : books) {
                    System.out.println("ID: " + book.getBookId());
                    System.out.println("Name: " + book.getBookName());
                    System.out.println("Author: " + book.getBookAuthor());
                    System.out.println("Genre: " + book.getBookCategory());
                }
                break;
            case 4:
                readHelpInfo("./docs/searchMenuHelp.txt");
                searchMenu();
                break;
            case 5:
                Role role = currentAccount.getAccountHolderRole();
                boolean isAdmin = role == Role.ADMIN;
                if (isAdmin) {
                    adminMainMenu();
                } else {
                    memberMainMenu();
                }
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 5.");
        }
    }

    private void handleLogin() throws SQLException {
        ModifyAccounts modifyAccounts = new ModifyAccounts();
        int choice = scanner.nextInt();
        // We need to consume the "\n" character after reading a number
        scanner.nextLine();
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
                System.out.println("Account ID:" + currentAccount.getAccountId());
                System.out.println("Store this ID some secure for future logins.");
                break;
            case 3:
                readHelpInfo("./docs/loginMenuHelp.txt");
                loginMenu();
                break;
            case 4:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 4.");
        }
    }

    private void handleUserMainMenu() throws SQLException, Exception {
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
                modifyRentedBooks.createRentedBooksTable(); // create rented book table if necessary
                ModifyBooks modifyBooks = new ModifyBooks();
                modifyBooks.createBookTable(); // create book table if necessary
                if (modifyRentedBooks.getRowCount("a_id", currentAccount.getAccountId().toString()) == 0) {
                    System.out.println("No books have been checked out so far.");
                } else {
                    List<RentedBook> rentedBooks = modifyRentedBooks.getRentedBooks(currentAccount.getAccountId());
                    System.out.println("Rented Books:");
                    for (RentedBook rentedBook : rentedBooks) {
                        Book book = modifyBooks.getBook(rentedBook.getBookID());
                        System.out.println(book.toString());
                    }
                }
                break;
            case 2:
                searchMenu();
                break;
            case 3:
                handleRentBookMenu();
                break;
            case 4:
                handleReturnBookMenu();
                break;
            case 5:
                readHelpInfo("./docs/memberMenuHelp.txt");
                memberMainMenu();
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

    private void handleAdminMainMenu() throws SQLException, Exception {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consumes next line
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
                handleRentBookMenu();
                break;
            case 4:
                handleReturnBookMenu();
                break;
            case 5:
                adminAccountLookUpMenu();
                break;
            case 6:
                adminAddBookMenu();
                break;
            case 7:
                adminDeleteBookMenu();
                break;
            case 8:
                readHelpInfo("./docs/adminMenuHelp.txt");
                adminMainMenu();
                break;
            case 9:
                currentAccount = null;
                loginMenu();
                break;
            case 10:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please provide an integer between 1 and 10.");
        }
    }

    private void handleRentBookMenu() throws SQLException, Exception {
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        modifyRentedBooks.createRentedBooksTable(); // create rented book table if necessary
        System.out.println("Enter book ID: ");
        UUID bookId = UUID.fromString(scanner.nextLine());
        ModifyBooks modifyBooks = new ModifyBooks();
        Book book = modifyBooks.getBook(bookId);
        LocalDate rentdate = modifyRentedBooks.rentBook(currentAccount, book);
        String path = System.getProperty("user.dir");
        System.out.println("Your receipt has been saved to: " + path + "/receipt.xml");
    }

    private void handleReturnBookMenu() throws SQLException, Exception {
        scanner.nextLine(); // consumes the new line
        System.out.println("Enter book ID: ");
        UUID bookId = UUID.fromString(scanner.nextLine());
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        ModifyBooks modifyBooks = new ModifyBooks();
        modifyRentedBooks.returnBook(currentAccount.getAccountId(), bookId);
    }

    private void readHelpInfo(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            int buffer;
            char data;
            
            try {
                while ((buffer = fileReader.read()) != -1) {
                    data = (char) buffer;
                    System.out.print(data);
                }
            } finally {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        scanner.close();
    }
}
