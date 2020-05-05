package my.test.banking.customer.transactions.repo;

import my.test.banking.customer.transactions.dto.AccountDTO;
import my.test.banking.customer.transactions.enums.ErrorEnum;
import my.test.banking.customer.transactions.exceptions.AccountServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AccountsRepo {

    @PostConstruct
    public void init(){
        accountDTOMap = new ConcurrentHashMap<>();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("100");
        accountDTO.setCustomerName("abc");
        accountDTO.setAmount(new AtomicLong(500));
        accountDTOMap.put("100",accountDTO);

        AccountDTO accountDTO2 = new AccountDTO();
        accountDTO2.setAccountNumber("200");
        accountDTO2.setCustomerName("def");
        accountDTO2.setAmount(new AtomicLong(1000));
        accountDTOMap.put("200",accountDTO2);
    }

    private ConcurrentHashMap<String, AccountDTO> accountDTOMap;


    public AtomicLong getBalanceForAccountNumber(String accountNumber) {
        if(accountDTOMap.containsKey(accountNumber)){
            return accountDTOMap.get(accountNumber).getAmount();
        }
        throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
    }

    public void addMoney(String accountNumber, String amount, int retryCount) {
        if(retryCount == 5){
            throw new AccountServiceException("Transaction declined", HttpStatus.INTERNAL_SERVER_ERROR, ErrorEnum.UNKNOWN_ERROR);
        }
        if(accountDTOMap.containsKey(accountNumber)){
            AtomicLong currentAmount = accountDTOMap.get(accountNumber).getAmount();
            boolean addResult = currentAmount.compareAndSet(currentAmount.longValue(), Long.valueOf(amount).longValue() + currentAmount.longValue());
            if(!addResult) {
                addMoney(accountNumber, amount, retryCount + 1);
            }
        }else{
            throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
        }

    }

    public void transferMoney(String fromAccountNumber, String toAccountNumber, String amount) {

        if(accountDTOMap.containsKey(fromAccountNumber) && accountDTOMap.containsKey(toAccountNumber)){
            List<String> accNumbers = new ArrayList<>();
            accNumbers.add(fromAccountNumber);
            accNumbers.add(toAccountNumber);
            Collections.sort(accNumbers);
            AccountDTO firstAcc = accountDTOMap.get(accNumbers.get(0));
            AccountDTO secAcc = accountDTOMap.get(accNumbers.get(1));
            synchronized (firstAcc){
                synchronized (secAcc){
                    AccountDTO fromAccount = accountDTOMap.get(fromAccountNumber);
                    AccountDTO toAccount = accountDTOMap.get(toAccountNumber);
                    //check if from account has enough balance

                    AtomicLong fromAccAmt = fromAccount.getAmount();
                    System.out.println("Thread id : "+Thread.currentThread().getName());
                    System.out.println("fromAccAmt" + fromAccAmt.longValue());
                    AtomicLong amountToTransfer = new AtomicLong(Long.valueOf(amount));
                    if(fromAccAmt.longValue() < amountToTransfer.longValue()) {

                        throw new AccountServiceException("Insufficient Balance in account "+ fromAccount.getAccountNumber(), HttpStatus.BAD_REQUEST, ErrorEnum.INSUFFICIENT_BAL);
                    }
                    long finalFromAccAmount = fromAccAmt.addAndGet(- amountToTransfer.longValue());
                    fromAccount.setAmount(new AtomicLong(finalFromAccAmount));
                    accountDTOMap.put(fromAccountNumber, fromAccount);
                    System.out.println("toAccount "+toAccount.getAmount().longValue());
                    long finalToAccountAmt = toAccount.getAmount().addAndGet(amountToTransfer.longValue());
                    toAccount.setAmount(new AtomicLong(finalToAccountAmt));
                    accountDTOMap.put(toAccountNumber, toAccount);
                }

            }
        }else{
            throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
        }

    }

    public void withDrawMoney(String accountNumber, String amount, int retryCount){
        if(retryCount == 5){
            throw new AccountServiceException("Transaction declined", HttpStatus.INTERNAL_SERVER_ERROR, ErrorEnum.UNKNOWN_ERROR);
        }
        if(accountDTOMap.containsKey(accountNumber)){
            AtomicLong currAmount = accountDTOMap.get(accountNumber).getAmount();
            if(currAmount.longValue() <= 0){
                throw new AccountServiceException("Can't withdraw ", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_AMOUNT);
            }
            boolean isWithdrawSuccess = currAmount.compareAndSet(currAmount.longValue(), currAmount.longValue() - Long.valueOf(amount));
            if(!isWithdrawSuccess){
                withDrawMoney(accountNumber, amount, retryCount + 1);
            }
        }else{
            throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
        }
    }


}
