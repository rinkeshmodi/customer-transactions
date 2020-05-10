package my.test.banking.customer.transactions.dto;


import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountDTO {

    private String accountNumber;

    private String customerName;

    private Long amount;

    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public ReentrantReadWriteLock getReentrantReadWriteLock() {
        return reentrantReadWriteLock;
    }

    public void setReentrantReadWriteLock(ReentrantReadWriteLock reentrantReadWriteLock) {
        this.reentrantReadWriteLock = reentrantReadWriteLock;
    }

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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
