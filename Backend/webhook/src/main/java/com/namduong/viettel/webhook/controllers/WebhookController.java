package com.namduong.viettel.webhook.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger =  LoggerFactory.getLogger(WebhookController.class);
    @GetMapping(value = "callback")
    public String callback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("callback:");
        String mode = request.getParameter("hub.mode");
        String challenge = request.getParameter("hub.challenge");
        String token = request.getParameter("hub.verify_token");
        logger.info("mode:" + mode);
        logger.info("challenge:" + challenge);
        logger.info("token:" + token);
        if ("subscribe".equals(mode) && token != null && token.equals("facebook_tokennnnnn")) {
            response.setHeader("content-type", "application/text");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setStatus(200);
        } else {
            response.setHeader("content-type", "application/text");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setStatus(403);
            challenge = "Invalid token";
        }
        return challenge;
    }

    @PostMapping(value = "callback")
    public ResponseEntity<String> callback(@RequestBody String input) {
        logger.info("requestBody:" + input);
        //messagingTemplate.convertAndSend("/topic/messages", input);
        String url = "https://ethical-coral-apparently.ngrok-free.app/callback";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(input, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return new ResponseEntity<>(input, HttpStatus.OK);
    }
}
