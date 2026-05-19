package space.gmarqueszx.desafio_itau.service;

import org.springframework.stereotype.Service;
import space.gmarqueszx.desafio_itau.model.TransactionRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final List<TransactionRequest> transactions = new ArrayList<>();

    public void save(TransactionRequest transaction) {
        transactions.add(transaction);
    }

    public void deleteAll() {
        transactions.clear();
    }

    public void validationTransaction(TransactionRequest transaction) {
        if (transaction.getValue() == null || transaction.getDateTime() == null) {
            System.out.println("The value and dateTime fields must be filled in.");
        }

        if (transaction.getDateTime().isAfter(OffsetDateTime.now())) {
            System.out.println("The transaction cannot happen in the future.");
        }

        if (transaction.getValue().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("The transaction cannot have a negative value.");
        }

        save(transaction);
    }

    public List<TransactionRequest> lastMinuteTransactions() {
        OffsetDateTime limit = OffsetDateTime.now().minusMinutes(1);
        return transactions.stream()
                .filter(t -> t.getDateTime().isAfter(limit))
                .toList();
    }
}

