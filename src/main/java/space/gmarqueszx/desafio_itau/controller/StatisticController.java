package space.gmarqueszx.desafio_itau.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.gmarqueszx.desafio_itau.model.StatisticResponse;
import space.gmarqueszx.desafio_itau.service.StatisticService;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    private final StatisticService service;

    public StatisticController(StatisticService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<StatisticResponse> lastMinuteStatistics() {
        StatisticResponse response = service.calculateStatistic();
        return ResponseEntity.ok(response);
    }
}
