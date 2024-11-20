package minhtan.authenticator.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;

public class Producer {

    public static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";

    public static void publishEvent(String topic, String value) {
        resetThreadContext();
        KafkaProducer<String, String> producer = new KafkaProducer<>(getProperties());
        ProducerRecord<String, String> eventRecord = new ProducerRecord<>(topic, value);

        producer.send(eventRecord);
        producer.flush();
        producer.close();

    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    private static void resetThreadContext() {
        Thread.currentThread().setContextClassLoader(null);
    }
}
