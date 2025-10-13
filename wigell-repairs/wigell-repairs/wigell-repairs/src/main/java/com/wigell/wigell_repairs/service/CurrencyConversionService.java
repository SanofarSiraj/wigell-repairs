package com.wigell.wigell_repairs.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Small conversion service. Hardcoded rate is acceptable per spec.
 */
@Service
public class CurrencyConversionService {
    // Example hardcoded rate: 1 SEK = 0.089 EUR (change if you want)
    private static final BigDecimal SEK_TO_EUR = BigDecimal.valueOf(0.089);

    public BigDecimal sekToEur(BigDecimal sek) {
        return sek.multiply(SEK_TO_EUR).setScale(2, BigDecimal.ROUND_HALF_UP);

    }
}
