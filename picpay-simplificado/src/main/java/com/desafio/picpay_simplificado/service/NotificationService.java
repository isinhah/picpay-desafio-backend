package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.web.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationService {

    private final RestTemplate restTemplate;

    public boolean isAuthorized() {
        try {
            log.info("Starting authorization check.");
            String response = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", String.class);
            boolean authorized = response != null && response.contains("Authorized");
            log.info("Authorization check result: {}", authorized);
            return authorized;
        } catch (Exception e) {
            log.error("Error occurred during authorization: {}", e.getMessage(), e);
            throw new NotificationException("Error occurred during authorization: " + e.getMessage());
        }
    }

    public void sendNotification(String email) {
        try {
            log.info("Sending notification to {}", email);
            Map<String, String> payload = Map.of(
                    "message", "You received a payment!",
                    "user", email
            );
            restTemplate.postForObject("https://util.devi.tools/api/v1/notify", payload, String.class);
            log.info("Notification sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Error sending notification to {}: {}", email, e.getMessage(), e);
            throw new NotificationException("Error sending notification to " + email + ": " + e.getMessage());
        }
    }
}
