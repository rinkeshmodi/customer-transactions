package my.test.banking.customer.transactions.dto;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class AccountDTO {

    private String accountNumber;

    private String customerName;

    private AtomicLong amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public AtomicLong getAmount() {
        return amount;
    }

    public void setAmount(AtomicLong amount) {
        this.amount = amount;
    }
}
