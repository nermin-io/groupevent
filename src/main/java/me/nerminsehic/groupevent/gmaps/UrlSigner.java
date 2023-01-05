package me.nerminsehic.groupevent.gmaps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;

@Component
public class UrlSigner {

  private final byte[] key;

  public String sign(String encodedUrl) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
    URL url = new URL(encodedUrl);
    String request = signRequest(url.getPath(),url.getQuery());

    return url.getProtocol() + "://" + url.getHost() + request;
  }

  public UrlSigner(@Value("${google.maps.static.signing-secret}") String signingSecret) {
    signingSecret = signingSecret.replace('-', '+');
    signingSecret = signingSecret.replace('_', '/');

    key = Base64.getDecoder().decode(signingSecret);
  }

  public String signRequest(String path, String query) throws NoSuchAlgorithmException, InvalidKeyException {
    String resource = path + '?' + query;
    SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(sha1Key);

    byte[] sigBytes = mac.doFinal(resource.getBytes());

    String signature = Base64.getEncoder().encodeToString(sigBytes);
    signature = signature.replace('+', '-');
    signature = signature.replace('/', '_');

    return resource + "&signature=" + signature;
  }
}