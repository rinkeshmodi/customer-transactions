package my.test.banking.customer.transactions.dto;

public class WithDrawMoneyResponseDTO {

    private String status;

    private ResponseError error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResponseError getError() {
        return error;
    }

    public void setError(ResponseError error) {
        this.error = error;
    }
}
