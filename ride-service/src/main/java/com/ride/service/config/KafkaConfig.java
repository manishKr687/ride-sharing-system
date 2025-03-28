package com.ride.service.config;

import com.ride.service.kafka.RideEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public NewTopic notificationTopic(){
        return TopicBuilder.name("notification_topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public ProducerFactory<String, RideEvent> producerFactory(KafkaProperties properties) {
        Map<String, Object> configProps = new HashMap<>(properties.buildProducerProperties());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, RideEvent> kafkaTemplate(KafkaProperties properties) {
        return new KafkaTemplate<>(producerFactory(properties));
    }
    @Bean
    public ConsumerFactory<String, RideEvent> consumerFactory(KafkaProperties properties) {
        Map<String, Object> configProps = new HashMap<>(properties.buildConsumerProperties());
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "ride-group");
        // Configure key and value deserializers
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Configure ErrorHandlingDeserializer with JsonDeserializer as the delegate
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.ride.service.kafka.RideEvent");
        //configure trusted packages
        configProps.put("spring.json.trusted.packages", "com.ride.service.kafka.RideEvent");

        // Return DefaultKafkaConsumerFactory with the correct deserializer
        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(RideEvent.class, false))
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RideEvent> kafkaListenerContainerFactory(
            KafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<String, RideEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(properties));
        return factory;
    }
}