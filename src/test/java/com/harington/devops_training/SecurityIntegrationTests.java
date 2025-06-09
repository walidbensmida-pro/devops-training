package com.harington.devops_training;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SecurityIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${test.username}")
    private String testUsername;

    @Value("${test.password}")
    private String testPassword;

    @Test
    @Disabled
    public void testAccessPublicEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/public", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Disabled
    public void testAccessPrivateEndpointWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity("/private", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Disabled
    public void testAccessPrivateEndpointWithAdminToken() {
        // Simulate login to get token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String loginPayload = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", testUsername, testPassword);
        HttpEntity<String> request = new HttpEntity<>(loginPayload, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", request, String.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = Objects.requireNonNull(loginResponse.getBody()).split("\":\"", 2)[1].replace("\"}", "");

        // Use token to access /private
        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> privateResponse = restTemplate.exchange("/private", HttpMethod.GET, entity, String.class);

        assertThat(privateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(privateResponse.getBody()).contains("Ceci est privé et réservé aux ADMIN");
    }
}
