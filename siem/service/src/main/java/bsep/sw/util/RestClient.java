package bsep.sw.util;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


public class RestClient {

    public static String get(final String uri) throws IOException {
        final RestTemplate rest = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");

        final HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        final ResponseEntity<String> responseEntity = rest.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        return responseEntity.getBody();
    }
}
