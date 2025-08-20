package com.jrm.conversor.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CurrencyApiClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiKey = System.getenv("EXCHANGE_API_KEY");

    /**
     * Si hay EXCHANGE_API_KEY usa ExchangeRate-API (/pair/{from}/{to});
     * si no, usa exchangerate.host (sin key) como respaldo.
     */
    public String getPair(String from, String to) throws IOException, InterruptedException {
        String url = (apiKey != null && !apiKey.isBlank())
                ? "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + from + "/" + to
                : "https://api.exchangerate.host/convert?from=" + from + "&to=" + to;

        // DEBUG para verificar proveedor y URL (la key queda enmascarada)
        boolean usingKey = (apiKey != null && !apiKey.isBlank());
        String shownUrl = usingKey
                ? "https://v6.exchangerate-api.com/v6/****/pair/" + from + "/" + to
                : url;
        System.out.println("[DEBUG] Provider=" + (usingKey ? "ExchangeRate-API" : "exchangerate.host")
                + " URL=" + shownUrl);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("User-Agent", "CurrencyConverter/1.0")
                .GET()
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            throw new IOException("HTTP " + res.statusCode() + ": " + res.body());
        }
        String ct = res.headers().firstValue("Content-Type").orElse("").toLowerCase();
        if (!ct.contains("application/json")) {
            throw new IOException("Respuesta no-JSON (Content-Type=" + ct + ")");
        }
        return res.body();
    }
}
