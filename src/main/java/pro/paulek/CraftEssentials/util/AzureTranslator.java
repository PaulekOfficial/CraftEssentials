package pro.paulek.CraftEssentials.util;

import com.google.gson.*;
import jdk.internal.jline.internal.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AzureTranslator implements Translator {

    private final JsonParser jsonParser = new JsonParser();

    private final String endPoint;
    private final String apiKey;
    @Nullable
    private String region;

    public AzureTranslator(String endPoint, String apiKey) {
        this.endPoint = endPoint;
        this.apiKey = apiKey;
    }

    public AzureTranslator(String endPoint, String region, String apiKey) {
        this.endPoint = endPoint;
        this.region = region;
        this.apiKey = apiKey;
    }

    @Override
    public List<Translation> translate(Locale locale, String... messages) {

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(endPoint + "/translate?api-version=3.0&to=" + locale.getLanguage()).openConnection();

            //Add headers
            connection.addRequestProperty("Ocp-Apim-Subscription-Key", apiKey);
            if(region != null) {
                connection.addRequestProperty("Ocp-Apim-Subscription-Region", region);
            }
            connection.addRequestProperty("Content-type", "application/json");

            //Prepare to connection
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);

            //Write request body
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = "[{'Text':'Hello, what is your name?'},{'Text':'Hello, world!'}]".getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Read response
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = bufferedReader.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return this.parseJson(response.toString());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Translation translate(Locale locale, String message) {
        return null;
    }

    private List<Translation> parseJson(String json) {
        List<Translation> translations = new ArrayList<>();
        JsonArray jsonArray = jsonParser.parse(json).getAsJsonArray().get(1).getAsJsonArray();
        for(JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            translations.add(new Translation(jsonObject.get("text").getAsString(), jsonObject.get("to").getAsString()));
        }
        return translations;
    }

    private String completeBody(String... messags) {
        List<String> messages = new ArrayList<>();
        for(String message : messags) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\"Text\":");
            stringBuilder.append("\"");
            stringBuilder.append(message);
            stringBuilder.append("\"");
            messages.add(stringBuilder.toString());
        }
        return "[" + String.join(",", messages) + "]";
    }

}
