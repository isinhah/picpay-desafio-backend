package com.desafio.picpay_simplificado.web.controller;

import com.desafio.picpay_simplificado.service.StatisticsService;
import com.desafio.picpay_simplificado.web.dto.StatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.DoubleSummaryStatistics;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsResponseDto> getStatistics() {
        DoubleSummaryStatistics stats = statisticsService.getStatistics();
        return ResponseEntity.ok(new StatisticsResponseDto(stats));
    }
}
