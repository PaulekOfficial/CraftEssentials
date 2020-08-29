package pro.paulek.CraftEssentials.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AzureTranslator implements Translator {

    private final Gson gson = new Gson();
    private final String IGNORE_TO_TRANSLATE_TAG = "</div><div class=\"notranslate\">";
    private final String IGNORE_TO_TRANSLATE_TAG_END = "</div><div>";

    private final String endPoint;
    private final String apiKey;
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
        List<Translation> translations = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(endPoint + "/translate?api-version=3.0&to=" + locale.getLanguage() + "&textType=html").openConnection();

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
                byte[] input = completeBody(messages).getBytes(StandardCharsets.UTF_8);
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

                for(AzureResponse azureResponse : this.parseJson(response.toString())) {
                    azureResponse.getTranslations().forEach(azureTranslation -> {
                        translations.add(new Translation(azureTranslation));
                    });
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return translations;
    }

    @Override
    public Translation translate(Locale locale, String message) {
        return translate(locale, new String[]{message}).get(0);
    }

    private List<AzureResponse> parseJson(String json) {
        return gson.fromJson(json, new TypeToken<List<AzureResponse>>(){}.getType());
    }

    private String completeBody(String... messags) {
        List<AzureRequest> messages = new ArrayList<>();
        for(String message : messags) {
            messages.add(new AzureRequest(message));
        }
        return gson.toJson(messages);
    }

    public String ignoreColorCodesInTranslation(String message) {
        int index = message.indexOf('&');
        if(index == -1) {
            return message;
        }
        do {
            String first = message.substring(0, index);
            String colorCode = message.substring(index, index + 2);
            String second = message.substring(index + 2);
            message = "<div>" + first + IGNORE_TO_TRANSLATE_TAG + colorCode + IGNORE_TO_TRANSLATE_TAG_END + second + "</div>";;
            index = "<div>".length() + first.length() + IGNORE_TO_TRANSLATE_TAG.length() + colorCode.length();
        } while ((index = message.indexOf('&', index)) != -1);
        return message;
    }

    static class AzureTranslation {

        private String text;
        private String to;

        public AzureTranslation(String text, String to) {
            this.text = text;
            this.to = to;
        }

        public String getText() {
            return text;
        }

        public String getTo() {
            return to;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    static class AzureRequest {

        private String Text;

        public AzureRequest(String text) {
            Text = text;
        }

        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }

    }

    static class AzureResponse {

        private Object detectedLanguage;
        private List<AzureTranslation> translations;

        public AzureResponse(Object detectedLanguage, List<AzureTranslation> translations) {
            this.detectedLanguage = detectedLanguage;
            this.translations = translations;
        }

        public Object getDetectedLanguage() {
            return detectedLanguage;
        }

        public List<AzureTranslation> getTranslations() {
            return translations;
        }
    }

}
