package my.test.banking.customer.transactions.service;

import my.test.banking.customer.transactions.dto.DepositMoneyRequestDTO;
import my.test.banking.customer.transactions.dto.DepositMoneyResponseDTO;
import my.test.banking.customer.transactions.dto.TransferMoneyRequestDTO;
import my.test.banking.customer.transactions.dto.WithDrawMoneyRequestDTO;
import my.test.banking.customer.transactions.repo.AccountsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountsRepo accountsRepo;


    public Long checkBalance(String accountNumber) {
        return accountsRepo.getBalanceForAccountNumber(accountNumber);
    }

    public DepositMoneyResponseDTO depositMoney(DepositMoneyRequestDTO depositMoneyRequestDTO) {
        accountsRepo.addMoney(depositMoneyRequestDTO.getAccountNumber(), depositMoneyRequestDTO.getAmount());
        return new DepositMoneyResponseDTO();
    }

    public void transferMoney(TransferMoneyRequestDTO transferMoneyRequestDTO) {
        accountsRepo.transferMoney(transferMoneyRequestDTO.getFromAccountNumber(), transferMoneyRequestDTO.getToAccountNumber(), transferMoneyRequestDTO.getAmount());
    }

    public void withdrawMoney(WithDrawMoneyRequestDTO withDrawMoneyRequestDTO) {
        accountsRepo.withDrawMoney(withDrawMoneyRequestDTO.getAccountNumber(), withDrawMoneyRequestDTO.getAmount());
    }
}
