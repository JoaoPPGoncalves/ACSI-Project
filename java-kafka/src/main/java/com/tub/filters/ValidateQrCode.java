package com.tub.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

public class ValidateQrCode {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public ValidateQrCode() {
    }

    public void sendMessage(String topicName, String message) {
        kafkaTemplate.send(topicName, message);
    }

    public void validadeQR(String qrCodeInfo){
        String[] qrCodeInfoSeparated = qrCodeInfo.split("\\,");

        System.out.println("olaaaaaaa");

        //sendMessage("teste2", "olaaa");
    }
}
