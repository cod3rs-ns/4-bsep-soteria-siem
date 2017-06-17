package bsep.sw.util;


import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RestClient {

    public static String get(final String uri) throws IOException {
        final RestTemplate rest = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, "*/*");

        final HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        final ResponseEntity<String> responseEntity = rest.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        return responseEntity.getBody();
    }

}
