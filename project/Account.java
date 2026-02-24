import java.time.LocalDate;
import java.util.UUID;

public class Account {
    private final UUID accountId;
    private String accountHolderName;
    private LocalDate accountHolderBirthDate;
    private Role role;

    public Account(String accountHolderName, LocalDate accountHolderBirthDate, Role role) {
        this.accountId = UUID.randomUUID();

        validateAccountHolderName(accountHolderName);
        this.accountHolderName = accountHolderName;

        validateAccountHolderBirthDate(accountHolderBirthDate);
        this.accountHolderBirthDate = accountHolderBirthDate;

        validateRole(role);
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setAccountHolderName(String accountHolderName) {
        validateAccountHolderName(accountHolderName);
        this.accountHolderName = accountHolderName;
    }

    public void setAccountHolderBirthDate(LocalDate accountHolderBirthDate) {
        validateAccountHolderBirthDate(accountHolderBirthDate);
        this.accountHolderBirthDate = accountHolderBirthDate;
    }

    public void setRole(Role role) {
        validateRole(role);
        this.role = role;
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

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }
}
