package com.desafio.picpay_simplificado.web.dto;

import java.util.DoubleSummaryStatistics;

public record StatisticsResponseDto(
        Long count,
        double sum,
        double avg,
        Double min,
        Double max
) {
    public StatisticsResponseDto(DoubleSummaryStatistics stats) {
        this(
                stats.getCount(),
                stats.getSum(),
                stats.getAverage(),
                stats.getCount() > 0 ? stats.getMin() : null,
                stats.getCount() > 0 ? stats.getMax() : null
        );
    }
}