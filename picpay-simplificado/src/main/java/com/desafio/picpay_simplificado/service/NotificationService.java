package com.desafio.picpay_simplificado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class NotificationService {

    private final RestTemplate restTemplate;

    public boolean isAuthorized() {
        try {
            String response = restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", String.class);
            return response != null && response.contains("Authorized");
        } catch (Exception e) {
            return false;
        }
    }

    public void sendNotification(String email) {
        try {
            Map<String, String> payload = Map.of(
                    "message", "You received a payment!",
                    "user", email
            );
            restTemplate.postForObject("https://util.devi.tools/api/v1/notify", payload, String.class);
        } catch (Exception e) {
            System.err.println("Error by sending notification: " + e.getMessage());
        }
    }
}