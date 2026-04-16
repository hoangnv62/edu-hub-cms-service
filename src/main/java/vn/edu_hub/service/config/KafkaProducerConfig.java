package vn.edu_hub.service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

//class này tạo ra bộ máy để gửi message(dạng json) đến kafka topic, với các thiết lập đảm bảo không duplicate, độ tin cậy cao, hiệu suất tốt
@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers:localhost:9094}")
    private String bootstrapServers;

    //tạo map property cấu hình cho kafka producer
    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        //chờ tất cả replica in-sync xác nhận
        // khi producer gửi 1 message hoặc (batch message) đến kafka, nó sẽ chờ broker xác nhận(ack) trước khi coi là gửi thành công
        // acks quyết định phải chờ bao nhiêu broker xác nhận thì mới ok
        //acks=0 => không chờ ai cả. Nhanh nhất nhưng có thể mất message mà producer không biết
        //acks=1 => chỉ chờ leader của partition xác nhận (leader đã ghi vào log của nó). Nhanh hơn all nhưng nếu leader crash ngay sau khi ack mà chưa replicate => mất message
        //acks=-1 (all) => chờ leader + tất cả in-sync replicas đều xác nhận đã replicate message
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        //thử lại tối đa 5 lần khi lỗi tạm thời
        props.put(ProducerConfig.RETRIES_CONFIG, 5);

        //số request chưa ack tối đa trên 1 connection
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        //không duplicate khi retry
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        //time out cho mỗi request
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 50000);
        return props;
    }

    //tạo factory sản xuất kafka producer  để serialize dữ liệu trước khi gửi
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    //tạo bean KafkaTemplate - công cụ chính để gửi message
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
