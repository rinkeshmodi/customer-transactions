package my.test.banking.customer.transactions.dto;

public class WithDrawMoneyRequestDTO {

    private String accountNumber;

    private String amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
