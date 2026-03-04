import java.time.LocalDate;
import java.util.UUID;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a users account in the system, storing the accounts unique identifier
 * name, birthdate and role (member or admin)
 * Each account has a unique identifier UUID assigned on creation
 * 
 * This class provides the constructors to create new accounts or load existing one
 * Allows controlled access to account information through getters
 */
    
public class Account implements Serializable{
/**
   * Unique identifier (UUID) for the account
   * Value is imutable once the account is created
   */
    private final UUID accountId;
    /** Name for the account holder */
    private String accountHolderName;
    /** Birth date of the account holder */
    private LocalDate accountHolderBirthDate;
    /** Role of account holder
     * 2 options MEMBER or ADMIN
     * Based on this the level of access and permissions are changed
     */
    private Role accountHolderRole;

    /**
     * Constructs a new account with a randomly generate ID
     * @param accountHolderName the name of account holder
     * @param accountHolderBirthDate the birth date of the account holder
     * @param accountHolderRole the role of the account holder
     */
    public Account(String accountHolderName, LocalDate accountHolderBirthDate, Role accountHolderRole) {
        this.accountId = UUID.randomUUID();
        setAccountDetails(accountHolderName, accountHolderBirthDate, accountHolderRole);
    }
    /**
     * Constructs an account with a specific ID
     * Used for loading an existing account
     * @param accountId the UUID of the account
     * @param accountHolderName the name of account holder
     * @param accountHolderBirthDate the birth date of account holder
     * @param accountHolderRole the role of the account holder
     */
    public Account(UUID accountId, String accountHolderName, LocalDate accountHolderBirthDate, Role accountHolderRole) {
        this.accountId = accountId;
        setAccountDetails(accountHolderName, accountHolderBirthDate, accountHolderRole);
    }

    /**
     * Returns unique identifier for account
     * @return the account UUID
     */
    public UUID getAccountId() {
        return accountId;
    }
    /**
     * Returns name for the account
     * @return the name of account holder
     */
    public String getAccountHolderName() {
        return accountHolderName;
    }

    /**
     * Returns the birthdate for account
     * @return the account holder birthdate
     */
    public LocalDate getAccountHolderBirthDate() {
        return accountHolderBirthDate;
    }

    /**
     * Returns the role for an account
     * @return the role of account holder
     */
    public Role getAccountHolderRole() {
        return accountHolderRole;
    }
    /**
     * Sets the account holders name by first validating it
     * Then set the parameter to account object
     * @param accountHolderName accounts holders name
     */
    public void setAccountHolderName(String accountHolderName) {
        validateAccountHolderName(accountHolderName);
        this.accountHolderName = accountHolderName;
    }
    /**
     * Sets the account holders birthdate by first validating it
     * Then set the parameter to account object
     * @param accountHolderBirthDate accounts holders birthdate
     */
    public void setAccountHolderBirthDate(LocalDate accountHolderBirthDate) {
        validateAccountHolderBirthDate(accountHolderBirthDate);
        this.accountHolderBirthDate = accountHolderBirthDate;
    }
    /**
     * Sets the account holders role by first validating it
     * Then set the parameter to account object
     * @param accountHolderRole accounts holders role
     */
    public void setAccountHolderRole(Role accountHolderRole) {
        validateAccountHolderRole(accountHolderRole);
        this.accountHolderRole = accountHolderRole;
    }

    /**
     * Checks to see if input is in correct format
     * @param accountHolderName the name of the account holder
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void validateAccountHolderName(String accountHolderName) {
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty");
        }
    }
    /**
     * Checks to see input is correct and not in the future
     * @param accountHolderBirthDate the account holder birthdate
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void validateAccountHolderBirthDate(LocalDate accountHolderBirthDate) {
        if (accountHolderBirthDate == null || accountHolderBirthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Account holder birth date cannot be null or in the future");
        }
    }
    /**
     * Checks to see if role is valid
     * @param accountHolderRole the role of account holder
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void validateAccountHolderRole(Role accountHolderRole) {
        if (accountHolderRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }

    /**
     * Takes the account parameters and validates using method validate[parameter]
     * If valid set account with parameter
     * @param accountHolderName the account holders name
     * @param accountHolderBirthDate the account holders birthdate
     * @param accountHolderRole the account holders role
     */
    private void setAccountDetails(String accountHolderName, LocalDate accountHolderBirthDate, Role accountHolderRole) {
        // validate name, birthdate and role
        validateAccountHolderName(accountHolderName);
        validateAccountHolderBirthDate(accountHolderBirthDate);
        validateAccountHolderRole(accountHolderRole);

        // set parameters to account
        this.accountHolderName = accountHolderName;
        this.accountHolderBirthDate = accountHolderBirthDate;
        this.accountHolderRole = accountHolderRole;
    }

    // SER04-J: Replicate constructor validation during serialization so that
    // an attacker cannot craft a serialized byte stream that bypasses the
    // checks normally enforced by the constructor.
    private void writeObject(ObjectOutputStream out) throws IOException {
        validateAccountHolderName(this.accountHolderName);
        validateAccountHolderBirthDate(this.accountHolderBirthDate);
        validateAccountHolderRole(this.accountHolderRole);
        out.defaultWriteObject();
    }

    // SER04-J: Re-run the same validation the constructor uses after
    // deserialization, so a tampered object cannot survive the process.
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        validateAccountHolderName(this.accountHolderName);
        validateAccountHolderBirthDate(this.accountHolderBirthDate);
        validateAccountHolderRole(this.accountHolderRole);
    }
}
