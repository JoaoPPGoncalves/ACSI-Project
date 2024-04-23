package com.tub.controllers;

import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private KafkaTemplate<String, String> kafkaTemplate;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/qrcode")
    public void publishQrCode(@RequestParam Map<String, String> body) {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            System.out.println(entry);
            //
        }

        JSONObject json = new JSONObject();



        body.forEach((key, value) -> {
            json.put("message", key);

            kafkaTemplate.send("qrcode", json.toString());//enviar para o topico

            System.out.println((String.format("body '%s' = %s", key, value)));
        });


    }
}
