package bench.perf.com.utility.deserializers;

import bench.perf.com.domain.kafka.KafkaMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class KafkaMessageDeserializer extends ObjectMapperDeserializer<KafkaMessage> {

    public KafkaMessageDeserializer() {
        super(KafkaMessage.class);
    }

}
