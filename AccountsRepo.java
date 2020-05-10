package my.test.banking.customer.transactions.repo;

import my.test.banking.customer.transactions.dto.AccountDTO;
import my.test.banking.customer.transactions.enums.ErrorEnum;
import my.test.banking.customer.transactions.exceptions.AccountServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class AccountsRepo {


    @PostConstruct
    public void init(){
        accountDTOMap = new HashMap<>();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("100");
        accountDTO.setCustomerName("abc");
        accountDTO.setAmount(new Long(500));
        accountDTOMap.put("100",accountDTO);

        AccountDTO accountDTO2 = new AccountDTO();
        accountDTO2.setAccountNumber("200");
        accountDTO2.setCustomerName("def");
        accountDTO2.setAmount(new Long(1000));
        accountDTOMap.put("200",accountDTO2);
    }

    private Map<String, AccountDTO> accountDTOMap;

//    ReadWriteLock lock = new ReentrantReadWriteLock();

    public Long getBalanceForAccountNumber(String accountNumber) {
        if(accountDTOMap.containsKey(accountNumber)){
            AccountDTO accountDTO = accountDTOMap.get(accountNumber);
            Long amount = null;
            ReentrantReadWriteLock reentrantReadWriteLock = accountDTO.getReentrantReadWriteLock();
            try{
                reentrantReadWriteLock.readLock().lock();
                amount = accountDTOMap.get(accountNumber).getAmount();
            }finally {
                reentrantReadWriteLock.readLock().unlock();
            }
            return amount;
        }
        throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
    }

    public void addMoney(String accountNumber, String amount) {
        if(accountDTOMap.containsKey(accountNumber)){
            AccountDTO accountDTO = accountDTOMap.get(accountNumber);
            ReentrantReadWriteLock reentrantReadWriteLock = accountDTO.getReentrantReadWriteLock();
            try{
                reentrantReadWriteLock.writeLock().lock();
                Long currentAmount = accountDTO.getAmount();
                accountDTO.setAmount(Long.valueOf(amount).longValue() + currentAmount.longValue());
            }finally {
                reentrantReadWriteLock.writeLock().unlock();
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
            try{
                firstAcc.getReentrantReadWriteLock().writeLock().lock();
                secAcc.getReentrantReadWriteLock().writeLock().lock();
                AccountDTO fromAccount = accountDTOMap.get(fromAccountNumber);
                AccountDTO toAccount = accountDTOMap.get(toAccountNumber);
                //check if from account has enough balance

                Long fromAccAmt = fromAccount.getAmount();
                System.out.println("Thread id : "+Thread.currentThread().getName());
                System.out.println("fromAccAmt" + fromAccAmt.longValue());
                Long amountToTransfer = new Long(Long.valueOf(amount));
                if(fromAccAmt.longValue() < amountToTransfer.longValue()) {

                    throw new AccountServiceException("Insufficient Balance in account "+ fromAccount.getAccountNumber(), HttpStatus.BAD_REQUEST, ErrorEnum.INSUFFICIENT_BAL);
                }
                long finalFromAccAmount = fromAccAmt - amountToTransfer.longValue();
                fromAccount.setAmount(new Long(finalFromAccAmount));
                accountDTOMap.put(fromAccountNumber, fromAccount);
                System.out.println("toAccount "+toAccount.getAmount().longValue());
                long finalToAccountAmt = toAccount.getAmount() + amountToTransfer.longValue();
                toAccount.setAmount(new Long(finalToAccountAmt));
                accountDTOMap.put(toAccountNumber, toAccount);

            }finally {
                secAcc.getReentrantReadWriteLock().writeLock().unlock();
                firstAcc.getReentrantReadWriteLock().writeLock().unlock();
            }
        }else{
            throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
        }

    }

    public void withDrawMoney(String accountNumber, String amount){

        if(accountDTOMap.containsKey(accountNumber)){
            AccountDTO accountDTO = accountDTOMap.get(accountNumber);
            try{
                accountDTO.getReentrantReadWriteLock().writeLock().lock();
                long currAmount = accountDTO.getAmount().longValue();
                if(currAmount <= 0 || (currAmount - Long.valueOf(amount)) < 0){
                    throw new AccountServiceException("Can't withdraw ", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_AMOUNT);
                }
                accountDTO.setAmount(currAmount - Long.valueOf(amount));
            }finally {
                accountDTO.getReentrantReadWriteLock().writeLock().unlock();
            }
        }else{
            throw new AccountServiceException("Account does not exist", HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_ACCOUNT_NUMBER);
        }
    }


}
