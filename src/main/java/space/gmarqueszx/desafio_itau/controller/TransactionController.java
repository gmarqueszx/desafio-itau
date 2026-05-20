package space.gmarqueszx.desafio_itau.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.gmarqueszx.desafio_itau.model.TransactionRequest;
import space.gmarqueszx.desafio_itau.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionRequest request) {
        service.validationTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransactions() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
