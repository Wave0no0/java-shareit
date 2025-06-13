package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

public class BaseClient {

    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, null, null);
    }

    protected ResponseEntity<Object> get(String path, long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, null, body);
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, null, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                          String path,
                                                          long userId,
                                                          @Nullable Map<String, Object> parameters,
                                                          @Nullable T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Object> response;

        try {
            if (parameters != null && !parameters.isEmpty()) {
                response = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                response = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }

        return prepareGatewayResponse(response);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
