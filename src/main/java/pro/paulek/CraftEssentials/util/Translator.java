package pro.paulek.CraftEssentials.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Translator {

    //private final JsonParser jsonParser = new JsonParser();

    private String endPoint;
    private Map<String, String> headers;
    private String method;

    public Translator(String endPoint, Map<String, String> headers, String method) {
        this.endPoint = endPoint;
        this.headers = headers;
        this.method = method;
    }

    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "api.deepl.com");
        headers.put("User-Agent", "CraftEssentials");
        headers.put("Accept", "*/*");
        headers.put("Content-Length", "54");
        headers.put("content-type", "application/x-www-form-urlencoded");
        Translator translator = new Translator("https://api.deepl.com/v2/translate?auth_key=309867", headers, "POST");
        System.out.println(translator.translate("Hello world", Locale.FRANCE.getLanguage()));
    }

    public String translate(String message, String target) {
        String body = "auth_key=309867&text={message}&target_lang={target}";
        body = body.replace("{target}", target);
        body = body.replace("{message}", message);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(endPoint).openConnection();

            //Add headers
            for (String key : headers.keySet()) {
                connection.addRequestProperty(key, headers.get(key));
            }

            //Prepare to connection
            connection.setRequestMethod(method);
            connection.setConnectTimeout(10 * 1000);
            connection.setDoOutput(true);

            //Write request body
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
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
                return response.toString();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "null";
    }

}
