package com.billing.service.config;

import com.common.model.PaymentEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, PaymentEvent> consumerFactory(KafkaProperties properties) {
        Map<String, Object> configProps = new HashMap<>(properties.buildConsumerProperties());

        // Configure key and value deserializers
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Configure ErrorHandlingDeserializer with JsonDeserializer as the delegate
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // Correctly configure trusted packages and default type
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.common.model.PaymentEvent");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.common.model");

        // Return DefaultKafkaConsumerFactory with proper deserializer
        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(PaymentEvent.class, false))
        );
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> containerFactory(
            KafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(properties));
        return factory;
    }
}
