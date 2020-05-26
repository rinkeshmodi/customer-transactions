package my.test.banking.customer.transactions.controller;

import my.test.banking.customer.transactions.dto.*;
import my.test.banking.customer.transactions.enums.ErrorEnum;
import my.test.banking.customer.transactions.exceptions.AccountServiceException;
import my.test.banking.customer.transactions.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CustomerTransactionsController {

    @Autowired
    private AccountService accountService;


    @GetMapping(value = "/check-balance", produces = { "application/json" })
    public ResponseEntity<Long> checkBalance(@RequestParam String accountNumber) {
        Long balanceAmount =  accountService.checkBalance(accountNumber);
        return new ResponseEntity<>(balanceAmount, HttpStatus.OK);
    }

    @PostMapping(value = "/deposit-money", produces = { "application/json" })
    public ResponseEntity<DepositMoneyResponseDTO> depositMoney(@RequestBody DepositMoneyRequestDTO depositMoneyRequestDTO) {
        DepositMoneyResponseDTO depositMoneyResponseDTO = null;
        HttpStatus httpStatus = null;
        try{
            depositMoneyResponseDTO = accountService.depositMoney(depositMoneyRequestDTO);
            depositMoneyResponseDTO.setStatus("SUCCESS");
            httpStatus = HttpStatus.OK;
        }catch (AccountServiceException e){
            e.printStackTrace();
            depositMoneyResponseDTO = new DepositMoneyResponseDTO();
            ResponseError responseError = new ResponseError();
            ErrorEnum errorEnum = e.getErrorEnum();
            responseError.setErrorCode(errorEnum.getErrorCode());
            responseError.setErrorMessage(errorEnum.getErrorMessage());
            depositMoneyResponseDTO.setError(responseError);
            depositMoneyResponseDTO.setStatus("FAILED");
            httpStatus = e.getHttpStatus();
        }catch (Exception e){
            e.printStackTrace();
            depositMoneyResponseDTO = new DepositMoneyResponseDTO();
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode(ErrorEnum.UNKNOWN_ERROR.getErrorCode());
            responseError.setErrorMessage(ErrorEnum.UNKNOWN_ERROR.getErrorMessage());
            depositMoneyResponseDTO.setError(responseError);
            depositMoneyResponseDTO.setStatus("FAILED");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(depositMoneyResponseDTO, httpStatus);
    }

    @PostMapping(value = "/transfer-money", produces = { "application/json" })
    public ResponseEntity<TransferMoneyResponseDTO> transferMoney(@RequestBody TransferMoneyRequestDTO transferMoneyRequestDTO) {
        TransferMoneyResponseDTO transferMoneyResponseDTO = new TransferMoneyResponseDTO();
        HttpStatus httpStatus = null;
        try{
            accountService.transferMoney(transferMoneyRequestDTO);
            transferMoneyResponseDTO.setStatus("SUCCESS");
            httpStatus = HttpStatus.OK;
        }catch (AccountServiceException e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            ErrorEnum errorEnum = e.getErrorEnum();
            responseError.setErrorCode(errorEnum.getErrorCode());
            responseError.setErrorMessage(errorEnum.getErrorMessage());
            transferMoneyResponseDTO.setError(responseError);
            transferMoneyResponseDTO.setStatus("FAILED");
            httpStatus = e.getHttpStatus();
        }catch (Exception e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode(ErrorEnum.UNKNOWN_ERROR.getErrorCode());
            responseError.setErrorMessage(ErrorEnum.UNKNOWN_ERROR.getErrorMessage());
            transferMoneyResponseDTO.setError(responseError);
            transferMoneyResponseDTO.setStatus("FAILED");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(transferMoneyResponseDTO, httpStatus);
    }

    @PostMapping(value = "/withdraw-money", produces = { "application/json" })
    public ResponseEntity<WithDrawMoneyResponseDTO> withdrawMoney(@RequestBody WithDrawMoneyRequestDTO withDrawMoneyRequestDTO) {
        WithDrawMoneyResponseDTO withDrawMoneyResponseDTO = new WithDrawMoneyResponseDTO();
        HttpStatus httpStatus = null;
        withDrawMoneyResponseDTO.setStatus("SUCCESS");
        httpStatus = HttpStatus.OK;
        try {
            accountService.withdrawMoney(withDrawMoneyRequestDTO);
        }catch (AccountServiceException e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            ErrorEnum errorEnum = e.getErrorEnum();
            responseError.setErrorCode(errorEnum.getErrorCode());
            responseError.setErrorMessage(errorEnum.getErrorMessage());
            withDrawMoneyResponseDTO.setError(responseError);
            withDrawMoneyResponseDTO.setStatus("FAILED");
            httpStatus = e.getHttpStatus();
        }catch (Exception e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode(ErrorEnum.UNKNOWN_ERROR.getErrorCode());
            responseError.setErrorMessage(ErrorEnum.UNKNOWN_ERROR.getErrorMessage());
            withDrawMoneyResponseDTO.setError(responseError);
            withDrawMoneyResponseDTO.setStatus("FAILED");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(withDrawMoneyResponseDTO, httpStatus);
    }
}
