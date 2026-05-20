package space.gmarqueszx.desafio_itau.service;

import org.springframework.stereotype.Service;
import space.gmarqueszx.desafio_itau.exception.InvalidTransactionException;
import space.gmarqueszx.desafio_itau.model.TransactionRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final List<TransactionRequest> transactions = new ArrayList<>();

    protected void save(TransactionRequest transaction) {
        transactions.add(transaction);
    }

    public void deleteAll() {
        transactions.clear();
    }

    public void validationTransaction(TransactionRequest transaction) {

        if (transaction.getValue() == null || transaction.getTimestamp() == null) {
            throw new InvalidTransactionException("The value and dateTime fields must be filled " +
                    "in.");
        }

        if (transaction.getTimestamp().isAfter(OffsetDateTime.now())) {
            throw new InvalidTransactionException("The transaction cannot happen in the future.");
        }

        if (transaction.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException("The transaction cannot have a negative value.");
        }

        save(transaction);

    }

    public List<TransactionRequest> lastMinuteTransactions() {
        OffsetDateTime limit = OffsetDateTime.now().minusMinutes(1);
        List<TransactionRequest> result = transactions.stream()
                .filter(t -> t.getTimestamp().isAfter(limit))
                .toList();
        return result;
    }
}

