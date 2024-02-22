package io.include9it.finance_java_application.client;

import feign.Param;
import feign.RequestLine;

public interface FixerApi {
    @RequestLine("GET /latest?base={base}")
    FixerRates getLatestRates(
            @Param("base") String base
    );
}
