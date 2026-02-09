package com.yourcompany.ratelimiter.cache;

public final class CacheKeys {

    private CacheKeys() {}

    /* ================================
       API KEY → TENANT cache
       ================================ */
    public static String apiKey(String apiKey) {
        return "cp:apikey:" + apiKey;
    }

    /* ================================
       TENANT DEFAULT POLICY cache
       ================================ */
    public static String policy(String tenantId) {
        return "cp:policy:" + tenantId;
    }

    /* ================================
       RATE LIMIT COUNTER (PER ENDPOINT)
       ================================ */
    public static String rateLimitKey(
            String tenantId,
            String apiKey,
            String endpoint,
            String method
    ) {
        return String.format(
                "rl:cnt:%s:%s:%s:%s",
                tenantId,
                apiKey,
                sanitize(endpoint),
                method
        );
    }

    /* ================================
       ENDPOINT-SPECIFIC POLICY cache
       (used in next step)
       ================================ */
    public static String endpointPolicy(
            String tenantId,
            String endpoint,
            String method
    ) {
        return String.format(
                "cp:policy:ep:%s:%s:%s",
                tenantId,
                sanitize(endpoint),
                method
        );
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9:/_-]", "_");
    }
}