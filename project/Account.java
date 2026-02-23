

import java.time.Instant;
import java.util.UUID;

public class Account {
    private final UUID accountId;
    private String accountHolderName;
    private Instant accountHolderBirthDate;
    private Role role;

    public Account(String accountHolderName, Instant accountHolderBirthDate, Role role) {
        this.accountId = UUID.randomUUID();

        if (accountHolderName == null) {
            throw new IllegalArgumentException("Account holder name cannot be null");
        } else if (accountHolderName.isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty");
        } else {
            this.accountHolderName = accountHolderName;
        }

        if (accountHolderBirthDate == null) {
            throw new IllegalArgumentException("Account holder birth date cannot be null");
        } else {
            this.accountHolderBirthDate = accountHolderBirthDate;
        }

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        } else {
            this.role = role;
        }
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        if (accountHolderName == null) {
            throw new IllegalArgumentException("Account holder name cannot be null");
        } else if (accountHolderName.isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty");
        } else {
            this.accountHolderName = accountHolderName;
        }
    }

    public Instant getAccountHolderBirthDate() {
        return accountHolderBirthDate;
    }

    public void setAccountHolderBirthDate(Instant accountHolderBirthDate) {
        if (accountHolderBirthDate == null) {
            throw new IllegalArgumentException("Account holder birth date cannot be null");
        } else {
            this.accountHolderBirthDate = accountHolderBirthDate;
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        } else {
            this.role = role;
        }
    }

    public UUID getAccountId() {
        return accountId;
    }

}
