package space.gmarqueszx.desafio_itau.service;

import org.springframework.stereotype.Service;
import space.gmarqueszx.desafio_itau.model.StatisticResponse;
import space.gmarqueszx.desafio_itau.model.TransactionRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
public class StatisticService {
    private final TransactionService transactionService;

    public StatisticService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public StatisticResponse calculateStatistic() {
        List<TransactionRequest> lastMinuteTransactions =
                transactionService.lastMinuteTransactions();

        StatisticResponse statisticResponse = new StatisticResponse();

        if (lastMinuteTransactions.isEmpty()) {
            statisticResponse.setCount(0L);
            statisticResponse.setAvg(BigDecimal.ZERO);
            statisticResponse.setMin(BigDecimal.ZERO);
            statisticResponse.setMax(BigDecimal.ZERO);
            statisticResponse.setSum(BigDecimal.ZERO);
            return statisticResponse;
        }

        List<BigDecimal> values = lastMinuteTransactions.stream()
                .map(TransactionRequest::getValue)
                .toList();


        statisticResponse.setCount((long) values.size());

        statisticResponse.setMax(values.stream().max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO));

        statisticResponse.setMin(values.stream().min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO));

        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        statisticResponse.setSum(sum);

        BigDecimal avg = sum.divide(
                new BigDecimal(values.size()), 2, RoundingMode.HALF_UP
        );
        statisticResponse.setAvg(avg);

        return statisticResponse;
    }
}
