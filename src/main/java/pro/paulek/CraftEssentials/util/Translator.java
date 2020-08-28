package pro.paulek.CraftEssentials.util;


import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

public interface Translator {

    List<Translation> translate(Locale locale, String... messages);

    Translation translate(Locale locale, String message);

    class Translation {

        private String text;
        private String to;

        public Translation(String text, String to) {
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

}
