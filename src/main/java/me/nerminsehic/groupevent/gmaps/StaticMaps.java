package me.nerminsehic.groupevent.gmaps;

import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.util.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class StaticMaps {

    private final String key;
    private final UrlSigner urlSigner;

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";
    private static final String DEFAULT_SIZE = "300x200";
    private static final int DEFAULT_ZOOM = 15;
    private static final int DEFAULT_SCALE = 2;
    private static final String DEFAULT_FORMAT = "png";
    private static final boolean VISUAL_REFRESH = true;

    private static final List<String> DEFAULT_STYLES = List.of(
            "feature:all|element:labels.text.fill|color:0x4b505b",
            "feature:all|element:labels.text.stroke|color:0xffffff|visibility:on",
            "feature:administrative.land_parcel|element:all|visibility:off",
            "feature:landscape.man_made|element:all|visibility:on",
            "feature:landscape.man_made|element:geometry.fill|color:0xe9e9eb",
            "feature:poi|element:all|visibility:off",
            "feature:road|element:geometry.fill|color:0xffffff",
            "feature:road|element:labels.text.fill|color:0x787c84",
            "feature:road|element:labels.icon|visibility:off",
            "feature:road.highway|element:geometry.fill|color:0xffbe32",
            "feature:road.highway|element:geometry.stroke|color:0xffbe32",
            "feature:transit|element:labels.text.fill|visibility:on|color:0x787c84",
            "feature:transit.line|element:geometry.fill|color:0xd2d4d6"
    );

    public StaticMaps(@Value("${google.maps.static.api-key}") String key, UrlSigner urlSigner) {
        this.urlSigner = urlSigner;
        this.key = key;
    }

    public String create(Address address) {
        String encodedUrl = buildUrlString(address);

        try {
            return urlSigner.sign(encodedUrl);
        } catch(IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            return encodedUrl;
        }
    }

    private String buildUrlString(Address address) {
        String formattedAddress = Encoder.encodeUtf8(address.getFormatted());
        String url = "%s?key=%s&center=%s&size=%s&zoom=%d&scale=%d&format=%s&visual_refresh=%s&markers=%s".formatted(
                BASE_URL,
                key,
                formattedAddress,
                DEFAULT_SIZE,
                DEFAULT_ZOOM,
                DEFAULT_SCALE,
                DEFAULT_FORMAT,
                VISUAL_REFRESH,
                createMarker(address)
        );

        return stylize(url, DEFAULT_STYLES);
    }

    private String createMarker(Address address) {
        return Encoder.encodeUtf8("scale:1|shadow:false|%s".formatted(address.getFormatted()));
    }


    private String stylize(String map, List<String> styles) {
        StringBuilder url = new StringBuilder(map);
        styles.forEach(style -> {
           url.append("&style=");
           url.append(Encoder.encodeUtf8(style));
        });

        return url.toString();
    }
}
