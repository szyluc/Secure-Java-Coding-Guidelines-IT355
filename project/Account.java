

import java.time.Instant;
import java.util.UUID;

public class Account {
    private final String accountId = UUID.randomUUID().toString();
    private String accountHolderName;
    private Instant accountHolderBirthDate;
    private Role role;

    public Account(String accountHolderName, Instant accountHolderBirthDate, Role role) {
        this.accountHolderName = accountHolderName;
        this.accountHolderBirthDate = accountHolderBirthDate;
        this.role = role;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public Instant getAccountHolderBirthDate() {
        return accountHolderBirthDate;
    }

    public void setAccountHolderBirthDate(Instant accountHolderBirthDate) {
        this.accountHolderBirthDate = accountHolderBirthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAccountId() {
        return accountId;
    }

}
