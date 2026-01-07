package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class RestClient {
    // Port 8081, Context Path /api/passenger-profile
    private static final String PASSENGER_SERVICE_URL = "http://localhost:8081/api/passenger-profile";
    private static final String BOOKING_SERVICE_URL = "http://localhost:8082";
    
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

    /**
     * @deprecated Use isPassengerServiceAvailable() or isBookingServiceAvailable()
     */
    @Deprecated
    public boolean isServiceAvailable() {
        return isPassengerServiceAvailable();
    }

    public boolean isPassengerServiceAvailable() {
        return isServiceAvailable(PASSENGER_SERVICE_URL);
    }

    public boolean isBookingServiceAvailable() {
        return isServiceAvailable(BOOKING_SERVICE_URL + "/api/booking/ping");
    }

    private boolean isServiceAvailable(String url) {
        try {
            restTemplate.getForEntity(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Generic methods that default to Passenger Service for backward compatibility
    public <T> T get(String endpoint, Class<T> responseType) {
        return getFromService(PASSENGER_SERVICE_URL, endpoint, responseType);
    }

    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) {
        return postToService(PASSENGER_SERVICE_URL, endpoint, requestBody, responseType);
    }

    public <T> T put(String endpoint, Object requestBody, Class<T> responseType) {
        return putToService(PASSENGER_SERVICE_URL, endpoint, requestBody, responseType);
    }

    // Service-specific methods
    public <T> T getPassenger(String endpoint, Class<T> responseType) {
        return getFromService(PASSENGER_SERVICE_URL, endpoint, responseType);
    }

    public <T> T getBooking(String endpoint, Class<T> responseType) {
        return getFromService(BOOKING_SERVICE_URL, endpoint, responseType);
    }
    
    public <T> T postBooking(String endpoint, Object requestBody, Class<T> responseType) {
        return postToService(BOOKING_SERVICE_URL, endpoint, requestBody, responseType);
    }

    public <T> T putBooking(String endpoint, Object requestBody, Class<T> responseType) {
        return putToService(BOOKING_SERVICE_URL, endpoint, requestBody, responseType);
    }

    // Internal implementation methods
    private <T> T getFromService(String baseUrl, String endpoint, Class<T> responseType) {
        try {
            return restTemplate.getForObject(baseUrl + endpoint, responseType);
        } catch (RestClientException e) {
            System.err.println("GET request failed: " + e.getMessage());
            return null;
        }
    }

    private <T> T postToService(String baseUrl, String endpoint, Object requestBody, Class<T> responseType) {
        try {
             org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
             headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
             org.springframework.http.HttpEntity<Object> entity = new org.springframework.http.HttpEntity<>(requestBody, headers);
             
             return restTemplate.postForObject(baseUrl + endpoint, entity, responseType);

        } catch (RestClientException e) {
            System.err.println("POST request failed: " + e.getMessage());
            throw e; 
        }
    }

    private <T> T putToService(String baseUrl, String endpoint, Object requestBody, Class<T> responseType) {
        try {
             org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
             headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
             org.springframework.http.HttpEntity<Object> entity = new org.springframework.http.HttpEntity<>(requestBody, headers);
             
             org.springframework.http.ResponseEntity<T> response = restTemplate.exchange(
                 baseUrl + endpoint,
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