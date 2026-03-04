import java.time.LocalDate;
import java.util.UUID;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Account implements Serializable{
    private final UUID accountId;
    private String accountHolderName;
    private LocalDate accountHolderBirthDate;
    private Role accountHolderRole;

    public Account(String accountHolderName, LocalDate accountHolderBirthDate, Role accountHolderRole) {
        this.accountId = UUID.randomUUID();
        setAccountDetails(accountHolderName, accountHolderBirthDate, accountHolderRole);
    }

    public Account(UUID accountId, String accountHolderName, LocalDate accountHolderBirthDate, Role accountHolderRole) {
        this.accountId = accountId;
        setAccountDetails(accountHolderName, accountHolderBirthDate, accountHolderRole);
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public LocalDate getAccountHolderBirthDate() {
        return accountHolderBirthDate;
    }

    public Role getAccountHolderRole() {
        return accountHolderRole;
    }

    public void setAccountHolderName(String accountHolderName) {
        validateAccountHolderName(accountHolderName);
        this.accountHolderName = accountHolderName;
    }

    public void setAccountHolderBirthDate(LocalDate accountHolderBirthDate) {
        validateAccountHolderBirthDate(accountHolderBirthDate);
        this.accountHolderBirthDate = accountHolderBirthDate;
    }

    public void setAccountHolderRole(Role accountHolderRole) {
        validateAccountHolderRole(accountHolderRole);
        this.accountHolderRole = accountHolderRole;
    }

    private void validateAccountHolderName(String accountHolderName) {
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty");
        }
    }

    private void validateAccountHolderBirthDate(LocalDate accountHolderBirthDate) {
        if (accountHolderBirthDate == null || accountHolderBirthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Account holder birth date cannot be null or in the future");
        }
    }

    private void validateAccountHolderRole(Role accountHolderRole) {
        if (accountHolderRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }

    private void setAccountDetails(String accountHolderName, LocalDate accountHolderBirthDate, Role accountHolderRole) {
        // name
        validateAccountHolderName(accountHolderName);
        this.accountHolderName = accountHolderName;
        // birth date
        validateAccountHolderBirthDate(accountHolderBirthDate);
        this.accountHolderBirthDate = accountHolderBirthDate;
        // role
        validateAccountHolderRole(accountHolderRole);
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
