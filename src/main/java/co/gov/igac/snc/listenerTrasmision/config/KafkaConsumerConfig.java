package co.gov.igac.snc.listenerTrasmision.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import co.gov.igac.snc.listenerTrasmision.dto.MensajeKafkaDTO;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value("${kafka.bootstrap-servers}")
	private String kafkaServer;

	@Bean
	public ConsumerFactory<String, MensajeKafkaDTO> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<>(MensajeKafkaDTO.class, false));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, MensajeKafkaDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MensajeKafkaDTO> 
				factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
		return factory;
	}
}
