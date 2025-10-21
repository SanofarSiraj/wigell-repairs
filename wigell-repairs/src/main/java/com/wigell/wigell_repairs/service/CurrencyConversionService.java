package com.wigell.wigell_repairs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyConversionService.class);
    private static final String API_URL = "https://api.exchangerate.host/latest?base=SEK&symbols=EUR";
    private static final BigDecimal FALLBACK_RATE = BigDecimal.valueOf(11.20);

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal sekToEur(BigDecimal sek) {
        if (sek == null) return BigDecimal.ZERO;

        try {
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            Double rate = (Double) rates.get("EUR");
            BigDecimal eurRate = BigDecimal.valueOf(rate);
            BigDecimal result = sek.multiply(eurRate).setScale(2, RoundingMode.HALF_UP);
            log.info("Converted {} SEK → {} EUR (rate {})", sek, result, eurRate);
            return result;
        } catch (Exception e) {
            log.warn("Currency API unavailable; using fallback rate {}", FALLBACK_RATE);
            return sek.divide(FALLBACK_RATE, 2, RoundingMode.HALF_UP);
        }
    }
}
