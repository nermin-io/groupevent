package me.nerminsehic.groupevent.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Encoder {
    private Encoder() {} // non-instantiable

    public static String encodeUtf8(String value) {
        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8
        );
    }
}
