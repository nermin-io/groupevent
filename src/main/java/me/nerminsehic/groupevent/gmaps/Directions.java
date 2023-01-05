package me.nerminsehic.groupevent.gmaps;

import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.util.Encoder;
import org.springframework.stereotype.Component;

@Component
public class Directions {

    public String getDirectionsLink(Address address) {
        String encodedAddress = Encoder.encodeUtf8(address.getFormatted());
        return "https://www.google.com/maps/dir//%s/".formatted(encodedAddress);
    }

}
