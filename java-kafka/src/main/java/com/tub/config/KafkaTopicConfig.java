package com.tub.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic testTopic(){
        return TopicBuilder.name("teste").build();
    }

    @Bean
    public NewTopic qrCodeTopic(){
        return TopicBuilder.name("qrcode").build();
    }

    @Bean
    public NewTopic historicoViagemTopic(){
        return TopicBuilder.name("historicoViagem").build();
    }

    @Bean
    public NewTopic verificarInformacoesQrCodeTopic(){
        return TopicBuilder.name("verificarInformacoesQrCode").build();
    }

    @Bean
    public NewTopic registarTentativaTopic(){
        return TopicBuilder.name("registarTentativa").build();
    }

    @Bean
    public NewTopic respostaQrCodeTopic(){
        return TopicBuilder.name("respostaQrCode").build();
    }

    @Bean
    public NewTopic lugaresLivresTopic(){
        return TopicBuilder.name("lugaresLivres").build();
    }

    @Bean
    public NewTopic mandarEmailTopic(){
        return TopicBuilder.name("enviarEmail").build();
    }

    @Bean
    public NewTopic ocupacaoParqueTopic(){
        return TopicBuilder.name("ocupacaoParque").build();
    }
}
