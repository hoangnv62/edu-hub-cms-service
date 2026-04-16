package vn.edu_hub.service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import vn.edu_hub.service.constants.KafkaConfigConstants;

import java.util.HashMap;
import java.util.Map;

@EnableKafka // => bật để sử dụng các annotation như @KafkaListener
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers:localhost:9094}")
    private String bootstrapServers;

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        //tên consumer group(các consumer cùng group sẽ chia tải partition)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfigConstants.GROUP_ID);

        //khi không có offset(hoặc offset mất) +> bắt đầu đọc từ message mới nhất
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        //mỗi lần poll chỉ lấy tối đa 500 message => tránh overload khi xử lý chậm
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        //consumer gửi heartbeat mỗi 10 giây để báo "tôi còn sống"
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);

        //nếu không nhận heartbeat trong 50 giây thì coordinator cọi consumer chết => cân bằng tải lại
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 50000);

        //timeout cho các request gửi đến broker
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 50000);

        //key của message được deserialize thành String
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        //value là json => deserialize thành object java
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        //cho phép deserialize(giải mã) bất kỳ package nào
        props.put(JsonDeserializer.TRUSTED_PACKAGES, true);

        // không dùng header để lưu tên class -> giả mã dựa vào Object.class
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, false);
        return props;
    }

    //tạo factory để sản xuất các KafkaConsumer
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class, false));
    }

    //bean quan trọng nhất, tạo container factory mà spring kafka dùng để quản lý các @KafkaListener
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
