package my.test.banking.customer.transactions.exceptions;

import my.test.banking.customer.transactions.enums.ErrorEnum;
import org.springframework.http.HttpStatus;

public class AccountServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    private ErrorEnum errorEnum;

    public AccountServiceException(String message, HttpStatus httpStatus, ErrorEnum errorEnum) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorEnum = errorEnum;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ErrorEnum getErrorEnum() {
        return errorEnum;
    }

    public void setErrorEnum(ErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }
}
