package com.epam.rd.jsp.currencies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Currencies {
    private static final BigDecimal NUMBER_ONE = new BigDecimal("1.00000");
    private Map<String, BigDecimal> curs = new TreeMap<>();
    private static final int scaleForBigDecimal = 5;

    public void addCurrency(String currency, BigDecimal weight) {
        curs.put(currency, weight);
    }

    public Collection<String> getCurrencies() {
        return curs.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Map<String, BigDecimal> getExchangeRates(String referenceCurrency) {
        Map<String, BigDecimal> mapWithNewCoefficient = new TreeMap<>();
        BigDecimal rate = curs.get(referenceCurrency);
        for (Map.Entry<String, BigDecimal> entry : curs.entrySet()) {
            if (!entry.getKey().equals(referenceCurrency)) {
                BigDecimal requiredNumber = rate.divide(entry.getValue(), scaleForBigDecimal, RoundingMode.HALF_UP);
                mapWithNewCoefficient.put(entry.getKey(), requiredNumber);
            } else {
                mapWithNewCoefficient.put(entry.getKey(), NUMBER_ONE);
            }
        }
        return mapWithNewCoefficient;
    }

    public BigDecimal convert(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        BigDecimal amountSourceCurrency = curs.get(sourceCurrency).multiply(amount);
        return amountSourceCurrency.divide(curs.get(targetCurrency), scaleForBigDecimal, RoundingMode.HALF_UP);
    }
}
