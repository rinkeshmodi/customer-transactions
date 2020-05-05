package my.test.banking.customer.transactions.enums;

public enum ErrorEnum {

    INVALID_ACCOUNT_NUMBER("101","Account Number Invalid"), UNKNOWN_ERROR("102", "Unknown Error" ),
    INSUFFICIENT_BAL("103", "Insuff Balance"), INVALID_AMOUNT("104", "Invalid Amount to withdraw");

    private String errorCode;

    private String errorMessage;

    private ErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
