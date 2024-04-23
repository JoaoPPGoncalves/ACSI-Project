package com.tub;

import com.tub.models.Cartao;
import com.tub.models.Conta;
import com.tub.repositories.CartaoRepository;
import com.tub.repositories.ContaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@ComponentScan("com.tub.repositories")
public class JavaKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaKafkaApplication.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate, ContaRepository contaRepository, CartaoRepository cartaoRepository){
        return args -> {


            //cartaoRepository.save(new Cartao(true));

            System.out.println("cartoes" + cartaoRepository.findAll());

            //Cartao cartao = new Cartao(true);

            //contaRepository.save(new Conta("Francisco" ,cartao, 0 ));

            //kafkaTemplate.send("qrcode", "Hello kafka!");
        };
    }

}
