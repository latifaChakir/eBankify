package com.example.ebankify.controller;
import com.example.ebankify.domain.vm.StatisticResponseVM;
import com.example.ebankify.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class StatisticController {
    private StatisticService statisticService;
    @GetMapping("/all")
    public ResponseEntity<StatisticResponseVM> getStatistics() {
        StatisticResponseVM statistics = statisticService.getStatistics();
        return ResponseEntity.ok(statistics);
    }
}
