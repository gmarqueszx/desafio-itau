package space.gmarqueszx.desafio_itau.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransactionRequest {
    private BigDecimal value;
    private OffsetDateTime timestamp;
}
