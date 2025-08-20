package com.jrm.conversor.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class CurrencyConverter {
    private final CurrencyApiClient api = new CurrencyApiClient();

    /** Devuelve solo la tasa (ej.: 1292.33 para USD→ARS). */
    public double getRate(String from, String to) throws IOException, InterruptedException {
        String json = api.getPair(from, to);

        JsonElement rootEl = JsonParser.parseString(json);
        JsonObject root = rootEl.getAsJsonObject();

        // 1) ExchangeRate-API: { ..., "conversion_rate": 1292.33, ... }
        if (root.has("conversion_rate")) {
            return root.get("conversion_rate").getAsDouble();
        }

        // 2) Frankfurter fallback: { "rates": { "ARS": 1292.33 }, "base": "USD", ... }
        if (root.has("rates") && root.get("rates").isJsonObject()) {
            JsonObject rates = root.getAsJsonObject("rates");
            if (rates.has(to)) {
                return rates.get(to).getAsDouble();
            }
        }

        // Si llegamos aquí, no pudimos extraer la tasa
        String preview = json.length() > 200 ? json.substring(0, 200) + "…" : json;
        throw new IllegalStateException("No se encontró la tasa en la respuesta: " + preview);
    }

    /** Convierte un monto usando la tasa actual. */
    public double convert(String from, String to, double amount) throws IOException, InterruptedException {
        double rate = getRate(from, to);
        return amount * rate;
    }
}
