package my.test.banking.customer.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(value = "/health", produces = { "application/json" })
    public ResponseEntity<?> health() {

        return new ResponseEntity<>("{\"status\":\"UP\"}", HttpStatus.OK);
    }
}
