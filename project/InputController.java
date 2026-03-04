import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.FileHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class InputController {
    private static final String NUM_INPUT_PROVIDE = "Please provide your input: ";
    private static final String UUID_INPUT_PROVIDE = "Please enter your account ID: ";
    private static final String NAME_INPUT_PROVIDE = "Please enter your name: ";
    private static final String DATE_INPUT_PROVIDE = "Please enter your birthdate (YYYY-MM-DD): ";
    private static final String UUID_BOOK_RENT_PROVIDE = "Please enter your book's ID to rent: ";
    private static final String UUID_BOOK_RETURN_PROVIDE = "Please enter your book's ID to return: ";
    private static final String UUID_BOOK_DELETE_PROVIDE = "Please enter your book's ID to delete: ";
    private static final String INPUT_LINES = "----------------";

    private static final Logger logger = Logger.getLogger("InputControllerLogger");
    FileHandler fh;

    try {
        fh = new FileHandler("./logs/InputController.log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    } catch (SecurityException e) {
        System.out.println("SecurityException caught");
    } catch (IOException e) {
        System.out.println("IOException caught");
    }

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
        System.out.println("\nWelcome to the ISU Library!\n");
    }
    
    public void loginMenu() throws SQLException, Exception {
        System.out.println(INPUT_LINES);
        System.out.println("(1) Log In"); // leads to log in through UUID
        System.out.println("(2) Create Account"); // leads to account creation
        System.out.println("(3) Help"); // exits program
        System.out.println("(4) Exit"); // exits program
        System.out.println(INPUT_LINES + "\n");
        handleLogin(); // with the way it is implemented, handleLogin() should be recursive
    }

    public void userMainMenu() throws SQLException, Exception {
        if (currentAccount.getAccountHolderRole() == Role.ADMIN) {
            adminMainMenu();
        } else {
            memberMainMenu();
        }
    }

    private void handleLogin() throws SQLException, Exception {
        ModifyAccounts modifyAccounts = new ModifyAccounts();
        int choice = numericInputValidation(4);
        switch (choice) {
            case 1: // LOGIN
                UUID id = userLoginValidation(modifyAccounts);
                currentAccount = modifyAccounts.getAccount(id);
                userMainMenu();
                break;
            case 2: // CREATE ACCOUNT
                String name = stringInputValidation(NAME_INPUT_PROVIDE);
                System.out.print(DATE_INPUT_PROVIDE);
                LocalDate birthdate = dateInputValidation();
                Account newAccount = new Account(name, birthdate, Role.MEMBER);
                modifyAccounts.addAccount(newAccount);
                currentAccount = newAccount; // current user is the newly made account.
                System.out.println("\nYour account ID: " + currentAccount.getAccountId());
                System.out.println("Important! Store this ID in a secure place for future logins.");
                userMainMenu();
                break;
            case 3: // HELP
                readHelpInfo("./docs/loginMenuHelp.txt");
                loginMenu();
                break;
            case 4: // EXIT
                return;
        }
    }

    private void memberMainMenu() throws SQLException, Exception {
        System.out.println("\n" + INPUT_LINES);
        System.out.println("Welcome, " + currentAccount.getAccountHolderName() + "!\n");
        System.out.println("(1) View Account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Rent a book"); // leads user to menu for renting book
        System.out.println("(4) Return a book"); // leads user to menu for returning book
        System.out.println("(5) Help"); // displays helps information to user
        System.out.println("(6) Log Out"); // logs user out
        System.out.println("(7) Exit"); // exits program
        System.out.println(INPUT_LINES);
        handleUserMainMenu();
    }

    private void adminMainMenu() throws SQLException, Exception {
        System.out.println("\n" + INPUT_LINES);
        System.out.println("Welcome, " + currentAccount.getAccountHolderName() + "!\n");
        System.out.println("(1) View account "); // leads to display of any books currently checked out
        System.out.println("(2) Search for a book"); // leads to separate menu for book search
        System.out.println("(3) Rent a book"); // leads user to menu for renting book
        System.out.println("(4) Return a book"); // leads user to menu for returning book
        System.out.println("(5) Look up another account"); // leads to separate menu for UUID search
        System.out.println("(6) Compare accounts"); // leads to seperate menu for comparing accounts
        System.out.println("(7) Add a book"); // leads to separate menu for adding book
        System.out.println("(8) Delete a book"); // leads to separate menu for deleting book
        System.out.println("(9) Help"); // displays helps information to user
        System.out.println("(10) Logout"); // logs user out
        System.out.println("(11) Exit"); // exits program
        System.out.println(INPUT_LINES);
        handleAdminMainMenu();
    }

    private void searchMenu() throws SQLException, Exception {
        System.out.println("\n" + INPUT_LINES);
        System.out.println("Select which filter to search by:");
        System.out.println("(1) Search by book name");
        System.out.println("(2) Search by book author");
        System.out.println("(3) Search by book genre");
        System.out.println("(4) Help");
        System.out.println("(5) Back");
        System.out.println(INPUT_LINES);
        handleSearchMenu();
    }

    private void adminAccountLookUpMenu() throws SQLException
    {
        UUID id = uuidInputValidation("\nEnter account ID to lookup: ");
        ModifyAccounts modifyAccounts = new ModifyAccounts();
        Account lookedUpAccount = modifyAccounts.getAccount(id);
        System.out.println("\nAccount Name: " + lookedUpAccount.getAccountHolderName());
        System.out.println("Account Birth Date: " + lookedUpAccount.getAccountHolderBirthDate() + "\n");
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        List<RentedBook> rentedBooks = modifyRentedBooks.getRentedBooks(id);
        if (rentedBooks.size() == 0) {
            System.out.println("No books are currently rented.");
        } else {
            for (RentedBook rentedBook : rentedBooks) {
                ModifyBooks modifyBooks = new ModifyBooks();
                Book book = modifyBooks.getBook(rentedBook.getBookID());
                System.out.println(book.toString());
            }
        }
    }

    private void adminAddBookMenu() throws SQLException, Exception {
        String bookName = stringInputValidation("\nEnter book name: ");
        String bookAuthor = stringInputValidation("\nEnter book author: ");
        String bookGenre = stringInputValidation("\nEnter book genre: ");
        Book newBook = new Book(bookName, bookAuthor, bookGenre);
        ModifyBooks modifyBooks = new ModifyBooks();
        modifyBooks.addBook(newBook);
    }

    private void adminDeleteBookMenu() throws SQLException, Exception {
        UUID bookId = uuidInputValidation(UUID_BOOK_DELETE_PROVIDE);
        ModifyBooks modifyBooks = new ModifyBooks();
        Book bookToDelete = modifyBooks.getBook(bookId);
        modifyBooks.removeBook(bookToDelete.getBookId());
    }

    private void handleSearchMenu() throws SQLException, Exception {
        int choice = numericInputValidation(5);
        ModifyBooks modifyBooks = new ModifyBooks();
        List<Book> books;
        switch (choice) {
            case 1:
                String bookName = stringInputValidation("Enter book name: ");
                books = modifyBooks.getBookByName(bookName);
                System.out.println(); // empty line
                if(books.isEmpty()){
                    System.out.println("No books found with that name.");
                } else {
                    for (Book book : books) {
                        System.out.println(book.toString());
                    }
                }
                userMainMenu();
                break;
            case 2:
                String bookAuthor = stringInputValidation("Enter book author: ");
                books = modifyBooks.getBookByAuthor(bookAuthor);
                System.out.println();
                if(books.isEmpty()){
                    System.out.println("No books found by that author.");
                } else {
                    for (Book book : books) {
                        System.out.println(book.toString());
                    }
                }
                userMainMenu();
                break;
            case 3:
                String bookGenre = stringInputValidation("Enter book genre: ");
                books = modifyBooks.getBookByGenre(bookGenre);
                System.out.println();
                if (books.isEmpty()) {
                    System.out.println("No books found in that genre.");
                } else {
                    for (Book book: books) {
                        System.out.println(book.toString());
                    }
                }
                userMainMenu();
                break;
            case 4:
                readHelpInfo("./docs/searchMenuHelp.txt");
                searchMenu();
                break;
            case 5:
                userMainMenu();
                break;
        }
    }

    private void handleUserMainMenu() throws SQLException, Exception {
        int choice = numericInputValidation(7);
        switch (choice) {
            case 1:
                displayUserBooks();
                userMainMenu();
                break;
            case 2:
                searchMenu();
                userMainMenu();
                break;
            case 3:
                handleRentBookMenu();
                userMainMenu();
                break;
            case 4:
                handleReturnBookMenu();
                userMainMenu();
                break;
            case 5:
                readHelpInfo("./docs/memberMenuHelp.txt");
                userMainMenu();
                break;
            case 6:
                currentAccount = null;
                loginMenu();
                break;
            case 7:
                return;
        }
    }

    private void handleAdminMainMenu() throws SQLException, Exception {
        int choice = numericInputValidation(10);
        switch (choice) {
            case 1:
                displayUserBooks();
                userMainMenu();
                break;
            case 2:
                searchMenu();
                userMainMenu();
                break;
            case 3:
                handleRentBookMenu();
                userMainMenu();
                break;
            case 4:
                handleReturnBookMenu();
                userMainMenu();
                break;
            case 5:
                adminAccountLookUpMenu();
                userMainMenu();
                break;
            case 6:
                compareAccountsMenu();
                userMainMenu();
                break;
            case 7:
                adminAddBookMenu();
                userMainMenu();
                break;
            case 8:
                adminDeleteBookMenu();
                break;
            case 9:
                readHelpInfo("./docs/adminMenuHelp.txt");
                userMainMenu();
                break;
            case 10:
                currentAccount = null;
                loginMenu();
                break;
            case 11:
                return;
        }
    }

    private void handleRentBookMenu() throws SQLException, Exception {
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        ModifyBooks modifyBooks = new ModifyBooks();

        modifyRentedBooks.createRentedBooksTable(); // create rented book table if necessary

        UUID bookId = bookTakenValidation(modifyRentedBooks, modifyBooks); 

        Book book = modifyBooks.getBook(bookId);
        LocalDateTime rentdate = modifyRentedBooks.rentBook(currentAccount, book);
        String path = System.getProperty("user.dir");
        System.out.println("\nYou have successfully checked out: " + book.transactionString());
        System.out.println("Your receipt has been saved to: " + path + "/" + rentdate.toString() + ".xml");
    }

    private void handleReturnBookMenu() throws SQLException, Exception {
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        ModifyBooks modifyBooks = new ModifyBooks();

        UUID bookId = bookNotTakenValidation(modifyRentedBooks, modifyBooks);

        Book book = modifyBooks.getBook(bookId);
        modifyRentedBooks.returnBook(currentAccount.getAccountId(), bookId);

        System.out.println("\nYou have successfully returned: " + book.transactionString());
    }

    private int numericInputValidation(int maxVal) {
        System.out.print(NUM_INPUT_PROVIDE);
        int choice = 0; // automatically out of range
        boolean invalidInput = true;
        while (invalidInput) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > maxVal) {
                    System.out.println(invalidInputEnter(maxVal));
                    System.out.print(NUM_INPUT_PROVIDE);
                } else {
                    // if we get here, we have a good number!
                    invalidInput = false;
                }
            } catch (InputMismatchException e) {
                logger.log(Level.WARNING, "Invalid input.", e);
                System.out.println(invalidInputEnter(maxVal));
                System.out.print(NUM_INPUT_PROVIDE);
            } finally {
                scanner.nextLine(); // consume new line character
            }
        }
        return choice;     
    }

    private UUID uuidInputValidation(String askingArgument) {
        String stringResult = null;
        UUID uuidResult = null;
        boolean invalidInput = true;
        while (invalidInput) {
            try {
                System.out.print(askingArgument);
                stringResult = scanner.nextLine();
                // Normalize string
                Normalizer.normalize(stringResult, Form.NFKC);
                uuidResult = UUID.fromString(stringResult);
                // if we make it past this step, we have valid UUID!
                invalidInput = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter a valid ID.");
                System.out.print(askingArgument);
            }
        }
        return uuidResult;
    }

    private LocalDate dateInputValidation() {
        String stringResult = null;
        LocalDate dateResult = null;
        boolean invalidInput = true;
        while (invalidInput) {
            try {
                stringResult = scanner.nextLine();
                // Normalize string
                Normalizer.normalize(stringResult, Form.NFKC);
                dateResult = LocalDate.parse(stringResult);
                // if we make it past this step, we have a valid date!
                invalidInput = false;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid input. Please enter a valid date.");
                System.out.print(DATE_INPUT_PROVIDE);
            }
        }
        return dateResult;
    }

    private String stringInputValidation(String askingArgument) {
        String stringResult = null;
        boolean invalidInput = true;
        while (invalidInput) {
            System.out.print(askingArgument);
            stringResult = scanner.nextLine();
            // Normalize string
            Normalizer.normalize(stringResult, Form.NFKC);
            if (stringResult == null || stringResult.isEmpty() || stringResult.isBlank()) {
                System.out.println("Invalid input. Please enter a valid string.");
                System.out.print(askingArgument);
            } else {
                invalidInput = false;
            }
        }
        return stringResult;
    }

    private String invalidInputEnter(int maxValue) {
        return "Invalid input. Please enter an integer between 1 and " + maxValue + ".";
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
                    System.out.println("IOException caught.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException caught");
        } catch (IOException e) {
            System.out.println("IOException caught");
        }
    }

    private void compareAccountsMenu() throws SQLException{
        System.out.println("Enter first account ID: ");
        UUID id1 = UUID.fromString(scanner.nextLine());
        System.out.println("Enter second account ID: ");
        UUID id2 = UUID.fromString(scanner.nextLine());
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        RentedBook[] account1Books = modifyRentedBooks.getRentedBooks(id1).toArray(new RentedBook[0]);
        RentedBook[] account2Books = modifyRentedBooks.getRentedBooks(id2).toArray(new RentedBook[0]);
        if (Arrays.equals(account1Books, account2Books)) {
            System.out.println("The accounts have rented the same books.");
        } else {
            System.out.println("The accounts have not rented the same books.");
        }
    }
    
        private UUID bookNotTakenValidation(ModifyRentedBooks rentedBooks, ModifyBooks books) throws SQLException {
        boolean invalidId = true;
        UUID bookId = null;

        while (invalidId) {
            bookId = uuidInputValidation(UUID_BOOK_RETURN_PROVIDE);
            if (books.getRowCount("book_id", bookId.toString()) == 0) {
                System.out.println("This book ID does not exist. Please try again.");
            } else if ((rentedBooks.getRowCount("b_id", bookId.toString()) == 0)) {
                System.out.println("This book ID is not rented out. Please try again.");
            } else {
                invalidId = false;
            }
        }
        return bookId;
    }

    private UUID bookTakenValidation(ModifyRentedBooks rentedBooks, ModifyBooks books) throws SQLException {
        // checks if the passed book is either (1) currently rented or (2) does not exist
        boolean invalidId = true;
        UUID bookId = null;
        // first, we must validate the UUID input.
        while (invalidId) {
            bookId = uuidInputValidation(UUID_BOOK_RENT_PROVIDE);
            if ((rentedBooks.getRowCount("b_id", bookId.toString())) > 0) {
                System.out.println("This book ID is currently rented out. Please try again.");
            } else if (books.getRowCount("book_id", bookId.toString()) == 0) {
                System.out.println("This book ID does not exist. Please try again.");
            } else {
                invalidId = false;
            }
        }
        return bookId;
    }

    private void displayUserBooks() throws Exception {
        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        modifyRentedBooks.createRentedBooksTable(); // create rented book table if necessary
        ModifyBooks modifyBooks = new ModifyBooks();
        modifyBooks.createBookTable(); // create book table if necessary
        if (modifyRentedBooks.getRowCount("a_id", currentAccount.getAccountId().toString()) == 0) {
            System.out.println("\nYou do not have any books checked out.");
        } else {
            List<RentedBook> rentedBooks = modifyRentedBooks.getRentedBooks(currentAccount.getAccountId());
            System.out.println("\nRented Books:");
            for (RentedBook rentedBook : rentedBooks) {
                Book book = modifyBooks.getBook(rentedBook.getBookID());
                System.out.println(book.toString());
            }
        }
    }

    private UUID userLoginValidation(ModifyAccounts accounts) throws SQLException {
        boolean invalidId = true;
        UUID accountId = null;

        while (invalidId) {
            accountId = uuidInputValidation(UUID_INPUT_PROVIDE);
            if ((accounts.getRowCount("account_id", accountId.toString())) == 0) {
                System.out.println("This account ID does not exist. Please try again.");
            } else {
                invalidId = false;
            }
        }
        return accountId;
    }

    public void cleanUp() {
        scanner.close();
    }
}
