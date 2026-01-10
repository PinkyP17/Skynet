package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;

public class FlightCatalogRestClient {
    // Port 8082, Context Path /api/flight-catalog
    private static final String BASE_URL = "http://localhost:8082/api/flight-catalog";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public FlightCatalogRestClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    private static FlightCatalogRestClient instance;
    public static synchronized FlightCatalogRestClient getInstance() {
        if (instance == null) {
            instance = new FlightCatalogRestClient();
        }
        return instance;
    }

    public boolean isServiceAvailable() {
        try {
            try {
                restTemplate.getForEntity(BASE_URL + "/flights", String.class);
                return true;
            } catch (HttpClientErrorException e) {
                return true; // Even 404 means service is running
            }
        } catch (Exception e) {
            System.err.println("Flight Catalog service check failed: " + e.getMessage());
            return false;
        }
    }

    public <T> T get(String endpoint, Class<T> responseType) {
        try {
            return restTemplate.getForObject(BASE_URL + endpoint, responseType);
        } catch (RestClientException e) {
            System.err.println("GET request failed: " + e.getMessage());
            return null;
        }
    }
    
    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) {
        try {
             org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
             headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
             org.springframework.http.HttpEntity<Object> entity = new org.springframework.http.HttpEntity<>(requestBody, headers);
             
             return restTemplate.postForObject(BASE_URL + endpoint, entity, responseType);

        } catch (RestClientException e) {
            System.err.println("POST request failed: " + e.getMessage());
            throw e; 
        }
    }

    public <T> T put(String endpoint, Object requestBody, Class<T> responseType) {
        try {
             org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
             headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
             org.springframework.http.HttpEntity<Object> entity = new org.springframework.http.HttpEntity<>(requestBody, headers);
             
             org.springframework.http.ResponseEntity<T> response = restTemplate.exchange(
                 BASE_URL + endpoint,
                 org.springframework.http.HttpMethod.PUT,
                 entity,
                 responseType
             );
             return response.getBody();

        } catch (RestClientException e) {
            System.err.println("PUT request failed: " + e.getMessage());
            throw e; 
        }
    }
    
    public void delete(String endpoint) {
        try {
            restTemplate.delete(BASE_URL + endpoint);
        } catch (RestClientException e) {
            System.err.println("DELETE request failed: " + e.getMessage());
            throw e;
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
