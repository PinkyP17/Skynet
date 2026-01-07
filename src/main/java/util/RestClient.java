package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class RestClient {
    // Port 8081, Context Path /api/passenger-profile
    private static final String BASE_URL = "http://localhost:8081/api/passenger-profile";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    private static RestClient instance;
    public static synchronized RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public boolean isServiceAvailable() {
        try {
            try {
                restTemplate.getForEntity(BASE_URL, String.class);
                return true;
            } catch (HttpClientErrorException e) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Spring Boot service check failed: " + e.getMessage());
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
    
    // Added POST method for creating resources
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

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}